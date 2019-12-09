import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ProgramRunnerTest {

    @Test
    fun `Day 2's sample test`() {
        val list = mutableListOf(1, 0, 0, 0)
        ProgramRunner.runProgram(Program(list, 0))
        assertThat(list[0]).isEqualTo(2)
    }
    @Test
    fun `Day 2's longer problem one test`() {
        val list = mutableListOf(1,9,10,3,2,3,11,0,99,30,40,50)
        ProgramRunner.runProgram(Program(list, 0))
        assertThat(list[0]).isEqualTo(3500)
    }
    @Test
    fun `Day 2's longer test`() {
        val list = mutableListOf(1,1,1,4,99,5,6,0,99)
        ProgramRunner.runProgram(Program(list, 0))
        assertThat(list[0]).isEqualTo(30)
    }

    @Test
    fun `testing with immediate mode examplars`() {
        var list = mutableListOf(1002,4,3,4,33)
        ProgramRunner.runProgram(Program(list, 0))
        assertThat(list[4]).isEqualTo(99)

        list = mutableListOf(1101,100,-1,4,0)
        ProgramRunner.runProgram(Program(list, 0))
        assertThat(list[4]).isEqualTo(99)
    }

    @Test
    fun `Testing less than op`() {
        var list = mutableListOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9)
        ProgramRunner.runProgram(Program(list, 0))
    }
}
