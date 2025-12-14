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

type Problem9 struct{}

func (p *Problem9) Part1(input []string) (int, error) {
	type coordinate struct {
		x, y int
	}
	coordinates := make([]coordinate, len(input))
	for i, line := range input {
		x := util.Must(strconv.Atoi(strings.Split(line, ",")[0]))
		y := util.Must(strconv.Atoi(strings.Split(line, ",")[1]))
		coordinates[i] = coordinate{x, y}
	}

	largest := -1
	for aIdx, a := range coordinates {
		for bIdx, b := range coordinates {
			if aIdx <= bIdx {
				continue
			}

			// are there any coordinates that would block this?

			width := max(a.x, b.x) - min(a.x, b.x) + 1
			height := max(a.y, b.y) - min(a.y, b.y) + 1
			largest = max(largest, width*height)
		}
	}

	return largest, nil
}

func (p *Problem9) Part2(input []string) (int, error) {
	type coordinate struct {
		x, y int
	}
	type lines struct {
		a, b       coordinate
		aIdx, bIdx int
		idx        int
	}
	coordinates := make([]coordinate, len(input))
	for i, line := range input {
		x := util.Must(strconv.Atoi(strings.Split(line, ",")[0]))
		y := util.Must(strconv.Atoi(strings.Split(line, ",")[1]))
		coordinates[i] = coordinate{x, y}
	}

	connections := make([]lines, len(input))
	for i := range coordinates {
		aIdx := i
		bIdx := (i + 1) % len(input)
		a := coordinates[aIdx]
		b := coordinates[bIdx]
		connections[i] = lines{a, b, aIdx, bIdx, i}
	}

	// ..............
	// .......#XXX#..
	// .......X...X..
	// ..#XXXX#...X..
	// ..X........X..
	// ..#XXXXXX#.X..
	// .........X.X..
	// .........#X#..
	// ..............

	findOverlappingForX := func(x int) []lines {
		result := []lines{}
		for _, line := range connections {
			x1 := min(line.a.x, line.b.x)
			x2 := max(line.a.x, line.b.x)

			if x1 <= x && x2 >= x {
				result = append(result, line)
			}
		}
		return result
	}

	var getRangesOverXCache = make(map[int][]struct {
		begin, end int
		isTiled    bool
	})
	getRangesOverX := func(x int) []struct {
		begin, end int
		isTiled    bool
	} {
		if known, ok := getRangesOverXCache[x]; ok {
			return known
		}

		overlappingRanges := findOverlappingForX(x)
		slices.SortFunc(overlappingRanges, flnt.SortBy(func(line lines) int {
			return min(line.a.y, line.b.y)
		}))

		var rangesForX []struct {
			begin, end int
			isTiled    bool
		}
		rangeStartY := 0
		isInside := false

		for i := 0; i < len(overlappingRanges); i++ {
			// okay now we apply our ugly logic...
			// is this horizontal or vertical
			line := overlappingRanges[i]
			isVertical := line.a.x == line.b.x

			if isVertical {
				// on the border of length of map it gets awkward
				var previousIdx, nextIdx int
				if max(line.aIdx, line.bIdx)-min(line.aIdx, line.bIdx) > 1 {
					// previous is actually 499 since we are crossing modulo point
					previousIdx = (max(line.aIdx, line.bIdx) - 1 + len(coordinates)) % len(coordinates)
					nextIdx = (min(line.aIdx, line.bIdx) + 1 + len(coordinates)) % len(coordinates)
				} else {
					previousIdx = (min(line.aIdx, line.bIdx) - 1 + len(coordinates)) % len(coordinates)
					nextIdx = (max(line.aIdx, line.bIdx) + 1 + len(coordinates)) % len(coordinates)
				}
				previous := coordinates[previousIdx]
				next := coordinates[nextIdx]
				// if they go the same way then we stay at what ever our previous state was (we rode an edge, but did not cross a border)
				goesAllLeft := previous.x < line.a.x && next.x < line.a.x
				goesAllRight := previous.x > line.a.x && next.x > line.a.x
				isBorder := !goesAllRight && !goesAllLeft

				if !isBorder && !isInside {
					// add 2 ranges - one for the blank area, and one for the valid area
					// does our vertical line start at the exact edge? then no empty range!
					hasEmptyRange := rangeStartY != min(line.a.y, line.b.y)
					if hasEmptyRange {
						rangesForX = append(rangesForX, struct {
							begin, end int
							isTiled    bool
						}{begin: rangeStartY, end: min(line.a.y, line.b.y) - 1, isTiled: false})
					}
					rangesForX = append(rangesForX, struct {
						begin, end int
						isTiled    bool
					}{begin: min(line.a.y, line.b.y), end: max(line.a.y, line.b.y), isTiled: true})

					rangeStartY = max(line.a.y, line.b.y) + 1
				} else if isBorder {
					// it's a border, we either finish recording a empty region or we
					if isInside {
						rangesForX = append(rangesForX, struct {
							begin, end int
							isTiled    bool
						}{begin: rangeStartY, end: max(line.a.y, line.b.y), isTiled: true})
						rangeStartY = max(line.a.y, line.b.y) + 1
						isInside = false
					} else {
						// outside
						hasEmptyRange := rangeStartY != min(line.a.y, line.b.y)
						if hasEmptyRange {
							rangesForX = append(rangesForX, struct {
								begin, end int
								isTiled    bool
							}{begin: rangeStartY, end: min(line.a.y, line.b.y) - 1, isTiled: false})
						}
						rangeStartY = min(line.a.y, line.b.y)
						isInside = true
					}
				}
			} else {
				// it's horizontal
				// if its vertice lands on our column, skip processing
				if line.a.x == x || line.b.x == x {
					continue
				}

				if !isInside {
					// I should be able to just skip vertex hits, they will always be covered by another
					// I increase at the end of the vertical for the next "zone, but actually it is continued here; maybe I can detect if range start is y+1
					// nothing really to do here, we ended on the vertex so it should be fine
					hasEmptyRange := rangeStartY != line.a.y

					if hasEmptyRange {
						rangesForX = append(rangesForX, struct {
							begin, end int
							isTiled    bool
						}{begin: rangeStartY, end: min(line.a.y, line.b.y) - 1, isTiled: false})
					}
					rangeStartY = line.a.y
					isInside = true
				} else {
					rangesForX = append(rangesForX, struct {
						begin, end int
						isTiled    bool
					}{begin: rangeStartY, end: line.a.y, isTiled: true})
					rangeStartY = line.a.y + 1
					isInside = false
				}
			}
		}
		rangesForX = append(rangesForX, struct {
			begin, end int
			isTiled    bool
		}{begin: rangeStartY, end: math.MaxInt, isTiled: isInside})

		getRangesOverXCache[x] = rangesForX
		return rangesForX
	}
	//
	//minX := math.MaxInt
	//maxX := math.MinInt
	//for i := 0; i < len(coordinates)-1; i++ {
	//	minX = min(minX, coordinates[i].x)
	//	minX = min(minX, coordinates[i+1].x)
	//	maxX = max(maxX, coordinates[i].x)
	//	maxX = max(maxX, coordinates[i+1].x)
	//	ranges := getRangesOverX(coordinates[i].x)
	//
	//	for i := 0; i < len(ranges)-1; i++ {
	//		curr := ranges[i]
	//		next := ranges[i+1]
	//		if curr.begin > curr.end {
	//			slog.Debug("current range has higher beginning than end", "x", coordinates[i].x, "current range", curr)
	//		}
	//		if curr.end+1 != next.begin {
	//			slog.Debug("current range end does not match next range begin", "x", coordinates[i].x, "current range", curr, "next", next)
	//		}
	//	}
	//
	//	if ranges[len(ranges)-1].end != math.MaxInt {
	//		slog.Debug("end of range isn't max int", "x", coordinates[i].x, "range", ranges[len(ranges)-1])
	//	}
	//}
	//for i := minX; i <= maxX; i++ {
	//	ranges := getRangesOverX(i)
	//
	//	for i := 0; i < len(ranges)-1; i++ {
	//		curr := ranges[i]
	//		next := ranges[i+1]
	//		if curr.begin > curr.end {
	//			slog.Debug("current range has higher beginning than end", "x", coordinates[i].x, "current range", curr)
	//		}
	//		if curr.end+1 != next.begin {
	//			slog.Debug("current range end does not match next range begin", "x", coordinates[i].x, "current range", curr, "next", next)
	//		}
	//	}
	//
	//	if ranges[len(ranges)-1].end != math.MaxInt {
	//		slog.Debug("end of range isn't max int", "x", coordinates[i].x, "range", ranges[len(ranges)-1])
	//	}
	//}

	// consider each pair again

	largest := -1
	for aIdx, a := range coordinates {
		for bIdx, b := range coordinates {
			if aIdx <= bIdx {
				continue
			}

			// assemble the rectangle
			x1 := min(a.x, b.x)
			x2 := max(a.x, b.x)
			y1 := min(a.y, b.y)
			y2 := max(a.y, b.y)
			isValidPair := true

			for x := x1; x <= x2; x++ {
				ranges := getRangesOverX(x)
				used := []struct {
					begin, end int
					isTiled    bool
				}{}
				isValid := true
				for _, chunk := range ranges {
					hasOverlap := y1 <= chunk.end && y2 >= chunk.begin
					if hasOverlap {
						//slog.Debug("found some overlap between these", "y1", y1, "y2", y2, "chunk", chunk, "y1 <= chunk.end", y1 <= chunk.end, "y2 >= chunk.begin", y2 >= chunk.begin)
						isValid = isValid && chunk.isTiled
						used = append(used, chunk)
					}
				}

				if !isValid {
					slog.Debug("invalid block", "a", a, "b", b, "x", x, "used", used)
					isValidPair = false
					break
				} else {
					slog.Debug("valid but should it?", "a", a, "b", b, "x", x, "used", used)
				}
			}

			if isValidPair {
				width := x2 - x1 + 1
				height := y2 - y1 + 1
				largest = max(largest, width*height)
				if largest == width*height {
					slog.Info("new max biggest at", "a", a, "b", b)
				}
			}

		}
	}

	return largest, nil
}
