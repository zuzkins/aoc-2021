package puzzle04

data class Board(val lines: List<Set<Int>>, val rows: List<Set<Int>>) {
    fun wins(draw: List<Int>): Boolean {
        return lines.any { line ->
            val remainder = line - draw
            remainder.isEmpty()
        } || rows.any { row ->
            val remainder = row - draw
            remainder.isEmpty()
        }
    }

    override fun toString(): String {
        val content = lines.map { it.joinToString(" ") }.joinToString("\n    ")
        return "Board(\n\t$content\n)"
    }


    companion object {
        fun makeBoard(b: List<Int>): Board {
            check(b.size == 25) {
                "Board needs exactly 25 numbers, but only ${b.size} were given"
            }
            val lines = b.windowed(5, 5).map { it.toSet() }
            val rows = (0..4).map { cnt ->
                b.drop(cnt).windowed(1, 5).flatten().toSet()
            }
            return Board(lines = lines, rows = rows)
        }

        fun parse(board: String): Board {
            val lines = board.lines()
            check(lines.size == 5) {
                "Board needs 5 lines: \n$board"
            }
            val parts = board.replace("\n", " ").split(" ")
            return makeBoard(parts.filter { it.isNotBlank() }.map { it.toInt() })
        }
    }
}

data class WinningBoard(val board: Board, val winningDraw: List<Int>) {
    val score by lazy {
        val unmarked = board.lines.flatten() - winningDraw
        val lastDraw = winningDraw.last()
        unmarked.fold(0) { acc, cur -> acc + cur } * lastDraw
    }
}

data class Bingo(val boards: List<Board>, val draw: List<Int>) {

    fun findAllWinners(): List<WinningBoard> {
        var remainingBoards = boards
        var winners = listOf<WinningBoard>()
        var currentDraw = draw.take(5)
        var drawToTry = draw.drop(5)

        while (drawToTry.isNotEmpty() && remainingBoards.isNotEmpty()) {
            currentDraw = currentDraw + drawToTry.first()
            drawToTry = drawToTry.drop(1)

            val currentWinners = remainingBoards.filter { it.wins(currentDraw) }
            currentWinners.forEach { winner ->
                winners = winners + WinningBoard(winner, currentDraw)
                remainingBoards = remainingBoards - winner
            }
        }
        check(winners.isNotEmpty()) {
            "No winner found for draw: $draw"
        }
        return winners
    }

    fun findWinner(): WinningBoard {
        return findAllWinners().first()
    }

    companion object {
        fun parse(input: String): Bingo {
            val inputs = input.lines()
            check(inputs.size >= 6) {
                "Expecting draw and at least one board: \n$input"
            }
            val draw = inputs.first().split(",").map { it.toInt() }
            val boards = inputs
                .drop(1)
                .filter { it.isNotBlank() }
                .windowed(5, 5)
                .map { it.joinToString("\n") }
            return Bingo(boards = boards.map { Board.parse(it) }, draw = draw)
        }
    }
}