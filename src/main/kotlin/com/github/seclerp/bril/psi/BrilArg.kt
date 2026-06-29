package com.github.seclerp.bril.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType

/**
 * `arg: IDENT ":" type`
 *
 * A function parameter; defines a variable that operands within the function body refer to.
 */
class BrilArg(node: ASTNode) : BrilNamedElement(node), BrilVariableDefinition {
    override val nameTokenType: IElementType get() = BrilTokenTypes.IDENTIFIER
}
