package twentytwenty

object Day9 {
    fun part1(input: List<String>): Long {
        return findFirstInvalid(input.map { it.toLong() }, preamble = 25)
    }

    fun part2(input: List<String>): Long {
        val correctInput = input.map { it.toLong() }
        val firstInvalid = findFirstInvalid(correctInput, preamble = 25)

        val (startIndex, endIndex) = findContiguousSummingIndicesForTarget(correctInput, firstInvalid)

        return sumMinAndMaxFor(correctInput, startIndex, endIndex)
    }

    fun findFirstInvalid(input: List<Long>, preamble: Int = 25): Long {
        return input.windowed(preamble + 1, step = 1).asSequence()
            .filter {
                val target = it.last()
                val options = it.subList(0, it.size - 1)

                !canSumToTarget(options, target)
            }
            .first()
            .last()
    }

    fun canSumToTarget(options: List<Long>, target: Long): Boolean {
        for (i in options.indices) {
            for (j in (i+1) until options.size) {
                if (options[i] + options[j] == target) {
                    return true
                }
            }
        }

        return false
    }

    class SlidingWindow(val input: List<Long>, val target: Long, var start: Int = 0, var end: Int = 0) {
        fun doesWindowSumEqualTarget() = getCurrentWindowSum() == target

        fun getCurrentWindowSum() = input.subList(start, end + 1) // plus one bc exclusive
            .sum()

        fun step() {
            val current = getCurrentWindowSum()

            if (current > target) {
                start++
            } else {
                end++
            }
        }
    }

    fun findContiguousSummingIndicesForTarget(input: List<Long>, target: Long): Pair<Int, Int> {
        val slidingWindow = SlidingWindow(input = input, target = target)

        while (!slidingWindow.doesWindowSumEqualTarget()) {
            slidingWindow.step()
        }

        return slidingWindow.start to slidingWindow.end
    }

    fun sumMinAndMaxFor(input: List<Long>, startIndex: Int, endIndex: Int): Long {
        return input.subList(startIndex, endIndex + 1).let {
            val max = it.max() ?: error("no elements in list!")
            val min = it.min() ?: error("no elements in list!")

            max + min
        }
    }
}

fun main() {
    val input = readFile(day = 9, part = 1)

    val part1Result = Day9.part1(input) // should be 731031916
    val part2Result = Day9.part2(input) // should be 93396727

    println("""
        part 1: $part1Result
        part 2: $part2Result
    """.trimIndent())
}
