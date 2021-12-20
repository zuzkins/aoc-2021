package puzzle20

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle20.ImageEnhancement.countPixels
import puzzle20.ImageEnhancement.enhance
import puzzle20.ImageEnhancement.neighbouringColours
import puzzle20.ImageEnhancement.neighbouringColoursDecimal
import puzzle20.ImageEnhancement.neighbouringPixels
import puzzle20.ImageEnhancement.parseEnhancementMap
import puzzle20.ImageEnhancement.parseOriginalImage
import puzzle20.ImageEnhancement.print

internal class ImageEnhancementTest {

    private fun exampleImage1() = parseOriginalImage(
        """
        #..#.
        #....
        ##..#
        ..#..
        ..###
        """.trimIndent().lines()
    )

    private fun enhancementMap1() = parseEnhancementMap(
        "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##" +
                "#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###" +
                ".######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#." +
                ".#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#....." +
                ".#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.." +
                "...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#....." +
                "..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#"
    )

    @Test
    fun `can parse example image`() {
        val i = exampleImage1()
        assertThat(i[0]).isEqualTo(
            intArrayOf(1, 0, 0, 1, 0)
        )
        assertThat(i[1]).isEqualTo(
            intArrayOf(1, 0, 0, 0, 0)
        )
        assertThat(i[2]).isEqualTo(
            intArrayOf(1, 1, 0, 0, 1)
        )
        assertThat(i[3]).isEqualTo(
            intArrayOf(0, 0, 1, 0, 0)
        )
        assertThat(i[4]).isEqualTo(
            intArrayOf(0, 0, 1, 1, 1)
        )
    }

    @Test
    fun `can produce neighbouring pixel indices`() {
        assertThat((3 to 3).neighbouringPixels()).isEqualTo(
            listOf(
                2 to 2, 3 to 2, 4 to 2, 2 to 3, 3 to 3, 4 to 3, 2 to 4, 3 to 4, 4 to 4
            )
        )
    }

    @Test
    fun `can get colours of neighbouring pixels`() {
        val i = exampleImage1()
        assertThat(i.neighbouringColours(2 to 2)).isEqualTo(listOf(0, 0, 0, 1, 0, 0, 0, 1, 0))
        assertThat(i.neighbouringColoursDecimal(2 to 2)).isEqualTo(34)

        assertThat(i.neighbouringColours(0 to 0)).isEqualTo(listOf(0, 0, 0, 0, 1, 0, 0, 1, 0))
    }


    @Test
    fun `can parse enhancement map`() {
        val m = enhancementMap1()
        assertThat(m[34]).isEqualTo(1)
    }

    @Test
    fun `can enhance example`() {
        val e1 = exampleImage1().enhance(enhancementMap1())
        assertThat(e1.print()).isEqualTo(
            """
            .........
            ..##.##..
            .#..#.#..
            .##.#..#.
            .####..#.
            ..#..##..
            ...##..#.
            ....#.#..
            .........
            """.trimIndent()
        )

        val e2 = e1.enhance(enhancementMap1())
        assertThat(e2.print()).isEqualTo(
            """
            .............
            .............
            .........#...
            ...#..#.#....
            ..#.#...###..
            ..#...##.#...
            ..#.....#.#..
            ...#.#####...
            ....#.#####..
            .....##.##...
            ......###....
            .............
            .............
            """.trimIndent()
        )

        assertThat(e2.countPixels().second).isEqualTo(35)
    }

    private val puzzleInput = Utils.readResource("/puzzle20/input").lines()
    val puzzleEnhancedMap = parseEnhancementMap(puzzleInput.first())
    fun puzzleImage() = parseOriginalImage(puzzleInput.drop(2))

    @Test
    fun `puzzle solution 1`() {
        val i = puzzleImage()
        val e1 = i.enhance(puzzleEnhancedMap)
        println(e1.print() + "\n\n\n\n")
        val e2 = e1.enhance(puzzleEnhancedMap, default = 1)
        println(e2.print() + "\n\n\n\n")
        assertThat(e2.countPixels().second).isEqualTo(4928)
    }
}