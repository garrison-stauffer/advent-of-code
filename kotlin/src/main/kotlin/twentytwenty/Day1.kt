package twentytwenty

import java.io.File

object Day1 {
    val input: List<Long> = Day1::class.java.getResource("/twentytwenty/day1pt1.txt")
            .readText()
            .lines()
            .filter(String::isNotBlank)
            .map { it.toLong() }

    fun problem1(goalSum: Long): Long {
        for (value in input) {
            val target = goalSum - value
            if (input.contains(target)) {
                return target * value
            }
        }

        error("Did not find any match")
    }

    fun problem2(goalSum: Long): Long {
        for (first in input) {
            for (second in input) {
                val target = goalSum - first - second

                if (target in input) {
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

    println("Part 1: $pt1Result") // correct answer is 181044
    println("Part 2: $pt2Result") // correct answer is 82660352
}
