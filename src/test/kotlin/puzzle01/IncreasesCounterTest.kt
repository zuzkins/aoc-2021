package puzzle01

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class IncreasesCounterTest {

    @Test
    fun `no measurements`() {
        assertThat(IncreasesCounter.countIncreases(emptyList())).isEqualTo(0)
    }

    @Test
    fun `single measurement`() {
        assertThat(IncreasesCounter.countIncreases(listOf(1))).isEqualTo(0)
    }

    @Test
    fun `no increases`() {
        assertThat(IncreasesCounter.countIncreases(listOf(1, 1))).isEqualTo(0)
    }

    @Test
    fun `single increase`() {
        assertThat(IncreasesCounter.countIncreases(listOf(1, 2))).isEqualTo(1)
    }

    @Test
    fun `all increases`() {
        assertThat(IncreasesCounter.countIncreases(listOf(1, 2, 3, 4, 5, 6))).isEqualTo(5)
    }

    @Test
    fun `some increases`() {
        assertThat(IncreasesCounter.countIncreases(listOf(1, 2, 1, 3, 1, 4, 4, 5, 1, 6))).isEqualTo(5)
    }
}