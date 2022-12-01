package twentytwenty

object Day6 {
    fun part1(input: List<String>): Int {
        return Day4.groupPassportRowsTogether(input)
            .map { it.replace("\\s+".toRegex(), "").trim() }
            .map { it.toCharArray().toSet() }
            .map { it.size }
            .sum()
    }

    private val charPositionLookup = ('a'..'z').foldIndexed(mutableMapOf<Char, Int>()) { index: Int, acc: MutableMap<Char, Int>, c: Char ->
        acc[c] = index
        acc
    }.toMap()

    fun getAlphabeticalPosition(letter: Char): Int {
        return charPositionLookup[letter] ?: error("Unknown char $letter")
    }

    fun mapAnswerStringToNumber(input: String): Int {
        return input.toCharArray()
            .map { 1 shl getAlphabeticalPosition(it) }
            .reduce(Int::or)
    }

    fun countSharedAnswersInFamily(familyAnswers: String): Int {
        return familyAnswers.split(" ")
            .map(this::mapAnswerStringToNumber)
            .reduce(Int::and)
            .toString(radix = 2).count { it == '1' }
    }

    fun part2(input: List<String>): Int {
        return Day4.groupPassportRowsTogether(input)
            .map { it.replace("\\s+".toRegex(), " ").trim() }
            .map { countSharedAnswersInFamily(it) }
            .reduce(Int::plus)
    }
}

fun main() {
    val fileInput = readFile(day = 6, part = 1, removeBlanks = false)
    val part1Answer = Day6.part1(fileInput)
    val part2Answer = Day6.part2(fileInput)


    // should be 6161 and 2971
    println(
        """
            Part 1: $part1Answer
            Part 2: $part2Answer
        """.trimIndent()
    )
}
