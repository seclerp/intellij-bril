package com.github.seclerp.bril

import com.github.seclerp.bril.psi.BrilArg
import com.github.seclerp.bril.psi.BrilConstInstruction
import com.github.seclerp.bril.psi.BrilFile
import com.github.seclerp.bril.psi.BrilFunction
import com.github.seclerp.bril.psi.BrilLabelDefinition
import com.github.seclerp.bril.psi.BrilValueOperation
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class BrilPsiTest : BasePlatformTestCase() {
    private fun parse(text: String): BrilFile =
        myFixture.configureByText("test.bril", text) as BrilFile

    fun testNameIntroducingElements() {
        val file = parse(
            """
            @fib(n: int): int {
              one: int = const 1;
              cond: bool = lt n one;
            .base:
              ret n;
            }
            """.trimIndent(),
        )

        val function = PsiTreeUtil.findChildOfType(file, BrilFunction::class.java)!!
        assertEquals("fib", function.name)
        assertEquals("@fib", function.nameIdentifier?.text)
        // navigation places the caret on the name, not the leading '@'
        assertEquals(function.nameIdentifier?.textOffset, function.textOffset)

        val arg = PsiTreeUtil.findChildOfType(file, BrilArg::class.java)!!
        assertEquals("n", arg.name)

        val const = PsiTreeUtil.findChildOfType(file, BrilConstInstruction::class.java)!!
        assertEquals("one", const.name)

        val vop = PsiTreeUtil.findChildOfType(file, BrilValueOperation::class.java)!!
        assertEquals("cond", vop.name)

        val label = PsiTreeUtil.findChildOfType(file, BrilLabelDefinition::class.java)!!
        assertEquals("base", label.name)
        assertEquals(".base", label.nameIdentifier?.text)
    }
}
