package com.github.seclerp.bril

import com.github.seclerp.bril.psi.BrilConstInstruction
import com.github.seclerp.bril.psi.BrilFunction
import com.github.seclerp.bril.psi.BrilLabelDefinition
import com.intellij.psi.PsiNamedElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class BrilReferenceTest : BasePlatformTestCase() {

    /** Resolves the reference at the `<caret>` and returns the definition it points at. */
    private fun resolveAtCaret(text: String): PsiNamedElement? {
        myFixture.configureByText("test.bril", text)
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        return ref?.resolve() as PsiNamedElement?
    }

    fun testVariableOperandResolvesToDefinition() {
        val target = resolveAtCaret(
            """
            @main {
              x: int = const 5;
              y: int = id <caret>x;
            }
            """.trimIndent(),
        )
        assertInstanceOf(target, BrilConstInstruction::class.java)
        assertEquals("x", target?.name)
    }

    fun testFunctionOperandResolvesToDefinition() {
        val target = resolveAtCaret(
            """
            @fib(n: int): int {
              ret n;
            }
            @main {
              v: int = const 1;
              r: int = call <caret>@fib v;
            }
            """.trimIndent(),
        )
        assertInstanceOf(target, BrilFunction::class.java)
        assertEquals("fib", target?.name)
    }

    fun testLabelOperandResolvesToDefinition() {
        val target = resolveAtCaret(
            """
            @main {
              jmp <caret>.done;
            .done:
              ret;
            }
            """.trimIndent(),
        )
        assertInstanceOf(target, BrilLabelDefinition::class.java)
        assertEquals("done", target?.name)
    }

    fun testDestinationIdentifierIsNotAReference() {
        myFixture.configureByText(
            "test.bril",
            """
            @main {
              <caret>x: int = const 5;
            }
            """.trimIndent(),
        )
        assertNull(myFixture.file.findReferenceAt(myFixture.caretOffset))
    }
}
