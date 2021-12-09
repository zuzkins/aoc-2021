package puzzle09

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle09.Levels.asLevels
import puzzle09.Levels.computeRiskLevel
import puzzle09.Levels.findLowestLevels

internal class LevelsTest {
    @Test
    fun `single number is a low spot`() {
        assertThat(listOf(listOf(1)).findLowestLevels()).isEqualTo(listOf(1))
    }

    @Test
    fun `can compute risk level`() {
        assertThat("2199943210".asLevels().findLowestLevels().computeRiskLevel()).isEqualTo(3)
    }

    val exampleInput = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent()

    @Test
    fun `example solution 1`() {
        val lowestLevels = exampleInput.asLevels().findLowestLevels()
        assertThat(lowestLevels).isEqualTo(listOf(1, 0, 5, 5))
        assertThat(lowestLevels.computeRiskLevel()).isEqualTo(15)
    }

    @Test
    fun `puzzle solution 1`() {
        val lowestLevels = Utils.readResource("/puzzle09/input").asLevels().findLowestLevels()
        assertThat(lowestLevels.computeRiskLevel()).isEqualTo(452)
    }
}