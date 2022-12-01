package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day5Test {

    @Test
    fun `test binary conversion`() {
        assertThat(Day5.convertStringToInt("BFFFBBFRRR"))
            .isEqualTo(567)
        assertThat(Day5.convertStringToInt("FFFBBBFRRR"))
            .isEqualTo(119)
        assertThat(Day5.convertStringToInt("BBFFBBFRLL"))
            .isEqualTo(820)
    }
}