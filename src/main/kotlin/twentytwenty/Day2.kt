package twentytwenty


data class PasswordRecord(val policy: Policy, val password: String) {
    fun isValidPasswordForPart1(): Boolean {
        return password.count(this.policy.mustInclude::equals) in policy.first..policy.second
    }

    fun isValidPasswordForPart2(): Boolean {
        val hasCharAtFirstPosition = password[policy.first - 1] == policy.mustInclude
        val hasCharAtSecondPosition = password[policy.second - 1] == policy.mustInclude

        return hasCharAtFirstPosition xor hasCharAtSecondPosition
    }
}

/**
 * For part 1, first and second were a range
 */
data class Policy(val first: Int, val second: Int, val mustInclude: Char)

object Day2 {
    private val part1Input: List<String> = Day2::class.java.getResource("/twentytwenty/day2pt1.txt").readText()
        .split("\n")
        .filter(String::isNotBlank)

    /**
     * Sample input will be like "1-3 a: abcde"
     */
    fun parsePasswordInput(input: String): PasswordRecord {
val min = input.split(" ")[0].split("-")[0].toInt()
val most = input.split(" ")[0].split("-")[1].toInt()
val target = input.split(" ")[1][0]
val password = input.split(" ")[2]

        val policy = Policy(first = min, second = most, mustInclude = target)
        return PasswordRecord(policy, password)
    }

    fun problem1() = part1Input.map(this::parsePasswordInput).count(PasswordRecord::isValidPasswordForPart1)
    fun problem2() = part1Input.map(this::parsePasswordInput).count(PasswordRecord::isValidPasswordForPart2)

}

fun main() {
    val numValidPasswords = Day2.problem1()

    println("""
        Part 1 solution: $numValidPasswords
        
        Part 2 solution: ${Day2.problem2()}
    """.trimIndent())
}
