package puzzle21

data class DiracDicePlayer(
    val startingPosition: Int,
    val playground: Int = 10,
    val currentPosition: Int = startingPosition,
    val score: Int = 0
) {

    fun move(roll: Triple<Int, Int, Int>): DiracDicePlayer {
        return move(roll.first, roll.second, roll.third)
    }

    fun move(a: Int, b: Int, c: Int): DiracDicePlayer {
        val m = a + b + c
        val nextPos = (currentPosition + m) % 10
        val finalPos = if (nextPos == 0) {
            10
        } else {
            nextPos
        }
        return copy(
            currentPosition = finalPos,
            score = score + finalPos
        )
    }
}

class Dice(seq: Sequence<Int>, var rolledCount: Int = 0) {
    val it = seq.iterator()
    fun roll(): Triple<Int, Int, Int> {
        rolledCount += 3
        return Triple(it.next(), it.next(), it.next())
    }
}

data class DiracDiceGame(val players: List<DiracDicePlayer>, val dice: Dice) {

    fun nextRound(): Pair<DiracDiceGame, Boolean> {
        var winner = false
        val newPlayers = players.map { p ->
            if (winner) {
                p
            } else {
                val roll = dice.roll()
                val nextPlayer = p.move(roll)
                winner = isWinner(nextPlayer)
                nextPlayer
            }
        }
        return copy(players = newPlayers) to winner
    }

    fun winner(): DiracDicePlayer {
        return players.firstOrNull { isWinner(it) } ?: error("Game has no winner")
    }

    private fun isWinner(p: DiracDicePlayer) = p.score >= 1000

    fun score(): Int {
        val losingScore = players.minOf { it.score }
        return losingScore * dice.rolledCount
    }

    fun play(): Sequence<DiracDiceGame> {
        var finished = false
        val seq = generateSequence(this) {
            if (finished) {
                null
            } else {
                val (game, winner) = it.nextRound()
                finished = winner
                game
            }
        }
        return seq
    }
}

object DiracDice {
    fun deterministicDice(): Dice {
        val seq = generateSequence(1) { next ->
            if (next == 100) {
                1
            } else {
                next + 1
            }
        }
        return Dice(seq)
    }

}