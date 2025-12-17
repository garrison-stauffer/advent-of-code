package golang

import (
	"strings"
)

type Problem11 struct{}

func (p *Problem11) Part1(input []string) (int, error) {
	// key: node, value: edges that begin at it
	graph := make(map[string][]string)
	// key: node, value: all edges that end at it
	reverseIndex := make(map[string][]string)

	for _, line := range input {
		key, valuesStr := strings.Split(line, ": ")[0], strings.Split(line, ": ")[1]
		values := strings.Split(valuesStr, " ")

		graph[key] = values
		for _, value := range values {
			reverseIndex[value] = append(reverseIndex[value], key)
		}
	}

	// go ahead and discover all accessible nodes
	memoizer := make(map[string]int)
	memoizer["you"] = 1
	// determine costs of all nodes that point to out

	var findCostToVisitNode func(key string) int
	findCostToVisitNode = func(key string) int {
		if paths, ok := memoizer[key]; ok {
			// we do a little memoization
			return paths
		}

		paths := 0
		for _, neighbor := range reverseIndex[key] {
			paths += findCostToVisitNode(neighbor)
		}
		memoizer[key] = paths

		return paths
	}

	// okay from out how many paths are there
	// for each node in reverseEdge, they get a cost of 1

	return findCostToVisitNode("out"), nil
}

func (p *Problem11) Part2(input []string) (int64, error) {
	// key: node, value: edges that begin at it
	graph := make(map[string][]string)
	// key: node, value: all edges that end at it
	reverseIndex := make(map[string][]string)

	for _, line := range input {
		key, valuesStr := strings.Split(line, ": ")[0], strings.Split(line, ": ")[1]
		values := strings.Split(valuesStr, " ")

		graph[key] = values
		for _, value := range values {
			reverseIndex[value] = append(reverseIndex[value], key)
		}
	}

	// need to go from svr to out
	// also need to know which comes first in the dag
	// then we can look at how many from start -> first, first -> second, second -> home
	// because it's a dag this should be fine to just run right? could a path from first -> second include any values in second -> home
	// or svr -> first?

	// determine costs of all nodes that point to out
	memoizer := make(map[string]int)
	var findCostToVisitNodeFrom func(key, from string, chain []string) int
	findCostToVisitNodeFrom = func(key, from string, chain []string) int {
		if paths, ok := memoizer[key]; ok {
			// we do a little memoization
			return paths
		} else if key == from {
			return 1
		}

		paths := 0
		for _, neighbor := range reverseIndex[key] {
			paths += findCostToVisitNodeFrom(neighbor, from, append(chain[0:], key))
		}
		memoizer[key] = paths

		return paths
	}

	// okay from out how many paths are there
	// for each node in reverseEdge, they get a cost of 1

	first := int64(findCostToVisitNodeFrom("fft", "svr", []string{}))
	memoizer = make(map[string]int)
	second := int64(findCostToVisitNodeFrom("dac", "fft", []string{}))
	memoizer = make(map[string]int)
	third := int64(findCostToVisitNodeFrom("out", "dac", []string{}))

	return first * second * third, nil
}
