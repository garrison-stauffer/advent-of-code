package golang

import (
	"garrison-stauffer.com/advent-of-code/flnt"
	"garrison-stauffer.com/advent-of-code/util"
	"log/slog"
	"math"
	"slices"
	"strconv"
	"strings"
)

type Problem8 struct {
	TopN int
}

func (p *Problem8) Part1(input []string) (int, error) {
	type coordinate struct {
		x, y, z int
	}
	var coordinates []coordinate
	for _, line := range input {
		rawCoords := strings.Split(line, ",")
		coordinates = append(coordinates, coordinate{
			x: util.Must(strconv.Atoi(rawCoords[0])),
			y: util.Must(strconv.Atoi(rawCoords[1])),
			z: util.Must(strconv.Atoi(rawCoords[2])),
		})
	}

	type pair struct {
		distance float64
		aIdx     int
		a        string
		bIdx     int
		b        string
	}
	var distances []pair
	for aIdx, a := range coordinates {
		for bIdx, b := range coordinates {
			if aIdx <= bIdx {
				continue
			}
			x2 := math.Pow(float64(a.x-b.x), 2)
			y2 := math.Pow(float64(a.y-b.y), 2)
			z2 := math.Pow(float64(a.z-b.z), 2)
			distance := math.Sqrt(x2 + y2 + z2)
			distances = append(distances, pair{
				distance: distance,
				aIdx:     aIdx,
				a:        input[aIdx],
				bIdx:     bIdx,
				b:        input[bIdx],
			})
		}
	}

	slices.SortFunc(distances, flnt.SortBy(func(pair pair) float64 {
		return pair.distance
	}))

	circuitMembers := make(map[int][]int)
	circuits := make([]int, len(input))
	for i := range circuits {
		circuits[i] = i
		circuitMembers[i] = []int{i}
	}

	slog.Debug("distances", "distances", distances)

	// [0 1 2 3 4 5 6 7 8 9]
	// [  ^         ^      ] // merge circuits 6 and 1 into 1
	// [0 1 2 3 4 5 1 7 8 9]
	// [^           ^      ] // merge circuits 0 and 1 into 0
	// [0 0 2 3 4 5 0 7 8 9]
	//
	for i := range p.TopN {
		pair := distances[i]
		aCircuit := circuits[pair.aIdx]
		bCircuit := circuits[pair.bIdx]
		slog.Debug("merging these", "a", input[pair.aIdx], "b", input[pair.bIdx], "aCircuit", aCircuit, "bCircuit", bCircuit)
		if aCircuit == bCircuit {
			continue
		}

		// merge the circuits now
		mergingInto := min(aCircuit, bCircuit)
		mergingOutOf := max(aCircuit, bCircuit)

		circuitMembers[mergingInto] = append(circuitMembers[mergingInto], circuitMembers[mergingOutOf]...)
		for _, nodeId := range circuitMembers[mergingOutOf] {
			circuits[nodeId] = mergingInto
		}
		delete(circuitMembers, mergingOutOf)
	}

	var lens []int
	for _, members := range circuitMembers {
		lens = append(lens, len(members))
	}
	slices.Sort(lens)
	slices.Reverse(lens)

	return lens[0] * lens[1] * lens[2], nil
}

func (p *Problem8) Part2(input []string) (int, error) {
	type coordinate struct {
		x, y, z int
	}
	var coordinates []coordinate
	for _, line := range input {
		rawCoords := strings.Split(line, ",")
		coordinates = append(coordinates, coordinate{
			x: util.Must(strconv.Atoi(rawCoords[0])),
			y: util.Must(strconv.Atoi(rawCoords[1])),
			z: util.Must(strconv.Atoi(rawCoords[2])),
		})
	}

	type pair struct {
		distance float64
		aIdx     int
		a        coordinate
		bIdx     int
		b        coordinate
	}
	var distances []pair
	for aIdx, a := range coordinates {
		for bIdx, b := range coordinates {
			if aIdx <= bIdx {
				continue
			}
			x2 := math.Pow(float64(a.x-b.x), 2)
			y2 := math.Pow(float64(a.y-b.y), 2)
			z2 := math.Pow(float64(a.z-b.z), 2)
			distance := math.Sqrt(x2 + y2 + z2)
			distances = append(distances, pair{
				distance: distance,
				aIdx:     aIdx,
				a:        a,
				bIdx:     bIdx,
				b:        b,
			})
		}
	}

	slices.SortFunc(distances, flnt.SortBy(func(pair pair) float64 {
		return pair.distance
	}))

	circuitMembers := make(map[int][]int)
	circuits := make([]int, len(input))
	for i := range circuits {
		circuits[i] = i
		circuitMembers[i] = []int{i}
	}

	slog.Debug("distances", "distances", distances)

	// [0 1 2 3 4 5 6 7 8 9]
	// [  ^         ^      ] // merge circuits 6 and 1 into 1
	// [0 1 2 3 4 5 1 7 8 9]
	// [^           ^      ] // merge circuits 0 and 1 into 0
	// [0 0 2 3 4 5 0 7 8 9]
	//
	for _, pair := range distances {
		aCircuit := circuits[pair.aIdx]
		bCircuit := circuits[pair.bIdx]
		slog.Debug("merging these", "a", input[pair.aIdx], "b", input[pair.bIdx], "aCircuit", aCircuit, "bCircuit", bCircuit)
		if aCircuit == bCircuit {
			continue
		}

		// merge the circuits now
		mergingInto := min(aCircuit, bCircuit)
		mergingOutOf := max(aCircuit, bCircuit)

		circuitMembers[mergingInto] = append(circuitMembers[mergingInto], circuitMembers[mergingOutOf]...)
		for _, nodeId := range circuitMembers[mergingOutOf] {
			circuits[nodeId] = mergingInto
		}
		delete(circuitMembers, mergingOutOf)

		if len(circuitMembers) == 1 {
			return pair.a.x * pair.b.x, nil
		}
	}

	return -1, nil
}
