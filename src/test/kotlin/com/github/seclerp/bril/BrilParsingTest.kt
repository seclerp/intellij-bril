package com.github.seclerp.bril

import com.github.seclerp.bril.psi.BrilElementTypes
import com.github.seclerp.bril.psi.BrilFile
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class BrilParsingTest : BasePlatformTestCase() {
    private fun parse(text: String): BrilFile =
        myFixture.configureByText("test.bril", text) as BrilFile

    private fun assertNoErrors(text: String) {
        val file = parse(text)
        val errors = PsiTreeUtil.findChildrenOfType(file, PsiErrorElement::class.java)
        assertEmpty("Unexpected parse errors: ${errors.map { it.errorDescription }}", errors)
    }

    fun testParsesFullProgram() {
        assertNoErrors(
            """
            # The classic example.
            @main {
              v0: int = const 1;
              v1: int = const 2;
              v2: int = add v0 v1;
              print v2;
              v3: ptr<int> = alloc v0;
              free v3;
            }
            """.trimIndent(),
        )
    }

    fun testParsesArgsReturnTypeAndControlFlow() {
        assertNoErrors(
            """
            @fib(n: int): int {
              one: int = const 1;
              cond: bool = lt n one;
              br cond .base .rec;
            .base:
              ret n;
            .rec:
              a: int = sub n one;
              b: int = call @fib a;
              ret b;
            }
            """.trimIndent(),
        )
    }

    fun testBuildsExpectedNodeTypes() {
        val file = parse(
            """
            @main {
              x: int = const 5;
              y: int = id x;
              print y;
            .done:
              ret;
            }
            """.trimIndent(),
        )
        assertNotNull(PsiTreeUtil.findChildOfType(file, com.intellij.psi.PsiElement::class.java))
        fun count(type: com.intellij.psi.tree.IElementType) =
            PsiTreeUtil.collectElements(file) { it.node.elementType == type }.size

        assertEquals(1, count(BrilElementTypes.FUNCTION))
        assertEquals(1, count(BrilElementTypes.CONST_INSTRUCTION))
        assertEquals(1, count(BrilElementTypes.VALUE_OPERATION))
        // print and ret are effect operations
        assertEquals(2, count(BrilElementTypes.EFFECT_OPERATION))
        assertEquals(1, count(BrilElementTypes.LABEL_DEFINITION))
    }

    fun testEmptyFileHasNoErrors() {
        assertNoErrors("")
    }
}
