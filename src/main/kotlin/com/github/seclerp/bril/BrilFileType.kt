package com.github.seclerp.bril

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object BrilFileType : LanguageFileType(BrilLanguage) {
    override fun getName(): String = "Bril"
    override fun getDescription(): String = "Bril intermediate representation"
    override fun getDefaultExtension(): String = "bril"
    override fun getIcon(): Icon = BrilIcons.File
}
