package golang

import (
	"garrison-stauffer.com/advent-of-code/util"
	"slices"
	"strconv"
	"strings"
)

type Problem6 struct {
}

func (p *Problem6) Part1(input []string) (int64, error) {
	type subproblem struct {
		inputs    []int64
		operation func(a, b int64) int64
	}
	numProblems := len(strings.Fields(input[0]))
	subproblems := make([]subproblem, numProblems)
	for i := range subproblems {
		subproblems[i] = subproblem{
			inputs: make([]int64, len(input)-1),
		}
	}

	for row := range len(input) - 1 {
		for problem, operand := range strings.Fields(input[row]) {
			subproblems[problem].inputs[row] = int64(util.Must(strconv.Atoi(operand)))
		}
	}

	for problem, operator := range strings.Fields(input[len(input)-1]) {
		if operator == "*" {
			subproblems[problem].operation = p.Multiply
		} else if operator == "+" {
			subproblems[problem].operation = p.Add
		} else {
			panic("unknown operator " + operator)
		}
	}

	total := int64(0)
	for _, problem := range subproblems {
		for i := range len(problem.inputs) - 1 {
			// copy it forward
			problem.inputs[i+1] = problem.operation(problem.inputs[i], problem.inputs[i+1])
		}

		total += problem.inputs[len(problem.inputs)-1]
	}

	return total, nil
}

func (p *Problem6) Add(a, b int64) int64 {
	return a + b
}

func (p *Problem6) Multiply(a, b int64) int64 {
	return a * b
}

func (p *Problem6) Part2(input []string) (int64, error) {
	type subproblem struct {
		inputs    []int64
		operation func(a, b int64) int64
	}
	subproblems := []subproblem{}

	parseNumber := func(i int) int64 {
		var value int64 = 0
		for line := 0; line < len(input)-1; line++ {
			char := input[line][i]
			if char == ' ' {
				continue
			}
			value = (value * 10) + int64(char-'0')
		}

		return value
	}

	isSplit := func(i int) bool {
		for line := 0; line < len(input)-1; line++ {
			if input[line][i] != ' ' {
				return false
			}
		}

		return true
	}

	lineLength := len(input[0])
	var problem subproblem
	for i := lineLength - 1; i >= 0; i-- {
		if isSplit(i) {
			subproblems = append(subproblems, problem)
			problem = subproblem{}
			continue
		}

		problem.inputs = append(problem.inputs, parseNumber(i))
	}

	subproblems = append(subproblems, problem)

	arr := strings.Fields(input[len(input)-1])
	slices.Reverse(arr)
	for problem, operator := range arr {
		if operator == "*" {
			subproblems[problem].operation = p.Multiply
		} else if operator == "+" {
			subproblems[problem].operation = p.Add
		} else {
			panic("unknown operator " + operator)
		}
	}

	total := int64(0)
	for _, problem := range subproblems {
		for i := range len(problem.inputs) - 1 {
			// copy it forward
			problem.inputs[i+1] = problem.operation(problem.inputs[i], problem.inputs[i+1])
		}

		total += problem.inputs[len(problem.inputs)-1]
	}

	return total, nil
}
