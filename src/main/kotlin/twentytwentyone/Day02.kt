package twentytwentyone

object Day02 {
    fun part1(input: List<String>) =
        input.fold(0 to 0) { coordinate, command ->
            val (horizontal, depth) = coordinate
            val (action, distance) = command.split(" ").let { it[0] to it[1].toInt() }

            when (action) {
                "forward" -> horizontal + distance to depth
                "down" -> horizontal to depth + distance
                "up" -> horizontal to depth - distance
                else -> error("unknown action $action")
            }
        }.let { it.first * it.second }

    fun part2(input: List<String>) =
            input.fold(SubmarineCoordinate.ZERO) { coordinate, command ->
                println(coordinate)
                val (action, distance) = command.split(" ").let { it[0] to it[1].toInt() }

                when (action) {
                    "forward" -> coordinate.forward(distance)
                    "down" -> coordinate.down(distance)
                    "up" -> coordinate.up(distance)
                    else -> error("unknown action $action")
                }
            }.let { it.horizontal * it.depth }

}

data class SubmarineCoordinate(
        val horizontal: Int,
        val depth: Int,
        val aim: Int
) {
    fun forward(distance: Int) = copy(
            horizontal = horizontal + distance,
            depth = depth + aim * distance
    )

    fun up(distance: Int) = copy(aim = aim - distance)

    fun down(distance: Int) = copy(aim = aim + distance)

    companion object {
        val ZERO = SubmarineCoordinate(0, 0, 0)
    }
}

fun main() {
    val input = readFile(2, 1)

    val part1 = Day02.part1(input)
    val part2 = Day02.part2(input)

    println("part 1: $part1")
    println("part 2: $part2")
}