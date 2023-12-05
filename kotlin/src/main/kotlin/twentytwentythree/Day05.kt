package twentytwentythree

object Day05 {
    val input: List<String> = Day05::class.java.getResource("/twentytwentythree/day05pt1.txt")!!
        .readText()
        .lines()
        .filter(String::isNotBlank)

    fun part1(input: List<String>): Long {
        val almanac = buildAlmanac(input)

        val outputs = mutableListOf<Long>()
        for (seedRange in almanac.seeds) {
            for (seed in seedRange) {
                var currentInput = seed

                for (mappingOffset in 0 until almanac.mappings.size) {
                    currentInput = almanac.findMappingFor(currentInput, mappingOffset)
                }

                outputs.add(currentInput)
            }
        }

        return outputs.min()
    }

    fun part2(input: List<String>): Long {
        val almanac = buildAlmanac(input, false)

        // iterate bottom up - find the lowest ranges of the final output and go backwards
        val bottomUpOutputs = almanac.mappings.last().ranges.map { it.output }.sortedBy { it.first }
        var outputCandidate = 0L
        while (outputCandidate < Long.MAX_VALUE) {
            // 0, 1, 2, 3, ...
            // look up in reverse order
            var interstitialMapping = outputCandidate
            for (mappingIndex in (0 until almanac.mappings.size).reversed()) {
                interstitialMapping = almanac.findReverseMappingFor(interstitialMapping, mappingIndex)
            }

            if (almanac.seeds.any { interstitialMapping in it }) {
                return outputCandidate
            }

            outputCandidate++
        }

        throw IllegalStateException("didn't expect to miss all candidates")
    }

    fun buildAlmanac(input: List<String>, useSimpleParsing: Boolean = true): Almanac {
        val seeds = parseTheSeeds(input[0], useSimpleParsing)
        val digitLineRex = """(\d+( )?)+""".toRegex()
        val mappings = mutableListOf<Mappings>()

        var label = "unset"
        var mapping = mutableListOf<Mapping>()
        for (index in 1 until input.size) {
            val line = input[index]
            if (line.matches(digitLineRex)) {
                val items = line.split(" ").map { it.toLong() }
                val output = items[0]
                val input = items[1]
                val length = items[2]
                mapping.add(Mapping(input until input + length, output))
            } else {
                if (index != 1) {
                    mappings.add(Mappings(label, mapping))
                }
                label = line
                mapping = mutableListOf()
            }
        }

        mappings.add(Mappings(label, mapping))

        return Almanac(seeds, mappings)
    }

    fun parseTheSeeds(input: String, useSimpleParsing: Boolean): List<LongRange> {
        if (useSimpleParsing) {
            return input.drop("seeds: ".length)
                .split(" ")
                .map {
                    it.toLong() .. it.toLong()
                }
        }

        return input.drop("seeds: ".length)
            .split(" ")
            .chunked(2)
            .map {
                it[0].toLong() until it[0].toLong() + it[1].toLong()
            }
    }

    data class Mappings(val label: String, val ranges: List<Mapping>)
    data class Mapping(val input: LongRange, val outputOffset: Long, val output: LongRange = outputOffset .. outputOffset + input.last - input.first)

    data class Almanac(val seeds: List<LongRange>, val mappings: List<Mappings>) {
        fun findMappingFor(input: Long, mappingIndex: Int): Long {
            return mappings[mappingIndex].ranges.find { input in it.input }?.let {
                // if we find a mapping with the right range, then calculate the offset
                val offset = input - it.input.first
                it.outputOffset + offset
            } ?: input
        }
        fun findReverseMappingFor(input: Long, mappingIndex: Int): Long {
            return mappings[mappingIndex].ranges.find { input in it.output }?.let {
                // if we find a mapping with the right range, then calculate the offset
                val offset = input - it.output.first
                it.input.first + offset
            } ?: input
        }
    }
}

fun main() {
    val part1 = Day05.part1(Day05.input)
    val part2 = Day05.part2(Day05.input)

    println("part1 result: $part1") // 324724204
    println("part2 result: $part2") // 104070862
}
