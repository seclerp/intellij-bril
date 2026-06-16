package com.github.seclerp.bril.psi

import com.intellij.psi.tree.IElementType
import com.github.seclerp.bril.BrilLanguage
import org.jetbrains.annotations.NonNls

class BrilTokenType(@NonNls debugName: String) : IElementType(debugName, BrilLanguage) {
    override fun toString(): String = "BrilTokenType." + super.toString()
}
