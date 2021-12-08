package puzzle08

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DisplayTest {

    @Test
    fun `can recognize unique numbers`() {
        assertThat(Display.guessDigit("cg")).isEqualTo(1)
        assertThat(Display.guessDigit("gecf")).isEqualTo(4)
        assertThat(Display.guessDigit("cgb")).isEqualTo(7)
        assertThat(Display.guessDigit("egdcabf")).isEqualTo(8)
    }

    val exampleInput = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent()

    private fun guessNumbers(signals: List<String>) = signals.mapNotNull {
        try {
            Display.guessDigit(it)
        } catch (e: IllegalStateException) {
            null
        }
    }

    @Test
    fun `example solution`() {
        val signals = exampleInput.lines().map { it.split(" | ")[1] }.flatMap { it.split(" ") }
        val guessedNumbers = guessNumbers(signals)

        assertThat(guessedNumbers.size).isEqualTo(26)
    }

    @Test
    fun `puzzle 1 solution`() {
        val signals = Utils.readResource("/puzzle08/input").lines().map { it.split(" | ")[1] }.flatMap { it.split(" ") }
        assertThat(guessNumbers(signals).size).isEqualTo(255)
    }
}