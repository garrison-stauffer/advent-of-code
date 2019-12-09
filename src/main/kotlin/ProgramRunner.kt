import java.lang.IllegalStateException

abstract class Param(val paramIndex: Int) {
    abstract fun readParameter(programMemory: List<Int>): Int
    abstract fun write(programMemory: MutableList<Int>, value: Int)
}

class PositionParam(index: Int): Param(index) {
    override fun write(programMemory: MutableList<Int>, value: Int) {
        val indexToWrite = programMemory[paramIndex]
        programMemory[indexToWrite] = value
    }

    override fun readParameter(programMemory: List<Int>): Int {
        val indexToRead = programMemory[paramIndex]
        return programMemory[indexToRead]
    }
}

class ImmediateParam(index: Int): Param(index) {
    override fun write(programMemory: MutableList<Int>, value: Int) {
        TODO("not implemented, this is not expected to happen")
    }

    override fun readParameter(programMemory: List<Int>): Int {
        return programMemory[paramIndex]
    }
}

abstract class Operation {
    abstract fun invoke(program: MutableList<Int>)
}

class AddOp(val paramOne: Param, val paramTwo: Param, val output: Param): Operation() {
    override fun invoke(program: MutableList<Int>) {
        val result = paramOne.readParameter(program) + paramTwo.readParameter(program)
        output.write(program, result)
    }
}

class MultOp(val paramOne: Param, val paramTwo: Param, val output: Param): Operation() {
    override fun invoke(program: MutableList<Int>) {
        val result = paramOne.readParameter(program) * paramTwo.readParameter(program)
        output.write(program, result)
    }
}

class InputOp(val paramOne: Param): Operation() {
    override fun invoke(program: MutableList<Int>) {
        print("[INPUT REQUESTED]: please enter a digit and press enter... ")
        val input = readLine()?.toInt() ?: error("read null value for input")
        paramOne.write(program, input)
    }
}

class OutputOp(val param: Param): Operation() {
    override fun invoke(program: MutableList<Int>) {
        println("[OUTPUT]: ${param.readParameter(program)}")
    }
}

class Halt: Operation() {
    override fun invoke(program: MutableList<Int>) {
        TODO("shouldn't be called on a halt command")
    }
}

object ProgramRunner {
    const val ADD = 1
    const val MULT = 2
    const val INPUT = 3
    const val OUTPUT = 4
    const val END = 99

    const val POSITION_MODE = 0
    const val IMMEDIATE_MODE = 1

    fun runProgram(index: Int, program: MutableList<Int>): Int {
        val command = program[index]

        val opcode = command % 100
        val paramOneMode = (command / 100) % 10
        val paramTwoMode = (command / 1000) % 10
        val paramThreeMode = command / 10000

        when (opcode) {
            ADD -> {
                val paramOne = paramFactory(paramOneMode, index + 1)
                val paramTwo = paramFactory(paramTwoMode, index + 2)
                val paramThree = paramFactory(paramThreeMode, index + 3)

                val operation = AddOp(paramOne, paramTwo, paramThree)
                operation.invoke(program)

                return runProgram(index + 4, program)
            }
            MULT -> {
                val paramOne = paramFactory(paramOneMode, index + 1)
                val paramTwo = paramFactory(paramTwoMode, index + 2)
                val paramThree = paramFactory(paramThreeMode, index + 3)

                val operation = MultOp(paramOne, paramTwo, paramThree)
                operation.invoke(program)

                return runProgram(index + 4, program)
            }
            INPUT -> {
                val paramOne = paramFactory(paramOneMode, index + 1)

                val operation = InputOp(paramOne)
                operation.invoke(program)
                return runProgram(index + 2, program)
            }
            OUTPUT -> {
                val paramOne = paramFactory(paramOneMode, index + 1)

                val operation = OutputOp(paramOne)
                operation.invoke(program)
                return runProgram(index + 2, program)
            }
            END -> return program[0]
            else -> throw IllegalStateException("Error! Unknown Command $command at paramIndex $index, current state is $program")
        }
    }

    private fun paramFactory(paramMode: Int, index: Int): Param {
        return when (paramMode) {
            POSITION_MODE -> PositionParam(index)
            IMMEDIATE_MODE -> ImmediateParam(index)
            else -> error("Unrecognized param mode: $paramMode")
        }
    }
}
