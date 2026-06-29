package com.github.seclerp.bril.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType

/**
 * `label: LABEL ":"`
 *
 * Defines a branch target `.name`; the target of `jmp`/`br` label-operand references.
 */
class BrilLabelDefinition(node: ASTNode) : BrilNamedElement(node) {
    override val nameTokenType: IElementType get() = BrilTokenTypes.LABEL_IDENT

    /** Label name without the leading `.` sigil. */
    override fun getName(): String? = super.getName()?.removePrefix(".")
}
