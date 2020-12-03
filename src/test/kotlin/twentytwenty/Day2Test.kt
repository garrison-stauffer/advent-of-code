package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day2Test {


    @Test
    fun parsePasswordInput() {
        val sampleString = "1-3 a: abcde"
        val expectedOutput = PasswordRecord(
            policy = Policy(first = 1, second = 3, mustInclude = 'a'),
            password = "abcde"
        )

        val actualOutput = Day2.parsePasswordInput(sampleString)

        assertThat(actualOutput).isEqualTo(expectedOutput)
    }

    @Test
    fun isValidPasswordForPart1() {
        val validRecord1 = Day2.parsePasswordInput("2-9 c: ccccccccc")
        val validRecord2 = Day2.parsePasswordInput("1-3 a: abcde")
        val invalidRecord = Day2.parsePasswordInput("1-3 b: cdefg")

        assertThat(validRecord1.isValidPasswordForPart1()).isTrue()
        assertThat(validRecord2.isValidPasswordForPart1()).isTrue()
        assertThat(invalidRecord.isValidPasswordForPart1()).isFalse()
    }

    @Test
    fun isValidPasswordForPart2() {
        val validRecord1 = Day2.parsePasswordInput("1-3 a: abcde")
        val invalidRecord1 = Day2.parsePasswordInput("1-3 b: cdefg")
        val invalidRecord2 = Day2.parsePasswordInput("2-9 c: ccccccccc")

        assertThat(validRecord1.isValidPasswordForPart2()).isTrue()
        assertThat(invalidRecord1.isValidPasswordForPart2()).isFalse()
        assertThat(invalidRecord2.isValidPasswordForPart2()).isFalse()
    }
}
