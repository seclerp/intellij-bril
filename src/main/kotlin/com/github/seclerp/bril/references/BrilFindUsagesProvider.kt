package com.github.seclerp.bril.references

import com.github.seclerp.bril.lexer.BrilLexer
import com.github.seclerp.bril.psi.BrilFunction
import com.github.seclerp.bril.psi.BrilLabelDefinition
import com.github.seclerp.bril.psi.BrilNamedElement
import com.github.seclerp.bril.psi.BrilTokenTypes
import com.github.seclerp.bril.psi.BrilVariableDefinition
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet

/**
 * Enables Find Usages for Bril's name-introducing elements. The words scanner indexes identifier
 * tokens so candidate occurrences can be located; [BrilReference.resolve] then confirms which of
 * them actually point at the queried definition.
 */
class BrilFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner =
        DefaultWordsScanner(
            BrilLexer(),
            TokenSet.create(BrilTokenTypes.IDENTIFIER, BrilTokenTypes.FUNC_IDENT, BrilTokenTypes.LABEL_IDENT),
            BrilTokenTypes.COMMENTS,
            BrilTokenTypes.LITERALS,
        )

    override fun canFindUsagesFor(element: PsiElement): Boolean = element is BrilNamedElement

    override fun getHelpId(element: PsiElement): String? = null

    override fun getType(element: PsiElement): String = when (element) {
        is BrilFunction -> "function"
        is BrilLabelDefinition -> "label"
        is BrilVariableDefinition -> "variable"
        else -> ""
    }

    override fun getDescriptiveName(element: PsiElement): String =
        (element as? PsiNamedElement)?.name ?: ""

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String =
        getDescriptiveName(element)
}
