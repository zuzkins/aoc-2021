package puzzle21

data class DiracDicePlayer(
    val startingPosition: Int,
    val playground: Int = 10,
    val currentPosition: Int = startingPosition,
    val score: Int = 0
) {

    fun move(roll: Triple<Int, Int, Int>): DiracDicePlayer {
        return move(roll.first + roll.second + roll.third)
    }

    fun move(m: Int): DiracDicePlayer {
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

val quantumRolls = (1..3).flatMap { r1 ->
    (1..3).flatMap { r2 ->
        (1..3).map { r3 ->
            r1 + r2 + r3
        }
    }
}.groupingBy { it }.eachCount()

typealias WinCounter = Pair<Long, Long>

private val P1Win = 1L to 0L
private val P2Win = 0L to 1L

private fun rollOrder() = generateSequence(true) { !it }.asIterable()

private operator fun WinCounter.plus(other: WinCounter): WinCounter =
    this.first + other.first to this.second + other.second

private operator fun WinCounter.times(universes: Int) = this.first * universes to this.second * universes

// 444356092776315L
//  42514214244212L

data class QuantumDiracDiceGame(val p1: DiracDicePlayer, val p2: DiracDicePlayer, val p1Turn: Boolean = true) {

    fun play(): WinCounter {
        return when {
            p1.score >= 21 -> P1Win
            p2.score >= 21 -> P2Win
            else -> quantumRolls.entries.map { (roll, universesCount) ->
                val (np1, np2) = if (p1Turn) {
                    p1.move(roll) to p2
                } else {
                    p1 to p2.move(roll)
                }
                QuantumDiracDiceGame(np1, np2, !p1Turn).play() * universesCount
            }.reduce { acc, n -> acc + n }
        }
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