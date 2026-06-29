package com.github.seclerp.bril.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType

/**
 * `vop: IDENT [":" type] "=" op ";"`
 *
 * Defines a variable bound to an operation's result. The destination identifier (the first child
 * [BrilTokenTypes.IDENTIFIER]) is the variable definition; the opcode is nested in an OPCODE node
 * and the operand identifiers follow it, so the destination is always the first direct child.
 */
class BrilValueOperation(node: ASTNode) : BrilNamedElement(node), BrilVariableDefinition {
    override val nameTokenType: IElementType get() = BrilTokenTypes.IDENTIFIER
}
