package com.github.seclerp.bril.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.tree.IElementType
import com.intellij.util.IncorrectOperationException

/**
 * Base class for Bril composite nodes that introduce a name into a scope — functions,
 * variables (arguments, `const`/value destinations) and labels.
 *
 * Implementing [PsiNameIdentifierOwner] is what makes these nodes useful for navigation:
 * Go to Symbol, Find Usages and (future) reference resolution can target the name leaf and
 * place the caret on it. Subclasses only declare which child token carries the name.
 */
abstract class BrilNamedElement(node: ASTNode) : ASTWrapperPsiElement(node), PsiNameIdentifierOwner {
    /** Token type of the child leaf that carries this element's name. */
    protected abstract val nameTokenType: IElementType

    override fun getNameIdentifier(): PsiElement? = node.findChildByType(nameTokenType)?.psi

    override fun getName(): String? = nameIdentifier?.text

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()

    override fun setName(name: String): PsiElement =
        throw IncorrectOperationException("Renaming Bril ${node.elementType} elements is not supported yet")
}
