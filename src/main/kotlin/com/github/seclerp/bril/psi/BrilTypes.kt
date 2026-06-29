package com.github.seclerp.bril.psi

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

/** Token types produced by [com.github.seclerp.bril.lexer.BrilLexer]. */
object BrilTokenTypes {
    @JvmField val IDENTIFIER: IElementType = BrilTokenType("IDENTIFIER")
    @JvmField val FUNC_IDENT: IElementType = BrilTokenType("FUNC_IDENT") // @name
    @JvmField val LABEL_IDENT: IElementType = BrilTokenType("LABEL_IDENT") // .name

    @JvmField val INT_LIT: IElementType = BrilTokenType("INT_LIT")
    @JvmField val FLOAT_LIT: IElementType = BrilTokenType("FLOAT_LIT")
    @JvmField val BOOL_LIT: IElementType = BrilTokenType("BOOL_LIT") // true | false
    @JvmField val CHAR_LIT: IElementType = BrilTokenType("CHAR_LIT")

    @JvmField val KW_CONST: IElementType = BrilTokenType("const")
    @JvmField val KW_NULLPTR: IElementType = BrilTokenType("nullptr")

    @JvmField val COLON: IElementType = BrilTokenType(":")
    @JvmField val SEMICOLON: IElementType = BrilTokenType(";")
    @JvmField val COMMA: IElementType = BrilTokenType(",")
    @JvmField val EQ: IElementType = BrilTokenType("=")
    @JvmField val LBRACE: IElementType = BrilTokenType("{")
    @JvmField val RBRACE: IElementType = BrilTokenType("}")
    @JvmField val LPAREN: IElementType = BrilTokenType("(")
    @JvmField val RPAREN: IElementType = BrilTokenType(")")
    @JvmField val LANGLE: IElementType = BrilTokenType("<")
    @JvmField val RANGLE: IElementType = BrilTokenType(">")

    @JvmField val COMMENT: IElementType = BrilTokenType("COMMENT")
    @JvmField val BAD_CHARACTER: IElementType = BrilTokenType("BAD_CHARACTER")

    @JvmField val COMMENTS: TokenSet = TokenSet.create(COMMENT)
    @JvmField val LITERALS: TokenSet = TokenSet.create(INT_LIT, FLOAT_LIT, BOOL_LIT, CHAR_LIT, KW_NULLPTR)
}

/** Composite AST node types produced by [com.github.seclerp.bril.parser.BrilParser]. */
object BrilElementTypes {
    @JvmField val FUNCTION: IElementType = BrilElementType("FUNCTION")
    @JvmField val ARG: IElementType = BrilElementType("ARG")
    @JvmField val TYPE: IElementType = BrilElementType("TYPE")
    @JvmField val CONST_INSTRUCTION: IElementType = BrilElementType("CONST_INSTRUCTION")
    @JvmField val VALUE_OPERATION: IElementType = BrilElementType("VALUE_OPERATION")
    @JvmField val EFFECT_OPERATION: IElementType = BrilElementType("EFFECT_OPERATION")
    @JvmField val LABEL_DEFINITION: IElementType = BrilElementType("LABEL_DEFINITION")
    @JvmField val OPCODE: IElementType = BrilElementType("OPCODE")
    @JvmField val LITERAL: IElementType = BrilElementType("LITERAL")

    // Operand wrappers — each holds a single identifier leaf and carries a reference to its
    // definition, powering Go to Declaration and Find Usages.
    @JvmField val VARIABLE_REFERENCE: IElementType = BrilElementType("VARIABLE_REFERENCE")
    @JvmField val FUNCTION_REFERENCE: IElementType = BrilElementType("FUNCTION_REFERENCE")
    @JvmField val LABEL_REFERENCE: IElementType = BrilElementType("LABEL_REFERENCE")
}

/** Bril core opcodes plus common extension opcodes, highlighted as keywords. */
object BrilOpcodes {
    @JvmField
    val ALL: Set<String> = setOf(
        // arithmetic
        "add", "mul", "sub", "div",
        // comparison
        "eq", "lt", "gt", "le", "ge",
        // logic
        "not", "and", "or",
        // control
        "jmp", "br", "call", "ret",
        // misc
        "id", "print", "nop",
        // memory extension
        "alloc", "free", "store", "load", "ptradd",
        // floating point extension
        "fadd", "fmul", "fsub", "fdiv", "feq", "flt", "fle", "fgt", "fge",
        // SSA extension
        "phi",
        // char extension
        "ceq", "clt", "cle", "cgt", "cge", "char2int", "int2char",
        // speculation extension
        "speculate", "commit", "guard",
    )
}

/** Bril primitive type names, highlighted as types. */
object BrilPrimitiveTypes {
    @JvmField
    val ALL: Set<String> = setOf("int", "bool", "float", "char", "ptr")
}
