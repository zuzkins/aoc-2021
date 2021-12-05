package puzzle05

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DirectOrDiagonalLineTest {
    @Test
    fun `renders diagonals correctly`() {
        val lines = DirectOrDiagonalLine(Point(5, 5), Point(8, 2))
        assertThat(PuzzleMap.create(9).markLines(listOf(lines)).toString()).isEqualTo(
            """
            Map:
            0 0 0 0 0 0 0 0 0 0 
            0 0 0 0 0 0 0 0 0 0 
            0 0 0 0 0 0 0 0 1 0 
            0 0 0 0 0 0 0 1 0 0 
            0 0 0 0 0 0 1 0 0 0 
            0 0 0 0 0 1 0 0 0 0 
            0 0 0 0 0 0 0 0 0 0 
            0 0 0 0 0 0 0 0 0 0 
            0 0 0 0 0 0 0 0 0 0 
            0 0 0 0 0 0 0 0 0 0 
        """.trimIndent()
        )
    }
}