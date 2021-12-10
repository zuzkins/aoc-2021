package puzzle10

import Utils.readResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle10.Navigation.toChars
import puzzle10.Navigation.tryParse

internal class NavigationTest {

    @Test
    fun `can find invalid line`() {
        assertThat("{([(<{}[<>[]}>{[]{[(<()>".tryParse()).isEqualTo(ParserResult.Invalid('}'))
        assertThat("[[<[([]))<([[{}[[()]]]".tryParse()).isEqualTo(ParserResult.Invalid(')'))
        assertThat("[{[{({}]{}}([{[{{{}}([]".tryParse()).isEqualTo(ParserResult.Invalid(']'))
        assertThat("[<(<(<(<{}))><([]([]()".tryParse()).isEqualTo(ParserResult.Invalid(')'))
        assertThat("<{([([[(<>()){}]>(<<{{".tryParse()).isEqualTo(ParserResult.Invalid('>'))
    }

    val exampleInput = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent()

    @Test
    fun `example solution 1`() {
        val invalids = exampleInput.lines().map { it.tryParse() }.filterIsInstance<ParserResult.Invalid>()
        assertThat(invalids.sumOf { it.score }).isEqualTo(26397)
    }

    @Test
    fun `puzzle solution 1`() {
        val invalids = readResource("/puzzle10/input")
            .lines()
            .map { it.tryParse() }
            .filterIsInstance<ParserResult.Invalid>()

        assertThat(invalids.sumOf { it.score }).isEqualTo(193275)
    }

    @Test
    fun `can find missing sequence`() {
        assertThat("[({(<(())[]>[[{[]{<()<>>".tryParse()).isEqualTo(ParserResult.Incomplete("}}]])})]".toChars()))
    }

    @Test
    fun `can complete incomplete lines in example`() {
        val incompletes = exampleInput.lines().map { it.tryParse() }.filterIsInstance<ParserResult.Incomplete>()
        assertThat(incompletes).isEqualTo(
            listOf(
                ParserResult.Incomplete("}}]])})]".toChars()),
                ParserResult.Incomplete(")}>]})".toChars()),
                ParserResult.Incomplete("}}>}>))))".toChars()),
                ParserResult.Incomplete("]]}}]}]}>".toChars()),
                ParserResult.Incomplete("])}>".toChars()),
            )
        )
    }

    @Test
    fun `can score incomplete lines`() {
        assertThat(ParserResult.Incomplete("])}>".toChars()).score).isEqualTo(294)
    }

    @Test
    fun `example solution 2`() {
        val incompletes = exampleInput.lines().map { it.tryParse() }.filterIsInstance<ParserResult.Incomplete>()
        val scores = incompletes.sortedBy { it.score }
        val idx = scores.size / 2
        val middleScore = scores[idx]

        assertThat(middleScore.score).isEqualTo(288957)
    }

    @Test
    fun `puzzle solution 2`() {
        val incompletes = readResource("/puzzle10/input")
            .lines()
            .map { it.tryParse() }
            .filterIsInstance<ParserResult.Incomplete>()
        val scores = incompletes.sortedBy { it.score }
        val idx = scores.size / 2
        val middleScore = scores[idx]

        assertThat(middleScore.score).isEqualTo(2429644557L)
    }
}