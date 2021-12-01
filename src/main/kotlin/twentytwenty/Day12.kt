package twentytwenty

import kotlin.math.abs

const val EAST = 0
const val SOUTH = 1
const val WEST = 2
const val NORTH = 3

const val I = 0
const val II = 2
const val III = 3
const val IV = 4

data class Waypoint(val position: Coordinate = Coordinate(x = 10, y = 1)) {
    fun rotate90DegreesRight() = Waypoint(this.position.rotate90DegreesRight())
}

data class ShipPart1(
    val direction: Int = EAST,
    val shipPosition: Coordinate = Coordinate(0, 0)
) {

    companion object {
        val commandRegex = """(\w)(\d+)""".toRegex()
    }

    fun deduce(input: String): ShipPart1 {
        val (command, number) = commandRegex.matchEntire(input)?.groupValues?.let {
            it[1] as String to it[2].toInt()
        } ?: error("Could not parse input $input into a command")

        return when (command) {
            "F" -> floatForward(number)
            "L","R" -> rotate(command, number)
            "N","S","W","E" -> moveInDirection(command, number)
            else -> error("cannot deduce command from $command (input was $input)")
        }
    }

    fun floatForward(amount: Int): ShipPart1 {
        return when (direction) {
            EAST -> moveInDirection("E", amount)
            WEST -> moveInDirection("W", amount)
            NORTH -> moveInDirection("N", amount)
            SOUTH -> moveInDirection("S", amount)
            else -> error("Unknown direction $direction")
        }
    }

    fun rotate(command: String, amount: Int): ShipPart1 {
        val newDirection = when (command) {
            "L" -> (direction - (amount / 90)) mod 4
            "R" -> (direction + (amount / 90)) mod 4
            else -> error("Cannot turn in direction $command")
        }

        return ShipPart1()
    }

    fun moveInDirection(command: String, amount: Int): ShipPart1 {
        return when (command) {
            "N" -> ShipPart1(direction, shipPosition.move(deltaY = amount))
            "S" -> ShipPart1(direction, shipPosition.move(deltaY = -amount))
            "E" -> ShipPart1(direction, shipPosition.move(deltaX = amount))
            "W" -> ShipPart1(direction, shipPosition.move(deltaX = -amount))
            else -> error("Cannot move in direction $command")
        }
    }
}


data class ShipPart2(
    val waypoint: Waypoint = Waypoint(),
    val position: Coordinate = Coordinate(0, 0)
) {

    companion object {
        val commandRegex = """(\w)(\d+)""".toRegex()
    }

    fun deduce(input: String): ShipPart2 {
        val (command, number) = commandRegex.matchEntire(input)?.groupValues?.let {
            it[1] as String to it[2].toInt()
        } ?: error("Could not parse input $input into a command")

        return when (command) {
            "F" -> floatForward(number)
            "L","R" -> rotate(command, number)
            "N","S","W","E" -> moveInDirection(command, number)
            else -> error("cannot deduce command from $command (input was $input)")
        }
    }

    fun floatForward(amount: Int): ShipPart2 {
        val deltaX = waypoint.position.x * amount
        val deltaY = waypoint.position.y * amount
        return ShipPart2(this.waypoint, this.position.move(deltaX, deltaY))
    }

    fun rotate(command: String, amount: Int): ShipPart2 {
        val numberOfQuarterTurns = when (command) {
            "L" -> (4 - (amount / 90)) mod 4
            "R" -> (amount / 90) mod 4
            else -> error("Cannot turn in direction $command")
        }

        var nextWaypoint = waypoint
        for (i in 0 until numberOfQuarterTurns) {
            nextWaypoint = waypoint.rotate90DegreesRight()
        }

        return ShipPart2(nextWaypoint, position)
    }

    fun moveInDirection(command: String, amount: Int): ShipPart2 {
        return when (command) {
            "N" -> ShipPart2(Waypoint(waypoint.position.move(deltaY = amount)), position)
            "S" -> ShipPart2(Waypoint(waypoint.position.move(deltaY = -amount)), position)
            "E" -> ShipPart2(Waypoint(waypoint.position.move(deltaX = amount)), position)
            "W" -> ShipPart2(Waypoint(waypoint.position.move(deltaX = -amount)), position)
            else -> error("Cannot move in direction $command")
        }
    }
}

infix fun Int.mod(other: Int) = Math.floorMod(this, other)

object Day12 {
    fun part1(input: List<String>): Int {
        var ship = ShipPart1()

        for (line in input) {
            ship = ship.deduce(line)
        }

        return abs(ship.shipPosition.x) + abs(ship.shipPosition.y)
    }

    fun part2(input: List<String>): Int {
        var ship = ShipPart2()

        for (line in input) {
            ship = ship.deduce(line)
        }

        return abs(ship.position.x) + abs(ship.position.y)
    }

}

fun main() {
    val input = readFile(day = 12, part = 1)

    val part1Result = Day12.part1(input) // should be 1106
    val part2Result = Day12.part2(input) // should be 1106

    println("""
        part 1: $part1Result
        part 2: $part2Result
    """.trimIndent())
}
