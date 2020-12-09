package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day9Test {

    @Test
    fun `test the canSumToTarget method`() {
        assertThat(Day9.canSumToTarget(listOf(1, 2, 3), 2)).isFalse()
        assertThat(Day9.canSumToTarget(listOf(1, 2, 3), 4)).isTrue()
        assertThat(Day9.canSumToTarget(listOf(1, 2, 3), 6)).isFalse()
    }

    @Test
    fun `test findFirstInvalid method`() {
        val input = listOf(35,20,15,25,47,40,62, 55, 65, 95,102,117,150,182,127,219,299,277,309,576).map { it.toLong() }

        assertThat(Day9.findFirstInvalid(input, preamble = 5)).isEqualTo(127)
    }

    @Test
    fun `test sliding window exemplar method`() {
        val input = listOf(35,20,15,25,47,40,62, 55, 65, 95,102,117,150,182,127,219,299,277,309,576).map { it.toLong() }
        val target = 127L

        assertThat(Day9.findContiguousSummingIndicesForTarget(input, target)).isEqualTo(2 to 5)
        assertThat(Day9.sumMinAndMaxFor(input, 2, 5)).isEqualTo(62)
    }

}
