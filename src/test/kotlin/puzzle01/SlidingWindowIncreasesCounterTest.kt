package puzzle01

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import puzzle01.SlidingWindowIncreasesCounter.Companion.createSlidingWindows
import puzzle01.SlidingWindowIncreasesCounter.Companion.sums

internal class SlidingWindowIncreasesCounterTest {
    @Test
    fun `sliding windows - empty list`() {
        assertThat(emptyList<Int>().createSlidingWindows()).isEmpty()
    }

    @Test
    fun `sliding windows - single measurement`() {
        assertThat(listOf(1).createSlidingWindows()).isEmpty()
    }

    @Test
    fun `sliding windows - two measurements`() {
        assertThat(listOf(1, 2).createSlidingWindows()).isEmpty()
    }

    @Test
    fun `sliding windows - three measurements`() {
        assertThat(listOf(1, 2, 3).createSlidingWindows()).isEqualTo(
            listOf(
                Triple(1, 2, 3),
            )
        )
    }

    @Test
    fun `sliding windows - four measurements`() {
        assertThat(listOf(1, 2, 3, 4).createSlidingWindows()).isEqualTo(
            listOf(
                Triple(1, 2, 3),
                Triple(2, 3, 4),
            )
        )
    }

    @Test
    fun `sliding windows - five measurements`() {
        assertThat(listOf(1, 2, 3, 4, 5).createSlidingWindows()).isEqualTo(
            listOf(
                Triple(1, 2, 3),
                Triple(2, 3, 4),
                Triple(3, 4, 5),
            )
        )
    }

    @Test
    fun `sliding windows - more measurements`() {
        assertThat(listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263).createSlidingWindows()).isEqualTo(
            listOf(
                Triple(199, 200, 208),
                Triple(200, 208, 210),
                Triple(208, 210, 200),
                Triple(210, 200, 207),
                Triple(200, 207, 240),
                Triple(207, 240, 269),
                Triple(240, 269, 260),
                Triple(269, 260, 263),
            )
        )
    }

    @Test
    fun `sums - empty list`() {
        assertThat(emptyList<Triple<Int, Int, Int>>().sums()).isEmpty()
    }

    @Test
    fun `sums a list`() {
        assertThat(listOf(Triple(1, 2, 3), Triple(10, 100, 1000)).sums()).isEqualTo(listOf(6, 1110))
    }
}