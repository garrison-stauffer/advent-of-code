package twentynineteen

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import twentynineteen.DayFour.containsDuplicateNumber
import twentynineteen.DayFour.hasTwoOfSameNumber
import twentynineteen.DayFour.isAlwaysIncreasingNumber

class DayFourTest {

    @Test
    fun `test something`() {
        assertThat(true).isTrue()
    }

    @Test
    fun `increasing test should work`() {
        assertThat(isAlwaysIncreasingNumber(123444)).isTrue()
        assertThat(isAlwaysIncreasingNumber(111111)).isTrue()
        assertThat(isAlwaysIncreasingNumber(12343)).isFalse()
        assertThat(isAlwaysIncreasingNumber(111111)).isTrue()
    }

    @Test
    fun `duplicates should be found`() {
        assertThat(containsDuplicateNumber(112345)).isTrue()
        assertThat(containsDuplicateNumber(111111)).isTrue()
        assertThat(containsDuplicateNumber(12345)).isFalse()
        assertThat(containsDuplicateNumber(1123455)).isTrue()
    }

    @Test
    fun `updated duplicate logic`() {
        assertThat(hasTwoOfSameNumber(123456)).isFalse()
        assertThat(hasTwoOfSameNumber(113456)).isTrue()
        assertThat(hasTwoOfSameNumber(111456)).isFalse()
        assertThat(hasTwoOfSameNumber(123444)).isFalse()
        assertThat(hasTwoOfSameNumber(111122)).isTrue()
    }
}
