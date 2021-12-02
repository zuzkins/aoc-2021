package puzzle02

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import puzzle02.NavigationUtils.parseInstructions

internal class NavigationUtilsTest {

    @Test
    fun `empty instructions`() {
        assertThat(emptyList<String>().parseInstructions()).isEmpty()
    }

    @Test
    fun `can parse horizontal instruction`() {
        assertThat(listOf("forward 1").parseInstructions()).isEqualTo(
            listOf(
                NavigationInstruction(
                    horizontal = 1,
                    vertical = 0
                )
            )
        )
    }

    @Test
    fun `can parse vertical instruction - down`() {
        assertThat(listOf("down 1").parseInstructions()).isEqualTo(
            listOf(
                NavigationInstruction(
                    horizontal = 0,
                    vertical = 1
                )
            )
        )
    }

    @Test
    fun `can parse vertical instruction`() {
        assertThat(listOf("up 1").parseInstructions()).isEqualTo(
            listOf(
                NavigationInstruction(
                    horizontal = 0,
                    vertical = -1
                )
            )
        )
    }

    @Test
    fun `fails when instruction cannot be parsed`() {
        assertThatCode {
            listOf("invalid 0").parseInstructions()
        }.hasMessage("Could not parse navigation instruction 'invalid 0'")
    }
}