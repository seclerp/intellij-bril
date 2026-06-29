package com.github.seclerp.bril.references

import com.github.seclerp.bril.psi.BrilFunction
import com.github.seclerp.bril.psi.BrilLabelDefinition
import com.github.seclerp.bril.psi.BrilVariableDefinition
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException

/** The kind of definition an operand identifier points at. */
enum class BrilReferenceKind { VARIABLE, FUNCTION, LABEL }

/**
 * Resolves an operand node (see `BrilReferenceElement`) to the element that introduces its name:
 *  - [BrilReferenceKind.VARIABLE] — an [BrilVariableDefinition] (argument or `const`/value
 *    destination) with the same name within the enclosing function;
 *  - [BrilReferenceKind.FUNCTION] — a [BrilFunction] with the same name anywhere in the file;
 *  - [BrilReferenceKind.LABEL] — a [BrilLabelDefinition] with the same name within the function.
 *
 * The range excludes the `@`/`.` sigil so the reference text matches the definition's name, and
 * powers both Go to Declaration (via [resolve]) and Find Usages (the search verifies that a
 * candidate occurrence's reference resolves back to the queried definition).
 */
class BrilReference(
    element: PsiElement,
    rangeInElement: TextRange,
    private val kind: BrilReferenceKind,
) : PsiReferenceBase<PsiElement>(element, rangeInElement, /* soft = */ true) {

    override fun resolve(): PsiElement? = when (kind) {
        BrilReferenceKind.VARIABLE -> {
            val function = PsiTreeUtil.getParentOfType(element, BrilFunction::class.java)
            function?.let {
                PsiTreeUtil.collectElementsOfType(it, BrilVariableDefinition::class.java)
                    .firstOrNull { def -> def.name == value } as PsiElement?
            }
        }
        BrilReferenceKind.FUNCTION -> {
            val name = value.removePrefix("@")
            PsiTreeUtil.collectElementsOfType(element.containingFile, BrilFunction::class.java)
                .firstOrNull { it.name == name }
        }
        BrilReferenceKind.LABEL -> {
            val name = value.removePrefix(".")
            val function = PsiTreeUtil.getParentOfType(element, BrilFunction::class.java)
            function?.let {
                PsiTreeUtil.collectElementsOfType(it, BrilLabelDefinition::class.java)
                    .firstOrNull { def -> def.name == name }
            }
        }
    }

    override fun handleElementRename(newElementName: String): PsiElement =
        throw IncorrectOperationException("Renaming Bril references is not supported yet")
}
