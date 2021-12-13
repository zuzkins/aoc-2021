package puzzle13

import Utils.readResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle13.PaperUtil.dots
import puzzle13.PaperUtil.foldAlongX
import puzzle13.PaperUtil.foldAlongY
import puzzle13.PaperUtil.print

internal class PaperTest {

    @Test
    fun `can parse paper dimensions`() {
        val paper = PaperUtil.parse(
            """
            1,10
            20,5
            """.trimIndent()
        )
        assertThat(paper.size).isEqualTo(11) // y dimension
        assertThat(paper[0].size).isEqualTo(21) // x dimension
    }

    @Test
    fun `can mark dots`() {
        val paper = PaperUtil.parse(
            """
            1,1
            2,2
            """.trimIndent()
        )

        assertThat(paper.print()).isEqualToNormalizingNewlines(
            """
            ...
            .#.
            ..#
            """.trimIndent()
        )
    }

    val exampleInput = """
        6,10
        0,14
        9,10
        0,3
        10,4
        4,11
        6,0
        6,12
        4,1
        0,13
        10,12
        3,4
        3,0
        8,4
        1,10
        2,14
        8,10
        9,0

        fold along y=7
        fold along x=5
        """.trimIndent()

    @Test
    fun `can mark example on paper`() {
        val paper = examplePaper()
        assertThat(paper.print()).isEqualToIgnoringNewLines(
            """
            ...#..#..#.
            ....#......
            ...........
            #..........
            ...#....#.#
            ...........
            ...........
            ...........
            ...........
            ...........
            .#....#.##.
            ....#......
            ......#...#
            #..........
            #.#........
            """.trimIndent()
        )
    }

    @Test
    fun `can fold along horizontal line`() {
        val folded = examplePaper().foldAlongY(7)
        assertThat(folded.print()).isEqualToIgnoringNewLines(
            """
            #.##..#..#.
            #...#......
            ......#...#
            #...#......
            .#.#..#.###
            ...........
            ...........
            """.trimIndent()
        )
    }

    @Test
    fun `can fold along vertical line`() {
        val folded = examplePaper().foldAlongY(7).foldAlongX(5)
        assertThat(folded.print()).isEqualToIgnoringNewLines(
            """
            #####
            #...#
            #...#
            #...#
            #####
            .....
            .....
            """.trimIndent()
        )
    }

    @Test
    fun `example solution 1`() {
        val dots = examplePaper().foldAlongY(7).dots()
        assertThat(dots.size).isEqualTo(17)
    }

    fun puzzlePaper() = readResource("/puzzle13/input").let { PaperUtil.parse(it) }

    @Test
    fun `puzzle solution 1`() {
        val dots = puzzlePaper().foldAlongX(655).dots()
        assertThat(dots.size).isEqualTo(785)
    }

    @Test
    fun `puzzle solution 2`() {
        val paper = puzzlePaper()
            .foldAlongX(655)
            .foldAlongY(447)
            .foldAlongX(327)
            .foldAlongY(223)
            .foldAlongX(163)
            .foldAlongY(111)
            .foldAlongX(81)
            .foldAlongY(55)
            .foldAlongX(40)
            .foldAlongY(27)
            .foldAlongY(13)
            .foldAlongY(6)

        println("--------------\n${paper.print()}")
    }

    private fun examplePaper() = PaperUtil.parse(exampleInput)
}