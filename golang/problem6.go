package golang

import (
	"garrison-stauffer.com/advent-of-code/util"
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
	numProblems := len(strings.Fields(input[0]))
	subproblems := make([]subproblem, numProblems)
	for i := range subproblems {
		subproblems[i] = subproblem{
			inputs: make([]int64, len(input)-1),
		}
	}

	for row := range len(input) - 1 {
		for problem, operand := range strings.Fields(input[row]) {
			// reverse it
			n := 0

			rune := make([]rune, len(operand))
			for _, r := range operand {
				rune[n] = r
				n++
			}
			rune = rune[0:n]
			// Reverse
			for i := 0; i < n/2; i++ {
				rune[i], rune[n-1-i] = rune[n-1-i], rune[i]
			}
			newOperand := string(rune)
			subproblems[problem].inputs[row] = int64(util.Must(strconv.Atoi(newOperand)))
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
