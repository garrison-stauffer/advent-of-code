package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/flnt"
	"garrison-stauffer.com/advent-of-code/util"
	"github.com/stretchr/testify/require"
	"strconv"
	"strings"
	"testing"
)

func TestProblem9(t *testing.T) {
	prob := Problem9{}

	t.Run("run the problem", func(t *testing.T) {
		//setDebugger(true)
		result, err := prob.Part1(ReadProblemInput(9, 1))
		result2, err2 := prob.Part2(ReadProblemInput(9, 1))

		require.NoError(t, err)
		require.NoError(t, err2)

		fmt.Printf("result is %v  %v \n", result, result2)
	})

	t.Run("part 1 test cases", func(t *testing.T) {
		do := Suite(prob.Part1)

		t.Run("provided test case", func(t *testing.T) {
			do(t).debug().parsing(flnt.Spl(
				`7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3`,
			)).hasToBe(50)
		})
	})
	t.Run("part 2 test cases", func(t *testing.T) {

		do := Suite(prob.Part2)

		t.Run("provided test cases", func(t *testing.T) {
			do(t).debug().parsing(flnt.Spl(
				`7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3`,
			)).hasToBe(24)
		})
		t.Run("provided test cases", func(t *testing.T) {
			do(t).debug().parsing(flnt.Spl(
				`1,2
1,7
3,7
3,9
4,9
4,5
5,5
5,9
6,9
6,5
7,5
7,9
8,9
8,2	`,
			)).hasToBe(36)
		})
	})

	t.Run("explore the data", func(t *testing.T) {

		input := ReadProblemInput(9, 1)
		type coordinate struct {
			x, y int
		}
		coordinates := make([]coordinate, len(input))
		for i, line := range input {
			x := util.Must(strconv.Atoi(strings.Split(line, ",")[0]))
			y := util.Must(strconv.Atoi(strings.Split(line, ",")[1]))
			coordinates[i] = coordinate{x, y}
		}

		for i := range coordinates {
			a := coordinates[i]
			b := coordinates[(i+1)%len(coordinates)]
			c := coordinates[(i+2)%len(coordinates)]

			if a.x == b.x && a.x == c.x {
				panic("yes there is a chained vertical")
			}
			if a.y == b.y && a.y == c.y {
				panic("yes there is a chained horizontal")
			}
		}
	})

	t.Run("explore what my results are", func(t *testing.T) {

		input := ReadProblemInput(9, 1)
		type coordinate struct {
			x, y int
		}
		coordinates := make([]coordinate, len(input))
		for i, line := range input {
			x := util.Must(strconv.Atoi(strings.Split(line, ",")[0]))
			y := util.Must(strconv.Atoi(strings.Split(line, ",")[1]))
			coordinates[i] = coordinate{x, y}
		}

		for i := range coordinates {
			a := coordinates[i]
			b := coordinates[(i+1)%len(coordinates)]
			c := coordinates[(i+2)%len(coordinates)]

			if a.x == b.x && a.x == c.x {
				panic("yes there is a chained vertical")
			}
			if a.y == b.y && a.y == c.y {
				panic("yes there is a chained horizontal")
			}
		}
	})
}
