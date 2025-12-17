package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/flnt"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestProblem11(t *testing.T) {
	prob := Problem11{}

	t.Run("run the problem", func(t *testing.T) {
		setDebugger(true)
		result, err := prob.Part1(ReadProblemInput(11, 1))
		result2, err2 := prob.Part2(ReadProblemInput(11, 1))

		require.NoError(t, err)
		require.NoError(t, err2)

		fmt.Printf("result is %v  %v \n", result, result2)
	})

	t.Run("part 1 test cases", func(t *testing.T) {
		do := Suite(prob.Part1)

		t.Run("provided test case", func(t *testing.T) {
			do(t).parsing(flnt.Spl(`aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out`)).hasToBe(5)
		})
	})

}
