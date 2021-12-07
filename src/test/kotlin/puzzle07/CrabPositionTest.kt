package puzzle07

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle07.CrabPosition.countFuelCostToMoveTo
import puzzle07.CrabPosition.findBestPosition

internal class CrabPositionTest {
    @Test
    fun `can continue fuel cost for no crabs`() {
        assertThat(emptyList<Int>().countFuelCostToMoveTo(123)).isEqualTo(0)
    }

    @Test
    fun `can continue fuel cost for single crab - move back`() {
        assertThat(listOf(10).countFuelCostToMoveTo(5)).isEqualTo(5)
    }

    @Test
    fun `can continue fuel cost for single crab - move forward`() {
        assertThat(listOf(10).countFuelCostToMoveTo(15)).isEqualTo(5)
    }

    @Test
    fun `can continue fuel cost for swarm of crabs`() {
        assertThat(listOf(5, 10, 15, 20, 25).countFuelCostToMoveTo(20)).isEqualTo(15 + 10 + 5 + 0 + 5)
    }

    @Test
    fun `can find best position for single crab`() {
        assertThat(listOf(10).findBestPosition()).isEqualTo(10 to 0L)
    }

    @Test
    fun `can find best position for two crabs`() {
        assertThat(listOf(8, 10).findBestPosition()).isEqualTo(8 to 2L)
    }

    val exampleCrabs = listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14)

    @Test
    fun `example 1 solution`() {
        assertThat(exampleCrabs.findBestPosition()).isEqualTo(2 to 37L)
    }

    fun puzzleInput() = Utils.readResource("/puzzle07/input").split(",").map { it.toInt() }

    @Test
    fun `puzzle 1 solution`() {
        assertThat(puzzleInput().findBestPosition()).isEqualTo(367 to 356179L)
    }
}