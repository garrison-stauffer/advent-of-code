package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day3Test {
    val testFileInput = this.javaClass.classLoader.getResource("twentytwenty/day3test.txt").readText().lines().filter { it.isNotBlank() }

    @Test
    fun `quick map test`() {
        val tobogganSlope = TobogganMap(testFileInput)

        assertThat(tobogganSlope.slopeLength).isEqualTo(11)
        assertThat(tobogganSlope.mapWidth).isEqualTo(11)

        assertThat(tobogganSlope.getEntityAt(0, 0)).isEqualTo(SlopeEntity.OPEN)
        assertThat(tobogganSlope.getEntityAt(11, 0)).isEqualTo(SlopeEntity.OPEN)

        assertThat(tobogganSlope.getEntityAt(2, 0)).isEqualTo(SlopeEntity.TREE)
    }

    @Test
    fun `sample input test`() {
        val numTrees = Day3.part1(testFileInput)

        assertThat(numTrees).isEqualTo(7)
    }

    @Test
    fun `simulation with sample inputs`() {
        val tobogganMap = TobogganMap(testFileInput)

        assertThat(Day3.runSimulatorForCursoredMap(tobogganMap, 1, 1)).isEqualTo(2)
        assertThat(Day3.runSimulatorForCursoredMap(tobogganMap, 3, 1)).isEqualTo(7)
        assertThat(Day3.runSimulatorForCursoredMap(tobogganMap, 5, 1)).isEqualTo(3)
        assertThat(Day3.runSimulatorForCursoredMap(tobogganMap, 7, 1)).isEqualTo(4)
        assertThat(Day3.runSimulatorForCursoredMap(tobogganMap, 1, 2)).isEqualTo(2)
    }

    @Test
    fun `part2 does multiplication right??`() {
        val result = Day3.part2(testFileInput, listOf(
            Coordinate(1, 1),
            Coordinate(3, 1),
            Coordinate(5, 1),
            Coordinate(7, 1),
            Coordinate(1, 2)
        ))

        assertThat(result).isEqualTo(336)
    }
}
