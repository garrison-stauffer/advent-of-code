package twentytwentythree

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day02Test {

    @Test
    fun testParsingGameLine() {
        assertThat(Day02.parseGameLine("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"))
            .isEqualTo(Day02.CubeGame(1, listOf(
                Day02.CubeCount(red = 4, blue = 3, green = 0),
                Day02.CubeCount(red = 1, blue = 6, green = 2),
                Day02.CubeCount(red = 0, blue = 0, green = 2),
            )))
    }

    @Test
    fun testPart1SampleInput() {
        val input = """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """.trimIndent().split("\n")

        assertThat(Day02.part1(input)).isEqualTo(8)
    }
}
