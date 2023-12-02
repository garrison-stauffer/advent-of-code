package twentytwentythree

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day01Test {
    @Test
    fun testParseFromLine() {
        assertThat(Day01.parseValueFromLine("1abc2")).isEqualTo(12)
        assertThat(Day01.parseValueFromLine("pqr3stu8vwx")).isEqualTo(38)
        assertThat(Day01.parseValueFromLine("a1b2c3d4e5f")).isEqualTo(15)
        assertThat(Day01.parseValueFromLine("treb7uchet")).isEqualTo(77)
    }

    @Test
    fun testParseFromLineWithStrings() {
        val testCases = mapOf(
            "one" to 11,
            "oneight" to 18,
            "two1nine" to 29,
            "eightwothree" to 83,
            "abcone2threexyz" to 13,
            "xtwone3four" to 24,
            "4nineeightseven2" to 42,
            "zoneight234" to 14,
            "7pqrstsixteen" to 76,
        )

        testCases.forEach {(test, expect) ->
            assertThat(Day01.parseValueFromLine(test)).isEqualTo(expect.toLong())
        }
    }

    @Test
    fun part1SampleTest() {
        assertThat(Day01.problem1(
            """
                1abc2 
                pqr3stu8vwx
                a1b2c3d4e5f
                treb7uchet
            """.trimIndent()
                .split("\n")
        )).isEqualTo(142)
    }

    @Test
    fun part2BuildTrie() {
        val trie = Day01.DigitTrie.newInstance()
        assertThat(trie.hasString("one")).isTrue()
        assertThat(trie.hasString("two")).isTrue()
        assertThat(trie.hasString("three")).isTrue()
        assertThat(trie.hasString("four")).isTrue()
        assertThat(trie.hasString("five")).isTrue()
        assertThat(trie.hasString("six")).isTrue()
        assertThat(trie.hasString("seven")).isTrue()
        assertThat(trie.hasString("eight")).isTrue()
        assertThat(trie.hasString("nine")).isTrue()
        assertThat(trie.hasString("tenn")).isFalse()
        assertThat(trie.hasString("xyz")).isFalse()
    }
}
