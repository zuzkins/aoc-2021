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

    object Incomplete : ParserResult()
    object Correct : ParserResult()
}

object Navigation {

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
            ParserResult.Incomplete
        } else {
            ParserResult.Correct
        }
    }
}