package com.github.seclerp.bril

import com.intellij.lang.Commenter

/** Enables line-comment (Ctrl+/) support using Bril's `#` comments. */
class BrilCommenter : Commenter {
    override fun getLineCommentPrefix(): String = "#"
    override fun getBlockCommentPrefix(): String? = null
    override fun getBlockCommentSuffix(): String? = null
    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}
