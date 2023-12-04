package twentytwentythree

object Day04 {
    val input: List<String> = Day04::class.java.getResource("/twentytwentythree/day04pt1.txt")!!
        .readText()
        .lines()
        .filter(String::isNotBlank)

    fun part1(input: List<String>): Long {
        return input.map { parseLottoCard(it) }
            .sumOf {
                // 5 - num matched (by set difference)
                val numMatched = it.winners.size - (it.winners - it.revealed).size
                if (numMatched > 0) {
                    1 shl (numMatched - 1)
                } else {
                    0
                }
            }.toLong()
    }

    fun part2(input: List<String>): Long {
        val lottoCards = input.map { parseLottoCard(it) }
        val lottoCardToWins = mutableMapOf<Int, Int>()

        fun evaluateTotalCards(index: Int): Int {
            if (lottoCardToWins.containsKey(index)) {
                return lottoCardToWins.getValue(index)
            }

            val card = lottoCards[index]
            val numWins = card.winners.size - (card.winners - card.revealed).size

            val totalBonusCards = (0 until numWins).sumOf {
                evaluateTotalCards(index + it + 1)
            }

            // add this card as one too
            lottoCardToWins[index] = 1 + totalBonusCards

            return 1 + totalBonusCards
        }

        var sum = 0
        for (i in lottoCards.indices) {
            sum += evaluateTotalCards(i)
        }

        return sum.toLong()
    }

    fun parseLottoCard(input: String): LottoCard {
        val matches = """Card\s+(?<id>\d+):(?<winners>((\s+\d+)*)) \|(?<revealed>((\s+\d+)*))""".toRegex().matchEntire(input)
            ?: throw IllegalStateException("couldn't match the input $input")

        // this is hacky, probably a better way but oh well
        val id = matches.groups["id"]?.value?.toInt() ?: throw IllegalStateException("couldn't find card id from $input")
        val winners = matches.groups["winners"]?.value?.trim()?.split(" ")
            ?.filter { it.trim() != "" }
            ?.map { it.trim().toInt() } ?:  throw IllegalStateException("couldn't parse winners from $input")
        val revealed = matches.groups["revealed"]?.value?.trim()?.split(" ")
            ?.filter { it.trim() != "" }
            ?.map { it.trim().toInt() } ?:  throw IllegalStateException("couldn't parse winners from $input")

        return LottoCard(id, winners.toSet(), revealed.toSet())
    }

    data class LottoCard(val id: Int, val winners: Set<Int>, val revealed: Set<Int>)
}

fun main() {
    val part1 = Day04.part1(Day04.input)
    val part2 = Day04.part2(Day04.input)

    println("part 1 result: $part1")
    println("part 2 result: $part2")
}
