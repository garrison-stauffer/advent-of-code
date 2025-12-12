package golang

import (
	"garrison-stauffer.com/advent-of-code/flnt"
	"garrison-stauffer.com/advent-of-code/util"
	"slices"
	"strconv"
	"strings"
)

type Problem5 struct {
}

func (p *Problem5) Part1(input []string) (int, error) {
	split := slices.Index(input, "")
	rangesInput := input[:split]
	ids := input[split+1:]

	type idRange struct {
		begin int
		end   int
	}

	ranges := make([]*idRange, len(rangesInput))
	for i, line := range rangesInput {
		begin, end :=
			util.Must(strconv.Atoi(strings.Split(line, "-")[0])),
			util.Must(strconv.Atoi(strings.Split(line, "-")[1]))

		ranges[i] = &idRange{begin, end}
	}

	slices.SortFunc(ranges, flnt.SortBy(func(e *idRange) int {
		return e.begin
	}))

	checking := 0
	for checking < len(ranges)-1 {
		current := ranges[checking]
		next := ranges[checking+1]

		if current.begin == next.begin {
			current.end = max(current.end, next.end)
			ranges = slices.Delete(ranges, checking+1, checking+2)
		} else if current.end >= next.begin {
			current.end = max(current.end, next.end)
			ranges = slices.Delete(ranges, checking+1, checking+2)
		} else {
			checking++
		}
	}

	valid := 0
	for _, line := range ids {
		id := util.Must(strconv.Atoi(line))

		if slices.ContainsFunc(ranges, func(e *idRange) bool {
			return e.begin <= id && e.end >= id
		}) {
			valid++
		}
	}

	return valid, nil
}

func (p *Problem5) Part2(input []string) (int64, error) {
	split := slices.Index(input, "")
	rangesInput := input[:split]

	type idRange struct {
		begin int
		end   int
	}

	ranges := make([]*idRange, len(rangesInput))
	for i, line := range rangesInput {
		begin, end :=
			util.Must(strconv.Atoi(strings.Split(line, "-")[0])),
			util.Must(strconv.Atoi(strings.Split(line, "-")[1]))

		ranges[i] = &idRange{begin, end}
	}

	slices.SortFunc(ranges, flnt.SortBy(func(e *idRange) int {
		return e.begin
	}))

	checking := 0
	for checking < len(ranges)-1 {
		current := ranges[checking]
		next := ranges[checking+1]

		if current.begin == next.begin {
			current.end = max(current.end, next.end)
			ranges = slices.Delete(ranges, checking+1, checking+2)
		} else if current.end >= next.begin {
			current.end = max(current.end, next.end)
			ranges = slices.Delete(ranges, checking+1, checking+2)
		} else {
			checking++
		}
	}

	var totalValid int64 = 0
	for _, ids := range ranges {
		totalValid += int64(ids.end-ids.begin) + 1
	}
	return totalValid, nil
}
