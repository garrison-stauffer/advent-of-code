package twentytwenty

object Day5 {


fun part1(input: List<String>) = input.map(this::convertStringToInt).max()

fun part2(input: List<String>) = input.map(this::convertStringToInt)
    .sorted()
    .windowed(size = 2, step = 1) {
        val a = it[0]
        val b = it[1]

        if (a + 2 == b) {
            println("found gap between $a and $b")
            b - 1
        } else {
            null
        }
    }.filterNotNull().first()

fun convertStringToInt(input: String): Int {
    return input.replace('F', '0')
        .replace('B', '1')
        .replace('L', '0')
        .replace('R', '1')
        .toInt(radix = 2)
}
}

fun main() {
    val fileInput = readFile(day = 5, part = 1)
    val part1Answer = Day5.part1(fileInput)
    val part2Answer = Day5.part2(fileInput)


    println(
        """
            Part 1: $part1Answer
            Part 2: $part2Answer
        """.trimIndent()
    )
}
