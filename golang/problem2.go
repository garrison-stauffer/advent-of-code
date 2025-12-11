package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/util"
	"log/slog"
	"regexp"
	"strconv"
	"strings"
)

type Problem2 struct {
}

func (p *Problem2) Part1(input string) (int, error) {
	template := regexp.MustCompile(`(\d+)-(\d+)`)

	sumOfInvalids := 0
	for _, sample := range strings.Split(input, ",") {

		match := template.FindStringSubmatch(sample)
		if len(match) != 3 {
			return -1, fmt.Errorf("bad template match for '%s': %v", sample, match)
		}
		var begin, end int
		var err error
		if begin, err = strconv.Atoi(match[1]); err != nil {
			return -1, fmt.Errorf("bad string match for begin in '%s'", sample)
		}
		if end, err = strconv.Atoi(match[2]); err != nil {
			return -1, fmt.Errorf("bad string match for begin in '%s'", sample)
		}

		for i := begin; i <= end; i++ {
			str := strconv.Itoa(i)
			if len(str)%2 == 1 {
				// this is an even number power of 10, i seeks forward until the next odd power of 10
				// e.g. we have 191; no possible invalid one is around until 1010, skip to 1000 and check from there
				nextPower := 10
				for range len(str) - 1 {
					nextPower *= 10
				}

				i = nextPower
			}

			midpoint := len(str) / 2
			first := str[:midpoint]
			second := str[midpoint:]
			if first == second {
				sumOfInvalids += i
			}
		}
	}

	return sumOfInvalids, nil
}

func (p *Problem2) Part2(input string) (int, error) {
	template := regexp.MustCompile(`(\d+)-(\d+)`)

	sumOfInvalids := 0
	for _, sample := range strings.Split(input, ",") {

		match := template.FindStringSubmatch(sample)
		if len(match) != 3 {
			return -1, fmt.Errorf("bad template match for '%s': %v", sample, match)
		}
		var begin, end int
		var err error
		if begin, err = strconv.Atoi(match[1]); err != nil {
			return -1, fmt.Errorf("bad string match for begin in '%s'", sample)
		}
		if end, err = strconv.Atoi(match[2]); err != nil {
			return -1, fmt.Errorf("bad string match for begin in '%s'", sample)
		}

		for i := begin; i <= end; i++ {
			str := strconv.Itoa(i)
			if len(str) == 1 {
				continue
			}

			keys := map[string]any{}
			for length := 1; length <= (1+len(str))/2; length++ {
				if len(str)%length == 0 {
					// if it's not divisible then there's no point
					key := str[0 : 0+length]
					keys[key] = nil
				}
			}

			slog.Debug("keys for value are", "keys", keys)

			for key := range keys {
				isValid := true
				length := len(key)
				for i := length; i+length <= len(str) && isValid; i += length {
					substring := str[i : i+length]

					if substring != key {
						isValid = false
					}
				}

				if isValid {
					sumOfInvalids += i
					break
				}
			}
		}
	}

	return sumOfInvalids, nil
}

func (p *Problem2) ParseLockInputLine(line string) (string, int) {
	runes := []rune(line)
	numStr := string(runes[1:])

	return string(runes[0]), util.Must(strconv.Atoi(numStr))
}
