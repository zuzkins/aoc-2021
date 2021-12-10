package puzzle10

sealed class ParserResult {
    data class Invalid(val offendingChar: Char) : ParserResult() {
        val score
            get() = when (offendingChar) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> error("Invalid offending character '$offendingChar'")
            }
    }

    data class Incomplete(val expecting: List<Char>) : ParserResult() {
        val score = expecting.fold(0L) { s, ch ->
            s * 5 + when (ch) {
                ')' -> 1
                ']' -> 2
                '}' -> 3
                '>' -> 4
                else -> error("Invalid expected character '$ch'")
            }
        }
    }

    object Correct : ParserResult()
}

object Navigation {

    fun String.toChars() = toCharArray().toList()

    fun String.tryParse(): ParserResult {
        var expecting = listOf<Char>()
        toCharArray().iterator().forEach { ch ->
            expecting = when (ch) {
                '(' -> listOf(')') + expecting
                '[' -> listOf(']') + expecting
                '{' -> listOf('}') + expecting
                '<' -> listOf('>') + expecting
                ')', ']', '}', '>' -> {
                    val expected = expecting.first()
                    if (expected != ch) {
                        return ParserResult.Invalid(ch)
                    }
                    expecting.drop(1)
                }
                else -> error("Unexpected character '$ch' in the navigation input")
            }
        }
        return if (expecting.isNotEmpty()) {
            ParserResult.Incomplete(expecting = expecting)
        } else {
            ParserResult.Correct
        }
    }
}