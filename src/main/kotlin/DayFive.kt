import java.io.BufferedReader
import java.io.FileReader

object DayFive {
    val DAY_FIVE_INPUT_FILE_PATH = "/Users/garrisonstauffer/toast/git-repos/advent-of-code/src/main/resources/DayFiveInput.txt"

    fun parseProgram(filepath: String): MutableList<Int> {
        val reader = BufferedReader(FileReader(filepath))
        return reader.use {
            it.readLine().split(",")
                .map { it.toInt() }
                .toList().toMutableList()
        }
    }

    fun partOne() {
        val program = parseProgram(DAY_FIVE_INPUT_FILE_PATH)
    }

    fun partTwo() {

    }



    fun readFile(filepath: String): MutableList<Int> {
        val reader = BufferedReader(FileReader(filepath))
        return reader.use {
            reader.readLine().split(",").map { it.toInt() }.toList().toMutableList()
        }
    }
}

fun main() {
    val program = DayFive.readFile(DayFive.DAY_FIVE_INPUT_FILE_PATH)
    ProgramRunner.runProgram(Program(program, 0))
//    var list = mutableListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
//        1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
//        999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)
//    ProgramRunner.runProgram(Program(list, 0))
}
