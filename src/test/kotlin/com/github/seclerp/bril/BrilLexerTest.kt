package com.github.seclerp.bril

import com.github.seclerp.bril.lexer.BrilLexer
import com.github.seclerp.bril.psi.BrilTokenTypes
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.junit.Assert.assertEquals
import org.junit.Test

class BrilLexerTest {
    private fun lex(text: String): List<Pair<IElementType?, String>> {
        val lexer = BrilLexer()
        lexer.start(text, 0, text.length, 0)
        val tokens = mutableListOf<Pair<IElementType?, String>>()
        while (lexer.tokenType != null) {
            tokens += lexer.tokenType to text.substring(lexer.tokenStart, lexer.tokenEnd)
            lexer.advance()
        }
        return tokens
    }

    /** Tokens excluding whitespace, for compact assertions. */
    private fun significant(text: String) =
        lex(text).filter { it.first != TokenType.WHITE_SPACE }

    @Test
    fun lexesConstInstruction() {
        val tokens = significant("v0: int = const 1;")
        assertEquals(
            listOf(
                BrilTokenTypes.IDENTIFIER to "v0",
                BrilTokenTypes.COLON to ":",
                BrilTokenTypes.IDENTIFIER to "int",
                BrilTokenTypes.EQ to "=",
                BrilTokenTypes.KW_CONST to "const",
                BrilTokenTypes.INT_LIT to "1",
                BrilTokenTypes.SEMICOLON to ";",
            ),
            tokens,
        )
    }

    @Test
    fun lexesIdentifierKinds() {
        val tokens = significant("@main .loop x")
        assertEquals(
            listOf(
                BrilTokenTypes.FUNC_IDENT to "@main",
                BrilTokenTypes.LABEL_IDENT to ".loop",
                BrilTokenTypes.IDENTIFIER to "x",
            ),
            tokens,
        )
    }

    @Test
    fun lexesLiterals() {
        assertEquals(BrilTokenTypes.FLOAT_LIT, significant("3.14").single().first)
        assertEquals(BrilTokenTypes.INT_LIT, significant("-42").single().first)
        assertEquals(BrilTokenTypes.FLOAT_LIT, significant("1e10").single().first)
        assertEquals(BrilTokenTypes.BOOL_LIT, significant("true").single().first)
        assertEquals(BrilTokenTypes.BOOL_LIT, significant("false").single().first)
        assertEquals(BrilTokenTypes.CHAR_LIT, significant("'a'").single().first)
        assertEquals(BrilTokenTypes.CHAR_LIT, significant("'\\n'").single().first)
        assertEquals(BrilTokenTypes.KW_NULLPTR, significant("nullptr").single().first)
    }

    @Test
    fun lexesComment() {
        val tokens = lex("# hello\n")
        assertEquals(BrilTokenTypes.COMMENT, tokens.first().first)
        assertEquals("# hello", tokens.first().second)
    }

    @Test
    fun paramTypeAndAngleBrackets() {
        val tokens = significant("ptr<int>")
        assertEquals(
            listOf(
                BrilTokenTypes.IDENTIFIER to "ptr",
                BrilTokenTypes.LANGLE to "<",
                BrilTokenTypes.IDENTIFIER to "int",
                BrilTokenTypes.RANGLE to ">",
            ),
            tokens,
        )
    }
}
