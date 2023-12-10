package twentytwentythree

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day09Test {

    @Test
    fun testPart1() {
        val input = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent().split("\n")

        assertThat(Day09.part1(input)).isEqualTo(114L)
    }
}
