package com.github.seclerp.bril

import com.github.seclerp.bril.psi.BrilTokenTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class BrilBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = PAIRS
    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true
    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS = arrayOf(
            BracePair(BrilTokenTypes.LBRACE, BrilTokenTypes.RBRACE, true),
            BracePair(BrilTokenTypes.LPAREN, BrilTokenTypes.RPAREN, false),
            BracePair(BrilTokenTypes.LANGLE, BrilTokenTypes.RANGLE, false),
        )
    }
}
