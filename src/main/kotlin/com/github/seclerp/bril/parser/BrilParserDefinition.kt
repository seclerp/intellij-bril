package com.github.seclerp.bril.parser

import com.github.seclerp.bril.BrilLanguage
import com.github.seclerp.bril.lexer.BrilLexer
import com.github.seclerp.bril.psi.BrilElementFactory
import com.github.seclerp.bril.psi.BrilFile
import com.github.seclerp.bril.psi.BrilTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class BrilParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = BrilLexer()
    override fun createParser(project: Project?): PsiParser = BrilParser()
    override fun getFileNodeType(): IFileElementType = FILE
    override fun getCommentTokens(): TokenSet = BrilTokenTypes.COMMENTS
    override fun getWhitespaceTokens(): TokenSet = WHITESPACE
    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY
    override fun createElement(node: ASTNode): PsiElement = BrilElementFactory.createElement(node)
    override fun createFile(viewProvider: FileViewProvider): PsiFile = BrilFile(viewProvider)

    companion object {
        val FILE = IFileElementType(BrilLanguage)
        private val WHITESPACE = TokenSet.create(TokenType.WHITE_SPACE)
    }
}
