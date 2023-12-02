package twentytwentythree

object Day01 {
    val input: List<String> = Day01::class.java.getResource("/twentytwentythree/day1pt1.txt")!!
            .readText()
            .lines()
            .filter(String::isNotBlank)
            .map { it }

    fun problem1(input: List<String>): Long {
        return input.sumOf { parseValueFromLine(it) }
    }

    fun problem2(input: List<String>): Long {
        return input.sumOf { parseValueFromLine(it, DigitTrie.newInstance()) }
    }

    fun parseValueFromLine(input: String, rootTrie: DigitTrie = DigitTrie(' ')): Long {
        var first: Long = -1
        var last: Long = -1
        val buffer = mutableListOf<DigitTrie>()

        for (char in input) {
            // store a list of items in the buffer that are no longer valid
            val indicesToDrop = mutableListOf<Int>()
            for (i in 0 until buffer.size) {
                val nextNode = buffer[i].nodeForChar(char)
                if (nextNode == null) {
                    indicesToDrop.add(i)
                } else if (nextNode.value() != null) {
                    // we found a match - get the value and potentially
                    // assign it to first/last, then drop it
                    indicesToDrop.add(i)
                    if (first < 0) {
                        first = nextNode.value()!!
                    }
                    last = nextNode.value()!!
                } else {
                    buffer[i] = nextNode
                }
            }

            // go backwards so we don't ruin later indices
            indicesToDrop.reverse()
            for (i in indicesToDrop) {
                buffer.removeAt(i)
            }

            rootTrie.nodeForChar(char)?.let {
                // only store if this is a valid character that should be buffered
                buffer.add(it)
            }

            if (!char.isDigit()) continue
            if (first < 0) {
                first = char.digitToInt().toLong()
            }
            last = char.digitToInt().toLong()
        }
        return (10 * first) + last
    }

    data class DigitTrie(val char: Char, val neighbors: MutableMap<Char, DigitTrie> = mutableMapOf(), var value: Long? = null) {
        fun hasString(input: String): Boolean {
            if (input.isEmpty()) {
                return true
            }
            if (this.neighbors.containsKey(input[0])) {
                return this.neighbors.getValue(input[0]).hasString(input.substring(1))
            }
            return false
        }

        fun nodeForChar(char: Char): DigitTrie? {
            return this.neighbors[char]
        }

        fun value(): Long? {
            return this.value
        }

        companion object {
            fun newInstance(): DigitTrie {
                val digits = mapOf(
                    "one" to 1,
                    "two" to 2,
                    "three" to 3,
                    "four" to 4,
                    "five" to 5,
                    "six" to 6,
                    "seven" to 7,
                    "eight" to 8,
                    "nine" to 9
                )

                val node = DigitTrie(' ')
                for ((stringValue, numValue) in digits) {
                    var cursor = node
                    for (char in stringValue) {
                        if (cursor.neighbors.containsKey(char)) {
                            cursor = cursor.neighbors[char]!!
                        } else {
                            cursor.neighbors[char] = DigitTrie(char)
                            cursor = cursor.neighbors[char]!!
                        }
                    }
                    cursor.value = numValue.toLong()
                }
                return node
            }
        }
    }
}

fun main() {
    val pt1Result = Day01.problem1(Day01.input)
    val pt2Result = Day01.problem2(Day01.input)

    println("Part 1: $pt1Result") // answer was 56465
    println("Part 2: $pt2Result") // answer was 55902
}
