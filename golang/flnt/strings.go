package flnt

import "strings"

func S(vals ...string) []string {
	return vals
}

func Spl(val string) []string {
	lines := strings.Split(val, "\n")
	for i, line := range lines {
		lines[i] = strings.TrimSpace(line)
	}
	return lines
}
