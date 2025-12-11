package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/flnt"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestProblem4(t *testing.T) {
	prob := Problem4{}

	t.Run("run the problem", func(t *testing.T) {
		result, err := prob.Part1(ReadProblemInput(4, 1))
		result2, err2 := prob.Part2(ReadProblemInput(4, 1))

		require.NoError(t, err)
		require.NoError(t, err2)

		fmt.Printf("result is %v  %v \n", result, result2)
	})

	t.Run("part 1 test cases", func(t *testing.T) {

		do := Suite(prob.Part1)

		t.Run("provided test case", func(t *testing.T) {
			do(t).parsing(flnt.S(
				"..@@.@@@@.",
				"@@@.@.@.@@",
				"@@@@@.@.@@",
				"@.@@@@..@.",
				"@@.@@@@.@@",
				".@@@@@@@.@",
				".@.@.@.@@@",
				"@.@@@.@@@@",
				".@@@@@@@@.",
				"@.@.@@@.@.",
			)).hasToBe(13)
		})
	})
	t.Run("part 2 test cases", func(t *testing.T) {

		do := Suite(prob.Part2)

		t.Run("provided test cases", func(t *testing.T) {
			do(t).parsing(flnt.S(
				"..@@.@@@@.",
				"@@@.@.@.@@",
				"@@@@@.@.@@",
				"@.@@@@..@.",
				"@@.@@@@.@@",
				".@@@@@@@.@",
				".@.@.@.@@@",
				"@.@@@.@@@@",
				".@@@@@@@@.",
				"@.@.@@@.@.",
			)).hasToBe(43)
		})
	})
}
