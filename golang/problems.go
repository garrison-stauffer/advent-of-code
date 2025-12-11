package golang

import (
	"bufio"
	"fmt"
	"garrison-stauffer.com/advent-of-code/util"
	"log/slog"
	"os"
)

var debugger bool

func setDebugger(val bool) bool {
	old := debugger
	if val != debugger {
		debugger = val
		level := slog.LevelInfo
		if debugger {
			level = slog.LevelDebug
		}
		slog.SetLogLoggerLevel(level)
	}
	return old
}

type Problem[Input any, Result any] func(input Input) (Result, error)

func ReadProblemInput(day int, part int) []string {
	file := util.Must(os.Open(fmt.Sprintf("./inputs/problem%02d_%d.txt", day, part)))
	defer file.Close()

	scanner := bufio.NewScanner(file)

	output := make([]string, 0, 150)
	for scanner.Scan() {
		line := scanner.Text()
		output = append(output, line)
	}

	return output
}
