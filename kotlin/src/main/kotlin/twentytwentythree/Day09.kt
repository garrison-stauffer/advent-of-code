package twentytwentythree

object Day09 {
    val input: List<String> = Day08::class.java.getResource("/twentytwentythree/day09pt1.txt")!!
        .readText()
        .lines()
        .filter(String::isNotBlank)

    fun part1(input: List<String>): Long {
        val oasisReadings = parseInput(input)
        return oasisReadings.sumOf { it.predictNextValue() }
    }

    fun parseInput(input: List<String>): List<OasisReadings> {
        return input.map {
            OasisReadings(it.split(" ").map { it.toLong() })
        }
    }

    data class OasisReadings(val dataPoints: List<Long>) {
        fun predictNextValue(): Long {
            val intermediaries = mutableListOf(dataPoints.toMutableList())
            do {
                if (intermediaries.last().all { it == 0L }) {
                    break
                }

                val next = intermediaries.last().windowed(2, 1) {
                    it[1] - it[0]
                }.toMutableList()
                intermediaries.add(next)
            } while (true)

            for (index in intermediaries.indices.reversed().drop(1)) {
                val delta = intermediaries[index + 1].last()
                val lastKnownDataPoint = intermediaries[index].last()
                val prediction = lastKnownDataPoint + delta
                intermediaries[index].add(prediction)
            }

            return intermediaries[0].last()
        }

        fun predictPriorValue(): Long {
            val intermediaries = mutableListOf(dataPoints.toMutableList())
            do {
                if (intermediaries.last().all { it == 0L }) {
                    break
                }

                val next = intermediaries.last().windowed(2, 1) {
                    it[1] - it[0]
                }.toMutableList()
                intermediaries.add(next)
            } while (true)

            for (index in intermediaries.indices.reversed().drop(1)) {
                val delta = intermediaries[index + 1].first()
                val lastKnownDataPoint = intermediaries[index].first()
                val prediction = lastKnownDataPoint - delta
                intermediaries[index].add(0, prediction)
            }

            return intermediaries[0].first()
        }
    }

    fun part2(input: List<String>): Long {
        val oasisReadings = parseInput(input)
        return oasisReadings.sumOf { it.predictPriorValue() }
    }
}

fun main() {
    val part1 = Day09.part1(Day09.input)
    val part2 = Day09.part2(Day09.input)

    println("part 1 result: $part1")
    println("part 2 result: $part2")
}
