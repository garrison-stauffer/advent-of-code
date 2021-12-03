package twentytwentyone

internal fun readFile(day: Int, part: Int = 1, removeBlanks: Boolean = true): List<String> {
    val expectedFileName = "/twentytwentyone/day${day}pt${part}.txt"
    return readFile(expectedFileName, removeBlanks = removeBlanks)
}

internal fun parseTestInput(input: String) = input.split("\n").toList()

internal fun readFile(qualifiedName: String, resourceLoader: Class<*> = object { }.javaClass, removeBlanks: Boolean): List<String> {
    return resourceLoader.getResource(qualifiedName).readText().lines().filter { if (removeBlanks) it.isNotBlank() else true }
}

internal interface Problem {
    fun part1(input: List<String>): Int

    fun part2(input: List<String>): Int
}
