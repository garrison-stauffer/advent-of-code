package golang

import (
	"garrison-stauffer.com/advent-of-code/util"
	"log/slog"
	"strconv"
)

type Problem3 struct {
}

func (p *Problem3) Part1(input []string) (int, error) {
	sum := 0

	for _, line := range input {
		first := line[0]
		second := line[1]

		for i := 2; i < len(line); i++ {
			digit := line[i]

			if second > first {
				first = second
				second = digit
			} else if digit > second {
				second = digit
			}
		}

		sum += int(10 * (first - '0'))
		sum += int(second - '0')
	}

	return sum, nil
}

func (p *Problem3) Part2(input []string) (int64, error) {
	var sum int64 = 0

	for _, line := range input {
		number := make([]uint8, 13)

		for i := 0; i < 12; i++ {
			number[i] = line[i]
		}

		var bubbleUp func(checkIndex int, val uint8)
		bubbleUp = func(checkIndex int, val uint8) {
			if checkIndex < 0 {
				return
			}

			if number[checkIndex] < val {
				// bubble up
				bubbleUp(checkIndex-1, number[checkIndex])

				number[checkIndex] = val
			}
		}

		bubbling := -1

		for i := 0; i < 12; i++ {
			if number[i] < number[i+1] {
				bubbling = i
				break
			}
		}

		toDecimal := func() int {
			powerOfTen := 1
			sum := 0
			for i := 11; i >= 0; i-- {
				sum += powerOfTen * int(number[i]-'0')
				powerOfTen *= 10
			}
			return sum
		}

		for i := 12; i < len(line); i++ {
			// assign to last digit
			number[12] = line[i]

			// bubble up from known check
			if bubbling < 0 {
				if number[12] > number[11] {
					number[11] = number[12]
					bubbling = 11
				}
			} else {
				for updating := bubbling; updating < 12; updating++ {
					number[updating] = number[updating+1]
				}

				// see if we have somewhere else to bubble from
				for i := 0; i < 12; i++ {
					if number[i] < number[i+1] {
						bubbling = i
						break
					}
				}

			}
			slog.Debug("value is", "in decimal", toDecimal())
		}

		powerOfTen := 1
		for i := 11; i >= 0; i-- {
			sum += int64(powerOfTen * int(number[i]-'0'))
			powerOfTen *= 10
		}
	}

	return sum, nil
}

func (p *Problem3) Part2Try2(input []string) (int64, error) {
	var sum int64 = 0

	for _, line := range input {
		var num int64 = 0
		var onOrAfter = 0
		// find the max starting at idx and at least Nth from the end

		for i := range 12 {
			nextMax := -1
			idxOfIt := -1

			for checking := onOrAfter; checking < len(line)-(12-(i+1)); checking++ {
				val := int(line[checking] - '0')
				if val > nextMax {
					nextMax = val
					idxOfIt = checking
				}
			}

			num = (num * 10) + int64(nextMax)
			onOrAfter = idxOfIt + 1
		}

		sum += num
	}

	return sum, nil
}

func (p *Problem3) ParseLockInputLine(line string) (string, int) {
	runes := []rune(line)
	numStr := string(runes[1:])

	return string(runes[0]), util.Must(strconv.Atoi(numStr))
}
