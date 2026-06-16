package com.github.seclerp.bril.highlighting

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

/** Text attribute keys for Bril, derived from the editor's default language colors. */
object BrilColors {
    val COMMENT = key("BRIL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
    val KEYWORD = key("BRIL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
    val OPCODE = key("BRIL_OPCODE", DefaultLanguageHighlighterColors.KEYWORD)
    val TYPE = key("BRIL_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME)
    val NUMBER = key("BRIL_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
    val CHAR = key("BRIL_CHAR", DefaultLanguageHighlighterColors.STRING)
    val FUNCTION = key("BRIL_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
    val LABEL = key("BRIL_LABEL", DefaultLanguageHighlighterColors.LABEL)
    val IDENTIFIER = key("BRIL_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
    val BRACES = key("BRIL_BRACES", DefaultLanguageHighlighterColors.BRACES)
    val PARENTHESES = key("BRIL_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
    val OPERATION_SIGN = key("BRIL_OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN)
    val SEMICOLON = key("BRIL_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
    val COMMA = key("BRIL_COMMA", DefaultLanguageHighlighterColors.COMMA)
    val BAD_CHARACTER = key("BRIL_BAD_CHARACTER", com.intellij.openapi.editor.HighlighterColors.BAD_CHARACTER)

    private fun key(name: String, fallback: TextAttributesKey) =
        TextAttributesKey.createTextAttributesKey(name, fallback)
}
