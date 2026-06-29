package com.github.seclerp.bril

import com.github.seclerp.bril.psi.BrilConstInstruction
import com.github.seclerp.bril.psi.BrilFunction
import com.github.seclerp.bril.psi.BrilLabelDefinition
import com.github.seclerp.bril.psi.BrilNamedElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
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

    /** The name-introducing element whose name leaf contains the `<caret>`. */
    private fun definitionAtCaret(text: String): BrilNamedElement {
        myFixture.configureByText("test.bril", text)
        val leaf = myFixture.file.findElementAt(myFixture.caretOffset)
        return PsiTreeUtil.getParentOfType(leaf, BrilNamedElement::class.java)!!
    }

    fun testFindUsagesOfVariable() {
        val definition = definitionAtCaret(
            """
            @main {
              <caret>x: int = const 5;
              y: int = id x;
              z: int = add x x;
            }
            """.trimIndent(),
        )
        // x is referenced three times: `id x`, `add x x`
        assertEquals(3, myFixture.findUsages(definition).size)
    }

    fun testFindUsagesOfFunction() {
        val definition = definitionAtCaret(
            """
            @<caret>helper(): int {
              ret;
            }
            @main {
              a: int = call @helper;
              b: int = call @helper;
            }
            """.trimIndent(),
        )
        assertEquals(2, myFixture.findUsages(definition).size)
    }
}
