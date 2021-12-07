package puzzle07

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle07.AdvancedCrabTechnology.Companion.generateFuelCost
import puzzle07.CrabPosition.findBestPosition

internal class CrabPositionTest {
    @Test
    fun `can continue fuel cost for no crabs`() {
        assertThat(SimpleCrabTechnology(emptyList()).countFuelCostToMoveTo(123)).isEqualTo(0)
    }

    @Test
    fun `can continue fuel cost for single crab - move back`() {
        assertThat(SimpleCrabTechnology(listOf(10)).countFuelCostToMoveTo(5)).isEqualTo(5)
    }

    @Test
    fun `can continue fuel cost for single crab - move forward`() {
        assertThat(SimpleCrabTechnology(listOf(10)).countFuelCostToMoveTo(15)).isEqualTo(5)
    }

    @Test
    fun `can continue fuel cost for swarm of crabs`() {
        assertThat(
            SimpleCrabTechnology(
                listOf(
                    5,
                    10,
                    15,
                    20,
                    25
                )
            ).countFuelCostToMoveTo(20)
        ).isEqualTo(15 + 10 + 5 + 0 + 5)
    }

    @Test
    fun `can find best position for single crab`() {
        assertThat(SimpleCrabTechnology(listOf(10)).findBestPosition()).isEqualTo(10 to 0L)
    }

    @Test
    fun `can find best position for two crabs`() {
        assertThat(SimpleCrabTechnology(listOf(8, 10)).findBestPosition()).isEqualTo(8 to 2L)
    }

    val exampleCrabs = listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14)

    @Test
    fun `example 1 solution`() {
        assertThat(SimpleCrabTechnology(exampleCrabs).findBestPosition()).isEqualTo(2 to 37L)
    }

    fun puzzleInput() = Utils.readResource("/puzzle07/input").split(",").map { it.toInt() }

    @Test
    fun `puzzle 1 solution`() {
        assertThat(SimpleCrabTechnology(puzzleInput()).findBestPosition()).isEqualTo(367 to 356179L)
    }

    @Test
    fun `can generate advanced fuel cost`() {
        assertThat(generateFuelCost().take(4).toList()).isEqualTo(listOf(0, 1, 3, 6))
    }

    @Test
    fun `example 2 solution`() {
        assertThat(AdvancedCrabTechnology(exampleCrabs).findBestPosition()).isEqualTo(5 to 168L)
    }

    @Test
    fun `puzzle 2 solution`() {
        assertThat(AdvancedCrabTechnology(puzzleInput()).findBestPosition()).isEqualTo(489 to 99788435L)
    }
}