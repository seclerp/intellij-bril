package com.github.seclerp.bril.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType

/**
 * `const: IDENT [":" type] "=" "const" lit ";"`
 *
 * Defines a variable bound to a literal value. The destination identifier (the first child
 * [BrilTokenTypes.IDENTIFIER]) is the variable definition; the opcode is nested in an OPCODE node.
 */
class BrilConstInstruction(node: ASTNode) : BrilNamedElement(node), BrilVariableDefinition {
    override val nameTokenType: IElementType get() = BrilTokenTypes.IDENTIFIER
}
