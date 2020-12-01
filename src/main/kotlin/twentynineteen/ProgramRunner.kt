import java.lang.IllegalStateException

interface IOInterface {
    fun fetchInput(): Int
    fun postOutput(value: Int)
}

class KeyboardIO: IOInterface {
    override fun fetchInput(): Int {
        print("[INPUT REQUESTED]: please enter a digit and press enter... ")
        return readLine()?.toInt() ?: error("read null value for input")
    }

    override fun postOutput(value: Int) {
        println("[OUTPUT]: $value")
    }
}

abstract class Param(val paramIndex: Int) {
    abstract fun readParameter(program: Program): Long
    abstract fun write(program: Program, value: Long)
}

class PositionParam(index: Int): Param(index) {
    override fun write(program: Program, value: Long) {
        val programMemory = program.memory
        val indexToWrite = programMemory[paramIndex]
        programMemory[indexToWrite.toInt()] = value
    }

    override fun readParameter(program: Program): Long {
        val programMemory = program.memory
        val indexToRead = programMemory[paramIndex]
        return programMemory[indexToRead.toInt()]
    }
}

class ImmediateParam(index: Int): Param(index) {
    override fun write(program: Program, value: Long) {
        TODO("not implemented, this is not expected to happen")
    }

    override fun readParameter(program: Program): Long {
        val programMemory = program.memory
        return programMemory[paramIndex]
    }
}

class RelativeParam(index: Int): Param(index) {
    override fun readParameter(program: Program): Long {
        return program.run {
            val relativeChange = memory[paramIndex]
            memory[(relativeBase + relativeChange).toInt()]
        }
    }

    override fun write(program: Program, value: Long) {
        program.apply {
            val relativeChange = memory[paramIndex]
            val indexToWriteTo = memory[(relativeBase + relativeChange).toInt()]
            memory[indexToWriteTo.toInt()] = value
        }
    }
}

class Program(initValues: MutableList<Int> = mutableListOf(), initLongValues: MutableList<Long> = mutableListOf(), var instructionPointer: Int = 0, var relativeBase: Long = 0) {
    val memory: MutableList<Long> = (initValues.map(Int::toLong) + initLongValues + MutableList(size = 10000, init = { 0L })).toMutableList()
    val int: Int = 5
}

abstract class Operation {
    abstract fun invoke(program: Program)
    abstract fun updateInstructionPointer(program: Program)
}

class AddOp(val paramOne: Param, val paramTwo: Param, val output: Param): Operation() {
    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 4
    }

    override fun invoke(program: Program) {
        val result = paramOne.readParameter(program) + paramTwo.readParameter(program)
        output.write(program, result)
    }
}

class MultOp(val paramOne: Param, val paramTwo: Param, val output: Param): Operation() {
    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 4
    }

    override fun invoke(program: Program) {
        val result = paramOne.readParameter(program) * paramTwo.readParameter(program)
        output.write(program, result)
    }
}

class InputOp(val paramOne: Param, val ioInterface: IOInterface): Operation() {
    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 2
    }

    override fun invoke(program: Program) {
        val input = ioInterface.fetchInput().toLong()
        paramOne.write(program, input)
    }
}

class OutputOp(val param: Param, val ioInterface: IOInterface): Operation() {
    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 2
    }

    override fun invoke(program: Program) {
        ioInterface.postOutput(param.readParameter(program).toInt())
    }
}

class JumpIfTrueOp(val paramOne: Param, val paramTwo: Param): Operation() {
    var shouldJumpInstructionPointer: Boolean = false

    override fun invoke(program: Program) {
        shouldJumpInstructionPointer = paramOne.readParameter(program) != 0L
    }

    override fun updateInstructionPointer(program: Program) {
        if (shouldJumpInstructionPointer) {
            val jumpToPointer = paramTwo.readParameter(program)
            program.instructionPointer = jumpToPointer.toInt()
        } else {
            program.instructionPointer += 3
        }
    }
}

class JumpIfFalseOp(val paramOne: Param, val paramTwo: Param): Operation() {
    var shouldJumpInstructionPointer: Boolean = false

    override fun invoke(program: Program) {
        shouldJumpInstructionPointer = paramOne.readParameter(program) == 0L
    }

    override fun updateInstructionPointer(program: Program) {
        if (shouldJumpInstructionPointer) {
            val jumpToPointer = paramTwo.readParameter(program)
            program.instructionPointer = jumpToPointer.toInt()
        } else {
            program.instructionPointer += 3
        }
    }
}

class LessThanOp(val paramOne: Param, val paramTwo: Param, val paramThree: Param): Operation() {
    override fun invoke(program: Program) {
        val input1 = paramOne.readParameter(program)
        val input2 = paramTwo.readParameter(program)

        val result = if (input1 < input2) 1L else 0L
        paramThree.write(program, result)
    }

    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 4
    }
}

