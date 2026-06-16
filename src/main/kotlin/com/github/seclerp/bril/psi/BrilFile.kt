package com.github.seclerp.bril.psi

import com.github.seclerp.bril.BrilFileType
import com.github.seclerp.bril.BrilLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class BrilFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, BrilLanguage) {
    override fun getFileType() = BrilFileType
    override fun toString(): String = "Bril File"
}
