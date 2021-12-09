package puzzle09

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle09.Levels.asLevels
import puzzle09.Levels.computeRiskLevel
import puzzle09.Levels.countScore
import puzzle09.Levels.findAllBasins
import puzzle09.Levels.findBasin
import puzzle09.Levels.findLowestLevels

internal class LevelsTest {
    @Test
    fun `single number is a low spot`() {
        assertThat(listOf(listOf(1)).findLowestLevels()).isEqualTo(listOf(Spot(x = 0, y = 0, height = 1)))
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
        assertThat(lowestLevels).isEqualTo(
            listOf(
                Spot(1, 0, 1),
                Spot(9, 0, 0),
                Spot(2, 2, 5),
                Spot(6, 4, 5)
            )
        )
        assertThat(lowestLevels.computeRiskLevel()).isEqualTo(15)
    }

    @Test
    fun `puzzle solution 1`() {
        val lowestLevels = Utils.readResource("/puzzle09/input").asLevels().findLowestLevels()
        assertThat(lowestLevels.computeRiskLevel()).isEqualTo(452)
    }

    @Test
    fun `can find single basin`() {
        val levels = exampleInput.asLevels()
        val basin = levels.findBasin(Spot(1, 0, 1))
        assertThat(basin).isEqualTo(
            listOf(
                Spot(1, 0, 1),
                Spot(0, 0, 2),
                Spot(0, 1, 3)
            )
        )
    }

    @Test
    fun `can find other basin`() {
        val levels = exampleInput.asLevels()
        val basin = levels.findBasin(Spot(9, 0, 0))
        assertThat(basin.map { it.height }).isEqualTo(listOf(0, 1, 2, 3, 4, 4, 2, 1, 2))
    }

    @Test
    fun `can find all basins`() {
        val levels = exampleInput.asLevels()
        assertThat(levels.findAllBasins().map { it.map { it.height } }).isEqualTo(
            listOf(
                listOf(1, 2, 3),
                listOf(0, 1, 2, 3, 4, 4, 2, 1, 2),
                listOf(5, 8, 6, 7, 8, 8, 8, 7, 8, 7, 6, 7, 8, 8),
                listOf(5, 6, 6, 7, 8, 8, 7, 8, 6),
            )
        )
    }

    @Test
    fun `example solution 2`() {
        val levels = exampleInput.asLevels()
        assertThat(
            levels.findAllBasins().map { it }.sortedByDescending { it.size }.take(3).countScore()
        ).isEqualTo(1134)
    }

    @Test
    fun `puzzle solution 2`() {
        val levels = Utils.readResource("/puzzle09/input").asLevels()
        assertThat(
            levels.findAllBasins().map { it }.sortedByDescending { it.size }.take(3).countScore()
        ).isEqualTo(1263735)
    }
}