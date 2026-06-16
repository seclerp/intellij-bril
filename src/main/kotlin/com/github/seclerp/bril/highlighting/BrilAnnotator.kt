package com.github.seclerp.bril.highlighting

import com.github.seclerp.bril.psi.BrilElementTypes
import com.github.seclerp.bril.psi.BrilOpcodes
import com.github.seclerp.bril.psi.BrilPrimitiveTypes
import com.github.seclerp.bril.psi.BrilTokenTypes
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType

/**
 * Context-sensitive highlighting that the lexer alone cannot provide:
 *  - known opcodes (`add`, `br`, `print`, …) are colored as operations;
 *  - primitive type names (`int`, `bool`, `float`, `char`, `ptr`) inside a type are colored as types.
 */
class BrilAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when {
            element.elementType == BrilElementTypes.OPCODE -> {
                if (element.text in BrilOpcodes.ALL) {
                    highlight(holder, element, BrilColors.OPCODE)
                }
            }
            element.elementType == BrilTokenTypes.IDENTIFIER &&
                element.parent.elementType == BrilElementTypes.TYPE -> {
                if (element.text in BrilPrimitiveTypes.ALL) {
                    highlight(holder, element, BrilColors.TYPE)
                }
            }
        }
    }

    private fun highlight(holder: AnnotationHolder, element: PsiElement, key: TextAttributesKey) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(key)
            .create()
    }
}
