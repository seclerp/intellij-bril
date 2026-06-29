package com.github.seclerp.bril.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

/**
 * Generic PSI wrapper for Bril composite nodes that do not introduce a name — opcodes, types,
 * literals and effect operations. Name-introducing nodes use dedicated [BrilNamedElement]
 * subclasses instead; [BrilElementFactory] decides which wrapper a node gets.
 */
class BrilPsiElement(node: ASTNode) : ASTWrapperPsiElement(node)
