package twentytwentyone

object Day01 {
    fun part1(input: List<Int>): Int =
        input.windowed(2, step = 1) { it[1] - it[0] }
            .count { it > 0 }

    fun part2(input: List<Int>): Int =
        input.windowed(size = 3, step = 1) { it.sum() }
            .windowed(size = 2, step = 1) { it[1] - it[0] }
            .count { it > 0 }
}

fun main() {
    val input = readFile(1, 1).map { it.toInt() }
    val part1 = Day01.part1(input)
    val part2 = Day01.part2(input)
    println("part 1: $part1")
    println("part 2: $part2")
}
