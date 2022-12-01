package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day4Test {
    val testFileInput = this.javaClass.classLoader.getResource("twentytwenty/day4test.txt").readText().lines()
    val testFileInputPt2 = this.javaClass.classLoader.getResource("twentytwenty/day4pt2test.txt").readText().lines()

    @Test
    fun `it should concatenate strings as expected, one line is one passport`() {
        val transformedList = Day4.groupPassportRowsTogether(testFileInput)

        assertThat(transformedList).isEqualTo(
            listOf(
                "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm",
                "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884 hcl:#cfa07d byr:1929",
                "hcl:#ae17e1 iyr:2013 eyr:2024 ecl:brn pid:760753108 byr:1931 hgt:179cm",
                "hcl:#cfa07d eyr:2025 pid:166559648 iyr:2011 ecl:brn hgt:59in"
            )
        )
    }

    @Test
    fun `it should pass the test run`() {
        assertThat(Day4.part1(testFileInput)).isEqualTo(2)
    }

    @Test
    fun `height test should work`() {
        assertThat(Attributes.HEIGHT.isValid("60in")).isTrue()
        assertThat(Attributes.HEIGHT.isValid("190cm")).isTrue()
        assertThat(Attributes.HEIGHT.isValid("190in")).isFalse()
        assertThat(Attributes.HEIGHT.isValid("60cm")).isFalse()
    }

    @Test
    fun `hair color test should work`() {
        assertThat(Attributes.HAIR_COLOR.isValid("#123abc")).isTrue()
        assertThat(Attributes.HAIR_COLOR.isValid("#123abz")).isFalse()
        assertThat(Attributes.HAIR_COLOR.isValid("123abc")).isFalse()
    }

    @Test
    fun `eye color test should work`() {
        assertThat(Attributes.EYE_COLOR.isValid("brn")).isTrue()
        assertThat(Attributes.EYE_COLOR.isValid("wat")).isFalse()
    }

    @Test
    fun `passport id check should work`() {
        assertThat(Attributes.PASSPORT_ID.isValid("000000001")).isTrue()
        assertThat(Attributes.PASSPORT_ID.isValid("0123456789")).isFalse()
    }

    @Test
    fun `part 2 test with invalid entries`() {
        assertThat(Day4.part2(testFileInputPt2)).isEqualTo(4)
    }
}
