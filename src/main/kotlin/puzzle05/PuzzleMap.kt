package puzzle05

import puzzle05.MapUtil.makeSeq

typealias Point = Pair<Int, Int>

data class DirectLine(val from: Point, val to: Point) {
    val points: List<Point> by lazy {
        if (from.first != to.first && from.second != to.second) {
            return@lazy emptyList<Point>()
        }

        val xs = makeSeq(from.first, to.first)
        val ys = makeSeq(from.second, to.second)
        xs.zip(ys).map { (x, y) -> Point(x, y) }
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

    fun markLines(lines: List<DirectLine>): PuzzleMap {
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
            return PuzzleMap.create(max)
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