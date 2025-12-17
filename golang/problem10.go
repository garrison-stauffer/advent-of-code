package golang

import (
	"fmt"
	"garrison-stauffer.com/advent-of-code/flnt"
	"garrison-stauffer.com/advent-of-code/util"
	"log/slog"
	"math"
	"slices"
	"strconv"
	"strings"
	"sync"
)

type Problem10 struct{}

func (p *Problem10) Part1(input []string) (int, error) {

	type panel struct {
		lights   uint32
		buttons  []uint32
		joltages []int
	}

	var panels []panel = make([]panel, len(input))
	for i, line := range input {
		pieces := strings.Split(line, " ")

		lights := strings.Trim(pieces[0], "[]")
		buttons := pieces[1 : len(pieces)-1]
		joltages := strings.Split(strings.Trim(pieces[len(pieces)-1], "{}"), ",")

		var row panel = panel{
			lights:   0,
			buttons:  make([]uint32, len(buttons)),
			joltages: make([]int, len(joltages)),
		}

		for i, char := range lights {
			if char == '#' {
				row.lights |= uint32(1 << i)
			}
		}

		for i, button := range buttons {
			button = strings.Trim(button, "()")
			idsStr := strings.Split(button, ",")
			var buttonNum uint32
			for _, id := range idsStr {
				val := util.Must(strconv.Atoi(id))
				buttonNum |= 1 << val
			}
			row.buttons[i] = buttonNum
		}

		for i, joltage := range joltages {
			row.joltages[i] = util.Must(strconv.Atoi(joltage))
		}

		panels[i] = row
	}

	sumOfMoves := 0
	// do bfs to try all rows, keep a memoization factor
	for _, target := range panels {

		var visited map[uint32]any = make(map[uint32]any)
		var toVisit [][]uint32 = [][]uint32{
			{0, 0},
		}

		minMoves := 0

		for i := 0; i < len(toVisit); i++ {
			visiting := toVisit[i]
			key, moves := visiting[0], visiting[1]
			if _, ok := visited[key]; ok {
				continue
			}

			visited[key] = nil

			if key == target.lights {
				minMoves = int(moves)
				break
			}

			for _, transform := range target.buttons {
				next := key ^ transform
				nextMoves := moves + 1

				toVisit = append(toVisit, []uint32{next, nextMoves})
			}
		}

		sumOfMoves += minMoves
	}

	slog.Debug("subrpoblems are", "problems", panels, "moves", sumOfMoves)
	return int(sumOfMoves), nil
}

func (p *Problem10) Part2(input []string) (int, error) {

	type panel struct {
		lights   uint32
		buttons  [][]int
		joltages []int
	}

	// get the input
	var panels []panel = make([]panel, len(input))
	for i, line := range input {
		pieces := strings.Split(line, " ")

		lights := strings.Trim(pieces[0], "[]")
		buttons := pieces[1 : len(pieces)-1]
		joltages := strings.Split(strings.Trim(pieces[len(pieces)-1], "{}"), ",")

		var row panel = panel{
			lights:   0,
			buttons:  make([][]int, len(buttons)),
			joltages: make([]int, len(joltages)),
		}

		for i, char := range lights {
			if char == '#' {
				row.lights |= uint32(1 << i)
			}
		}

		for i, button := range buttons {
			button = strings.Trim(button, "()")
			idsStr := strings.Split(button, ",")
			ids := make([]int, len(idsStr))
			for i, id := range idsStr {
				ids[i] = util.Must(strconv.Atoi(id))
			}
			row.buttons[i] = ids
		}

		for i, joltage := range joltages {
			row.joltages[i] = util.Must(strconv.Atoi(joltage))
		}

		panels[i] = row
	}

	sum := 0
	// order by widest buttons first
	for i, problem := range panels {
		slog.Debug("processing new problem", "index", i, "problem", problem)
		// sort buttons according to how many lights they affect, descending
		buttons := problem.buttons
		slices.SortFunc(buttons, flnt.SortBy(func(e []int) int {
			return len(e)
		}))
		slices.Reverse(buttons)

		minSolution := math.MaxInt
		var checkAfter func(remainder []int, cost int, idxInclusive int)
		checkAfter = func(remainder []int, cost int, idxInclusive int) {

			// okay given the buttons that are remaining, which remaining light is affected by the least # of buttons?

			// if remainder is zero we have nothing to do
			isAllZero := true
			totalRemainder := 0
			maxRemainder := 0
			for _, r := range remainder {
				isAllZero = isAllZero && r == 0
				maxRemainder = max(maxRemainder, r)
				totalRemainder += r
			}
			if isAllZero {
				slog.Info("found a new solution!", "cost", cost)
				// we found a solution
				minSolution = min(minSolution, cost)
				return
			}
			if idxInclusive >= len(buttons) {
				// we didn't find a valid solution and are out of coins to check
				return
			}
			// if we're adding 5 at a time and we have 501, then the cost is at least 101
			minRemainder := totalRemainder / len(buttons[idxInclusive])
			if totalRemainder%len(buttons[idxInclusive]) > 0 {
				minRemainder += 1
			}
			if minRemainder+cost >= minSolution {
				return
			}

			buttonSet := buttons[idxInclusive]
			maxICanAdd := math.MaxInt
			for _, button := range buttonSet {
				maxICanAdd = min(maxICanAdd, remainder[button])
			}

			for i := maxICanAdd; i >= 0; i-- {
				newRemainder := append([]int{}, remainder...)
				for _, button := range buttonSet {
					newRemainder[button] = newRemainder[button] - i
				}
				newCost := cost + i
				checkAfter(newRemainder, newCost, idxInclusive+1)

				if idxInclusive <= 5 {
					slog.Debug("finished checking one of the subproblems", "idx", idxInclusive, "i contributed", i)
				}
			}
		}

		checkAfter(problem.joltages, 0, 0)

		sum += minSolution
	}

	return sum, nil
}

