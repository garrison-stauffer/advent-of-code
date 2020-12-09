package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day8Test {
    @Test
    fun `test execution parsing`() {
        assertThat(Operation.parseLine("acc +42")).isEqualTo(Operation.Accumulate(42))
        assertThat(Operation.parseLine("acc -31")).isEqualTo(Operation.Accumulate(-31))
        assertThat(Operation.parseLine("jmp -31")).isEqualTo(Operation.Jump(-31))
        assertThat(Operation.parseLine("jmp +12")).isEqualTo(Operation.Jump(12))
        assertThat(Operation.parseLine("nop +0")).isEqualTo(Operation.Noop)
    }

    @Test
    fun `run test program and get result`() {
        val input = listOf(
            "nop +0",
            "acc +1",
            "jmp +4",
            "acc +3",
            "jmp -3",
            "acc -99",
            "acc +1",
            "jmp -4",
            "acc +6"
        )

        val result = Day8.part1(input)
        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `test some regex matching`() {
        val regex = "^(nop|acc).*$".toRegex()

        assertThat("nop +0".matches(regex)).isTrue()
    }

    @Test
    fun `test part 2 with exemplar input`() {
        val input = listOf(
            "nop +0",
            "acc +1",
            "jmp +4",
            "acc +3",
            "jmp -3",
            "acc -99",
            "acc +1",
            "jmp -4",
            "acc +6"
        )

        val result = Day8.part2(input)
        assertThat(result).isEqualTo(8)
    }
}
