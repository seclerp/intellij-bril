package com.github.seclerp.bril.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType

/**
 * `func: FUNC ["(" arg ("," arg)* ")"] [":" type] "{" instr* "}"`
 *
 * Defines a callable `@name`; the target of `call`/branch operand references.
 */
class BrilFunction(node: ASTNode) : BrilNamedElement(node) {
    override val nameTokenType: IElementType get() = BrilTokenTypes.FUNC_IDENT

    /** Function name without the leading `@` sigil. */
    override fun getName(): String? = super.getName()?.removePrefix("@")
}
