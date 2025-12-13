package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/flnt"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestProblem8(t *testing.T) {
	prob := Problem8{}

	t.Run("run the problem", func(t *testing.T) {
		prob.TopN = 1000
		result, err := prob.Part1(ReadProblemInput(8, 1))
		result2, err2 := prob.Part2(ReadProblemInput(8, 1))

		require.NoError(t, err)
		require.NoError(t, err2)

		fmt.Printf("result is %v  %v \n", result, result2)
	})

	t.Run("part 1 test cases", func(t *testing.T) {
		prob.TopN = 10
		do := Suite(prob.Part1)

		t.Run("provided test case", func(t *testing.T) {
			do(t).debug().parsing(flnt.Spl(
				`162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689`,
			)).hasToBe(40)
		})
	})
	t.Run("part 2 test cases", func(t *testing.T) {

		do := Suite(prob.Part2)

		t.Run("provided test cases", func(t *testing.T) {
			do(t).parsing(flnt.Spl(
				`162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689`,
			)).hasToBe(21)
		})
	})
}
