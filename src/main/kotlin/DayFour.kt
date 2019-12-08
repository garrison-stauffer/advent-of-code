object DayFour {

    fun partOne(start: Int, end: Int): Int {
        var numberOfValidPasscodes = 0
        for (passcodeToTry in start..end) {
            if (testIncreasingSequence(passcodeToTry) && testForTwoConsecutiveDigits(passcodeToTry)) {
                numberOfValidPasscodes++
            }
        }

        return numberOfValidPasscodes
    }

    fun testIncreasingSequence(passcode: Int): Boolean {
        // compare the last digit to second to last digit, then use integer division to move further towards the front
        fun validateLastTwoDigits(number: Int): Boolean {
            if (number < 10) return true
            return (number % 10 >= number / 10 % 10) && validateLastTwoDigits(number / 10)
        }

        return validateLastTwoDigits(passcode)
    }

    val validDuplicates = listOf("11", "22", "33", "44", "55", "66", "77", "88", "99")
    fun testForTwoConsecutiveDigits(passcode: Int): Boolean {
        val passcodeString = passcode.toString()
        return validDuplicates.map {
            passcodeString.contains(it)
        }.reduce(Boolean::or)
    }
}

fun main() {
    val numValidPasscodes = DayFour.partOne(183564, 657474)
    println("Number of valid passcodes: $numValidPasscodes")
}
