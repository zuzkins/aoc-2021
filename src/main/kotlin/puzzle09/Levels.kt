package puzzle09

data class Spot(val x: Int, val y: Int, val height: Int) : Comparable<Spot> {
    override fun compareTo(other: Spot): Int {
        return height.compareTo(other.height)
    }
}

object Levels {

    fun List<List<Int>>.findLowestLevels(): List<Spot> {
        val result = mutableListOf<Spot>()
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

    fun List<List<Int>>.findBasin(spot: Spot): List<Spot> {
        check(this.isNotEmpty()) {
            "Cannot find basin without data"
        }
        val left = mayBeGet(spot.x - 1, spot.y, -1)
        val right = mayBeGet(spot.x + 1, spot.y, -1)
        val top = mayBeGet(spot.x, spot.y - 1, -1)
        val bottom = mayBeGet(spot.x, spot.y + 1, -1)

        return listOf(left, right, top, bottom).filter { it > spot && it.height < 9 }
            .fold(listOf(spot)) { result, cur ->
                result + findBasin(cur)
            }.toSet().toList()
    }

    fun List<List<Int>>.findAllBasins(): List<List<Spot>> {
        val spots = this.findLowestLevels()
        return spots.map { findBasin(it) }
    }

    fun List<List<Spot>>.countScore() = fold(1) { score, basin ->
        score * basin.size
    }

    fun List<Spot>.computeRiskLevel() = fold(0) { risk, cur ->
        risk + cur.height + 1
    }

    fun List<List<Int>>.mayBeGet(x: Int, y: Int, defaultValue: Int = Integer.MAX_VALUE): Spot {
        val defaultResult = Spot(x, y, defaultValue)
        if (y < 0 || y >= this.size) return defaultResult
        val row = this[y]
        if (x < 0 || x >= row.size) return defaultResult
        return defaultResult.copy(height = row[x])
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