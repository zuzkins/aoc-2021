package puzzle02

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import puzzle02.Navigation.Companion.move
import puzzle02.NavigationUtils.parseInstructions
import java.io.BufferedReader

internal class NavigationTest {
    val start = Navigation()

    @Test
    fun `can move according to horizontal instruction`() {
        assertThat(start.move(NavigationInstruction(horizontal = 1, vertical = 0))).isEqualTo(
            Navigation(
                horizontalPosition = 1,
                verticalPosition = 0
            )
        )
    }

    @Test
    fun `can move according to horizontal instructions`() {
        assertThat(
            start
                .move(NavigationInstruction(horizontal = 1, vertical = 0))
                .move(NavigationInstruction(horizontal = 10, vertical = 0))
        ).isEqualTo(
            Navigation(
                horizontalPosition = 11,
                verticalPosition = 0
            )
        )
    }

    @Test
    fun `can move according to vertical instruction`() {
        assertThat(start.move(NavigationInstruction(horizontal = 0, vertical = 2))).isEqualTo(
            Navigation(
                horizontalPosition = 0,
                verticalPosition = 2
            )
        )
    }

    @Test
    fun `can move according to vertical instructions`() {
        assertThat(
            start
                .move(NavigationInstruction(horizontal = 0, vertical = 2))
                .move(NavigationInstruction(horizontal = 0, vertical = 20))
        ).isEqualTo(
            Navigation(
                horizontalPosition = 0,
                verticalPosition = 22
            )
        )
    }

    @Test
    fun `can move according to instruction`() {
        assertThat(start.move(NavigationInstruction(horizontal = 1, vertical = 2))).isEqualTo(
            Navigation(
                horizontalPosition = 1,
                verticalPosition = 2
            )
        )
    }

    @Test
    fun `can move according to instructions`() {
        assertThat(
            start
                .move(NavigationInstruction(horizontal = 1, vertical = 2))
                .move(NavigationInstruction(horizontal = 10, vertical = 20))
        ).isEqualTo(
            Navigation(
                horizontalPosition = 11,
                verticalPosition = 22
            )
        )
    }

    @Test
    fun `can move according to example`() {
        val navigation = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")
            .parseInstructions()
            .move()

        assertThat(navigation).isEqualTo(Navigation(15, 10))
        assertThat(navigation.positionSum).isEqualTo(150)
    }

    @Test
    fun `puzzle solution`() {
        val lines = this::class.java.getResourceAsStream("/puzzle02/input")!!
            .bufferedReader()
            .use(BufferedReader::readText)
            .lines()

        val positionSum = lines.parseInstructions().move().positionSum
        assertThat(positionSum).isEqualTo(1947824)
    }
}