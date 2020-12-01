package twentynineteen

import java.io.BufferedReader
import java.io.FileReader
import java.lang.IllegalStateException

object DayTwo {
    const val DAY_TWO_FILE_PATH = "/Users/garrisonstauffer/toast/git-repos/advent-of-code/src/main/resources/DayTwoInput.txt"

    const val ADD = 1
    const val MULT = 2
    const val INPUT = 3
    const val OUTPUT = 4
    const val END = 99

    fun problemOne() {
        println("START problem 1")
        // iterate over list
        val reader = BufferedReader(FileReader(DAY_TWO_FILE_PATH))
        val program: MutableList<Int> = reader.readLine().split(",").map { it.toInt() }.toList().toMutableList()
        val noun = 78
        val verb = 70
        program[1] = noun
        program[2] = verb
        val result = ProgramRunner.runProgram(Program(program, 0))
        println("END   problem 1. Result: $result")
    }

    fun problemTwo() {
        println("START problem 1")
        // iterate over list
        val reader = BufferedReader(FileReader(DAY_TWO_FILE_PATH))
        val program: MutableList<Int> = reader.readLine().split(",").map { it.toInt() }.toList().toMutableList()

        for (noun in 1..99) {
            for (verb in 1..99) {
                val programCopy = program.toMutableList()
                programCopy[1] = noun
                programCopy[2] = verb
                try {
                    ProgramRunner.runProgram(Program(programCopy, 0))
                    if (programCopy[0] == 19690720) {
                        println("Success! $noun & $verb ==> ${100 * noun + verb}")
                        return
                    }
                } catch (e: IllegalStateException) {
                    println(e)
                }
            }
        }

        println("END   problem 1. ERROR")
    }
}

fun main() {
    println("starting program")
    DayTwo.problemTwo()
}

