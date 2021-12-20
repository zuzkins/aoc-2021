package puzzle20

import Utils.intFromBinary

typealias Image = Array<IntArray>
typealias ImageEnhancementMap = IntArray

private val neighbours = listOf(
    -1 to -1,
    0 to -1,
    1 to -1,
    -1 to 0,
    0 to 0,
    1 to 0,
    -1 to 1,
    0 to 1,
    1 to 1
)

object ImageEnhancement {

    fun Image.enhance(map: ImageEnhancementMap, enh: Int = 2, default: Int = 0): Image {
        val originalYSize = size
        val originalXSize = this[0].size
        val enhancedYSize = originalYSize + enh
        val enhancedXSize = originalXSize + enh
        val enhanced = Array(enhancedYSize) {
            IntArray(enhancedXSize) { 0 }
        }
        (0 until enhancedYSize).forEach { y ->
            (0 until enhancedXSize).forEach { x ->
                val mapIdx = this.neighbouringColoursDecimal(x - enh / 2 to y - enh / 2, default)
                val enhancedPixel = map[mapIdx]
                enhanced[y][x] = enhancedPixel
            }
        }
        return enhanced
    }

    fun Image.neighbouringColours(pixel: Pair<Int, Int>, default: Int = 0): List<Int> {
        val neighbours = pixel.neighbouringPixels()
        return neighbours.map { (x, y) ->
            if (x !in this[0].indices) {
                default
            } else if (y !in this.indices) {
                default
            } else {
                this[y][x]
            }
        }
    }

    fun Image.neighbouringColoursDecimal(pixel: Pair<Int, Int>, default: Int = 0): Int {
        return this.neighbouringColours(pixel, default = default).joinToString("").intFromBinary()
    }

    fun parseOriginalImage(image: List<String>): Image {
        val ySize = image.size
        val xSize = image.first().length
        val i = Array(ySize) {
            IntArray(xSize) { 0 }
        }
        image.forEachIndexed { y, row ->
            row.forEachIndexed { x, pixelColor ->
                i[y][x] = parsePixelColor(pixelColor)
            }
        }
        return i
    }

    fun parsePixelColor(pixelColor: Char) = when (pixelColor) {
        '#' -> 1
        '.' -> 0
        else -> error("Invalid pixel color: $pixelColor")
    }

    fun Pair<Int, Int>.neighbouringPixels(): List<Pair<Int, Int>> {
        return neighbours.map { (dx, dy) ->
            first + dx to second + dy
        }
    }

    fun parseEnhancementMap(map: String): ImageEnhancementMap {
        check(map.length == 512)
        return map.map { parsePixelColor(it) }.toIntArray()
    }

    fun Image.print() = joinToString("\n") { row ->
        row.joinToString("") { value ->
            when (value) {
                0 -> "."
                1 -> "#"
                else -> error("Invalid pixel color")
            }
        }
    }

    fun Image.countPixels(): Pair<Int, Int> {
        var dark = 0
        var light = 0
        forEach { row ->
            row.forEach { pixel ->
                when (pixel) {
                    1 -> light++
                    0 -> dark++
                    else -> error("Invalid pixel color: $pixel")
                }
            }
        }
        return dark to light
    }

}