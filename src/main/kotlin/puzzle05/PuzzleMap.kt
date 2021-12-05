package puzzle05

import puzzle05.MapUtil.makeSeq
import kotlin.math.abs

typealias Point = Pair<Int, Int>

interface Line {
    val points: List<Point>
}

data class DirectLine(val from: Point, val to: Point) : Line {
    override val points: List<Point> by lazy {
        if (!isDirectLine(from, to)) {
            return@lazy emptyList<Point>()
        }

        val xs = makeSeq(from.first, to.first)
        val ys = makeSeq(from.second, to.second)
        xs.zip(ys).map { (x, y) -> Point(x, y) }
    }

    companion object {
        fun isDirectLine(from: Point, to: Point) = from.first == to.first || from.second == to.second
    }
}

data class DirectOrDiagonalLine(val from: Point, val to: Point) : Line {
    override val points: List<Point> by lazy {
        if (DirectLine.isDirectLine(from, to)) {
            return@lazy DirectLine(from, to).points
        } else if (!isSquare(from, to)) {
            return@lazy emptyList()
        }

        val dx = sig(to.first - from.first)
        val dy = sig(to.second - from.second)

        (0..Math.abs(from.first - to.first)).map { offset ->
            Point(from.first + offset * dx, from.second + offset * dy)
        }
    }

    companion object {
        fun isSquare(from: Point, to: Point): Boolean {
            return abs(from.first - to.first) == abs(from.second - to.second)
        }

        fun sig(i: Int) = if (i < 0) -1 else 1
    }
}

data class PuzzleMap(val m: List<List<Int>>) : List<List<Int>> by m {

    val score by lazy {
        m.sumOf { o -> o.filter { it > 1 }.size }
    }

    fun markPoint(p: Point): PuzzleMap {
        check(p.first < m.size && p.second < m.size) {
            "Point $p is outside of our map: ${m.size}"
        }

        val o = m.toMutableList()
        val i = m[p.second].toMutableList()

        i[p.first] = i[p.first] + 1
        o[p.second] = i
        return PuzzleMap(o)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("Map:")
        m.forEach { row ->
            sb.append("\n")
            row.forEach { column ->
                sb.append(column).append(" ")
            }
        }
        return sb.toString()
    }

    fun markLines(lines: List<Line>): PuzzleMap {
        return lines.flatMap { it.points }.fold(this) { m, p ->
            m.markPoint(p)
        }
    }

    companion object {
        fun create(dimension: Int) = PuzzleMap(
            List(dimension + 1) {
                List(dimension + 1) {
                    0
                }
            }
        )

        fun createLargeEnoughFor(points: List<Pair<Point, Point>>): PuzzleMap {
            val max = points.flatMap { (f, s) -> listOf(f.first, f.second, s.first, s.second) }.maxOrNull() ?: 0
            return create(max)
        }
    }
}

object MapUtil {
    fun parsePoint(input: String): Point = input.split(",").let { (x, y) ->
        x.toInt() to y.toInt()
    }

    fun parsePoints(input: String): Pair<Point, Point> {
        val (p1, p2) = input.split(" -> ")

        return Pair(parsePoint(p1), parsePoint(p2))
    }

    fun makeSeq(from: Int, to: Int): Iterable<Int> {
        if (from == to) {
            return generateSequence { from }.asIterable()
        }

        return (from.coerceAtMost(to)..from.coerceAtLeast(to)).asIterable()
    }
}