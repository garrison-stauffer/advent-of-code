package twentynineteen

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DayNineTest {
    @Test fun `input should be printed to output`() {
        val programInput = mutableListOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99)
        val program = Program(programInput)

        val testableIO = TestIO()
        ProgramRunner.runProgram(program, ioInterface = testableIO)

        assertThat(testableIO.outputs).isEqualTo(programInput)
    }

    @Test fun `can it output a 16 digit number?`() {
        val programInput = mutableListOf(1102,34915192,34915192,7,4,7,99,0)
        val program = Program(programInput)
        val io = TestIO()
        ProgramRunner.runProgram(program, io)

        assertThat(io.outputs[0]).isEqualTo(34915192L * 34915192L)
    }
}
