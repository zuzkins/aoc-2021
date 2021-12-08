package puzzle08

val originalWiring = mapOf(
    0 to listOf('a', 'b', 'c', 'e', 'f', 'g'),      // 6
    1 to listOf('c', 'f'),                          // 2
    2 to listOf('a', 'c', 'd', 'e', 'g'),           // 5
    3 to listOf('a', 'c', 'd', 'f', 'g'),           // 5
    4 to listOf('b', 'c', 'd', 'f'),                // 4
    5 to listOf('a', 'b', 'd', 'f', 'g'),           // 5
    6 to listOf('a', 'b', 'd', 'e', 'f', 'g'),      // 6
    7 to listOf('a', 'c', 'f'),                     // 3
    8 to listOf('a', 'b', 'c', 'd', 'e', 'f', 'g'), // 7
    9 to listOf('a', 'b', 'c', 'd', 'f', 'g'),      // 6
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

        fun discoverDigits(exampleSignals: List<String>): Map<Int, Set<Char>> {
            val signals = exampleSignals.map { it.toCharArray().toSet() }
            val one = signals.single { it.size == 2 }
            val four = signals.single { it.size == 4 }
            val seven = signals.single { it.size == 3 }
            val eight = signals.single { it.size == 7 }

            // NINE contains FOUR as the only 6-segment digit
            val nine = signals.single { it.size == 6 && it.superseeds(four) }

            // THREE contains SEVEN as the only 5-segment digit
            val three = signals.single { it.size == 5 && it.superseeds(seven) }

            // ZERO contains ONE as the only 6-segment digit
            val zero = signals.single {
                it.size == 6 && it.superseeds(one) && it.isNot(nine)
            }

            // SIX is the last 6-segment digit
            val six = signals.single {
                it.size == 6 && it.isNot(nine) && it.isNot(zero)
            }

            // FIVE is contained in a SIX as the only 5-segment digit
            val five = signals.single {
                it.size == 5 && six.superseeds(it)
            }

            // TWI is the last 5-segment digit
            val two = signals.single {
                it.size == 5 && it.isNot(five) && it.isNot(three)
            }

            return mapOf(
                0 to zero,
                1 to one,
                2 to two,
                3 to three,
                4 to four,
                6 to six,
                5 to five,
                7 to seven,
                8 to eight,
                9 to nine,
            )
        }

        fun String.toSet() = toCharArray().toSet()
        private fun Set<Char>.superseeds(other: Set<Char>) = containsAll(other)
        private fun Set<Char>.isNot(other: Set<Char>) = !containsAll(other)

        fun Map<Int, Set<Char>>.decode(digit: String): Int {
            val chars = digit.toSet()
            return this.asIterable().single { it.value == chars }.key
        }

        fun Map<Int, Set<Char>>.readValue(digits: List<String>) = digits
            .map { this.decode(it) }
            .fold(0) { acc, cur ->
                acc * 10 + cur
            }

        fun String.discoverAndRead(): Int {
            val (signals, digits) = split(" | ")
            val mapping = discoverDigits(signals.split(" "))
            return mapping.readValue(digits.split(" "))
        }
    }
}