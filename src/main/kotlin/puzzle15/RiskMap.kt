package puzzle15

import puzzle05.Point
import java.util.*

typealias RiskMap = Array<IntArray>
typealias PointWithDistance = Pair<Point, Int>

val neighbours = listOf(
    0 to -1,
    0 to 1,
    1 to 0,
    -1 to 0
)

object RiskMapUtils {

    fun RiskMap.findDijsktrasDistance(): Int {
        val distances = Array(size) { IntArray(first().size) { Int.MAX_VALUE } }
        distances[0][0] = 0
        val yInd = indices
        val xInd = first().indices

        val alreadyVisited = mutableSetOf<Point>()
        val candidatesToVisit = PriorityQueue<PointWithDistance> { self, that ->
            self.second.compareTo(that.second)
        }
        candidatesToVisit += (0 to 0) to 0

        while (candidatesToVisit.isNotEmpty()) {
            val (cur, _) = candidatesToVisit.poll()
            alreadyVisited += cur
            neighbours.asSequence().map { (dx, dy) ->
                val x = cur.first + dx
                val y = cur.second + dy
                x to y
            }.filter { point ->
                point.first in xInd && point.second in yInd && point !in alreadyVisited
            }.map { point ->
                val risk = this[point.second][point.first]
                val distance = distances[cur.second][cur.first] + risk
                point to distance
            }.filter { (point, distance) ->
                distance < distances[point.second][point.first]
            }.forEach { pWithDistance ->
                val (point, distance) = pWithDistance
                distances[point.second][point.first] = distance
                candidatesToVisit += pWithDistance
            }
        }

        return distances[distances.indices.last][distances.first().indices.last]
    }

    fun RiskMap.extend(): RiskMap {
        val originalXSize = this.first().size
        val originalYSize = size
        val newMap = Array(size * 5) {
            IntArray(originalXSize * 5) { 0 }
        }

        val mapIndicies = indices.flatMap { y ->
            first().indices.map { x ->
                x to y
            }
        }

        // copy the original map into the 0,0 sector
        mapIndicies.forEach { (y, x) ->
            newMap[y][x] = this[y][x]
        }

        // fill in first row of copies of the original
        (1 until 5).forEach { column ->
            mapIndicies.forEach { (x, y) ->
                val dx = originalXSize * column
                val orig = newMap[y][dx + x - originalXSize]

                newMap[y][x + dx] = (orig + 1).cap()
            }
        }
        val newXIndices = newMap.first().indices

        // fill remaining rows - incrementing the number from the previous row on the same y position
        (1 until 5).forEach { step ->
            indices.forEach { origY ->
                newXIndices.forEach { x ->
                    val dy = step * originalYSize
                    val y = dy + origY

                    val orig = newMap[y - originalYSize][x]
                    newMap[y][x] = (orig + 1).cap()
                }
            }
        }

        return newMap
    }

    fun parse(input: List<String>): Array<IntArray> {
        val map = Array(input.size) {
            IntArray(input[0].length) {
                0
            }
        }
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, value ->
                map[y][x] = value.digitToInt()
            }
        }
        return map
    }

    fun RiskMap.print() = joinToString("\n") { row ->
        row.joinToString("") { it.toString() }
    }

    private fun Int.cap() = if (this > 9) {
        this - 9
    } else {
        this
    }
}