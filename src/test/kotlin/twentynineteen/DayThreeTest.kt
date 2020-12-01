package twentynineteen

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

import DayThree.WireCommand
import DayThree.Direction.*
import DayThree.parseLine
import DayThree.Point
import DayThree.Line

class DayThreeTest{

    @Test
    fun `line parser correctly works on sample input`() {
        val sampleInput = "U7,R6,D4,L4"
        val expectedOutput = listOf(
            WireCommand(UP, 7),
            WireCommand(RIGHT, 6),
            WireCommand(DOWN, 4),
            WireCommand(LEFT, 4)
        )
        assertThat(DayThree.parseLine(sampleInput)).isEqualTo(expectedOutput)
    }

    @Test
    fun `map wire commands to coordinates works as expected`() {
        val sampleInput = "U5,R5,D5,L6"
        val commands = parseLine(sampleInput)

        val expectedCoordinates = listOf(
            Point(0, 0), Point(0, 5), Point(5, 5), Point(5, 0), Point(-1, 0)
        )

        assertThat(DayThree.mapWireDirectionsToPoints(commands)).isEqualTo(expectedCoordinates)
    }

    @Test
    fun `test intersection logic for Line class`() {
        val lineOne = Line(Point(-1, 0), Point(1, 0))
        val lineTwo = Line(Point(0, -1), Point(0, 1))
        assertThat(lineOne intersects lineTwo).isTrue()
        assertThat(lineTwo intersects lineOne).isTrue()

        val lineThree = Line(Point(0, -4), Point(0, -1))
        assertThat(lineOne intersects lineThree).isFalse()
    }

    @Test
    fun `test cases from problem`() {
        var closestIntersection = DayThree.partOne("R8,U5,L5,D3", "U7,R6,D4,L4")
        assertThat(closestIntersection).isEqualTo(Point(3, 3))

        closestIntersection = DayThree.partOne("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")
        assertThat(closestIntersection.calcManhattanDistance()).isEqualTo(159)

        closestIntersection = DayThree.partOne("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        assertThat(closestIntersection.calcManhattanDistance()).isEqualTo(135)
    }

    @Test
    fun `testing points on a line`() {
        val horizontalLine = Line(Point(0, 3), Point(6, 3))
        val verticalLine = Line(Point(5, -7), Point(5, -1))
        assertThat(horizontalLine.containsPoint(Point(4, 3))).isTrue()
        assertThat(verticalLine.containsPoint(Point(5, -3))).isTrue()
    }

    @Test
    fun `testing distance calculator for line to point`() {
        // purposely mis-order...
        val vertexOne = Point(5, -1)
        val vertextTwo = Point(5, -7)

        assertThat(Line(vertexOne, vertextTwo).calculateDistanceToPoint(vertexOne)).isEqualTo(0)
        assertThat(Line(vertextTwo, vertexOne).calculateDistanceToPoint(vertexOne)).isEqualTo(6)

        val verticalLine = Line(Point(5, -1), Point(5, -7))
        val testPoint = Point(5, -3)

        assertThat(verticalLine.calculateDistanceToPoint(testPoint)).isEqualTo(2)
    }

    @Test
    fun `testing distance to given point works as expected`() {
        val horizontalVertexOne = Point(-5, -3)
        val horizontalVertexTwo = Point(-12, -3)
        assertThat(Line(horizontalVertexOne, horizontalVertexTwo).calculateDistanceToPoint(Point(-5, -3))).isEqualTo(0)
        assertThat(Line(horizontalVertexOne, horizontalVertexTwo).calculateDistanceToPoint(Point(-12, -3))).isEqualTo(7)
        assertThat(Line(horizontalVertexTwo, horizontalVertexOne).calculateDistanceToPoint(Point(-5, -3))).isEqualTo(7)
        assertThat(Line(horizontalVertexTwo, horizontalVertexOne).calculateDistanceToPoint(Point(-12, -3))).isEqualTo(0)
    }

    @Test
    fun `test samples from advent for part 2`() {
        var steps = DayThree.partTwo("R8,U5,L5,D3", "U7,R6,D4,L4")
        assertThat(steps).isEqualTo(30)
        steps = DayThree.partTwo("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")
        assertThat(steps).isEqualTo(610)
        steps = DayThree.partTwo("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        assertThat(steps).isEqualTo(410)
    }

    @Test
    fun `why does this not intersect?`() {
        val lineOne = Line(start=Point(x=6, y=3), end=Point(x=2, y=3))
        val lineTwo = Line(start=Point(x=3, y=5), end=Point(x=3, y=2))
        assertThat(lineOne intersects lineTwo).isTrue()
        assertThat(lineTwo intersects lineOne).isTrue()
        println(lineOne.calculateIntersection(lineTwo))
    }

}
