package twentytwentythree

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day07Test {
    @Test
    fun testParsingHands() {
        var hand = "TJT87 0"
        assertThat(Day07.Hand.parse(hand)).isEqualTo(Day07.Hand(listOf(10, 11, 10, 8, 7), Day07.Classification.OnePair, 0))
        hand = "12345 0"
        assertThat(Day07.Hand.parse(hand)).isEqualTo(Day07.Hand(listOf(1, 2, 3, 4, 5), Day07.Classification.HighKard, 0))
        hand = "12124 0"
        assertThat(Day07.Hand.parse(hand)).isEqualTo(Day07.Hand(listOf(1, 2, 1, 2, 4), Day07.Classification.TwoPair, 0))
        hand = "12131 0"
        assertThat(Day07.Hand.parse(hand)).isEqualTo(Day07.Hand(listOf(1, 2, 1, 3, 1), Day07.Classification.ThreeOfAKind, 0))
        hand = "12121 0"
        assertThat(Day07.Hand.parse(hand)).isEqualTo(Day07.Hand(listOf(1, 2, 1, 2, 1), Day07.Classification.FullHouse, 0))
        hand = "A1AAA 0"
        assertThat(Day07.Hand.parse(hand)).isEqualTo(Day07.Hand(listOf(14, 1, 14, 14, 14), Day07.Classification.FourOfAKind, 0))
        hand = "QQQQQ 0"
        assertThat(Day07.Hand.parse(hand)).isEqualTo(Day07.Hand(listOf(12, 12, 12, 12, 12), Day07.Classification.FiveOfAKind, 0))
    }
}
