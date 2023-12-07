package twentytwentythree


object Day07 {
    val input: List<String> = Day07::class.java.getResource("/twentytwentythree/day07pt1.txt")!!
        .readText()
        .lines()
        .filter(String::isNotBlank)

    fun part1(input: List<String>): Long {
        return input.map { Hand.parse(it) }
            .sorted()
            .mapIndexed {i, hand ->
                (i + 1) * hand.bet
            }
            .sum()
    }

    fun part2(input: List<String>): Long {
        return input.map { Hand.parseV2(it) }
            .sorted()
            .mapIndexed {i, hand ->
                (i + 1) * hand.bet
            }
            .sum()
    }

    data class Hand(val cards: List<Int>, val classification: Classification, val bet: Long): Comparable<Hand> {
        override fun compareTo(other: Hand): Int {
            if (other.classification > this.classification) {
                return 1
            } else if (this.classification > other.classification) {
                return -1
            } else {
                // this is the problem
                for (i in 0 until 5) {
                    if (cards[i] - other.cards[i] != 0) return cards[i] - other.cards[i]
                }
                return 0
            }
        }

        companion object {
            fun parse(input: String): Hand {
                val split = input.split(" ")
                val hand = split[0]
                val bet = split[1].toLong()
                val parsed = hand.map {
                    when {
                        it.isDigit() -> it.digitToInt()
                        it == 'T' -> 10
                        it == 'J' -> 11
                        it == 'Q' -> 12
                        it == 'K' -> 13
                        it == 'A' -> 14 // TODO: Confirm ace is higher than king
                        else -> throw IllegalStateException("unexpected value $it")
                    }
                }
                val classification = parseHandSansJokers(parsed)

                return Hand(parsed, classification, bet)
            }

            fun parseHandSansJokers(input: List<Int>): Classification {
                return when (input.distinct().size) {
                    5 -> Classification.HighKard
                    4 -> Classification.OnePair
                    3  -> {
                        val grouped = input.groupBy { it }
                        when (grouped.values.filter { it.size == 3 }.size) {
                            1 -> Classification.ThreeOfAKind
                            else -> Classification.TwoPair
                        }
                    } // or the of a kind
                    2 -> {
                        val grouped = input.groupBy { it }
                        when (grouped.values.filter { it.size == 4 }.size) {
                            1 -> Classification.FourOfAKind
                            else -> Classification.FullHouse
                        }
                    }
                    1 -> Classification.FiveOfAKind
                    else -> throw IllegalStateException("unexpected hand $input")
                }
            }

            fun parseV2(input: String): Hand {
                val split = input.split(" ")
                val hand = split[0]
                val bet = split[1].toLong()
                val jokers = hand.count { it == 'J' }
                val parsed = hand.map {
                    when {
                        it.isDigit() -> it.digitToInt()
                        it == 'T' -> 10
                        it == 'Q' -> 12
                        it == 'K' -> 13
                        it == 'A' -> 14
                        it == 'J' -> 0
                        else -> throw IllegalStateException("unexpected value $it")
                    }
                }

                val classification = when (parsed.distinct().size) {
                    5 -> {
                        // 12345 vs J2345
                        if (jokers == 1) {
                            Classification.OnePair
                        } else {
                            Classification.HighKard
                        }
                    }
                    4 -> {
                        // JJ345 -> best is 3 of a kind
                        // J3345 -> best is pair
                        // 11345
                        if (jokers == 2 || jokers == 1) {
                            Classification.ThreeOfAKind
                        } else {
                            Classification.OnePair
                        }
                    }
                    3  -> {
                        // JJJ45 -> four of a kind
                        // jj335 -> four of a kind
                        // jj335 -> four of a kind
                        // j2233 -> full house
                        // j2223 -> four of a kind
                        val biggestGroupMissingJoker = parsed.filter { it != 0 }.groupBy { it }.map { it.value }
                        if (jokers == 3) {
                            Classification.FourOfAKind
                        } else if (jokers == 2) {
                            Classification.FourOfAKind
                        } else if (jokers == 1) {
                            // full house is 22 33 with a joker, otherwise it has to be four of a kind
                            val hasFullHouse = parsed.filter { it != 0 }.groupBy { it }.filter { it.value.size == 2 }.size == 2
                            if (hasFullHouse) {
                                Classification.FullHouse
                            } else {
                                Classification.FourOfAKind
                            }
                        } else {
                            parseHandSansJokers(parsed)
                        }

                    } // or the of a kind
                    2 -> {
                        if (jokers > 0) {
                            Classification.FiveOfAKind
                        } else {
                            parseHandSansJokers(parsed)
                        }
                    }
                    1 -> Classification.FiveOfAKind
                    else -> parseHandSansJokers(parsed)
                }

                return Hand(parsed, classification, bet)
            }
        }
    }

    enum class Classification {
        FiveOfAKind,
        FourOfAKind,
        FullHouse,
        ThreeOfAKind,
        TwoPair,
        OnePair,
        HighKard,
    }
}

fun main() {
    val part1 = Day07.part1(Day07.input)
    val part2 = Day07.part2(Day07.input)

    println("part1 result: $part1")
    println("part2 result: $part2")
}
