package flnt

import "cmp"

func SortBy[E any, J cmp.Ordered](extractor func(e E) J) func(e1, e2 E) int {
	return func(e1, e2 E) int {
		j1 := extractor(e1)
		j2 := extractor(e2)

		return cmp.Compare(j1, j2)
	}

}
