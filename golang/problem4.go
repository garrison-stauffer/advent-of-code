package golang

import (
	"log/slog"
)

type Problem4 struct {
}

func (p *Problem4) Part1(input []string) (int, error) {
	// parse the input
	moveableRolls := 0
	paperMap := make([][]bool, len(input))
	for i := range paperMap {
		paperMap[i] = make([]bool, len(input[i]))
	}

	for row, line := range input {
		for col, char := range line {
			paperMap[row][col] = char == '@'
		}
	}

	for row := 0; row < len(paperMap); row++ {
		for col := 0; col < len(paperMap[row]); col++ {
			if input[row][col] != '@' {
				continue
			}

			toCheck := p.getCellsToCheck(paperMap, row, col)
			neighbors := 0
			for _, check := range toCheck {
				if paperMap[check.row][check.col] {
					neighbors++
				}
			}

			if neighbors < 4 {
				moveableRolls++
			}
		}
	}

	return moveableRolls, nil
}

func (p *Problem4) Part2(input []string) (int, error) {
	// parse the input
	moveableRolls := 0
	paperMap := make([][]bool, len(input))
	adjacencies := make([][]int, len(input))
	for i := range paperMap {
		paperMap[i] = make([]bool, len(input[i]))
		adjacencies[i] = make([]int, len(input[i]))
	}

	for row, line := range input {
		for col, char := range line {
			paperMap[row][col] = char == '@'
		}
	}

	// seed adjacency counts
	for row := 0; row < len(paperMap); row++ {
		for col := 0; col < len(paperMap[row]); col++ {
			if input[row][col] != '@' {
				continue
			}

			toCheck := p.getCellsToCheck(paperMap, row, col)
			neighbors := 0
			for _, check := range toCheck {
				if paperMap[check.row][check.col] {
					neighbors++
				}
			}

			adjacencies[row][col] = neighbors
		}
	}

	hasRowsToRemove := func() bool {
		for row := range adjacencies {
			for col := range adjacencies[row] {
				if adjacencies[row][col] < 4 && paperMap[row][col] {
					return true
				}
			}
		}
		return false
	}

	for hasRowsToRemove() {
		nextAdjacency := make([][]int, len(adjacencies))
		for i := range nextAdjacency {
			nextAdjacency[i] = make([]int, len(adjacencies[i]))
			for j, val := range adjacencies[i] {
				nextAdjacency[i][j] = val
			}
		}

		for row := range adjacencies {
			for col := range adjacencies[row] {
				neighbors := adjacencies[row][col]
				if neighbors > 3 || !paperMap[row][col] {
					nextAdjacency[row][col] = nextAdjacency[row][col]
					continue
				}

				cellsToCheck := p.getCellsToCheck(paperMap, row, col)
				for _, check := range cellsToCheck {
					current := nextAdjacency[check.row][check.col]

					nextAdjacency[check.row][check.col] = max(0, current-1)
					slog.Debug("updating adjacency", "row", row, "col", col, "check", check, "value", max(0, current-1), "actual", nextAdjacency[check.row][check.col])
				}

				nextAdjacency[row][col] = 0
				paperMap[row][col] = false
				moveableRolls++
			}
		}

		adjacencies = nextAdjacency
		slog.Debug("removed rows", "count", moveableRolls)
	}

	return moveableRolls, nil
}

func (p *Problem4) getCellsToCheck(paper [][]bool, row, col int) []struct{ row, col int } {
	maxRow := len(paper)
	maxCol := len(paper[0])

	var toCheck []struct{ row, col int }
	for i := -1; i <= 1; i++ {
		for j := -1; j <= 1; j++ {
			if i == 0 && j == 0 {
				continue
			}

			maybeRow := row + i
			maybeCol := col + j
			if maybeRow < 0 || maybeRow >= maxRow {
				continue
			}
			if maybeCol < 0 || maybeCol >= maxCol {
				continue
			}

			toCheck = append(toCheck, struct{ row, col int }{maybeRow, maybeCol})
		}
	}

	return toCheck
}
