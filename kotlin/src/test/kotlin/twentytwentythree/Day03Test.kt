package twentytwentythree

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day03Test {

    @Test
    fun testEngineSchematicLineParsing() {
        var sum = Day03.part1(listOf("*1234"))
        assertThat(sum).isEqualTo(1234)

        sum = Day03.part1(
            """
                467..114..
                ...*......
                ..35..633.
                ......#...
                617*......
                .....+.58.
                ..592.....
                ......755.
                ...${'$'}.*....
                .664.598..
            """.trimIndent().split("\n")
        )
        assertThat(sum).isEqualTo(4361)
    }

    @Test
    fun testLineParsingV2() {
        var line = Day03.processLineV2(".664.598..", 0).components
        assertThat(line).isEqualTo(
            mapOf(
                1 to Day03.EngineComponent.PartNumber("0_1", 664),
                2 to Day03.EngineComponent.PartNumber("0_1", 664),
                3 to Day03.EngineComponent.PartNumber("0_1", 664),
                5 to Day03.EngineComponent.PartNumber("0_5", 598),
                6 to Day03.EngineComponent.PartNumber("0_5", 598),
                7 to Day03.EngineComponent.PartNumber("0_5", 598),
            )
        )

        line = Day03.processLineV2("*.664", 1).components
        assertThat(line).isEqualTo(
            mapOf(
                0 to Day03.EngineComponent.Cog,
                2 to Day03.EngineComponent.PartNumber("1_2", 664),
                3 to Day03.EngineComponent.PartNumber("1_2", 664),
                4 to Day03.EngineComponent.PartNumber("1_2", 664),
            )
        )
    }

    @Test
    fun testPart2() {
        val result = Day03.part2("""
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
        """.trimIndent().split("\n"))
        assertThat(result).isEqualTo(467835L)
    }
}
