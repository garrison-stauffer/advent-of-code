package golang

import (
	"garrison-stauffer.com/advent-of-code/util"
	"strconv"
)

type Problem1 struct {
}

func (p *Problem1) Run(input []string) (string, error) {
	landedAtZero := 0
	crossedZero := 0

	cursor := 50
	cursorClamp := 50

	for _, line := range input {
		dir, amt := p.ParseLockInputLine(line)

		// going around 100 is always going to cross zero
		crossedZero += amt / 100
		amt = amt % 100

		if dir == "L" {
			cursor = (cursor + 100 - amt) % 100
			cursorClamp = max(0, cursor-amt)
		} else if dir == "R" {
			cursor = (cursor + amt) % 100
			cursorClamp = min(100, cursor+amt)
		}

		if cursor == 0 || cursor == 100 {
			landedAtZero++
			crossedZero++
		} else if cursorClamp == 0 || cursorClamp == 100 {
			crossedZero++
		}

		cursor = cursor + 100%100
	}

	return strconv.Itoa(landedAtZero), nil
}

func (p *Problem1) Part2(input []string) (string, error) {
	crossedZero := 0
	cursor := 50

	for _, line := range input {
		dir, amt := p.ParseLockInputLine(line)

		// going around 100 is always going to cross zero
		crossedZero += amt / 100
		amt = amt % 100
		wasOnZero := cursor == 0

		if amt == 0 {
			continue
		}

		if dir == "L" {
			cursor -= amt
		} else {
			cursor += amt
		}

		if !wasOnZero && (cursor > 100 || cursor < 0) {
			crossedZero++
		}
		cursor = (cursor + 100) % 100

		if cursor == 0 {
			crossedZero++
		}
	}

	return strconv.Itoa(crossedZero), nil
}

func (p *Problem1) ParseLockInputLine(line string) (string, int) {
	runes := []rune(line)
	numStr := string(runes[1:])

	return string(runes[0]), util.Must(strconv.Atoi(numStr))
}
