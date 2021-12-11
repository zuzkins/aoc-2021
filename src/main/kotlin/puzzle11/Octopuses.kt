package puzzle11

val neighbours = listOf(
    -1 to -1,
    0 to -1,
    1 to -1,
    1 to 0,
    1 to 1,
    0 to 1,
    -1 to 1,
    -1 to 0
)

object Octopuses {

    fun maybeFlash(
        levels: Array<IntArray>,
        x: Int,
        y: Int,
        alreadyFlashed: MutableSet<Pair<Int, Int>>,
        triggered: Boolean = false
    ): Boolean {
        var flashed = false
        if (x < 0 || y < 0 || x >= levels.size || y >= levels.size) {
            return flashed
        }
        val octopusLevel = levels[y][x]
        if (octopusLevel > 9 && (x to y) !in alreadyFlashed) {
            flashed = true
            alreadyFlashed.add(x to y)
            neighbours.forEach { n ->
                maybeFlash(levels, x + n.first, y + n.second, alreadyFlashed, triggered = true)
            }
        }
        if (triggered) {
            levels[y][x] = octopusLevel + 1
        }
        return flashed
    }

    fun Array<IntArray>.nextStep(): Pair<Array<IntArray>, MutableSet<Pair<Int, Int>>> {
        val flashed = mutableSetOf<Pair<Int, Int>>()
        forEachIndexed { y, row ->
            row.forEachIndexed { x, level ->
                this[y][x] = level + 1
            }
        }

        var didFlash: Boolean
        do {
            didFlash = false
            forEachIndexed { y, row ->
                row.forEachIndexed { x, _ ->
                    didFlash = didFlash or maybeFlash(this, x, y, flashed)
                }
            }
        } while (didFlash)

        flashed.forEach { (x, y) ->
            this[y][x] = 0
        }
        return this to flashed
    }

    fun Array<IntArray>.print(): String {
        val sb = StringBuilder()
        forEach { row ->
            row.forEach { level ->
                sb.append(level)
            }
            sb.append("\n")
        }

        return sb.toString().dropLast(1)
    }

    fun from(definition: String): Array<IntArray> {
        val lines = definition.lines()
        val levels = lines.map { line ->
            val levels = line.toCharArray().map { it.digitToInt() }.toIntArray()
            check(levels.size == lines.size) {
                "Expected a rectangle, got ${lines.size}x${levels.size}"
            }
            levels
        }.toTypedArray()
        return levels
    }
}
