package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/flnt"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestProblem10(t *testing.T) {
	prob := Problem10{}

	t.Run("run the problem", func(t *testing.T) {
		setDebugger(true)
		result, err := prob.Part1(ReadProblemInput(10, 1))
		result2, err2 := prob.Part2V2(ReadProblemInput(10, 1))

		require.NoError(t, err)
		require.NoError(t, err2) // 18681

		fmt.Printf("result is %v  %v \n", result, result2)
	})

	t.Run("part 1 test cases", func(t *testing.T) {
		do := Suite(prob.Part1)

		t.Run("provided test case", func(t *testing.T) {
			do(t).debug().parsing(flnt.Spl(
				`[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}`,
			)).hasToBe(7)
		})
	})
	t.Run("part 2 test cases", func(t *testing.T) {

		do := Suite(prob.Part2V2)

		t.Run("provided test case", func(t *testing.T) {
			do(t).debug().parsing(flnt.Spl(
				`[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
			[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
			[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}`,
			)).hasToBe(33)
			do(t).debug().parsing(flnt.Spl(
				`[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}`,
			)).hasToBe(10)
			do(t).debug().parsing(flnt.Spl(
				`[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}`,
			)).hasToBe(12)
			do(t).debug().parsing(flnt.Spl(
				`[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}`,
			)).hasToBe(11)
		})
	})

	t.Run("part 2 test cases", func(t *testing.T) {

		do := Suite(prob.Part2V2)

		t.Run("provided test case", func(t *testing.T) {
			do(t).debug().parsing(flnt.Spl(
				`[.###...##.] (0,1,2,4,5,7,8) (1,2,3,5,6,7,8,9) (0,2,4,5,6,7,9) (3,6) (0,1,9) (0,1,4) (0,2,3,4,6,8,9) (0,2,3,4,7,9) (2,4,5,9) (0,1,2,5,6,8,9) (0,2,3,5,6,7,9) {286,248,300,62,259,273,77,256,239,116}`,
			)).hasToBe(33)
			// 2025/12/16 01:55:18 DEBUG starting recursion level=7
			//remainder="[84 0 60 3 66 39 11 28 0 47]" cost=255 buttonsUsed=1001110111 problem="{lights:398 buttons:[
			//             0    2 3  4  5  6  7    9
			//           [   1  2 3     5  6  7 8  9]
			//           [ 0    2    4  5  6  7    9]
			//           [ 0    2 3  4     6    8  9]
		})
	})

	t.Run("run the problem", func(t *testing.T) {
		setDebugger(true)
		result, err := prob.Part1(ReadProblemInput(10, 1))
		result2, err2 := prob.InvestigateData(ReadProblemInput(10, 1))

		require.NoError(t, err)
		require.NoError(t, err2)

		fmt.Printf("result is %v  %v \n", result, result2)
	})

}
