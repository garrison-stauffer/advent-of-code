package golang

import (
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestReadingFiles(t *testing.T) {
	t.Run("it should panic if the problem input isn't there", func(t *testing.T) {
		assert.Panics(t, func() { _ = ReadProblemInput(-1, -1) })
	})

	t.Run("it can read problem 1", func(t *testing.T) {
		text := ReadProblemInput(7357, 1)

		assert.Equal(t, "its problem 1", text[0])
	})

	t.Run("it returns blank spaces in the middle", func(t *testing.T) {
		input := ReadProblemInput(7357, 1)

		assert.Equal(t, []string{"test", "", "and"}, input[4:7])
	})

	t.Run("it keeps trailing new lines except for the final", func(t *testing.T) {
		input := ReadProblemInput(7357, 1)

		assert.Equal(t, []string{"and trailing spaces except the last", "", ""}, input[15:])
	})
}

type testCase[Input any, Result any] struct {
	t      *testing.T
	input  Input
	result Result

	debugger bool
	pauser   bool

	fn Problem[Input, Result]
}

func (t testCase[I, R]) parsing(input I) testCase[I, R] {
	t.input = input
	return t
}

func (t testCase[I, R]) hasToBe(expected R) testCase[I, R] {
	if t.debugger {
		old := setDebugger(t.debugger)
		defer func() {
			setDebugger(old)
		}()
	}

	if t.pauser {
		return t
	}

	actual, err := t.fn(t.input)

	require.NoError(t.t, err)
	assert.Equal(t.t, expected, actual)

	return t
}

func (t testCase[I, R]) debug() testCase[I, R] {
	t.debugger = true
	return t
}

func (t testCase[I, R]) pause() testCase[I, R] {
	t.pauser = true
	return t
}

func (t testCase[I, R]) resume() testCase[I, R] {
	t.pauser = false
	return t
}
