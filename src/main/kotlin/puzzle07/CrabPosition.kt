package puzzle07

import kotlin.math.abs

data class SimpleCrabTechnology(private val crabs: List<Int>) : List<Int> by crabs {
    fun countFuelCostToMoveTo(position: Int) = fold(0L) { totalFuelCost, crab ->
        val fuelCost = abs(position - crab)
        totalFuelCost + fuelCost
    }
}

object CrabPosition {
    fun SimpleCrabTechnology.findBestPosition(): Pair<Int, Long> {
        if (isEmpty()) error("no solution for empty crab crew")

        val max = maxOrNull() ?: 0
        val costs = (0..max).map { pos -> pos to this.countFuelCostToMoveTo(pos) }

        return costs.minByOrNull { it.second } ?: error("no solution found!")
    }
}