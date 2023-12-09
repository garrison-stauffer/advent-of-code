package twentytwentythree

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day08Test {

    @Test
    fun testPart1() {
        var input = """
            RL
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().split("\n")

        assertThat(Day08.part1(input)).isEqualTo(2)

        input = """
            LLR
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().split("\n")
        assertThat(Day08.part1(input)).isEqualTo(6)
    }
    @Test
    fun testPart2() {
        var input = """
            LR
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent().split("\n")

        assertThat(Day08.part2(input)).isEqualTo(6)
    }
}
