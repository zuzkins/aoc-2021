package puzzle10

import Utils.readResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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
}