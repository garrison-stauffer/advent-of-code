package twentynineteen

import java.io.BufferedReader
import java.io.FileReader
import kotlin.math.abs
import kotlin.math.min

object DayThree {
    const val DAY_THREE_FILE_PATH = "/Users/garrisonstauffer/toast/git-repos/advent-of-code/src/main/resources/DayThreeInput.txt"

    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    data class WireCommand(val direction: Direction, val distance: Int)
    data class Point(val x: Int, val y: Int) {
        fun createNewPoint(deltaX: Int, deltaY: Int) = Point(x + deltaX, y + deltaY)

        fun calcManhattanDistance() = abs(x) + abs(y)
    }

    // it will be helpful to ensure lines only go left-to-right or bottom-to-top
    data class Line(val start: Point, val end: Point) {
        // orderedStart -> orderedEnd will always go to the right, or upwards
        private val orderedStart: Point
        private val orderedEnd: Point

        init {
            if (isVertical()) {
                if (start.y < end.y) {
                    orderedStart = start
                    orderedEnd = end
                } else {
                    orderedStart = end
                    orderedEnd = start
                }

                assert(orderedStart.y < orderedEnd.y) {
                    "$start $end"
                }
            } else {
                if (start.x < end.x) {
                    orderedStart = start
                    orderedEnd = end
                } else {
                    orderedStart = end
                    orderedEnd = start
                }

                assert(orderedStart.x < orderedEnd.x) {
                    "ordering failed for: $start $end"
                }
            }
        }

        infix fun intersects(other: Line): Boolean {
            if (this.isHorizontal() && other.isHorizontal()) return false

            return if (isHorizontal()) {
                (other.orderedStart.x <= orderedEnd.x && other.orderedStart.x >= orderedStart.x) &&
                    (other.orderedStart.y <= orderedStart.y && other.orderedEnd.y >= orderedStart.y)
            } else {
                (other.orderedStart.x <= orderedEnd.x && other.orderedEnd.x >= orderedEnd.x) &&
                    (other.orderedStart.y >= orderedStart.y && other.orderedStart.y <= orderedEnd.y)
            }
        }

        fun calculateIntersection(other: Line): Point {
            return if (isHorizontal()) {
                // must be at the "y" for this line
                Point(other.start.x, start.y)
            } else {
                // must be at the "x" for this line, "y" for the other line
                Point(start.x, other.start.y)
            }
        }

        fun containsPoint(point: Point): Boolean {
            if (isHorizontal()) {
                return point.y == start.y && point.x >= orderedStart.x && point.x <= orderedEnd.x
            } else {
                return point.x == start.x && point.y >= orderedStart.y && point.y <= orderedEnd.y
            }

                return false
        }

        fun calculateDistanceToPoint(point: Point): Int {
            val length = length()
            return if (isHorizontal()) {
                val distanceFromOrderedStart = point.x - orderedStart.x
                if (orderedStart === start) {
                    distanceFromOrderedStart
                } else {
                    length - distanceFromOrderedStart
                }
            } else {
                val distanceFromOrderedStart = point.y - orderedStart.y
                if (orderedStart === start) {
                    distanceFromOrderedStart
                } else {
                    length - distanceFromOrderedStart
                }
            }
        }

        fun length() = if (isHorizontal()) { orderedEnd.x - orderedStart.x } else { orderedEnd.y - orderedStart.y }

        private fun isHorizontal() = start.y == end.y
        private fun isVertical() = start.x == end.x
    }

    fun partOne(wireOneString: String, wireTwoString: String): Point {
        val wireOneInput = parseLine(wireOneString)
        val wireTwoInput = parseLine(wireTwoString)

        val coordinatesForWireOne = mapWireDirectionsToPoints(wireOneInput)
        val coordinatesForWireTwo = mapWireDirectionsToPoints(wireTwoInput)

        val linesForWireOne = mapPointsToLines(coordinatesForWireOne)
        val linesForWireTwo = mapPointsToLines(coordinatesForWireTwo)

        val closestIntersection = linesForWireOne.map { wireOneLine ->
            val minPoint: Point? = linesForWireTwo
                .filter { wireOneLine intersects it}
                .map { wireOneLine.calculateIntersection(it) }
                .filter { !(it.x == 0 && it.y == 0) } // filter out collisions at (0, 0)
                .minBy { it.calcManhattanDistance() }

            minPoint
        }.minBy { it?.calcManhattanDistance() ?: Int.MAX_VALUE } ?: Point(Int.MAX_VALUE, Int.MAX_VALUE)

        println("Closest intersection is: $closestIntersection")

        return closestIntersection
    }

