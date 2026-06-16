package com.github.seclerp.bril

import com.intellij.lang.Language

/**
 * The Bril language. Bril is a small educational intermediate representation; this plugin
 * supports its human-readable text format (the one produced by `bril2txt`).
 */
object BrilLanguage : Language("Bril") {
    private fun readResolve(): Any = BrilLanguage
    override fun getDisplayName(): String = "Bril"
    override fun isCaseSensitive(): Boolean = true
}
