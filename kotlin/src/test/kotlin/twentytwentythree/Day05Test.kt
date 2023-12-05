package twentytwentythree

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day05Test {

    @Test
    fun testParsingSeeds() {
        val seeds = Day05.parseTheSeeds("seeds: 79 14 55 13", true)
        assertThat(seeds).isEqualTo(listOf(79L, 14L, 55L, 13L))
    }

    @Test
    fun testMappingsParsing() {
        val input = """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
        """.trimIndent().split("\n").filter(String::isNotBlank)
        val almanac = Day05.buildAlmanac(input)

        assertThat(almanac.findMappingFor(79, 0)).isEqualTo(81L)
        assertThat(almanac.findMappingFor(14, 0)).isEqualTo(14L)
    }

    @Test
    fun testPart1() {
        val input = """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
        """.trimIndent().split("\n").filter(String::isNotBlank)

        assertThat(Day05.part1(input)).isEqualTo(35)
    }

    @Test
    fun testPart2() {
        val input = """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
        """.trimIndent().split("\n").filter(String::isNotBlank)

        assertThat(Day05.part2(input)).isEqualTo(46L)
    }
}
