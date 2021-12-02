package puzzle02

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import puzzle02.Navigation.Companion.move
import puzzle02.Navigation.Companion.moveWithAim
import puzzle02.NavigationUtils.parseInstructions
import java.io.BufferedReader

internal class NavigationWithAimTest {
    @Test
    fun `can move with aim`() {
        assertThat(
            NavigationWithAim()
                .move(NavigationInstruction(5, 0))
                .move(NavigationInstruction(0, 5))
                .move(NavigationInstruction(8, 0))
        ).isEqualTo(NavigationWithAim(13, 40, 5))
    }

    @Test
    fun `can move with example`() {
        val result = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")
            .parseInstructions()
            .moveWithAim()
        assertThat(result).isEqualTo(NavigationWithAim(horizontalPosition = 15, verticalPosition = 60, aim = 10))
        assertThat(result.positionSum).isEqualTo(900)
    }

    @Test
    fun `puzzle solution`() {
        val lines = this::class.java.getResourceAsStream("/puzzle02/input")!!
            .bufferedReader()
            .use(BufferedReader::readText)
            .lines()

        val positionSum = lines.parseInstructions().moveWithAim().positionSum
        assertThat(positionSum).isEqualTo(1813062561)
    }
}