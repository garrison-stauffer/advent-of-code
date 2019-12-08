import java.io.BufferedReader
import java.io.FileReader
import java.lang.IllegalStateException

object DayTwo {
    const val DAY_TWO_FILE_PATH = "/Users/garrisonstauffer/toast/git-repos/advent-of-code/src/main/resources/DayTwoInput.txt"

    const val ADD = 1
    const val MULT = 2
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
        val result = runProgram(0, program)
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
                    runProgram(0, programCopy)
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

    fun runProgram(index: Int, program: MutableList<Int>): Int {
        val command = program[index]
        when (command) {
            ADD -> {
                val paramOneIndex = program[index + 1]
                val paramTwoIndex = program[index + 2]
                val outputIndex = program[index + 3]

                val paramOne = program[paramOneIndex]
                val paramTwo = program[paramTwoIndex]
                program[outputIndex] = paramOne + paramTwo
                return runProgram(index + 4, program)
            }
            MULT -> {
                val paramOnePointer = program[index + 1]
                val paramTwoPointer = program[index + 2]
                val outputIndex = program[index + 3]

                val paramOne = program[paramOnePointer]
                val paramTwo = program[paramTwoPointer]
                program[outputIndex] = paramOne * paramTwo
                return runProgram(index + 4, program)
            }
            END -> return program[0]
            else -> throw IllegalStateException("Error! Unknown Command $command at index $index, current state is $program")
        }
    }
}

fun main() {
    println("starting program")
    DayTwo.problemTwo()
}

