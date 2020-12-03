package twentynineteen

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DaySixTest {
    @Test
    fun `arbitrary test`() {
        assertThat(1).isEqualTo(1)
    }

    @Test
    fun `does counting entities work`() {
        val entityA = Entity("A")
        val entityB = Entity("B", mutableListOf(entityA))
        val entityC = Entity("C", mutableListOf(entityB))

        val numOrbits = DaySix.getNumberOfOrbitsForEntity(entityC)
        assertThat(numOrbits).isEqualTo(2)
    }
}
