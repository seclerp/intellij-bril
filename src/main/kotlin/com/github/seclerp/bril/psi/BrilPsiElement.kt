package com.github.seclerp.bril.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

/**
 * Generic PSI wrapper for every Bril composite node. The plugin currently only needs the
 * element-type distinctions (for highlighting and tree inspection), so a single wrapper class
 * is enough; dedicated PSI classes can be introduced later for navigation/reference support.
 */
class BrilPsiElement(node: ASTNode) : ASTWrapperPsiElement(node)
