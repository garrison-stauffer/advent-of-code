package twentytwentythree

import kotlin.math.max

object Day02 {
    val input: List<String> = Day01::class.java.getResource("/twentytwentythree/day02pt1.txt")!!
        .readText()
        .lines()
        .filter(String::isNotBlank)

    fun part1(input: List<String>): Long {
        return input.map { parseGameLine(it) }
            .filter { game ->
                game.getSeenCubes().let { count ->
                    count.red <= 12 && count.green <= 13 && count.blue <= 14
                }
            }
            .sumOf { it.id }
            .toLong()
    }

    fun part2(input: List<String>): Long {
        return input.map { parseGameLine(it) }
            .map { game ->
                game.getSeenCubes().let { count ->
                    count.red * count.green * count.blue
                }.toLong()
            }
            .sumOf { it }
    }

    fun parseGameLine(line: String): CubeGame {
        val rex = """Game (?<id>\d+): (?<round>.*)""".toRegex()
        rex.matchEntire(line)!!.let {
            val id = it.groupValues[1].toInt()
            val draws = mutableListOf<CubeCount>()
            for (round in it.groupValues[2].split("; ")) {
                var red = 0
                var blue = 0
                var green = 0

                val drawsStr = round.split(", ")
                val drawRex = """(?<num>\d+) (?<color>\w+)""".toRegex()
                drawsStr.forEach { drawStr ->
                    val match = drawRex.matchEntire(drawStr)!!
                    val totalCubes = match.groupValues[1].toInt()
                    when (val color = match.groupValues[2]) {
                        "red" -> red += totalCubes
                        "blue" -> blue += totalCubes
                        "green" -> green += totalCubes
                        else -> throw IllegalStateException("unexpected color cube $color in round $id")
                    }
                }

                draws.add(CubeCount(red = red, blue = blue, green = green))
            }
            return CubeGame(it.groupValues[1].toInt(), draws)
        }
    }

    data class CubeGame(val id: Int, val draws: List<CubeCount>) {
        fun getSeenCubes(): CubeCount {
            return draws.fold(CubeCount()) { acc, draw ->
                CubeCount(
                    red = max(acc.red, draw.red),
                    green = max(acc.green, draw.green),
                    blue = max(acc.blue, draw.blue),
                )
            }
        }
    }
    data class CubeCount(val red: Int = 0, val blue: Int = 0, val green: Int = 0)
}

fun main() {
    val part1Result = Day02.part1(Day02.input)
    val part2Result = Day02.part2(Day02.input)

    println("part1: $part1Result") // 2720
    println("part2: $part2Result") // 71535
}
