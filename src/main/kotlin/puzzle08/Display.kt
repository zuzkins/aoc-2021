package puzzle08

val originalWiring = mapOf(
    0 to listOf('a', 'b', 'c', 'e', 'f', 'g'),
    1 to listOf('c', 'f'),
    2 to listOf('a', 'c', 'd', 'e', 'g'),
    3 to listOf('a', 'c', 'd', 'f', 'g'),
    4 to listOf('b', 'c', 'd', 'f'),
    5 to listOf('a', 'b', 'd', 'f', 'g'),
    6 to listOf('a', 'b', 'd', 'e', 'f', 'g'),
    7 to listOf('a', 'c', 'f'),
    8 to listOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
    9 to listOf('a', 'b', 'c', 'd', 'f', 'g'),
)

val uniqueDigits = setOf(1, 4, 7, 8)

class Display {

    companion object {
        fun guessDigit(signals: String): Int {
            val requiredLength = signals.length

            return uniqueDigits.singleOrNull { digit ->
                requiredLength == originalWiring.getValue(digit).size
            } ?: error("Could not guess digit from given signals ($signals) by wire count $requiredLength")
        }
    }
}