package com.github.seclerp.bril.parser

import com.github.seclerp.bril.psi.BrilElementTypes
import com.github.seclerp.bril.psi.BrilTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

/**
 * Recursive-descent parser for the Bril text format, following the `briltxt.py` grammar:
 *
 * ```
 * start:  func*
 * func:   FUNC ["(" arg ("," arg)* ")"] [":" type] "{" instr* "}"
 * arg:    IDENT ":" type
 * instr:  const | vop | eop | label
 * const:  IDENT [":" type] "=" "const" lit ";"
 * vop:    IDENT [":" type] "=" op ";"
 * eop:    op ";"
 * label:  LABEL ":"
 * op:     IDENT (FUNC | LABEL | IDENT)*
 * type:   IDENT ["<" type ">"]
 * ```
 *
 * Whitespace and comment tokens are filtered out by the platform before they reach the builder.
 */
class BrilParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        while (!builder.eof()) {
            if (builder.tokenType == BrilTokenTypes.FUNC_IDENT) {
                parseFunction(builder)
            } else {
                // Recover: a top-level construct must start with a function name.
                val err = builder.mark()
                builder.advanceLexer()
                err.error("Expected a function definition starting with '@'")
            }
        }
        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseFunction(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // FUNC_IDENT

        if (builder.tokenType == BrilTokenTypes.LPAREN) {
            parseArgList(builder)
        }
        if (builder.tokenType == BrilTokenTypes.COLON) {
            builder.advanceLexer()
            parseType(builder)
        }

        if (builder.tokenType == BrilTokenTypes.LBRACE) {
            builder.advanceLexer()
            while (!builder.eof() && builder.tokenType != BrilTokenTypes.RBRACE) {
                if (!parseInstruction(builder)) {
                    // Avoid an infinite loop on an unexpected token.
                    val err = builder.mark()
                    builder.advanceLexer()
                    err.error("Unexpected token in function body")
                }
            }
            expect(builder, BrilTokenTypes.RBRACE, "'}'")
        } else {
            builder.error("Expected '{'")
        }
        marker.done(BrilElementTypes.FUNCTION)
    }

    private fun parseArgList(builder: PsiBuilder) {
        builder.advanceLexer() // LPAREN
        while (!builder.eof() && builder.tokenType != BrilTokenTypes.RPAREN) {
            parseArg(builder)
            if (builder.tokenType == BrilTokenTypes.COMMA) {
                builder.advanceLexer()
            } else {
                break
            }
        }
        expect(builder, BrilTokenTypes.RPAREN, "')'")
    }

    private fun parseArg(builder: PsiBuilder) {
        val marker = builder.mark()
        expect(builder, BrilTokenTypes.IDENTIFIER, "argument name")
        if (expect(builder, BrilTokenTypes.COLON, "':'")) {
            parseType(builder)
        }
        marker.done(BrilElementTypes.ARG)
    }

    /** type: IDENT ("<" type ">")? */
    private fun parseType(builder: PsiBuilder) {
        val marker = builder.mark()
        if (!expect(builder, BrilTokenTypes.IDENTIFIER, "type name")) {
            marker.done(BrilElementTypes.TYPE)
            return
        }
        if (builder.tokenType == BrilTokenTypes.LANGLE) {
            builder.advanceLexer()
            parseType(builder)
            expect(builder, BrilTokenTypes.RANGLE, "'>'")
        }
        marker.done(BrilElementTypes.TYPE)
    }

    /** Returns true if anything was consumed. */
    private fun parseInstruction(builder: PsiBuilder): Boolean {
        val token = builder.tokenType ?: return false
        return when (token) {
            BrilTokenTypes.LABEL_IDENT -> {
                // label definition only when followed by ':'
                if (builder.lookAhead(1) == BrilTokenTypes.COLON) {
                    val marker = builder.mark()
                    builder.advanceLexer() // LABEL_IDENT
                    builder.advanceLexer() // COLON
                    marker.done(BrilElementTypes.LABEL_DEFINITION)
                    true
                } else {
                    false
                }
            }
            BrilTokenTypes.IDENTIFIER -> {
                val next = builder.lookAhead(1)
                if (next == BrilTokenTypes.COLON || next == BrilTokenTypes.EQ) {
                    parseDestInstruction(builder)
                } else {
                    parseEffectOperation(builder)
                }
                true
            }
            else -> false
        }
    }

    /** const or value operation: IDENT [":" type] "=" (("const" lit) | op) ";" */
    private fun parseDestInstruction(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // dest IDENTIFIER
        if (builder.tokenType == BrilTokenTypes.COLON) {
            builder.advanceLexer()
            parseType(builder)
        }
        expect(builder, BrilTokenTypes.EQ, "'='")

        val isConst = builder.tokenType == BrilTokenTypes.KW_CONST
        if (isConst) {
            builder.advanceLexer() // 'const'
            parseLiteral(builder)
        } else {
            parseOp(builder)
        }
        expect(builder, BrilTokenTypes.SEMICOLON, "';'")
        marker.done(if (isConst) BrilElementTypes.CONST_INSTRUCTION else BrilElementTypes.VALUE_OPERATION)
    }

    /** effect operation: op ";" */
    private fun parseEffectOperation(builder: PsiBuilder) {
        val marker = builder.mark()
        parseOp(builder)
        expect(builder, BrilTokenTypes.SEMICOLON, "';'")
        marker.done(BrilElementTypes.EFFECT_OPERATION)
    }

    /**
     * op: IDENT (FUNC | LABEL | IDENT)* — the leading IDENT is wrapped as an OPCODE and each
     * operand is wrapped in a reference node so it can resolve to its definition.
     */
    private fun parseOp(builder: PsiBuilder) {
        val opcode = builder.mark()
        expect(builder, BrilTokenTypes.IDENTIFIER, "operation name")
        opcode.done(BrilElementTypes.OPCODE)
        while (!builder.eof()) {
            val operandType = when (builder.tokenType) {
                BrilTokenTypes.IDENTIFIER -> BrilElementTypes.VARIABLE_REFERENCE
                BrilTokenTypes.FUNC_IDENT -> BrilElementTypes.FUNCTION_REFERENCE
                BrilTokenTypes.LABEL_IDENT -> BrilElementTypes.LABEL_REFERENCE
                else -> return
            }
            val operand = builder.mark()
            builder.advanceLexer()
            operand.done(operandType)
        }
    }

    private fun parseLiteral(builder: PsiBuilder) {
        val marker = builder.mark()
        if (BrilTokenTypes.LITERALS.contains(builder.tokenType)) {
            builder.advanceLexer()
            marker.done(BrilElementTypes.LITERAL)
        } else {
            marker.drop()
            builder.error("Expected a literal value")
        }
    }

    /** Consumes the expected token if present; otherwise reports an error without consuming. */
    private fun expect(builder: PsiBuilder, type: IElementType, name: String): Boolean {
        if (builder.tokenType == type) {
            builder.advanceLexer()
            return true
        }
        builder.error("Expected $name")
        return false
    }
}
