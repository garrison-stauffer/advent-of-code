package twentytwenty

fun readFile(day: Int, part: Int = 1): List<String> {
    val expectedFileName = "/twentytwenty/day${day}pt${part}.txt"
    return readFile(expectedFileName)
}

fun readFile(qualifiedName: String, resourceLoader: Class<*> = object { }.javaClass): List<String> {

    return resourceLoader.getResource(qualifiedName).readText().lines().filter { it.isNotBlank() }
}
