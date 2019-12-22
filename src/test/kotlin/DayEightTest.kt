import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DayEightTest {

    @Test
    fun `simple test`() {
        assertThat(true).isTrue()
    }

    @Test
    fun `easy input`() {
        var output = DayEight.partOne(listOf(0, 1), 1, 1);
        assertThat(output).isEqualTo(1)

        output = DayEight.partOne(listOf(0, 2, 2, 1, 1, 1, 1, 2, 1, 1), 5, 1)
        assertThat(output).isEqualTo(4)
    }
}