    fun partTwo(wireOneString: String, wireTwoString: String): Int {
        val wireOneInput = parseLine(wireOneString)
        val wireTwoInput = parseLine(wireTwoString)

        val coordinatesForWireOne = mapWireDirectionsToPoints(wireOneInput)
        val coordinatesForWireTwo = mapWireDirectionsToPoints(wireTwoInput)

        val linesForWireOne = mapPointsToLines(coordinatesForWireOne)
        val linesForWireTwo = mapPointsToLines(coordinatesForWireTwo)

        val allIntersections: Set<Point> = linesForWireOne.map {wireOneLine ->
            val intersections = linesForWireTwo
                .filter {
                    wireOneLine intersects it
                }
                .map { wireOneLine.calculateIntersection(it) }
                .filter { !(it.x == 0 && it.y == 0) } // filter out the origin.. its just a pain to deal with
            intersections
        }.flatMap { it.toList() }.toSet()

        println("All intersections: $allIntersections")
        val distancesForWireOne: Map<Point, Int> = calculateMinimumDistancesToPoints(linesForWireOne, allIntersections)
        val distancesForWireTwo: Map<Point, Int> = calculateMinimumDistancesToPoints(linesForWireTwo, allIntersections)

        val minimumDistance = allIntersections
            .map { distancesForWireOne[it]!! + distancesForWireTwo[it]!! }
            .min() ?: -1

        return minimumDistance
    }

    fun calculateMinimumDistancesToPoints(lines: List<Line>, intersections: Set<Point>): Map<Point, Int> {
        var distanceTraveledSoFar = 0
        val output = mutableMapOf<Point, Int>()
        for (line in lines) {
            // calculate any intersections for this line with distances
            val distances = intersections
                .filter { line.containsPoint(it) }
                .map { it to line.calculateDistanceToPoint(it) }
                .toMap()

            // merge map back together
            for (entry in distances.entries) {
                if (output.containsKey(entry.key)) {
                    val currentVal = output[entry.key]!!
                    output[entry.key] = min(currentVal, entry.value + distanceTraveledSoFar)
                } else {
                    output[entry.key] = entry.value + distanceTraveledSoFar
                }
            }

            distanceTraveledSoFar += line.length()
        }

        return output
    }

    fun parseLine(line: String): List<WireCommand> {
        return line.split(",").map {
            val direction = directionFromString(it.get(0))
            val distance = it.substring(1).toInt()
            WireCommand(direction, distance)
        }
    }

    fun directionFromString(char: Char) = when (char) {
            'U' -> Direction.UP
            'D' -> Direction.DOWN
            'L' -> Direction.LEFT
            'R' -> Direction.RIGHT
            else -> error("Unknown direction for: $char")
    }

    fun mapWireDirectionsToPoints(commands: List<WireCommand>): List<Point> {
        // start with (0, 0)
        // each direction indicates +, - and whether its on x, y
        val coordinates = mutableListOf<Point>()

        var currentPoint = Point(0, 0)
        coordinates.add(currentPoint)

        commands.forEach {
            val (deltaX, deltaY) = when (it.direction) {
                Direction.UP -> 0 to it.distance
                Direction.DOWN -> 0 to -it.distance
                Direction.LEFT -> -it.distance to 0
                Direction.RIGHT -> it.distance to 0
            }

            currentPoint = currentPoint.createNewPoint(deltaX, deltaY)
            coordinates.add(currentPoint)
        }

        return coordinates
    }

    fun mapPointsToLines(points: List<Point>): List<Line> {
        return points.windowed(size = 2, step = 1, transform =  {
            val pointOne = it[0]
            val pointTwo = it[1]

            Line(pointOne, pointTwo)
        })
    }

}

fun main() {
    val reader = BufferedReader(FileReader(DayThree.DAY_THREE_FILE_PATH))

    lateinit var lineOne: String
    lateinit var lineTwo: String


    reader.use {
        lineOne = it.readLine()
        lineTwo = it.readLine()
    }

    val point = DayThree.partOne(lineOne, lineTwo)
    print("Manhattan depth for part one: ${point.calcManhattanDistance()}")

    val distance = DayThree.partTwo(lineOne, lineTwo)
    print("Closest short-circuit is $distance steps away")
}
