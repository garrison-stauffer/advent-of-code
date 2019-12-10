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

class Program(val memory: MutableList<Int>, var instructionPointer: Int = 0)

abstract class Operation {
    abstract fun invoke(program: Program)
    abstract fun updateInstructionPointer(program: Program)
}

class AddOp(val paramOne: Param, val paramTwo: Param, val output: Param): Operation() {
    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 4
    }

    override fun invoke(program: Program) {
        val result = paramOne.readParameter(program.memory) + paramTwo.readParameter(program.memory)
        output.write(program.memory, result)
    }
}

class MultOp(val paramOne: Param, val paramTwo: Param, val output: Param): Operation() {
    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 4
    }

    override fun invoke(program: Program) {
        val result = paramOne.readParameter(program.memory) * paramTwo.readParameter(program.memory)
        output.write(program.memory, result)
    }
}

class InputOp(val paramOne: Param, val ioInterface: IOInterface): Operation() {
    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 2
    }

    override fun invoke(program: Program) {
        val input = ioInterface.fetchInput()
        paramOne.write(program.memory, input)
    }
}

class OutputOp(val param: Param, val ioInterface: IOInterface): Operation() {
    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 2
    }

    override fun invoke(program: Program) {
        ioInterface.postOutput(param.readParameter(program.memory))
    }
}

class JumpIfTrueOp(val paramOne: Param, val paramTwo: Param): Operation() {
    var shouldJumpInstructionPointer: Boolean = false

    override fun invoke(program: Program) {
        shouldJumpInstructionPointer = paramOne.readParameter(program.memory) != 0
    }

    override fun updateInstructionPointer(program: Program) {
        if (shouldJumpInstructionPointer) {
            val jumpToPointer = paramTwo.readParameter(program.memory)
            program.instructionPointer = jumpToPointer
        } else {
            program.instructionPointer += 3
        }
    }
}

class JumpIfFalseOp(val paramOne: Param, val paramTwo: Param): Operation() {
    var shouldJumpInstructionPointer: Boolean = false

    override fun invoke(program: Program) {
        shouldJumpInstructionPointer = paramOne.readParameter(program.memory) == 0
    }

    override fun updateInstructionPointer(program: Program) {
        if (shouldJumpInstructionPointer) {
            val jumpToPointer = paramTwo.readParameter(program.memory)
            program.instructionPointer = jumpToPointer
        } else {
            program.instructionPointer += 3
        }
    }
}

class LessThanOp(val paramOne: Param, val paramTwo: Param, val paramThree: Param): Operation() {
    override fun invoke(program: Program) {
        val input1 = paramOne.readParameter(program.memory)
        val input2 = paramTwo.readParameter(program.memory)

        val result = if (input1 < input2) 1 else 0
        paramThree.write(program.memory, result)
    }

    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 4
    }
}

class EqualOp(val paramOne: Param, val paramTwo: Param, val paramThree: Param): Operation() {
    override fun invoke(program: Program) {
        val input1 = paramOne.readParameter(program.memory)
        val input2 = paramTwo.readParameter(program.memory)

        val result = if (input1 == input2) 1 else 0
        paramThree.write(program.memory, result)
    }

    override fun updateInstructionPointer(program: Program) {
        program.instructionPointer += 4
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
    const val ADD = 1
    const val MULT = 2
    const val INPUT = 3
    const val OUTPUT = 4
    const val JUMP_IF_TRUE = 5
    const val JUMP_IF_FALSE = 6
    const val LESS_THAN = 7
    const val EQUALS = 8
    const val END = 99

    const val POSITION_MODE = 0
    const val IMMEDIATE_MODE = 1

    fun runProgram(program: Program, ioInterface: IOInterface = KeyboardIO(), tag: String = ""): Int {
        var operation = getNextOperation(program, ioInterface)

        while (operation !is Halt) {
            println("[PROGRAM_$tag]: ${operation::class.simpleName}")
            operation.invoke(program)
            operation.updateInstructionPointer(program)

            operation = getNextOperation(program, ioInterface)
        }

        println("Halt command read!")

        return program.memory[0]
    }

    private fun paramFactory(paramMode: Int, index: Int): Param {
        return when (paramMode) {
            POSITION_MODE -> PositionParam(index)
            IMMEDIATE_MODE -> ImmediateParam(index)
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
            END -> return Halt()
            else -> throw IllegalStateException("Error! Unknown Command $command at paramIndex $index, current state is $program")
        }
    }

    fun setupMonadicOperation(program: Program, constructor: (p1: Param) -> Operation): Operation {
        val index = program.instructionPointer
        val command = program.memory[index]

        val paramOneMode = (command / 100) % 10
        val paramOne = paramFactory(paramOneMode, index + 1)

        return constructor(paramOne)
    }

    fun setupDiadicOperation(program: Program, constructor: (p1: Param, p2: Param) -> Operation): Operation {
        val index = program.instructionPointer
        val command = program.memory[index]

        val paramOneMode = (command / 100) % 10
        val paramTwoMode = (command / 1000) % 10

        val paramOne = paramFactory(paramOneMode, index + 1)
        val paramTwo = paramFactory(paramTwoMode, index + 2)

        return constructor(paramOne, paramTwo)
    }

    fun setupTriadOperation(program: Program, constructor: (p1: Param, p2: Param, p3: Param) -> Operation): Operation {
        val index = program.instructionPointer
        val command = program.memory[index]

        val paramOneMode = (command / 100) % 10
        val paramTwoMode = (command / 1000) % 10
        val paramThreeMode = command / 10000

        val paramOne = paramFactory(paramOneMode, index + 1)
        val paramTwo = paramFactory(paramTwoMode, index + 2)
        val paramThree = paramFactory(paramThreeMode, index + 3)

        return constructor(paramOne, paramTwo, paramThree)
    }
}
