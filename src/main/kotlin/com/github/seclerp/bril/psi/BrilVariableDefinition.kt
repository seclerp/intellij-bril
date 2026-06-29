package com.github.seclerp.bril.psi

import com.intellij.psi.PsiNamedElement

/**
 * Marker for name-introducing nodes that define a variable within a function scope:
 * arguments ([BrilArg]) and `const`/value-operation destinations ([BrilConstInstruction],
 * [BrilValueOperation]). Variable operand references resolve to one of these.
 */
interface BrilVariableDefinition : PsiNamedElement
