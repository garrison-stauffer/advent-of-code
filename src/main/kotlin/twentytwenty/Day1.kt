package twentytwenty

import java.io.File

object Day1 {
    const val PT1_INPUT = "/Users/garrisonstauffer/toast/git-repos/advent-of-code/src/main/resources/twentytwenty/day1pt1.txt"
    fun problem1(goalSum: Long): Long {
        val input = mutableSetOf<Long>()
        File(PT1_INPUT).forEachLine {
            input.add(it.toLong())
        }

        for (value in input) {
            val target = goalSum - value
            if (input.contains(target)) {
                return target * value
            }
        }

        error("Did not find any match")
    }

    fun problem2(goalSum: Long): Long {
        val input = mutableSetOf<Long>()
        File(PT1_INPUT).forEachLine {
            input.add(it.toLong())
        }

        for (first in input) {
            for (second in input) {
                val target = goalSum - first - second

                if (input.contains(target)) {
                    print("Result found for $first + $second + $target = ${first + second + target} == $goalSum")
                    return target * first * second
                }
            }
        }

        error("Did not find any match")
    }

}

fun main() {
    val pt1Result = Day1.problem1(2020)
    val pt2Result = Day1.problem2(2020)

    println("Part 1: $pt1Result")
    println("Part 2: $pt2Result")
}
