package twentytwenty

object Day10 {

    fun part1(input: List<String>): Int {
        val mappedResults: List<Int> =
            input.map { it.toInt() }
                .sorted()

        val deviceJolts = mappedResults.last() + 3
        val socket = 0

        val realList = listOf(socket) + mappedResults + deviceJolts

        return realList
            .windowed(size = 2, step = 1) {
                it[1] - it[0]
            }
            .groupBy { it }
            .let {
                val numberOf1Jumps = it[1]?.size ?: error("1 is not in map")
                val numberof3Jumps = it[3]?.size ?: error("3 is not in map")

                numberOf1Jumps * numberof3Jumps
            }
    }

    data class StoredResult(val joltage: Int, var numberOfPathsToEnd: Long = 0)

    fun part2(input: List<String>): Long {
        val listOfAdapters: List<Int> = (input + "0") // manually add socket to map
                .map { it.toInt() }
                .sorted()

        val deviceJolts = listOfAdapters.last() + 3

        val lookupMap = listOfAdapters.map { it to StoredResult(joltage = it) }.toMap().toMutableMap()
        // manually load the device joltage with hardcoded paths = 1
        lookupMap[deviceJolts] = StoredResult(joltage = deviceJolts, numberOfPathsToEnd = 1)

        for (adapter in listOfAdapters.asReversed()) {
            val pathsUsingPlus1 = lookupMap[adapter + 1]?.numberOfPathsToEnd ?: 0
            val pathsUsingPlus2 = lookupMap[adapter + 2]?.numberOfPathsToEnd ?: 0
            val pathsUsingPlus3 = lookupMap[adapter + 3]?.numberOfPathsToEnd ?: 0

            lookupMap[adapter] = StoredResult(joltage = adapter, numberOfPathsToEnd = pathsUsingPlus1 + pathsUsingPlus2 + pathsUsingPlus3)
        }

        return lookupMap[0]?.numberOfPathsToEnd ?: error("0 wasn't in result set?")
    }

}

fun main() {
    val input = readFile(day = 10, part = 1)

    val part1Result = Day10.part1(input)
    val part2Result = Day10.part2(input) // should be 4628074479616

    println("""
        part 1: $part1Result
        part 2: $part2Result
    """.trimIndent())
}
