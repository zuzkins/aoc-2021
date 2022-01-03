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
        p1.move(1 + 2 + 3).also {
            assertThat(it.currentPosition).isEqualTo(10)
            assertThat(it.score).isEqualTo(10)
        }.move(7 + 8 + 9).also {
            assertThat(it.currentPosition).isEqualTo(4)
            assertThat(it.score).isEqualTo(14)
        }.move(13 + 14 + 15).also {
            assertThat(it.currentPosition).isEqualTo(6)
            assertThat(it.score).isEqualTo(20)
        }.move(19 + 20 + 21).also {
            assertThat(it.currentPosition).isEqualTo(6)
            assertThat(it.score).isEqualTo(26)
        }
    }

    val exampleP1 = DiracDicePlayer(startingPosition = 4)
    val exampleP2 = DiracDicePlayer(startingPosition = 8)

    @Test
    fun `can play game`() {
        val game = DiracDiceGame(players = listOf(exampleP1, exampleP2), dice = deterministicDice())

        val winningGame = game.play().last()
        val (winner, loser) = winningGame.players

        assertThat(winner.score).isEqualTo(1000)
        assertThat(loser.score).isEqualTo(745)
        assertThat(winningGame.dice.rolledCount).isEqualTo(993)
        assertThat(winningGame.score()).isEqualTo(739785)

    }

    val puzzleP1 = DiracDicePlayer(startingPosition = 4)
    val puzzleP2 = DiracDicePlayer(startingPosition = 6)

    @Test
    fun `puzzle solution 1`() {
        val game = DiracDiceGame(players = listOf(puzzleP1, puzzleP2), dice = deterministicDice())

        val winningGame = game.play().last()
        val winner = winningGame.winner()
        assertThat(winner).isNotNull
        assertThat(winningGame.score()).isEqualTo(888735)
    }

    @Test
    fun `example solution 2`() {
        val result = QuantumDiracDiceGame(exampleP1, exampleP2).play()
        assertThat(result.first).isEqualTo(444356092776315)
    }

    @Test
    fun `puzzle solution 2`() {
        val result = QuantumDiracDiceGame(puzzleP1, puzzleP2).play()
        assertThat(result.first).isEqualTo(647608359455719)
    }
}