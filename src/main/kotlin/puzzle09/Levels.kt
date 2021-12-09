package puzzle09

object Levels {

    fun List<List<Int>>.findLowestLevels(): List<Int> {
        val result = mutableListOf<Int>()
        check(isNotEmpty()) {
            "Heights must not be empty"
        }
        indices.forEach { y ->
            get(y).indices.forEach { x ->
                val cur = mayBeGet(x, y)
                val left = mayBeGet(x - 1, y)
                val right = mayBeGet(x + 1, y)
                val top = mayBeGet(x, y - 1)
                val bottom = mayBeGet(x, y + 1)
                val lowestNeighbour = listOf(left, right, top, bottom).minOrNull()
                    ?: error("Could not find minimum for x=$x, y=$y")
                val lowest = cur < lowestNeighbour
                if (lowest) {
                    result.add(cur)
                }
            }
        }
        return result
    }

    fun List<Int>.computeRiskLevel() = fold(0) { risk, cur ->
        risk + cur + 1
    }

    fun List<List<Int>>.mayBeGet(x: Int, y: Int): Int {
        if (y < 0 || y >= this.size) return Integer.MAX_VALUE
        val row = this[y]
        if (x < 0 || x >= row.size) return Integer.MAX_VALUE
        return row[x]
    }

    fun String.asLevels(): List<List<Int>> = lines().map { line -> line.toCharArray().map { it.digitToInt() } }

    fun debugPrint(value: Int) = when (value) {
        Integer.MAX_VALUE -> " "
        else -> value.toString()
    }

    fun debugPrint(x: Int, y: Int, cur: Int, left: Int, right: Int, top: Int, bottom: Int) {
        println("Point at: \t${x}x${y}")
        print("  ${debugPrint(top)}  \n")
        print("${debugPrint(left)} ${debugPrint(cur)} ${debugPrint(right)}\n")
        print("  ${debugPrint(bottom)}  ")
        println()
    }
}