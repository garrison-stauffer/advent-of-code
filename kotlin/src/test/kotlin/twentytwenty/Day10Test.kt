package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day10Test {
    val test1 = listOf("16",
        "10",
        "15",
        "5",
        "1",
        "11",
        "7",
        "19",
        "6",
        "12",
        "4")

    val test2 = listOf("28",
        "33",
        "18",
        "42",
        "31",
        "14",
        "46",
        "20",
        "48",
        "47",
        "24",
        "23",
        "49",
        "45",
        "19",
        "38",
        "39",
        "11",
        "1",
        "32",
        "25",
        "35",
        "8",
        "17",
        "7",
        "9",
        "4",
        "2",
        "34",
        "10",
        "3")
    @Test
    fun `test something`() {

        assertThat(Day10.part1(test1)).isEqualTo(7 * 5)
    }

    @Test
    fun `test second input example`() {
        assertThat(Day10.part1(test2)).isEqualTo(22 * 10)
    }

    @Test
    fun `test path finding with first example`() {
        assertThat(Day10.part2(test1)).isEqualTo(8)
    }

    @Test
    fun `test path finding with second example`() {
        assertThat(Day10.part2(test2)).isEqualTo(19208)
    }



}
