package puzzle21

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle21.DiracDice.deterministicDice

internal class DiracDiceTest {

    @Test
    fun `dice rolls`() {
        val roll = DiracDice.deterministicDice().roll()
        assertThat(roll).isEqualTo(Triple(1, 2, 3))
    }

    @Test
    fun `player can move through rolls`() {
        val p1 = DiracDicePlayer(
            startingPosition = 4,
            playground = 10,
        )
        p1.move(1, 2, 3)
        assertThat(p1.currentPosition).isEqualTo(10)
        assertThat(p1.score).isEqualTo(10)
        p1.move(7, 8, 9)
        assertThat(p1.currentPosition).isEqualTo(4)
        assertThat(p1.score).isEqualTo(14)
        p1.move(13, 14, 15)
        assertThat(p1.currentPosition).isEqualTo(6)
        assertThat(p1.score).isEqualTo(20)
        p1.move(19, 20, 21)
        assertThat(p1.currentPosition).isEqualTo(6)
        assertThat(p1.score).isEqualTo(26)
    }

    @Test
    fun `can play game`() {
        val p1 = DiracDicePlayer(startingPosition = 4)
        val p2 = DiracDicePlayer(startingPosition = 8)

        val game = DiracDiceGame(players = listOf(p1, p2), dice = deterministicDice())
        while (!game.nextRound()) {

        }
        val winner = game.winner()

        assertThat(winner).isNotNull
        check(winner != null)
        assertThat(winner).isEqualTo(p1)
        assertThat(winner.score).isEqualTo(1000)
        assertThat(game.score()).isEqualTo(739785)
    }

    val puzzleP1 = DiracDicePlayer(startingPosition = 4)
    val puzzleP2 = DiracDicePlayer(startingPosition = 6)

    @Test
    fun `puzzle solution 1`() {
        val game = DiracDiceGame(players = listOf(puzzleP1, puzzleP2), dice = deterministicDice())
        while (!game.nextRound()) {

        }
        val winner = game.winner()
        assertThat(winner).isNotNull
        assertThat(game.score()).isEqualTo(888735)
    }
}