package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/flnt"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestProblem3(t *testing.T) {
	prob := Problem3{}

	t.Run("run the problem", func(t *testing.T) {
		result, err := prob.Part1(ReadProblemInput(3, 1))
		result2, err2 := prob.Part2(ReadProblemInput(3, 1))
		result3, err3 := prob.Part2Try2(ReadProblemInput(3, 1))

		require.NoError(t, err)
		require.NoError(t, err2)
		require.NoError(t, err3)

		fmt.Printf("result is %v  %v  %v\n", result, result2, result3)
	})

	t.Run("part 1 test cases", func(t *testing.T) {

		do := Suite(prob.Part1)

		t.Run("provided test cases", func(t *testing.T) {
			do(t).parsing(flnt.S("987654321111111")).hasToBe(98).
				parsing(flnt.S("811111111111119")).hasToBe(89).
				parsing(flnt.S("234234234234278")).hasToBe(78).
				parsing(flnt.S("818181911112111")).hasToBe(92)
		})
	})
	t.Run("part 2 test cases", func(t *testing.T) {

		do := Suite(prob.Part2Try2)

		t.Run("provided test cases", func(t *testing.T) {
			do(t).parsing(flnt.S("987654321111111")).hasToBe(987654321111).
				debug().parsing(flnt.S("811111111111119")).hasToBe(811111111119).
				parsing(flnt.S("234234234234278")).hasToBe(434234234278).
				parsing(flnt.S("818181911112111")).hasToBe(888911112111).
				debug().parsing(flnt.S("987954321000123456789")).hasToBe(995433456789).resume().
				parsing(flnt.S("987954321000123456789", "818181911112111")).hasToBe(888911112111 + 995433456789)
		})
	})
}
