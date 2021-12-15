package puzzle14

import Utils.readResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle14.Polymer.polymerize
import puzzle14.Polymer.polymerizeSmart

internal class PolymerTest {
    @Test
    fun `can parse instructions`() {
        assertThat(Polymer.parseInstructions(listOf("AB -> C"))).isEqualTo(
            mapOf(('A' to 'B') to 'C')
        )
    }

    val exampleInstructions = """
        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
        """.trimIndent().lines().let { Polymer.parseInstructions(it) }

    @Test
    fun `can do simple polymerization`() {
        assertThat(exampleInstructions.polymerize("NNCB")).isEqualTo("NCNBCHB")
    }

    @Test
    fun `can step through polymerization`() {
        assertThat(exampleInstructions.polymerize("NCNBCHB")).isEqualTo("NBCCNBBBCBHCB")
        assertThat(exampleInstructions.polymerize("NCNBCHB")).isEqualTo("NBCCNBBBCBHCB")
        assertThat(exampleInstructions.polymerize("NBBBCNCCNBBNBNBBCHBHHBCHB")).isEqualTo("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB")

        assertThat(exampleInstructions.polymerize(template = "NNCB", steps = 4)).isEqualTo("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB")
    }

    @Test
    fun `example solution1`() {
        val result = exampleInstructions.polymerize("NNCB", 10) //NCNBCHB
        val score = Polymer.polymerScore(result)
        assertThat(score).isEqualTo(1588)
    }

    @Test
    fun `example solution 2`() {
        val pairs = parseTemplate("NNCB")
        var step = pairs
        (0 until 40).forEach {
            step = exampleInstructions.polymerizeSmart(step)
        }
        val score = Polymer.polymerScore(step, 'N', 'B')
        assertThat(score).isEqualTo(2188189693529L)
    }

    fun puzzleInput() = readResource("/puzzle14/input")
    fun puzzleTemplate() = puzzleInput().lines().first()

    fun puzzleInstructions() = puzzleInput().lines().drop(2).dropLast(1).let {Polymer.parseInstructions(it)}

    @Test
    fun `puzzle solution 1`() {
        val result = puzzleInstructions().polymerize(puzzleTemplate(), 10)
        assertThat(Polymer.polymerScore(result)).isEqualTo(3259)
    }

    @Test
    fun `puzzle solution 2 - smart`() {
        val tmpl = puzzleTemplate()
        val pairs = parseTemplate(tmpl)
        var step = pairs
        (0 until 40).forEach {
            step = puzzleInstructions().polymerizeSmart(step)
        }
        val score = Polymer.polymerScore(step, tmpl.first(), tmpl.last())
        assertThat(score).isEqualTo(3_459_174_981_021L)
    }

    private fun parseTemplate(template: String) = template.toCharArray().toList().windowed(2, 1).map { it.first() to it.drop(1).first() }.associateWith { 1L }.toMutableMap()
}
