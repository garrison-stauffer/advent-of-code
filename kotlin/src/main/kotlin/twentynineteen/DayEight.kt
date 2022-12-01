package twentynineteen

import java.io.BufferedReader
import java.io.FileReader

object DayEight {
    val INPUT_FILE_PATH = "/Users/garrisonstauffer/toast/git-repos/advent-of-code/src/main/resources/DayEightInput.txt"

    fun partOne(input: List<Int>, imageWidth: Int, imageHeight: Int): Int? {
        val frameSize = imageWidth * imageHeight
        assert(input.size % frameSize == 0)

        val image = input.asSequence().windowed(frameSize, step = frameSize)
            .minBy { it.count { it == 0 } }
            ?.filter { it == 1 || it == 2}
        return decodeImage(image!!)
    }

    fun decodeImage(image: List<Int>): Int {
        return image.filter { it == 1 || it == 2 }
            .groupBy { it }
            .map { it.value.size }
            .reduce(Int::times)
    }

    fun partTwo(input: List<Int>, imageWidth: Int, imageHeight: Int): Int {
        val frameSize = imageWidth * imageHeight

        val allLayers = input.windowed(frameSize, step = frameSize)
        val resolvedImage = allLayers.reduce { acc, list ->
            val output = mutableListOf<Int>()
            for (i in 0 until frameSize) {
                output.add(
                    if (acc[i] == 2) {
                        list[i]
                    }
                    else acc[i]
                )
            }
            output
        }

        println(resolvedImage)

        return decodeImage(resolvedImage)
    }
}

fun main() {
    val input = BufferedReader(FileReader(DayEight.INPUT_FILE_PATH)).use {
        it.readLine().map { char -> char.toString().toInt() }
    }
    println(DayEight.partOne(input, 25, 6))
    println(DayEight.partTwo(input, 25, 6))
}
