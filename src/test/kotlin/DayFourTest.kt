import DayFour.testForTwoConsecutiveDigits
import DayFour.testIncreasingSequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DayFourTest {

    @Test
    fun `test something`() {
        assertThat(true).isTrue()
    }

    @Test
    fun `increasing test should work`() {
        assertThat(testIncreasingSequence(123444)).isTrue()
        assertThat(testIncreasingSequence(111111)).isTrue()
        assertThat(testIncreasingSequence(12343)).isFalse()
        assertThat(testIncreasingSequence(111111)).isTrue()
    }

    @Test
    fun `duplicates should be found`() {
        assertThat(testForTwoConsecutiveDigits(112345)).isTrue()
        assertThat(testForTwoConsecutiveDigits(111111)).isTrue()
        assertThat(testForTwoConsecutiveDigits(12345)).isFalse()
        assertThat(testForTwoConsecutiveDigits(1123455)).isTrue()
    }
}
