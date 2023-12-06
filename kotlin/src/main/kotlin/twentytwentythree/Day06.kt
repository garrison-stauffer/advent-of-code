package twentytwentythree

import kotlin.math.*

object Day06 {
    val input: List<String> = Day05::class.java.getResource("/twentytwentythree/day06pt1.txt")!!
        .readText()
        .lines()
        .filter(String::isNotBlank)

    fun part1(input: List<String>): Long {
        return parseInputPt1(input).map { it.calculateNumberOfDurationsHeld() }.fold(1L) { acc, l -> acc * l }
    }

    fun part2(input: List<String>): Long {
        return parseInputPt2(input).calculateNumberOfDurationsHeld()
    }

    fun parseInputPt1(input: List<String>): List<RaceRecord> {
        val times = "\\d+".toRegex().findAll(input[0]).map { it.value.toLong() }.toList()
        val distance = "\\d+".toRegex().findAll(input[1]).map { it.value.toLong() }.toList()

        return times.mapIndexed { index, time ->
            RaceRecord(time, distance[index])
        }
    }

    fun parseInputPt2(input: List<String>): RaceRecord {
        val time = "\\d+".toRegex().findAll(input[0]).map { it.value }.joinToString(separator = "").toLong()
        val distance = "\\d+".toRegex().findAll(input[1]).map { it.value }.joinToString(separator = "").toLong()

        return RaceRecord(time, distance)
    }

    // part 1 is just a quadratic function - I think. Distance traveled is: t * (total_time - t) = distance
    // => -t^2 + total_time * t - distance = 0
    data class RaceRecord(val time: Long, val distance: Long) {
        fun calculateNumberOfDurationsHeld(): Long {
            // (-b ± sqrt(b^2 - 4ac)) / 2a
            val sqrtBSquaredMinus4AC = sqrt(time.toDouble().pow(2) - 4 * (/*a*/-1) * (/*c*/-1 * distance))
            val twoA = 2 * -1
            val minusB = -1 * time

            val root1 = (minusB + sqrtBSquaredMinus4AC) / twoA
            val root2 = (minusB - sqrtBSquaredMinus4AC) / twoA

            val first = ceil(min(root1, root2))
            val last = floor(max(root1, root2))

            return last.toLong() - first.toLong() + 1
        }

    }
}

fun main() {
    val part1 = Day06.part1(Day06.input)
    val part2 = Day06.part2(Day06.input)

    println("part1 result: $part1")
    println("part2 result: $part2")
}
