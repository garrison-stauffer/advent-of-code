package twentytwentythree

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day06Test {
    @Test
    fun testParsingInput() {
        val input = Day06.parseInputPt1("""
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent().split("\n"))

        assertThat(input).isEqualTo(listOf(
            Day06.RaceRecord(7, 9),
            Day06.RaceRecord(15, 40),
            Day06.RaceRecord(30, 200),
        ))
    }

    @Test
    fun testPart1() {
        val input = Day06.RaceRecord(7, 9)

        assertThat(input.calculateNumberOfDurationsHeld()).isEqualTo(4)
    }

}
