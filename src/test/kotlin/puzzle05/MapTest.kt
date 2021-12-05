package puzzle05

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle05.MapUtil.parsePoints

internal class MapTest {

    @Test
    fun `can be constructed`() {
        val map = PuzzleMap.create(dimension = 9)
        assertThat(map.size).isEqualTo(10)
        assertThat(map[0].size).isEqualTo(10)
    }

    @Test
    fun `map can mark point`() {
        val m = PuzzleMap.create(dimension = 2)
        assertThat(m.markPoint(Point(1, 1)).toString()).isEqualTo(
            """
            Map:
            0 0 0 
            0 1 0 
            0 0 0 
        """.trimIndent()
        )
    }

    @Test
    fun `map counts points`() {
        val m = PuzzleMap.create(dimension = 2)
        assertThat(
            m
                .markPoint(Point(1, 0))
                .markPoint(Point(1, 1))
                .markPoint(Point(1, 1))
                .markPoint(Point(1, 2))
                .markPoint(Point(1, 2))
                .markPoint(Point(1, 2))
                .toString()
        ).isEqualTo(
            """
            Map:
            0 1 0 
            0 2 0 
            0 3 0 
        """.trimIndent()
        )
    }

    val exampleInput = """
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
    """.trimIndent()

    @Test
    fun `example solution`() {
        val points = exampleInput.lines().map(::parsePoints)
        val puzzleMap = PuzzleMap.createLargeEnoughFor(points)
        val lines = points.map { DirectLine(it.first, it.second) }

        val finalMap = puzzleMap.markLines(lines)

        assertThat(finalMap.score).isEqualTo(5)
    }

    @Test
    fun `puzzle solution 01`() {
        val input = Utils.readResource("/puzzle05/input")
        val points = input.lines().map(::parsePoints)
        val puzzleMap = PuzzleMap.createLargeEnoughFor(points)
        val lines = points.map { DirectLine(it.first, it.second) }
        val finalMap = puzzleMap.markLines(lines)

        assertThat(finalMap.score).isEqualTo(7644)
    }

    @Test
    fun `example solution 2`() {
        val points = exampleInput.lines().map(::parsePoints)
        val puzzleMap = PuzzleMap.createLargeEnoughFor(points)
        val lines = points.map { DirectOrDiagonalLine(it.first, it.second) }

        val finalMap = puzzleMap.markLines(lines)

        assertThat(finalMap.toString()).isEqualTo(
            """
            Map:
            1 0 1 0 0 0 0 1 1 0 
            0 1 1 1 0 0 0 2 0 0 
            0 0 2 0 1 0 1 1 1 0 
            0 0 0 1 0 2 0 2 0 0 
            0 1 1 2 3 1 3 2 1 1 
            0 0 0 1 0 2 0 0 0 0 
            0 0 1 0 0 0 1 0 0 0 
            0 1 0 0 0 0 0 1 0 0 
            1 0 0 0 0 0 0 0 1 0 
            2 2 2 1 1 1 0 0 0 0 
        """.trimIndent()
        )
        assertThat(finalMap.score).isEqualTo(12)
    }

    @Test
    fun `puzzle solution 2`() {
        val input = Utils.readResource("/puzzle05/input")
        val points = input.lines().map(::parsePoints)
        val puzzleMap = PuzzleMap.createLargeEnoughFor(points)
        val lines = points.map { DirectOrDiagonalLine(it.first, it.second) }
        val finalMap = puzzleMap.markLines(lines)

        assertThat(finalMap.score).isEqualTo(18627)
    }
}