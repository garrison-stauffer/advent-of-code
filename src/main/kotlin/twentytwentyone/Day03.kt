package twentytwentyone

object Day03 : Problem {
    override fun part1(input: List<String>): Int {
        // accumulates the sum of bits for each index
        val accumulator = sortedMapOf<Int, Int>().withDefault { 0 }

        input.forEach { binary ->
            binary.forEachIndexed { index, bit ->
                accumulator[index] = accumulator.getValue(index) + when (bit) {
                    '0' -> 0
                    '1' -> 1
                    else -> error("unexpected bit value $bit")
                }
            }
        }

        val majority: Int = input.size / 2

        val gamma = accumulator.values.fold("") { gammaString, numBits ->
            gammaString + if (numBits > majority) "1" else "0"
        }.toInt(radix = 2)

        val epsilon = accumulator.values.fold("") { gammaString, numBits ->
            // bad case if they're split even...
            gammaString + if (numBits < majority) "1" else "0"
        }.toInt(radix = 2)

        println("gamma: $gamma, epsilon: $epsilon")

        return gamma * epsilon
    }

    override fun part2(input: List<String>): Int {
        var oxygenRatings: List<String> = input
        var oxygenCriteriaIndex = 0
        do {
            val bit = bitMatchingCriteria(oxygenRatings, oxygenCriteriaIndex, shouldFindMajority = true)

            oxygenRatings = oxygenRatings.filter { it[oxygenCriteriaIndex] == bit }
            oxygenCriteriaIndex++
        } while (oxygenRatings.size > 1)

        var co2Scrubbings: List<String> = input
        var co2CriteriaIndex = 0
        do {
            val bit = bitMatchingCriteria(co2Scrubbings, co2CriteriaIndex, shouldFindMajority = false)

            co2Scrubbings = co2Scrubbings.filter { it[co2CriteriaIndex] == bit }
            co2CriteriaIndex++
        } while (co2Scrubbings.size > 1)

        val o2Rating = oxygenRatings.first().toInt(radix = 2)
        val co2ScrubberRating = co2Scrubbings.first().toInt(radix = 2)

        println("O2 Rating: $o2Rating, CO2 Scrubber Rating: $co2ScrubberRating")

        return o2Rating * co2ScrubberRating
    }

    private fun bitMatchingCriteria(input: List<String>, index: Int, shouldFindMajority: Boolean): Char {
        val sum = input.sumBy {
            when (it[index]) {
                '0' -> 0
                '1' -> 1
                else -> error("unexpected bit ${it[index]}")
            }
        }

        val majority = (input.size / 2) + (input.size % 2)

        return if (shouldFindMajority) {
            if (sum >= majority) '1'
            else '0'
        } else {
            if (sum >= majority) '0'
            else '1'
        }
    }
}

fun main() {
    val input = readFile(day = 3, part = 1)

    val part1 = Day03.part1(input)
    val part2 = Day03.part2(input)

    println("part1: $part1")
    println("part2: $part2")
}