func (p *Problem10) Part2V2(input []string) (int, error) {

	type panel struct {
		lights        uint32
		buttons       [][]int
		buttonsBitMap []uint

		joltages []int
	}

	// get the input
	var panels []panel = make([]panel, len(input))
	for i, line := range input {
		pieces := strings.Split(line, " ")

		lights := strings.Trim(pieces[0], "[]")
		buttons := pieces[1 : len(pieces)-1]
		joltages := strings.Split(strings.Trim(pieces[len(pieces)-1], "{}"), ",")

		var row panel = panel{
			lights:        0,
			buttons:       make([][]int, len(buttons)),
			buttonsBitMap: make([]uint, len(buttons)),
			joltages:      make([]int, len(joltages)),
		}

		for i, char := range lights {
			if char == '#' {
				row.lights |= uint32(1 << i)
			}
		}

		for i, button := range buttons {
			button = strings.Trim(button, "()")
			idsStr := strings.Split(button, ",")
			ids := make([]int, len(idsStr))
			for i, id := range idsStr {
				ids[i] = util.Must(strconv.Atoi(id))
			}
			row.buttons[i] = ids
			var bitMap uint = 0
			for _, led := range ids {
				bitMap |= 1 << led
			}
			row.buttonsBitMap[i] = bitMap
		}

		for i, joltage := range joltages {
			row.joltages[i] = util.Must(strconv.Atoi(joltage))
		}

		panels[i] = row
	}

	sum := 0
	var mux sync.Mutex
	var wg sync.WaitGroup
	// order by widest buttons first
	for i, problem := range panels {
		slog.Debug("processing new problem", "index", i, "problem", problem)
		// sort buttons according to how many lights they affect, descending
		buttons := problem.buttons
		//slices.SortFunc(buttons, flnt.SortBy(func(e []int) int {
		//	return len(e)
		//}))
		//slices.Reverse(buttons)

		doProblem := func(problem panel) {

			minSolution := math.MaxInt
			var checkAfter func(remainder []int, cost int, buttonsUsed uint32, recursion int)
			checkAfter = func(remainder []int, cost int, buttonsUsed uint32, recursion int) {
				// Short circuit checks
				// if remainder is zero we have nothing to do

				slog.Debug("starting recursion", "level", recursion, "remainder", remainder, "cost", cost, "buttonsUsed", fmt.Sprintf("%0b", buttonsUsed), "problem", problem)
				isAllZero := true
				totalRemainder := 0
				maxRemainder := 0
				for _, r := range remainder {
					isAllZero = isAllZero && r == 0
					maxRemainder = max(maxRemainder, r)
					totalRemainder += r
				}
				if isAllZero && cost < minSolution {
					slog.Info("found a new solution!", "cost", cost)
					// we found a solution
					minSolution = min(minSolution, cost)
					return
				}

				// okay given the buttons that are remaining, which remaining light is affected by the least # of buttons?
				ledsByNumButtons := make(map[int]int)
				numButtonsRemaining := 0
				for btnIdx, leds := range problem.buttons {
					if buttonsUsed&(1<<btnIdx) > 0 {
						continue
					}
					numButtonsRemaining += 1
					for _, led := range leds {
						ledsByNumButtons[led] = ledsByNumButtons[led] + 1
					}
				}

				for led, r := range remainder {
					if r > 0 && ledsByNumButtons[led] == 0 {
						// led has a remainder but no buttons
						slog.Debug("led has a remainder but no buttons left to vary", "led", led, "remainder", remainder, "used", buttonsUsed)
						return
					}
				}

				ledToTest, minCount := math.MaxInt, math.MaxInt
				for led, count := range ledsByNumButtons {
					if count < minCount && remainder[led] > 0 {
						ledToTest = led
						minCount = count
					}
				}

				if ledToTest >= len(remainder) {
					// we didn't find a valid solution and are out of coins to check
					return
				}

				// select the button we'll vary now
				// the button should affect the led we want and ideally has the most leds affected

				// if I have 2 buttons left, the max number of numbers I can fix is...

				// 2025/12/16 00:39:51 DEBUG starting recursion level=7
				//remainder="[91 0 60 0 74 37 11 23 0 43]" buttonsUsed=1100111101 problem="{lights:398 buttons:[[] [] [0 1 2 5 6 8 9] <- nothing to use with this [0 2 3 4 6 8 9]  [0 1 9]] joltages:[286 248 300 62 259 273 77 256 239 116]}"
				//            0     2    4  5  6  7    9
				// 3 buttons left to change all of these numbers, can I even produce 7 different numbers

				// 2025/12/16 00:51:13 DEBUG starting recursion level=7
				//remainder="[80 0 53 1 60 31 3 20 0 42]" buttonsUsed=100111111 problem="{lights:398 buttons:[[0 2 3 5 6 7 9] [0 1 2 5 6 8 9]] joltages:[286 248 300 62 259 273 77 256 239 116]}"
				//             0    2 3  4  5 6  7    9
				// [[0 2 3 5 6 7 9] [0 1 2 5 6 8 9]]
				varyingBtn := -1
				numLeds := math.MinInt
				// find our next button to test
				for id, leds := range problem.buttons {
					if buttonsUsed&(1<<id) > 0 {
						continue
					}
					if !slices.Contains(leds, ledToTest) {
						continue
					}

					wouldHitAZero := false
					for _, led := range leds {
						wouldHitAZero = wouldHitAZero || remainder[led] == 0
					}
					if wouldHitAZero {
						continue
					}

					if len(leds) > numLeds {
						varyingBtn = id
						numLeds = len(leds)
					}
				}

				if varyingBtn < 0 {
					//slog.Debug("short circuiting because no remaining buttons are valid")
					return
				}

				if recursion < 6 {
					slog.Debug("I chose to vary the following", "led", ledToTest, "btnIndex", varyingBtn)
				}

				leds := buttons[varyingBtn]
				numButtonsRemaining -= 1
				maxICanAdd := math.MaxInt
				for _, led := range leds {
					maxICanAdd = min(maxICanAdd, remainder[led])
					ledsByNumButtons[led] = ledsByNumButtons[led] - 1
				}

				maxICanAdd = min(maxICanAdd, minSolution-cost)

				buttonsUsed |= 1 << varyingBtn
				// I should be able to

				// okay I selected this button, are any other states made invalid?

				var numIterations = 0
				defer func() {
					slog.Debug("spent iteratioins at ", "iteration", numIterations, "recursion", recursion)
				}()
				for i := maxICanAdd; i >= 0; i-- {
					numIterations++
					newCost := cost + i
					newRemainder := append([]int{}, remainder...)

					for _, led := range leds {
						newRemainder[led] = newRemainder[led] - i
					}

					allLeds := uint(0)
					zeroedLeds := uint(0)
					remainingLedsMap := uint(0)
					for led, r := range newRemainder {
						allLeds |= 1 << led
						if r == 0 {
							zeroedLeds |= 1 << led
						} else {
							remainingLedsMap |= 1 << led
						}
					}

					if allLeds == zeroedLeds {
						// new solution found!
						slog.Info("found a new solution!", "cost", newCost)
						// we found a solution
						minSolution = min(minSolution, newCost)
						return
					}
					// if

					ledsWeCanChangeMap := uint(0)

					for id, ledMap := range problem.buttonsBitMap {
						if buttonsUsed&(1<<id) > 0 {
							continue
						}

						if ledMap&zeroedLeds == 0 {
							// does not touch a zero'd led, it's ok to touch
							ledsWeCanChangeMap |= ledMap
						}
					}
					if ledsWeCanChangeMap&remainingLedsMap != remainingLedsMap {
						slog.Debug("remaining combination of valid lights does not let us come to zero")
						return
					}

					// i've chosen a button, the remaining buttons will poke numbers others
					// e.g. if i have 2 buttons left, one affects index 0 and one affects index 1
					// and both 0 and 1 are zero'd

					//    0001
					//    0010
					//    0011 (OR'd together)
					//

					// buttons 1110  0
					//         1101  1

					// valid problems for led 1:
					// 0001
					// valid problems for led 2:
					// 0010
					// is 1 valid? no overlap with 0001

					// if remainder is zero, then there must be leds that do not affect led
					// if remainder is > 0, then there must be leds that do affect the led
					// there must be some buttons that fit both conditions

					//for led, r := range newRemainder {
					//	if r > 0 {
					//		// if there's a remainder then there'd better be more buttons that can affect this
					//		if ledsByNumButtons[led] == 0 {
					//			// led has a remainder but no buttons
					//			slog.Debug("led has a remainder but no buttons left to reduce remainder", "led", led, "remainder", remainder, "used", buttonsUsed)
					//			return
					//		}
					//	} else if r == 0 {
					//		validProblemsForLed := 0
					//		for id, leds := range problem.buttons {
					//			if buttonsUsed&(1<<id) > 0 {
					//				continue
					//			}
					//
					//			if slices.Contains(leds, led) {
					//				continue
					//			}
					//
					//			validProblemsForLed |= 1 << id
					//		}
					//
					//		validProblems &= validProblemsForLed
					//		// I could do an or check here too
					//		// if there isn't a remainder then there'd better be buttons that don't hit it, unless we hit zero
					//		if ledsByNumButtons[led] == numButtonsRemaining {
					//			slog.Debug("led has a remainder but no buttons left to vary will avoid remainder", "led", led, "remainder", remainder, "used", buttonsUsed)
					//		}
					//	}
					//}

					checkAfter(newRemainder, newCost, buttonsUsed|(1<<varyingBtn), recursion+1)
				}
			}

			checkAfter(problem.joltages, 0, 0, 0)

			mux.Lock()
			sum += minSolution

			slog.Info("finished problem", "solution", minSolution, "index", i)
			mux.Unlock()
			wg.Done()
		}

		wg.Add(1)
		go doProblem(problem)
	}

	wg.Wait()

	return sum, nil
}

