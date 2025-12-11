package golang

import (
	"fmt"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestProblem1(t *testing.T) {
	prob := Problem1{}

	t.Run("run the problem", func(t *testing.T) {
		result, err := prob.Run(ReadProblemInput(1, 1))
		result2, err2 := prob.Part2(ReadProblemInput(1, 1))

		require.NoError(t, err)
		require.NoError(t, err2)
		fmt.Printf("result is %v  %v\n", result, result2)
	})

	t.Run("part 2 test cases", func(t *testing.T) {
		t.Run("0 -> 0 only adds 1", func(t *testing.T) {
			result, err := prob.Part2([]string{
				"R50",  // add 1
				"L100", // add 1
			})

			require.NoError(t, err)
			assert.Equal(t, "2", result)

			result, err = prob.Part2([]string{
				"L50",  // add 1
				"R100", // add 1
			})

			require.NoError(t, err)
			assert.Equal(t, "2", result)
		})

		t.Run("0 -> 0 only adds 1", func(t *testing.T) {
			result, err := prob.Part2([]string{
				"R150", // add 2
				"L900", // add 9
				"R700", // add 7
			})

			require.NoError(t, err)
			assert.Equal(t, "18", result)
		})

		t.Run("left counts correctly", func(t *testing.T) {
			result, err := prob.Part2([]string{
				"L99",  // at 51
				"L99",  // at 52
				"L99",  // at 53
				"L199", // at 54
				"L54",  // at 0
			})

			require.NoError(t, err)
			assert.Equal(t, "6", result)
		})

		t.Run("left counts correctly", func(t *testing.T) {
			result, err := prob.Part2([]string{
				"R99",  // at 49
				"R99",  // at 48
				"R99",  // at 47
				"R199", // at 46
				"R54",  // at 0
			})

			require.NoError(t, err)
			assert.Equal(t, "6", result)
		})

		t.Run("going not across shouldn't count", func(t *testing.T) {
			result, err := prob.Part2([]string{
				"R31", // at 81
			})

			require.NoError(t, err)
			assert.Equal(t, "0", result)
		})

		t.Run("input example", func(t *testing.T) {
			result, err := prob.Part2([]string{
				"L68", "L30", "R48", "L5", "R60", "L55", "L1", "L99", "R14", "L82",
			})

			require.NoError(t, err)
			assert.Equal(t, "6", result)
		})
	})

}
