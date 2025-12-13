package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/flnt"
	"github.com/stretchr/testify/require"
	"strings"
	"testing"
)

func TestProblem6(t *testing.T) {
	prob := Problem6{}

	t.Run("run the problem", func(t *testing.T) {
		result, err := prob.Part1(ReadProblemInput(6, 1))
		result2, err2 := prob.Part2(ReadProblemInput(6, 1))

		require.NoError(t, err)
		require.NoError(t, err2)

		fmt.Printf("result is %v  %v \n", result, result2)
	})

	t.Run("part 1 test cases", func(t *testing.T) {

		do := Suite(prob.Part1)

		t.Run("provided test case", func(t *testing.T) {
			do(t).parsing(flnt.Spl(
				`123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  `,
			)).hasToBe(4277556)
		})
	})
	t.Run("part 2 test cases", func(t *testing.T) {

		do := Suite(prob.Part2)

		t.Run("provided test cases", func(t *testing.T) {
			do(t).parsing(strings.Split(
				`123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  `, "\n",
			)).hasToBe(3263827)
		})
	})
}
