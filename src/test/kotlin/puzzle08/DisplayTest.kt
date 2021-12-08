package puzzle08

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle08.Display.Companion.decode
import puzzle08.Display.Companion.discoverAndRead
import puzzle08.Display.Companion.readValue
import puzzle08.Display.Companion.toSet

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

    val exampleSignals = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab".split(" ")

    @Test
    fun `can discover basic numbers`() {
        val digits = Display.discoverDigits(exampleSignals)

        assertThat(digits[1]).isEqualTo("ab".toSet())
        assertThat(digits[4]).isEqualTo("eafb".toSet())
        assertThat(digits[7]).isEqualTo("dab".toSet())
        assertThat(digits[8]).isEqualTo("acedgfb".toSet())
        assertThat(digits[9]).isEqualTo("cefabd".toSet())
        assertThat(digits[3]).isEqualTo("fbcad".toSet())
        assertThat(digits[0]).isEqualTo("cagedb".toSet())
        assertThat(digits[6]).isEqualTo("cdfgeb".toSet())
        assertThat(digits[5]).isEqualTo("cdfbe".toSet())
        assertThat(digits[2]).isEqualTo("gcdfa".toSet())
    }

    @Test
    fun `can decode discovered digits`() {
        val digits = Display.discoverDigits(exampleSignals)

        assertThat(digits.decode("cdfeb")).isEqualTo(5)
        assertThat(digits.decode("fcadb")).isEqualTo(3)
    }

    @Test
    fun `can calculate value from display`() {
        val digits = Display.discoverDigits(exampleSignals)
        assertThat(digits.readValue(listOf("cdfeb", "fcadb", "cdfeb", "cdbaf"))).isEqualTo(5353)
    }

    @Test
    fun `can discover and read digits from display`() {
        assertThat("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf".discoverAndRead()).isEqualTo(
            5353
        )
    }

    @Test
    fun `example solution 2`() {
        assertThat(exampleInput.lines().sumOf { it.discoverAndRead() }).isEqualTo(61229)
    }

    @Test
    fun `puzzle solution 2`() {
        assertThat(Utils.readResource("/puzzle08/input").lines().sumOf { it.discoverAndRead() }).isEqualTo(982158)
    }
}