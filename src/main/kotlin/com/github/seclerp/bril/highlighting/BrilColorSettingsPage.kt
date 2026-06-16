package com.github.seclerp.bril.highlighting

import com.github.seclerp.bril.BrilIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class BrilColorSettingsPage : ColorSettingsPage {
    override fun getDisplayName(): String = "Bril"
    override fun getIcon(): Icon = BrilIcons.File
    override fun getHighlighter(): SyntaxHighlighter = BrilSyntaxHighlighter()
    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> = emptyMap()
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDemoText(): String = """
        # Compute the n-th Fibonacci number.
        @fib(n: int): int {
          one: int = const 1;
          cond: bool = lt n one;
          br cond .base .rec;
        .base:
          ret n;
        .rec:
          a: int = sub n one;
          b: int = call @fib a;
          ret b;
        }

        @main {
          v: int = const 10;
          r: int = call @fib v;
          print r;
          p: ptr<int> = alloc v;
          free p;
        }
    """.trimIndent()

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Comment", BrilColors.COMMENT),
            AttributesDescriptor("Keyword", BrilColors.KEYWORD),
            AttributesDescriptor("Operation", BrilColors.OPCODE),
            AttributesDescriptor("Type", BrilColors.TYPE),
            AttributesDescriptor("Number", BrilColors.NUMBER),
            AttributesDescriptor("Character", BrilColors.CHAR),
            AttributesDescriptor("Function name", BrilColors.FUNCTION),
            AttributesDescriptor("Label", BrilColors.LABEL),
            AttributesDescriptor("Identifier", BrilColors.IDENTIFIER),
            AttributesDescriptor("Braces", BrilColors.BRACES),
            AttributesDescriptor("Parentheses", BrilColors.PARENTHESES),
            AttributesDescriptor("Operation sign", BrilColors.OPERATION_SIGN),
            AttributesDescriptor("Semicolon", BrilColors.SEMICOLON),
            AttributesDescriptor("Comma", BrilColors.COMMA),
            AttributesDescriptor("Bad character", BrilColors.BAD_CHARACTER),
        )
    }
}
