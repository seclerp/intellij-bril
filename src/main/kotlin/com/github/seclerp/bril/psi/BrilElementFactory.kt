package com.github.seclerp.bril.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * Maps composite [BrilElementTypes] nodes to their PSI wrappers.
 *
 * Name-introducing nodes (functions, variables, labels) get dedicated [BrilNamedElement]
 * subclasses so they can participate in navigation/reference features; everything else
 * (opcodes, types, literals, effect operations) keeps the generic [BrilPsiElement].
 */
object BrilElementFactory {
    fun createElement(node: ASTNode): PsiElement = when (node.elementType) {
        BrilElementTypes.FUNCTION -> BrilFunction(node)
        BrilElementTypes.ARG -> BrilArg(node)
        BrilElementTypes.CONST_INSTRUCTION -> BrilConstInstruction(node)
        BrilElementTypes.VALUE_OPERATION -> BrilValueOperation(node)
        BrilElementTypes.LABEL_DEFINITION -> BrilLabelDefinition(node)
        else -> BrilPsiElement(node)
    }
}
