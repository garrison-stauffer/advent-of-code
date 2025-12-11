package golang

import (
	"fmt"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestProblem2(t *testing.T) {
	prob := Problem2{}

	t.Run("run the problem", func(t *testing.T) {
		result, err := prob.Part1(ReadProblemInput(2, 1)[0])
		result2, err2 := prob.Part2(ReadProblemInput(2, 1)[0])

		require.NoError(t, err)
		require.NoError(t, err2)

		fmt.Printf("result is %v  %v\n", result, result2)
	})

	t.Run("part 1 test cases", func(t *testing.T) {

		do := Suite(prob.Part1)

		t.Run("provided test cases", func(t *testing.T) {
			do(t).parsing("11-22").hasToBe(33).
				parsing("95-115").hasToBe(99).
				parsing("998-1012").hasToBe(1010).
				parsing("1188511880-1188511890").hasToBe(1188511885).
				parsing("222220-222224").hasToBe(222222).
				parsing("1698522-1698528").hasToBe(0).
				parsing("446443-446449").hasToBe(446446).
				parsing("38593856-38593862").hasToBe(38593859)
		})
	})
	t.Run("part 1 test cases", func(t *testing.T) {

		do := Suite(prob.Part2)

		t.Run("provided test cases", func(t *testing.T) {
			do(t).parsing("11-22").hasToBe(11 + 22).
				parsing("95-115").hasToBe(99 + 111).
				parsing("998-1012").hasToBe(999 + 1010).
				parsing("1188511880-1188511890").hasToBe(1188511885).
				parsing("222220-222224").hasToBe(222222).
				parsing("1698522-1698528").hasToBe(0).
				parsing("446443-446449").hasToBe(446446).
				parsing("38593856-38593862").hasToBe(38593859).
				parsing("565653-565659").hasToBe(565656).
				parsing("824824821-824824827").hasToBe(824824824).
				parsing("2121212118-2121212124").hasToBe(2121212121)
		})
	})
}

func Suite[I any, R any](problem Problem[I, R]) func(t *testing.T) testCase[I, R] {
	return func(t *testing.T) testCase[I, R] {
		return testCase[I, R]{
			t:  t,
			fn: problem,
		}
	}
}
