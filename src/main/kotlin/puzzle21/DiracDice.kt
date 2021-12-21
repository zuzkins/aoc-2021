package puzzle21

data class DiracDicePlayer(
    var startingPosition: Int,
    val playground: Int = 10,
    var currentPosition: Int = startingPosition,
    var score: Int = 0
) {

    fun move(roll: Triple<Int, Int, Int>) {
        move(roll.first, roll.second, roll.third)
    }

    fun move(a: Int, b: Int, c: Int) {
        val m = a + b + c
        val nextPos = (currentPosition + m) % 10
        val finalPos = if (nextPos == 0) {
            10
        } else {
            nextPos
        }
        currentPosition = finalPos
        score += finalPos
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

    fun nextRound(): Boolean {
        players.forEach { p ->
            val roll = dice.roll()
            p.move(roll)
            if (isWinner(p)) {
                return true
            }
        }
        return false
    }

    fun winner(): DiracDicePlayer? {
        return players.firstOrNull { isWinner(it) }
    }

    private fun isWinner(p: DiracDicePlayer) = p.score >= 1000

    fun score(): Int {
        val losingScore = players.minOf { it.score }
        return losingScore * dice.rolledCount
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