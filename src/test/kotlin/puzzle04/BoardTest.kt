package puzzle04

import Utils.readResource
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

internal class BoardTest {

    @Test
    fun `board can be created only from 25 numbers`() {
        assertThatCode {
            Board.makeBoard(listOf(1, 2, 3))
        }.hasMessage("Board needs exactly 25 numbers, but only 3 were given")
    }

    @Test
    fun `board can be created`() {
        assertThatCode {
            Board.makeBoard(
                listOf(
                    1, 1, 1, 1, 1,
                    2, 2, 2, 2, 2,
                    3, 3, 3, 3, 3,
                    4, 4, 4, 4, 4,
                    5, 5, 5, 5, 5,
                )
            )
        }.doesNotThrowAnyException()
    }

    @Test
    fun `board can be parsed`() {
        val board = """
            1 1 1 1 1
            2 2 2 2 2
            3 3 3 3 3
            4 4 4 4 4
            5 5 5 5 5
        """.trimIndent()

        assertThat(Board.parse(board)).isEqualTo(
            Board.makeBoard(
                listOf(
                    1, 1, 1, 1, 1,
                    2, 2, 2, 2, 2,
                    3, 3, 3, 3, 3,
                    4, 4, 4, 4, 4,
                    5, 5, 5, 5, 5,
                )
            )
        )
    }

    @Test
    fun `board has lines correct`() {
        val lines = Board.parse(
            """
            0 1 2 3 4
            10 11 12 13 14
            20 21 22 23 24
            30 31 32 33 34
            40 41 42 43 44
            """.trimIndent()
        ).lines

        assertThat(lines.first()).isEqualTo(setOf(0, 1, 2, 3, 4))
        assertThat(lines).isEqualTo(
            listOf(
                setOf(0, 1, 2, 3, 4),
                setOf(10, 11, 12, 13, 14),
                setOf(20, 21, 22, 23, 24),
                setOf(30, 31, 32, 33, 34),
                setOf(40, 41, 42, 43, 44),
            )
        )
    }

    @Test
    fun `board has rows correct`() {
        val rows = Board.parse(
            """
            0 1 2 3 4
            10 11 12 13 14
            20 21 22 23 24
            30 31 32 33 34
            40 41 42 43 44
            """.trimIndent()
        ).rows
        assertThat(rows.first()).isEqualTo(setOf(0, 10, 20, 30, 40))

        assertThat(rows).isEqualTo(
            listOf(
                setOf(0, 10, 20, 30, 40),
                setOf(1, 11, 21, 31, 41),
                setOf(2, 12, 22, 32, 42),
                setOf(3, 13, 23, 33, 43),
                setOf(4, 14, 24, 34, 44),
            )
        )
    }

    @Test
    fun `example board can be parsed`() {
        assertThatCode {
            Board.parse(
                """
                22 13 17 11  0
                 8  2 23  4 24
                21  9 14 16  7
                 6 10  3 18  5
                 1 12 20 15 19
                """.trimIndent()
            )
        }.doesNotThrowAnyException()
    }

    @Test
    fun `board can win with line`() {
        val b = Board.parse(
            """
            0 1 2 3 4
            10 11 12 13 14
            20 21 22 23 24
            30 31 32 33 34
            40 41 42 43 44
            """.trimIndent()
        )
        assertThat(b.wins(listOf(20, 21, 22, 23, 24))).isTrue()
    }

    @Test
    fun `board can win with row`() {
        val b = Board.parse(
            """
            0 1 2 3 4
            10 11 12 13 14
            20 21 22 23 24
            30 31 32 33 34
            40 41 42 43 44
            """.trimIndent()
        )
        assertThat(b.wins(listOf(3, 13, 23, 33, 43))).isTrue()
    }

    val exampleTest = """
        7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

        22 13 17 11  0
         8  2 23  4 24
        21  9 14 16  7
         6 10  3 18  5
         1 12 20 15 19
        
         3 15  0  2 22
         9 18 13 17  5
        19  8  7 25 23
        20 11 10 24  4
        14 21 16 12  6
        
        14 21 17 24  4
        10 16 15  9 19
        18  8 23 26 20
        22 11 13  6  5
         2  0 12  3  7
    """.trimIndent()

    @Test
    fun `Bingo can parse the draw`() {
        assertThat(Bingo.parse(exampleTest).draw).isEqualTo(
            listOf(
                7,
                4,
                9,
                5,
                11,
                17,
                23,
                2,
                0,
                14,
                21,
                24,
                10,
                16,
                13,
                6,
                15,
                25,
                12,
                22,
                18,
                20,
                8,
                19,
                3,
                26,
                1
            )
        )
    }

    @Test
    fun `Bingo can parse boards`() {
        val bingo = Bingo.parse(exampleTest)
        assertThat(bingo.boards.first()).isEqualTo(
            Board.parse(
                """
                22 13 17 11  0
                 8  2 23  4 24
                21  9 14 16  7
                 6 10  3 18  5
                 1 12 20 15 19
                """.trimIndent()
            )
        )
        assertThat(bingo.boards.drop(1).first()).isEqualTo(
            Board.parse(
                """
                 3 15  0  2 22
                 9 18 13 17  5
                19  8  7 25 23
                20 11 10 24  4
                14 21 16 12  6
                """.trimIndent()
            )
        )
        bingo.boards.forEach {
            println("$it")
        }
    }

    @Test
    fun `Bingo can find winner`() {
        assertThat(Bingo.parse(exampleTest).findWinner().board).isEqualTo(
            Board.parse(
                """
                14 21 17 24  4
                10 16 15  9 19
                18  8 23 26 20
                22 11 13  6  5
                 2  0 12  3  7
                """.trimIndent()
            )
        )
    }

    @Test
    fun `Bingo winner score`() {
        assertThat(Bingo.parse(exampleTest).findWinner().score).isEqualTo(4512)
    }

    @Test
    fun `puzzle solution`() {
        val input = readResource("/puzzle04/input")
        val bingo = Bingo.parse(input)
        assertThat(bingo.findWinner().score).isEqualTo(35670)
    }

    @Test
    fun `can find last winner`() {
        val last = Bingo.parse(exampleTest).findAllWinners().last()
        assertThat(last.score).isEqualTo(1924)
        assertThat(last.winningDraw).isEqualTo(listOf(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16, 13))
    }

    @Test
    fun `puzzle solution 2`() {
        val input = readResource("/puzzle04/input")
        val bingo = Bingo.parse(input)
        val winners = bingo.findAllWinners()
        assertThat(winners.last().score).isEqualTo(22704)
    }
}