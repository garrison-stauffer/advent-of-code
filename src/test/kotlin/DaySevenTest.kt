import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DaySevenTest {
    @Test
    fun `trying amplification`() {
//
//        3,15, // sets 15 to 4
//        3,16, // sets 16 to 0
//        1002,16,10,16, // multiply [16] by 10, store in [16] => 0
//        1,16,15,15, // add [16] and [15], store in [15] => 4
//        4,15, // output [15] => 4
//        99,
//        [15] [16]
//         4,   0

//        3,15, // sets 15 to 3
//        3,16, // sets 16 to 4
//        1002,16,10,16, // multiply [16] by 10, store in [16] => 40
//        1,16,15,15, // add [16] and [15], store in [15] => 4
//        4,15, // output [15] => 43
//        99,
//        [15] [16]
//         4,   0
//
//        3,15, // sets 15 to 2
//        3,16, // sets 16 to 43
//        1002,16,10,16, // multiply [16] by 10, store in [16] => 430
//        1,16,15,15, // add [16] and [15], store in [15] => 432
//        4,15, // output [15] => 432
//        99,
//        [15] [16]
//         4,   0
        var maxOutput = DaySeven.partOne(mutableListOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0))
        assertThat(maxOutput).isEqualTo(43210)
        maxOutput = DaySeven.partOne(mutableListOf(3,23,3,24,1002,24,10,24,1002,23,-1,23,
            101,5,23,23,1,24,23,23,4,23,99,0,0))
        assertThat(maxOutput).isEqualTo(54321)
    }

    @Test
    fun `test feedback loop`() {
        var input = mutableListOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,
            27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5)
        var output = DaySeven.createAndRunAmplifiers("98765", input)
        assertThat(output).isEqualTo(139629729)
    }
}
