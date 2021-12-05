package puzzle05

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DirectLineTest {
    @Test
    fun `renders a line`() {
        val l = DirectLine(Point(0, 0), Point(0, 2))
        assertThat(l.points).isEqualTo(listOf(Point(0, 0), Point(0, 1), Point(0, 2)))
    }
}