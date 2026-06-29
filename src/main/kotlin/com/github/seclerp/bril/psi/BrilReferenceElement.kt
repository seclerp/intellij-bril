package com.github.seclerp.bril.psi

import com.github.seclerp.bril.references.BrilReference
import com.github.seclerp.bril.references.BrilReferenceKind
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference

/**
 * Base class for operand nodes that point at a definition (a variable, function or label use).
 *
 * Each wraps a single identifier leaf and exposes a [BrilReference] over it; serving the reference
 * from a composite node (rather than the raw leaf) is what lets the platform pick it up for Go to
 * Declaration and Find Usages — leaf tokens do not consult reference contributors.
 */
abstract class BrilReferenceElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    protected abstract val referenceKind: BrilReferenceKind

    override fun getReference(): PsiReference =
        // Cover the whole operand (including any '@'/'.' sigil) so a click anywhere on it navigates;
        // the sigil is stripped during resolution.
        BrilReference(this, TextRange(0, textLength), referenceKind)

    override fun getReferences(): Array<PsiReference> = arrayOf(reference)
}

/** A variable operand (`IDENT`) — resolves to its [BrilVariableDefinition] in the function. */
class BrilVariableReference(node: ASTNode) : BrilReferenceElement(node) {
    override val referenceKind: BrilReferenceKind get() = BrilReferenceKind.VARIABLE
}

/** A function operand (`@name`) — resolves to a [BrilFunction] in the file. */
class BrilFunctionReference(node: ASTNode) : BrilReferenceElement(node) {
    override val referenceKind: BrilReferenceKind get() = BrilReferenceKind.FUNCTION
}

/** A label operand (`.name`) — resolves to a [BrilLabelDefinition] in the function. */
class BrilLabelReference(node: ASTNode) : BrilReferenceElement(node) {
    override val referenceKind: BrilReferenceKind get() = BrilReferenceKind.LABEL
}
