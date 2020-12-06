package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day6Test {

    @Test
    fun testBorrowedDay4Fn() {
        val output = Day4.groupPassportRowsTogether(listOf(
            "abc",
            "",
            "a",
            "b",
            "c",
            "",
            "ab",
            "ac",
            "",
            "a",
            "a",
            "a",
            "a",
            "",
            "b"
        )).map { it.replace("\\s+".toRegex(), "").trim() }

        assertThat(output).isEqualTo(listOf(
            "abc",
            "abc",
            "abac",
            "aaaa",
            "b"
        ))
    }

    @Test
    fun testAnswerPart1() {
        val output = Day6.part1(listOf(
            "abc",
            "",
            "a",
            "b",
            "c",
            "",
            "ab",
            "ac",
            "",
            "a",
            "a",
            "a",
            "a",
            "",
            "b"
        ))

        assertThat(output).isEqualTo(11)
    }

    @Test
    fun testMapLookup() {
        assertThat(Day6.getAlphabeticalPosition('a')).isEqualTo(0)
        assertThat(Day6.getAlphabeticalPosition('z')).isEqualTo(25)
        assertThat(Day6.getAlphabeticalPosition('m')).isEqualTo(12)
        assertThat(Day6.getAlphabeticalPosition('n')).isEqualTo(13)
    }

    @Test
    fun testResolvedAnswerString() {
        assertThat(Day6.mapAnswerStringToNumber("abcdefghijklmnopqrstuvwxyz")).isEqualTo(67108863)
    }

    @Test
    fun testFamilyAnswerSpliting() {
        assertThat(Day6.countSharedAnswersInFamily(
            "abcd bcd bxyzc"
        )).isEqualTo(2)
    }

    @Test
    fun testPart2ForSampleInput() {
        val sampleInput = listOf(
            "abc",
            "",
            "a",
            "b",
            "c",
            "",
            "ab",
            "ac",
            "",
            "a",
            "a",
            "a",
            "a",
            "",
            "b"
        )

        assertThat(Day6.part2(sampleInput)).isEqualTo(6)
    }



}