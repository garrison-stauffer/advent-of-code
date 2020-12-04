package twentytwenty

enum class Attributes(
    val code: String,
    val isValid: (String) -> Boolean = { true }
) {
    BIRTH_YEAR("byr", { input ->
        input.toInt() in 1920..2002
    }),
    ISSUE_YEAR("iyr", { input ->
        input.toInt() in 2010..2020
    }),
    EXPIRATION_YEAR("eyr", { input ->
        input.toInt() in 2020..2030
    }),
    HEIGHT("hgt", { input ->
        val values = """(\d+)(in|cm)""".toRegex().matchEntire(input)?.groupValues

        when (values) {
            null -> false
            else -> {
                val size = values[1].toInt()
                val measurement = values[2]

                when (measurement) {
                    "cm" -> size in 150..193
                    "in" -> size in 59..76
                    else -> error("Unknown type of measurement: $measurement")
                }
            }
        }
    }),
    HAIR_COLOR("hcl", { input ->
        """#[a-f0-9]{6}""".toRegex().matches(input)
    }),
    EYE_COLOR("ecl", setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")::contains),
    PASSPORT_ID("pid", { input ->
        """[0-9]{9}""".toRegex().matches(input)
    }),
    COUNTRY_ID("cid")
}

val requiredAttributes = listOf(
    Attributes.BIRTH_YEAR,
    Attributes.ISSUE_YEAR,
    Attributes.EXPIRATION_YEAR,
    Attributes.HEIGHT,
    Attributes.HAIR_COLOR,
    Attributes.EYE_COLOR,
    Attributes.PASSPORT_ID
)

object Day4 {
    fun part1(inputList: List<String>): Int {
        return groupPassportRowsTogether(inputList)
            .count { row ->
                val tags = row.split(" ")

                requiredAttributes.all { attributeToHave ->
                    tags.any { it.startsWith(attributeToHave.code) }
                }
            }
    }


    fun part2(inputList: List<String>): Int {
        return groupPassportRowsTogether(inputList)
            .count { row ->
                val tags = row.split(" ")
                requiredAttributes.all { requiredAttribute ->
                    tags.any { it.startsWith(requiredAttribute.code) && requiredAttribute.isValid(it.split(":")[1]) }
                }
            }
    }

    fun groupPassportRowsTogether(inputList: List<String>) = inputList
        .fold(listOf<String>()) { accumulator, inputString ->
            when {
                inputString.isBlank() -> {
                    accumulator + inputString
                }
                accumulator.isEmpty() -> {
                    accumulator + inputString
                }
                else -> {
                    accumulator.lastIndex.let {
                        accumulator.subList(0, it) + (accumulator[it] + " " + inputString)
                    }
                }
            }
        }
        .map { it.replace("\\s+".toRegex(), " ").trim() }
        .filter { it.isNotBlank() }
}

fun main() {
    val inputFile = readFile(day = 4, part = 1, removeBlanks = false)

    val part1Result = Day4.part1(inputFile)
    val part2Result = Day4.part2(inputFile)
    println(
        """
            pt1: $part1Result
            pt2: $part2Result
        """.trimIndent()
    )
}