class EqualOp(val paramOne: Param, val paramTwo: Param, val paramThree: Param): Operation() {
    override fun invoke(program: Program) {
        val input1 = paramOne.readParameter(program)
        val input2 = paramTwo.readParameter(program)

        val result = if (input1 == input2) 1L else 0L
        paramThree.write(program, result)
    }

    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 4
    }
}

class UpdateRelativeParam(val paramOne: Param): Operation() {
    override fun invoke(program: Program) {
        val input = paramOne.readParameter(program)

        val newRelativeBase = program.relativeBase + input
        program.relativeBase = newRelativeBase
    }

    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 2
    }
}

class Halt: Operation() {
    override fun updateInstructionPointer(program: Program) {
        // no-op
    }

    override fun invoke(program: Program) {
        // no-op
    }
}

object ProgramRunner {
    const val ADD = 1L
    const val MULT = 2L
    const val INPUT = 3L
    const val OUTPUT = 4L
    const val JUMP_IF_TRUE = 5L
    const val JUMP_IF_FALSE = 6L
    const val LESS_THAN = 7L
    const val EQUALS = 8L
    const val ADJUST_BASE = 9L
    const val END = 99L

    const val POSITION_MODE = 0L
    const val IMMEDIATE_MODE = 1L
    const val RELATIVE_MODE = 2L

    fun runProgram(program: Program, ioInterface: IOInterface = KeyboardIO(), tag: String = ""): Int {
        var operation = getNextOperation(program, ioInterface)

        while (operation !is Halt) {
//            println("[PROGRAM_$tag]: ${operation::class.simpleName}")
            operation.invoke(program)
            operation.updateInstructionPointer(program)

            operation = getNextOperation(program, ioInterface)
        }

        println("Halt command read!")

        return program.memory[0].toInt()
    }

    private fun paramFactory(paramMode: Int, index: Int): Param {
        return when (paramMode.toLong()) {
            POSITION_MODE -> PositionParam(index)
            IMMEDIATE_MODE -> ImmediateParam(index)
            RELATIVE_MODE -> RelativeParam(index)
            else -> error("Unrecognized param mode: $paramMode")
        }
    }

    fun getNextOperation(program: Program, ioInterface: IOInterface): Operation {
        val index = program.instructionPointer
        val command = program.memory[index]

        val opcode = command % 100

        return when (opcode) {
            ADD -> setupTriadOperation(program, ::AddOp)
            MULT -> setupTriadOperation(program, ::MultOp)
            INPUT -> setupMonadicOperation(program) { param ->
                InputOp(param, ioInterface)
            }
            OUTPUT -> setupMonadicOperation(program) { param ->
                OutputOp(param, ioInterface)
            }
            JUMP_IF_TRUE -> setupDiadicOperation(program, ::JumpIfTrueOp)
            JUMP_IF_FALSE -> setupDiadicOperation(program, ::JumpIfFalseOp)
            LESS_THAN -> setupTriadOperation(program, ::LessThanOp)
            EQUALS -> setupTriadOperation(program, ::EqualOp)
            ADJUST_BASE -> setupMonadicOperation(program, ::UpdateRelativeParam)
            END -> return Halt()
            else -> throw IllegalStateException("Error! Unknown Command $command at paramIndex $index, current state is $program")
        }
    }

    fun setupMonadicOperation(program: Program, constructor: (p1: Param) -> Operation): Operation {
        val index = program.instructionPointer
        val command = program.memory[index]

        val paramOneMode = (command / 100) % 10
        val paramOne = paramFactory(paramOneMode.toInt(), index + 1)

        return constructor(paramOne)
    }

    fun setupDiadicOperation(program: Program, constructor: (p1: Param, p2: Param) -> Operation): Operation {
        val index = program.instructionPointer
        val command = program.memory[index]

        val paramOneMode = (command / 100) % 10
        val paramTwoMode = (command / 1000) % 10

        val paramOne = paramFactory(paramOneMode.toInt(), index + 1)
        val paramTwo = paramFactory(paramTwoMode.toInt(), index + 2)

        return constructor(paramOne, paramTwo)
    }

    fun setupTriadOperation(program: Program, constructor: (p1: Param, p2: Param, p3: Param) -> Operation): Operation {
        val index = program.instructionPointer
        val command = program.memory[index]

        val paramOneMode = (command / 100) % 10
        val paramTwoMode = (command / 1000) % 10
        val paramThreeMode = command / 10000

        val paramOne = paramFactory(paramOneMode.toInt(), index + 1)
        val paramTwo = paramFactory(paramTwoMode.toInt(), index + 2)
        val paramThree = paramFactory(paramThreeMode.toInt(), index + 3)

        return constructor(paramOne, paramTwo, paramThree)
    }
}
