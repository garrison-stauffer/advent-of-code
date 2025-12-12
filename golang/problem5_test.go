package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/flnt"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestProblem5(t *testing.T) {
	prob := Problem5{}

	t.Run("run the problem", func(t *testing.T) {
		result, err := prob.Part1(ReadProblemInput(5, 1))
		result2, err2 := prob.Part2(ReadProblemInput(5, 1))

		require.NoError(t, err)
		require.NoError(t, err2)

		fmt.Printf("result is %v  %v \n", result, result2)
	})

	t.Run("part 1 test cases", func(t *testing.T) {

		do := Suite(prob.Part1)

		t.Run("provided test case", func(t *testing.T) {
			do(t).parsing(flnt.Spl(
				`3-5
10-14
16-20
12-18

1
5
8
11
17
32`,
			)).hasToBe(3)
		})
	})
	t.Run("part 2 test cases", func(t *testing.T) {

		do := Suite(prob.Part2)

		t.Run("provided test cases", func(t *testing.T) {
			do(t).parsing(flnt.Spl(
				`3-5
10-14
16-20
12-18

1
5
8
11
17
32`,
			)).hasToBe(13)
		})
	})
}
