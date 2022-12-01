package twentytwenty

class Executable(
    var programCounter: Int = 0,
    var accumulator: Int = 0,
    val program: List<String>
) {
    companion object {
        val programRegex = """(\w+) ([\+,-]?\d+)""".toRegex()
    }

    fun runProgramAndStopBeforeRepeatedExecution() {
        val callStack = mutableSetOf<Int>()

        while (!callStack.contains(programCounter)) {
            callStack.add(programCounter)

            val currentLine = program[programCounter]

            val operation = Operation.parseLine(currentLine)
            execute(operation)
        }
    }

    fun runProgramWithResult(): ExecutionResult {
        val callStack = mutableListOf<Int>()

        while (!callStack.contains(programCounter)) {
            if (programCounter == program.size) {
                return ExecutionResult.ReachedEnd(accumulator = this.accumulator)
            }

            callStack.add(programCounter)

            val currentLine = program[programCounter]

            val operation = Operation.parseLine(currentLine)
            execute(operation)
        }

        return ExecutionResult.KilledOnRepeat(callstack = callStack)
    }

    private fun execute(operation: Operation) {
        when (operation) {
            is Operation.Noop -> programCounter++
            is Operation.Accumulate -> {
                programCounter++
                accumulator += operation.accumulation
            }
            is Operation.Jump -> {
                programCounter += operation.jumpDistance
            }
        }
    }
}

sealed class Operation {
    data class Accumulate(val accumulation: Int): Operation()

    data class Jump(val jumpDistance: Int): Operation()

    object Noop : Operation()

    companion object {
        fun parseLine(line: String): Operation {
            val results = Executable.programRegex.matchEntire(line)?.groupValues ?: error("could not parse command from: $line")

            val operation = results[1]
            val regA = results[2].toInt()

            return when (operation) {
                "nop" -> Noop
                "acc" -> Accumulate(regA)
                "jmp" -> Jump(regA)
                else -> error("unknown operation $operation on line $line")
            }
        }
    }
}

sealed class ExecutionResult {
    /**
     * callstack can be used to prune lines that won't make a difference I think? not sure...
     */
    data class KilledOnRepeat(val callstack: List<Int>): ExecutionResult()
    data class ReachedEnd(val accumulator: Int): ExecutionResult()
}

object Day8 {
    fun part1(input: List<String>): Int {
        val executable = Executable(program = input)
        executable.runProgramAndStopBeforeRepeatedExecution()

        return executable.accumulator
    }

    fun part2(input: List<String>): Int {
        val nopJmpRegex = "^(nop|jmp).*$".toRegex()

        val first = (0..input.size).asSequence()
            .filter { input[it].matches(nopJmpRegex) }
            .map {
                val listCopy = input.toMutableList()
                val lineOfInterest = listCopy[it]
                listCopy[it] = when {
                    lineOfInterest.startsWith("nop") -> {
                        lineOfInterest.replace("nop", "jmp")
                    }
                    lineOfInterest.startsWith("jmp") -> {
                        lineOfInterest.replace("jmp", "nop")
                    }
                    else -> {
                        error("wtf is this string! $lineOfInterest at index $it")
                    }
                }
                listCopy
            }
            .map { Executable(program = it).runProgramWithResult() }
            .filter { it is ExecutionResult.ReachedEnd }
            .first()

        return (first as ExecutionResult.ReachedEnd).accumulator
    }
}

fun main() {
    val input = readFile(day = 8)
    val part1Result = Day8.part1(input) // should be 1859
    val part2Result = Day8.part2(input) // should be 1235

    println("""
        part1: $part1Result
        part2: $part2Result
    """.trimIndent())
}