func (p *Problem10) InvestigateData(input []string) (int, error) {

	type panel struct {
		lights   uint32
		buttons  [][]int
		joltages []int
	}

	// get the input
	var panels []panel = make([]panel, len(input))
	for i, line := range input {
		pieces := strings.Split(line, " ")

		lights := strings.Trim(pieces[0], "[]")
		buttons := pieces[1 : len(pieces)-1]
		joltages := strings.Split(strings.Trim(pieces[len(pieces)-1], "{}"), ",")

		var row panel = panel{
			lights:   0,
			buttons:  make([][]int, len(buttons)),
			joltages: make([]int, len(joltages)),
		}

		for i, char := range lights {
			if char == '#' {
				row.lights |= uint32(1 << i)
			}
		}

		for i, button := range buttons {
			button = strings.Trim(button, "()")
			idsStr := strings.Split(button, ",")
			ids := make([]int, len(idsStr))
			for i, id := range idsStr {
				ids[i] = util.Must(strconv.Atoi(id))
			}
			row.buttons[i] = ids
		}

		for i, joltage := range joltages {
			row.joltages[i] = util.Must(strconv.Atoi(joltage))
		}

		panels[i] = row
	}

	sum := 0
	// order by widest buttons first
	for _, problem := range panels {
		foo := append([]int{}, problem.joltages...)
		slices.Sort(foo)
		length := len(foo)
		// 0  1  2  3
		//      ^     (4 / 2) (3 / 2=> 1)
		// 0  1  2  3  4  (4 / 2) (5 / 2)
		median := (foo[(length-1)/2] + foo[length/2]) / 2
		maxJ := foo[length-1]
		minJ := foo[0]
		avg := float64(flnt.Sum(foo)) / float64(length)

		buttonByChangers := make(map[int]int)
		for _, buttons := range problem.buttons {
			for _, btn := range buttons {
				buttonByChangers[btn] = buttonByChangers[btn] + 1
			}
		}

		slog.Info("joltages distribution is", "median", median, "max", maxJ, "min", minJ, "avg", avg, "problem", problem, "buttonByChangers", buttonByChangers)
	}

	return sum, nil
}
