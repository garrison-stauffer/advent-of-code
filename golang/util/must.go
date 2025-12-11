package util

import (
	"fmt"
	"reflect"
)

// Must returns the provided value if err is nil, otherwise it panics
// with an error that includes the value's type for easier debugging.
func Must[T any](v T, err error) T {
	if err != nil {
		typeName := "<unknown>"
		if t := reflect.TypeOf(v); t != nil {
			typeName = t.String()
		}
		panic(fmt.Errorf("must: value type %s: %w", typeName, err))
	}
	return v
}
