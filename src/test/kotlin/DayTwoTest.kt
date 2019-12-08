import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DayTwoTest {

    @Test
    fun `sample test`() {
        val list = mutableListOf(1, 0, 0, 0)
        DayTwo.runProgram(0, list)
        assertThat(list[0]).isEqualTo(2)
    }
    @Test
    fun `longer problem one test`() {
        val list = mutableListOf(1,9,10,3,2,3,11,0,99,30,40,50)
        DayTwo.runProgram(0, list)
        assertThat(list[0]).isEqualTo(3500)
    }
    @Test
    fun `longer test`() {
        val list = mutableListOf(1,1,1,4,99,5,6,0,99)
        DayTwo.runProgram(0, list)
        assertThat(list[0]).isEqualTo(30)
    }
}
