package com.github.seclerp.bril.highlighting

import com.github.seclerp.bril.lexer.BrilLexer
import com.github.seclerp.bril.psi.BrilTokenTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

/**
 * Lexer-based highlighting. This colors everything that can be decided from a single token
 * (comments, literals, `@func`/`.label` identifiers, punctuation). Type names and opcodes,
 * which depend on context, are handled by [BrilAnnotator].
 */
class BrilSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = BrilLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(KEYS[tokenType])

    companion object {
        private val KEYS: Map<IElementType, TextAttributesKey> = buildMap {
            put(BrilTokenTypes.COMMENT, BrilColors.COMMENT)
            put(BrilTokenTypes.KW_CONST, BrilColors.KEYWORD)
            put(BrilTokenTypes.KW_NULLPTR, BrilColors.KEYWORD)
            put(BrilTokenTypes.BOOL_LIT, BrilColors.KEYWORD)
            put(BrilTokenTypes.INT_LIT, BrilColors.NUMBER)
            put(BrilTokenTypes.FLOAT_LIT, BrilColors.NUMBER)
            put(BrilTokenTypes.CHAR_LIT, BrilColors.CHAR)
            put(BrilTokenTypes.FUNC_IDENT, BrilColors.FUNCTION)
            put(BrilTokenTypes.LABEL_IDENT, BrilColors.LABEL)
            put(BrilTokenTypes.IDENTIFIER, BrilColors.IDENTIFIER)
            put(BrilTokenTypes.LBRACE, BrilColors.BRACES)
            put(BrilTokenTypes.RBRACE, BrilColors.BRACES)
            put(BrilTokenTypes.LPAREN, BrilColors.PARENTHESES)
            put(BrilTokenTypes.RPAREN, BrilColors.PARENTHESES)
            put(BrilTokenTypes.LANGLE, BrilColors.OPERATION_SIGN)
            put(BrilTokenTypes.RANGLE, BrilColors.OPERATION_SIGN)
            put(BrilTokenTypes.EQ, BrilColors.OPERATION_SIGN)
            put(BrilTokenTypes.COLON, BrilColors.OPERATION_SIGN)
            put(BrilTokenTypes.SEMICOLON, BrilColors.SEMICOLON)
            put(BrilTokenTypes.COMMA, BrilColors.COMMA)
            put(BrilTokenTypes.BAD_CHARACTER, BrilColors.BAD_CHARACTER)
        }
    }
}
