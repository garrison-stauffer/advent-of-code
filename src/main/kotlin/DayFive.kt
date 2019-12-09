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
    ProgramRunner.runProgram(Program(program, 0), KeyboardIO())
}
