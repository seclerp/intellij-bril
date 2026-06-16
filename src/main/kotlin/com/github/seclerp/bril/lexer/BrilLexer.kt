package com.github.seclerp.bril.lexer

import com.github.seclerp.bril.psi.BrilTokenTypes
import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

/**
 * Hand-written lexer for the Bril text format. Mirrors the terminals of the official
 * `briltxt.py` Lark grammar:
 *
 * ```
 * IDENT: ("_"|"%"|LETTER) ("_"|"%"|"."|LETTER|DIGIT)*
 * FUNC:  "@" IDENT
 * LABEL: "." IDENT
 * BOOL:  "true" | "false"
 * CHAR:  /'.'/ | /'\[0abtnvfr]'/
 * COMMENT: /#.*\/
 * ```
 */
class BrilLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset = 0
    private var endOffset = 0

    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        advance()
    }

    override fun getState(): Int = 0
    override fun getTokenType(): IElementType? = tokenType
    override fun getTokenStart(): Int = tokenStart
    override fun getTokenEnd(): Int = tokenEnd
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        tokenStart = tokenEnd
        if (tokenStart >= endOffset) {
            tokenType = null
            return
        }
        val c = buffer[tokenStart]
        when {
            isWhitespace(c) -> {
                tokenEnd = scanWhile(tokenStart) { isWhitespace(it) }
                tokenType = TokenType.WHITE_SPACE
            }
            c == '#' -> {
                tokenEnd = scanWhile(tokenStart) { it != '\n' }
                tokenType = BrilTokenTypes.COMMENT
            }
            c == '@' -> scanPrefixedIdent(BrilTokenTypes.FUNC_IDENT)
            c == '.' -> scanPrefixedIdent(BrilTokenTypes.LABEL_IDENT)
            c == '\'' -> scanChar()
            c == '-' || isDigit(c) -> scanNumber()
            isIdentStart(c) -> scanIdentOrKeyword()
            else -> scanPunctuation(c)
        }
    }

    /** Scans an `@name` or `.name` token; the `@`/`.` prefix is already at [tokenStart]. */
    private fun scanPrefixedIdent(type: IElementType) {
        val next = tokenStart + 1
        if (next < endOffset && isIdentStart(buffer[next])) {
            tokenEnd = scanWhile(next) { isIdentPart(it) }
            tokenType = type
        } else {
            // A lone '@' or '.' is not a valid token.
            tokenEnd = tokenStart + 1
            tokenType = BrilTokenTypes.BAD_CHARACTER
        }
    }

    private fun scanChar() {
        // CHAR: '<any>'  or  '\<esc>'
        var i = tokenStart + 1
        if (i < endOffset && buffer[i] == '\\') {
            i++
        }
        if (i < endOffset && buffer[i] != '\n') {
            i++ // the character itself
        }
        if (i < endOffset && buffer[i] == '\'') {
            tokenEnd = i + 1
            tokenType = BrilTokenTypes.CHAR_LIT
        } else {
            tokenEnd = tokenStart + 1
            tokenType = BrilTokenTypes.BAD_CHARACTER
        }
    }

    private fun scanNumber() {
        var i = tokenStart
        if (buffer[i] == '-') i++
        // A bare '-' with no following digit is a bad character.
        if (i >= endOffset || !isDigit(buffer[i])) {
            tokenEnd = tokenStart + 1
            tokenType = BrilTokenTypes.BAD_CHARACTER
            return
        }
        i = scanWhile(i) { isDigit(it) }
        var isFloat = false
        if (i < endOffset && buffer[i] == '.' && i + 1 < endOffset && isDigit(buffer[i + 1])) {
            isFloat = true
            i = scanWhile(i + 1) { isDigit(it) }
        }
        if (i < endOffset && (buffer[i] == 'e' || buffer[i] == 'E')) {
            var j = i + 1
            if (j < endOffset && (buffer[j] == '+' || buffer[j] == '-')) j++
            if (j < endOffset && isDigit(buffer[j])) {
                isFloat = true
                i = scanWhile(j) { isDigit(it) }
            }
        }
        tokenEnd = i
        tokenType = if (isFloat) BrilTokenTypes.FLOAT_LIT else BrilTokenTypes.INT_LIT
    }

    private fun scanIdentOrKeyword() {
        tokenEnd = scanWhile(tokenStart) { isIdentPart(it) }
        tokenType = when (buffer.subSequence(tokenStart, tokenEnd).toString()) {
            "const" -> BrilTokenTypes.KW_CONST
            "nullptr" -> BrilTokenTypes.KW_NULLPTR
            "true", "false" -> BrilTokenTypes.BOOL_LIT
            else -> BrilTokenTypes.IDENTIFIER
        }
    }

    private fun scanPunctuation(c: Char) {
        tokenEnd = tokenStart + 1
        tokenType = when (c) {
            ':' -> BrilTokenTypes.COLON
            ';' -> BrilTokenTypes.SEMICOLON
            ',' -> BrilTokenTypes.COMMA
            '=' -> BrilTokenTypes.EQ
            '{' -> BrilTokenTypes.LBRACE
            '}' -> BrilTokenTypes.RBRACE
            '(' -> BrilTokenTypes.LPAREN
            ')' -> BrilTokenTypes.RPAREN
            '<' -> BrilTokenTypes.LANGLE
            '>' -> BrilTokenTypes.RANGLE
            else -> BrilTokenTypes.BAD_CHARACTER
        }
    }

    private inline fun scanWhile(from: Int, predicate: (Char) -> Boolean): Int {
        var i = from
        while (i < endOffset && predicate(buffer[i])) i++
        return i
    }

    private fun isWhitespace(c: Char) = c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\u000C'
    private fun isDigit(c: Char) = c in '0'..'9'
    private fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
    private fun isIdentStart(c: Char) = isLetter(c) || c == '_' || c == '%'
    private fun isIdentPart(c: Char) = isIdentStart(c) || isDigit(c) || c == '.'
}
