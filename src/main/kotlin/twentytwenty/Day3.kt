package twentytwenty

enum class SlopeEntity(val representation: Char) {
    TREE('#'),
    OPEN('.')
}

class TobogganMap(val map: List<String>) {
    val slopeLength = map.size
    val mapWidth = map[0].length

    fun getEntityAt(x: Int, y: Int) = getEntityAt(Coordinate(x, y))

    fun getEntityAt(point: Coordinate) = when (map[point.y][point.x % mapWidth]) {
        '#' -> SlopeEntity.TREE
        '.' -> SlopeEntity.OPEN
        else -> error("Unknown value at $point: ${map[point.y][point.x]}")
    }
}

data class Coordinate(val x: Int, val y: Int) {
    fun move(deltaX: Int = 0, deltaY: Int = 0): Coordinate {
        return Coordinate(x + deltaX, y + deltaY)
    }
}

class CursoredMap(val map: TobogganMap, startingPoint: Coordinate, val deltaX: Int, val deltaY: Int) {
    var currentSpot = startingPoint

    fun isAtEndOfMap() = currentSpot.y >= map.slopeLength
    fun getEntityAtCurrentPoint() = map.getEntityAt(currentSpot)

    fun step() {
        val nextSpot = currentSpot.move(deltaX, deltaY)
        currentSpot = nextSpot
    }
}

object Day3 {
    fun part1(input: List<String>): Int {
        return runSimulatorForCursoredMap(
            tobogganMap = TobogganMap(input),
            deltaX = 3,
            deltaY = 1
        )
    }

    // coordinate should maybe be renamed but whogaf
    fun part2(input: List<String>, slopes: List<Coordinate>): Int {
        val tobogganMap = TobogganMap(input)

        return slopes.map {
            runSimulatorForCursoredMap(tobogganMap, it.x, it.y)
        }.reduce(Int::times)
    }

    // returns num trees hit
fun runSimulatorForCursoredMap(tobogganMap: TobogganMap, deltaX: Int, deltaY: Int): Int {
    var treesEncountered = 0
    val cursoredMap = CursoredMap(
        map = tobogganMap,
        startingPoint = Coordinate(0, 0),
        deltaX = deltaX,
        deltaY = deltaY
    )

    do {
        if (cursoredMap.getEntityAtCurrentPoint() == SlopeEntity.TREE) {
            treesEncountered++
        }

        cursoredMap.step()
    } while (!cursoredMap.isAtEndOfMap())

    return treesEncountered
}
}

fun main() {
    val fileInput = readFile(day = 3, part = 1)
    val part1Result = Day3.part1(fileInput); // real answer is 270
    val part2Result = Day3.part2(fileInput, listOf(
        Coordinate(1, 1),
        Coordinate(3, 1),
        Coordinate(5, 1),
        Coordinate(7, 1),
        Coordinate(1, 2)
    )) // real answer is 2122848000

    println(
        """
            Part 1: $part1Result
            
            Part 2: $part2Result
        """.trimIndent()
    )
}
