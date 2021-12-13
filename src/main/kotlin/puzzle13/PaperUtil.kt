package puzzle13

import kotlin.math.max

typealias Paper = Array<IntArray>

object PaperUtil {

    fun Paper.markDot(x: Int, y: Int): Paper {
        if (y < 0 || y >= size) return this
        if (x < 0 || x >= get(0).size) return this
        this[y][x] += 1
        return this
    }

    fun Paper.foldAlongX(column: Int): Paper {
        val newPaper = copyPaper(size, column)
        indices.forEach { y ->
            (0 until column).forEach { x ->
                newPaper[y][column - 1 - x] += this[y][column + 1 + x]
            }
        }
        return newPaper
    }

    fun Paper.foldAlongY(foldRow: Int): Paper {
        val newDimX = get(0).size
        val newDimY = foldRow
        val newPaper = copyPaper(newDimY, newDimX)

        (0 until foldRow).forEach { offsetY ->
            (0 until newDimX).forEach { x ->
                newPaper[offsetY][x] += this[size - 1 - offsetY][x]
            }
        }
        return newPaper
    }

    private fun Paper.copyPaper(newDimY: Int, newDimX: Int) = Array(newDimY) { idxY ->
        IntArray(newDimX) { idxX ->
            this[idxY][idxX]
        }
    }

    fun Paper.dots(): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        forEachIndexed { y, row ->
            row.forEachIndexed { x, value ->
                if (value > 0) {
                    result += x to y
                }
            }
        }
        return result
    }


    fun Paper.print(): String {
        val lines = map { row ->
            val chars = row.map { point ->
                when (point) {
                    0 -> '.'
                    else -> '#'
                }
            }.toCharArray()
            String(chars)
        }
        return lines.joinToString("\n")
    }

    fun parse(input: String): Paper {
        val dots = input.lines().takeWhile { it.isNotBlank() }.map {
            val (x, y) = it.split(",")
            x.toInt() to y.toInt()
        }

        val (dimX, dimY) = dots.fold(0 to 0) { (x, y), (newX, newY) ->
            max(x, newX) to max(y, newY)
        }
        val paper = Array(dimY + 1) {
            IntArray(dimX + 1) {
                0
            }
        }

        dots.forEach { (x, y) ->
            paper.markDot(x, y)
        }

        return paper
    }
}