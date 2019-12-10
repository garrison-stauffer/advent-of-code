import java.io.BufferedReader
import java.io.FileReader

object DaySeven {
    val INPUT_FILE_PATH = "/Users/garrisonstauffer/toast/git-repos/advent-of-code/src/main/resources/DaySevenInput.txt"

    fun getAllPhaseCombos(): List<String> {
        val results = mutableListOf<String>()
        // there's gotta be a better way to do this...
        for (a in 0..4) {
            for (b in 0..4) {
                if (a == b) continue
                for (c in 0..4) {
                    if (a == c || b == c) continue
                    for (d in 0..4) {
                        if (a == d || b == d || c == d) continue
                        for (e in 0..4) {
                            if (a == e || b == e || c == e || d == e) continue
                            results.add(a.toString() + b.toString() + c.toString() + d.toString() + e.toString())
                        }
                    }
                }
            }
        }
        return results
    }

    fun getAllPhaseCombosPt2(): List<String> {
        val results = mutableListOf<String>()
        // there's gotta be a better way to do this...
        for (a in 5..9) {
            for (b in 5..9) {
                if (a == b) continue
                for (c in 5..9) {
                    if (a == c || b == c) continue
                    for (d in 5..9) {
                        if (a == d || b == d || c == d) continue
                        for (e in 5..9) {
                            if (a == e || b == e || c == e || d == e) continue
                            results.add(a.toString() + b.toString() + c.toString() + d.toString() + e.toString())
                        }
                    }
                }
            }
        }
        return results
    }

    fun partOne(memory: MutableList<Int>): Int {
        return getAllPhaseCombos().map {
            val amplifier = recursivelyBuildAmplifierChain(it, null, memory)
            amplifier.runAmplification(0)
        }.max() ?: Int.MIN_VALUE
    }

    fun recursivelyBuildAmplifierChain(phaseChain: String, head: Amplifier?, programMemory: MutableList<Int>): Amplifier {
        val phase = phaseChain[phaseChain.length - 1]

        val program = Program(programMemory.toMutableList())
        val nextAmplifier = Amplifier(program, phase.toString().toInt(), head)

        return if (phaseChain.length > 1) {
            // base case..
            recursivelyBuildAmplifierChain(phaseChain.substring(0, phaseChain.length - 1), nextAmplifier, programMemory)
        } else {
            nextAmplifier
        }
    }

    fun createAndRunAmplifiers(phaseChain: String, programMemory: MutableList<Int>): Int {
        val phase0 = phaseChain[0].toString().toInt()
        val phase1 = phaseChain[1].toString().toInt()
        val phase2 = phaseChain[2].toString().toInt()
        val phase3 = phaseChain[3].toString().toInt()
        val phase4 = phaseChain[4].toString().toInt()

        val amplifier0 = Amplifier(Program(programMemory.toMutableList()), phase0, null, "0")
        val amplifier1 = Amplifier(Program(programMemory.toMutableList()), phase1, null, "1")
        val amplifier2 = Amplifier(Program(programMemory.toMutableList()), phase2, null, "2")
        val amplifier3 = Amplifier(Program(programMemory.toMutableList()), phase3, null, "3")
        val amplifier4 = Amplifier(Program(programMemory.toMutableList()), phase4, null, "4")

        amplifier0.nextAmplifier = amplifier1
        amplifier1.nextAmplifier = amplifier2
        amplifier2.nextAmplifier = amplifier3
        amplifier3.nextAmplifier = amplifier4
        amplifier4.nextAmplifier = amplifier0

        amplifier0.runAmplification(0)
        return amplifier4.lastOutput
    }

    /*
    I think this model is bad.. I need the programs to be able to take continuous input, not just pick up where they left off.
     */
    class Amplifier(val program: Program, val phase: Int, var nextAmplifier: Amplifier?, val tag: String = "") {
        val amplifierIo = AmplifierIO(mutableListOf(phase)) {
            lastOutput = it
            nextAmplifier!!.runAmplification(it)
        }

        var lastOutput = Int.MIN_VALUE

        fun runAmplification(input: Int): Int {
            amplifierIo.inputs.add(input) // just add it to the stack

            // this is the problem.. I can't call "runProgram" every time
            return ProgramRunner.runProgram(program, amplifierIo, tag)
        }
    }

    class AmplifierIO(val inputs: MutableList<Int>, val onOutput: (Int) -> Unit): IOInterface {
        var inputIndex = 0

        override fun fetchInput(): Int {
            return inputs[inputIndex++]
        }

        override fun postOutput(value: Int) {
            onOutput(value)
        }
    }
}

fun main() {
    val reader = BufferedReader(FileReader(DaySeven.INPUT_FILE_PATH))
    val programMemory = reader.use {
        it.readLine().split(",").map{ it.toInt() }.toList().toMutableList()
    }
    println(DaySeven.partOne(programMemory))
}
