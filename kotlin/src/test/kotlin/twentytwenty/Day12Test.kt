package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day12Test {

    @Test
    fun `test rotations`() {
        val ship = ShipPart1(0, Coordinate(0, 0))

        assertThat(ship.rotate("R", 90)).isEqualTo(
            ShipPart1(SOUTH, Coordinate(0, 0))
        )
        assertThat(ship.rotate("R", 180)).isEqualTo(
            ShipPart1(WEST, Coordinate(0, 0))
        )
        assertThat(ship.rotate("R", 270)).isEqualTo(
            ShipPart1(NORTH, Coordinate(0, 0))
        )
        assertThat(ship.rotate("R", 360)).isEqualTo(
            ShipPart1(EAST, Coordinate(0, 0))
        )

        assertThat(ship.rotate("L", 90)).isEqualTo(
            ShipPart1(NORTH, Coordinate(0, 0))
        )
        assertThat(ship.rotate("L", 180)).isEqualTo(
            ShipPart1(WEST, Coordinate(0, 0))
        )
        assertThat(ship.rotate("L", 270)).isEqualTo(
            ShipPart1(SOUTH, Coordinate(0, 0))
        )
        assertThat(ship.rotate("L", 360)).isEqualTo(
            ShipPart1(EAST, Coordinate(0, 0))
        )
    }

    @Test
    fun `test part 1 with test input`() {
        val input = listOf(
            "F10",
            "N3",
            "F7",
            "R90",
            "F11"
        )

        var ship = ShipPart1()

        ship = ship.deduce("F10")
        assertThat(ship).isEqualTo(ShipPart1(EAST, Coordinate(10, 0)))
        ship = ship.deduce("N3")
        assertThat(ship).isEqualTo(ShipPart1(EAST, Coordinate(10, 3)))
        ship = ship.deduce("F7")
        assertThat(ship).isEqualTo(ShipPart1(EAST, Coordinate(17, 3)))
        ship = ship.deduce("R90")
        assertThat(ship).isEqualTo(ShipPart1(SOUTH, Coordinate(17, 3)))
        ship = ship.deduce("F11")
        assertThat(ship).isEqualTo(ShipPart1(SOUTH, Coordinate(17, -8)))

        assertThat(Day12.part1(input)).isEqualTo(25)
    }

    @Test
    fun `test rotating`() {
        var waypoint = Waypoint(position = Coordinate(5, 5))

        waypoint = waypoint.rotate90DegreesRight()
        assertThat(waypoint).isEqualTo(Waypoint(position = (Coordinate(5, -5))))
        waypoint = waypoint.rotate90DegreesRight()
        assertThat(waypoint).isEqualTo(Waypoint(position = (Coordinate(-5, -5))))
        waypoint = waypoint.rotate90DegreesRight()
        assertThat(waypoint).isEqualTo(Waypoint(position = (Coordinate(-5, 5))))
    }

    @Test
    fun `test moving forward`() {
        val shipPart2 = ShipPart2()

        assertThat(shipPart2.floatForward(10)).isEqualTo(
            ShipPart2(waypoint = Waypoint(), position = Coordinate(100, 10))
        )
    }

    @Test
    fun `test part 2 with sample input`() {
        val input = listOf(
            "F10",
            "N3",
            "F7",
            "R90",
            "F11"
        )

        var ship = ShipPart2()

        ship = ship.deduce("F10")
        assertThat(ship).isEqualTo(ShipPart2(Waypoint(), Coordinate(100, 10)))
        ship = ship.deduce("N3")
        assertThat(ship).isEqualTo(ShipPart2(Waypoint(Coordinate(10, 4)), Coordinate(100, 10)))
        ship = ship.deduce("F7")
        assertThat(ship).isEqualTo(ShipPart2(Waypoint(Coordinate(10, 4)), Coordinate(170, 38)))
        ship = ship.deduce("R90")
        assertThat(ship).isEqualTo(ShipPart2(Waypoint(Coordinate(4, -10)), Coordinate(170, 38)))
        ship = ship.deduce("F11")
        assertThat(ship).isEqualTo(ShipPart2(Waypoint(Coordinate(4, -10)), Coordinate(214, -72)))

        assertThat(Day12.part2(input)).isEqualTo(286)
    }

    @Test
    fun testMyLeftLogic() {
        assertThat((4 - (270 / 90)) mod 4).isEqualTo(1)
        assertThat((4 - (180 / 90)) mod 4).isEqualTo(2)
        assertThat((4 - (90 / 90)) mod 4).isEqualTo(3)
        assertThat((4 - (360 / 90)) mod 4).isEqualTo(0)
    }


}
