object DayFour {

    fun partOne(start: Int, end: Int): Int {
        var numberOfValidPasscodes = 0
        for (passcodeToTry in start..end) {
            if (isAlwaysIncreasingNumber(passcodeToTry) && containsDuplicateNumber(passcodeToTry)) {
                numberOfValidPasscodes++
            }
        }

        return numberOfValidPasscodes
    }

    fun partTwo(start: Int, end: Int): Int {
        var numberOfValidPasscodes = 0
        for (passcodeToTry in start..end) {
            if (isAlwaysIncreasingNumber(passcodeToTry) && hasTwoOfSameNumber(passcodeToTry)) {
                numberOfValidPasscodes++
            }
        }

        return numberOfValidPasscodes
    }

    fun isAlwaysIncreasingNumber(passcode: Int): Boolean {
        // compare the last digit to second to last digit, then use integer division to move further towards the front
        if (passcode< 10) return true
        return (passcode % 10 >= passcode / 10 % 10) && isAlwaysIncreasingNumber(passcode / 10)
    }

    val validDuplicates = listOf("11", "22", "33", "44", "55", "66", "77", "88", "99")
    fun containsDuplicateNumber(passcode: Int): Boolean {
        val passcodeString = passcode.toString()
        return validDuplicates.map {
            passcodeString.contains(it)
        }.reduce(Boolean::or)
    }

    fun hasTwoOfSameNumber(passcode: Int): Boolean {
        fun recursiveMatcher(passcode: Int, lastNumber: Int, numberOfTimesInARow: Int): Boolean {
            if (passcode == 0) {
                // base case, if we made it this far, there needs to be a 2 in numberOfTimesInARow or it fails
                return numberOfTimesInARow == 2
            }

            val lastDigit = passcode % 10
            if (lastDigit == lastNumber) {
                return recursiveMatcher(passcode / 10, lastDigit, numberOfTimesInARow + 1)
            } else {
                return (numberOfTimesInARow == 2)
                    || recursiveMatcher(passcode / 10, lastDigit, 1)
            }
        }

        return recursiveMatcher(passcode, 0, 0)
    }
}

fun main() {
    var numValidPasscodes = DayFour.partOne(183564, 657474)
    println("Part 1: $numValidPasscodes")
    numValidPasscodes = DayFour.partTwo(183564, 657474)
    println("Part 2: $numValidPasscodes")
}
