package puzzle22

import java.util.*

fun IntRange.intersection(other: IntRange): IntRange {
    val start = kotlin.math.max(this.first, other.first)
    val end = kotlin.math.min(this.endInclusive, other.endInclusive)
    return if (start > end) {
        IntRange.EMPTY
    } else {
        IntRange(start, end)
    }
}

fun Pair<Int, Int>.intersection(other: Pair<Int, Int>): IntRange {
    val start = kotlin.math.max(this.first, other.first)
    val end = kotlin.math.min(this.second, other.second)
    return if (start > end) {
        IntRange.EMPTY
    } else {
        IntRange(start, end)
    }
}

fun Triple<IntRange, IntRange, IntRange>.forAll(f: (x: Int, y: Int, z: Int) -> Unit) {
    this.third.forEach { z ->
        this.second.forEach { y ->
            this.first.forEach { x ->
                f(x, y, z)
            }
        }
    }
}

data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange, val toggle: Boolean)
data class Cube(
    val originX: Int,
    val originY: Int,
    val originZ: Int,
    val size: Int,
    private val marker: Int = 0,
    val data: Array<Array<IntArray>> = Array(size) {
        Array(size) {
            IntArray(size) { -1 }
        }
    }
) {
    private var counter: Long = 0

    fun process(cuboid: Cuboid) {
        val xAxis = (originX to originX + size - 1).intersection(cuboid.x.first to cuboid.x.last)
        val yAxis = (originY to originY + size - 1).intersection(cuboid.y.first to cuboid.y.last)
        val zAxis = (originZ to originZ + size - 1).intersection(cuboid.z.first to cuboid.z.last)
        Triple(xAxis, yAxis, zAxis).forAll { x, y, z ->
            val prev = data[z - originZ][y - originY][x - originX]
            if (cuboid.toggle) {
                data[z - originZ][y - originY][x - originX] = marker
                counter++
            } else if (prev == marker) {
                data[z - originZ][y - originY][x - originX] = -1
                counter--
            }
        }
    }


    fun shift(toX: Int, toY: Int, toZ: Int): Cube {
        return Cube(toX, toY, toZ, size, data = data, marker = marker + 1)
    }

    fun getTurnedOn(): List<Triple<Int, Int, Int>> {
        val result = LinkedList<Triple<Int, Int, Int>>()
        Triple(0 until size, 0 until size, 0 until size).forAll { x, y, z ->
            val flag = data[z][y][x]
            if (flag == marker) {
                result.add(Triple(originX + x, originY + y, originZ + z))
            }
        }
        return result
    }

    fun getTurnedCount() = counter
}

object ReactorReboot {

    fun parseCuboid(line: String): Cuboid {
        val (flagStr, dirs) = line.split(" ")
        val flag = when (flagStr) {
            "on" -> true
            "off" -> false
            else -> error("Unknown toggle flag '$flagStr'")
        }
        val (xExpr, yExpr, zExpr) = dirs.split(",")
        val x = parseExpr(xExpr)
        val y = parseExpr(yExpr)
        val z = parseExpr(zExpr)
        return Cuboid(x, y, z, flag)
    }

    private fun parseExpr(e: String): IntRange {
        val (from, to) = e.drop(2).split("..").map { it.toInt() }
        return IntRange(from, to)
    }
}