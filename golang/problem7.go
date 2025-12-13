package golang

import (
	"log/slog"
	"slices"
)

type Problem7 struct {
}

func (p *Problem7) Part1(input []string) (int, error) {
	var lightBeams []int

	for i, char := range input[0] {
		if char == 'S' {
			lightBeams = append(lightBeams, i)
		}
	}

	numSplits := 0
	for _, line := range input[1:] {
		var nextBeams []int

		for _, beam := range lightBeams {
			char := line[beam]
			if char == '^' {
				numSplits++
				if !slices.Contains(nextBeams, beam-1) {
					nextBeams = append(nextBeams, beam-1)
				}
				if !slices.Contains(nextBeams, beam+1) {
					nextBeams = append(nextBeams, beam+1)
				}
			} else if !slices.Contains(nextBeams, beam) {
				nextBeams = append(nextBeams, beam)
			}
		}

		lightBeams = nextBeams
	}

	return numSplits, nil
}

func (p *Problem7) Part2(input []string) (int64, error) {
	type ray struct {
		idx      int
		numPaths int64
	}
	var rays []ray

	for i, char := range input[0] {
		if char == 'S' {
			rays = append(rays, ray{i, 1})
		}
	}

	for _, line := range input[1:] {
		var nextRays []ray

		for _, beam := range rays {
			char := line[beam.idx]
			if char == '^' {
				left := slices.IndexFunc(nextRays, func(ray ray) bool {
					return ray.idx == beam.idx-1
				})
				right := slices.IndexFunc(nextRays, func(ray ray) bool {
					return ray.idx == beam.idx+1
				})

				if left < 0 {
					nextRays = append(nextRays, ray{beam.idx - 1, beam.numPaths})
				} else {
					nextRays[left].numPaths += beam.numPaths
				}

				if right < 0 {
					nextRays = append(nextRays, ray{beam.idx + 1, beam.numPaths})
				} else {
					nextRays[right].numPaths += beam.numPaths
				}
			} else {
				idx := slices.IndexFunc(nextRays, func(ray ray) bool {
					return ray.idx == beam.idx
				})

				if idx < 0 {
					nextRays = append(nextRays, ray{beam.idx, beam.numPaths})
				} else {
					nextRays[idx].numPaths += beam.numPaths
				}
			}
		}

		rays = nextRays

		var paths int64
		for _, ray := range rays {
			paths += ray.numPaths
		}
		slog.Debug("number of paths rn is ", "paths", paths)
	}

	var paths int64
	for _, ray := range rays {
		paths += ray.numPaths
	}

	return paths, nil
}
