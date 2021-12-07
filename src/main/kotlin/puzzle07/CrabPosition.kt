package puzzle07

import kotlin.math.abs

interface CrabTechnology : List<Int> {
    fun countFuelCostToMoveTo(position: Int): Long
}

data class SimpleCrabTechnology(private val crabs: List<Int>) : List<Int> by crabs, CrabTechnology {
    override fun countFuelCostToMoveTo(position: Int) = fold(0L) { totalFuelCost, crab ->
        val fuelCost = abs(position - crab)
        totalFuelCost + fuelCost
    }
}

data class AdvancedCrabTechnology(private val crabs: List<Int>) : List<Int> by crabs, CrabTechnology {

    override fun countFuelCostToMoveTo(position: Int) = crabs.fold(0L) { totalFuelCost, crab ->
        val distance = abs(position - crab)
        val fuelCost = fuelCosts.get(distance)
        totalFuelCost + fuelCost
    }

    val maxPossibleDistance by lazy {
        crabs.maxOrNull() ?: 0
    }

    val fuelCosts by lazy {
        generateFuelCost().take(maxPossibleDistance + 1).toList()
    }

    companion object {
        fun generateFuelCost(): Sequence<Int> {
            var start = 0
            var lastCost = 0
            return generateSequence {
                val currentCost = lastCost + start
                start += 1
                lastCost = currentCost
                return@generateSequence currentCost
            }
        }
    }
}

object CrabPosition {
    fun CrabTechnology.findBestPosition(): Pair<Int, Long> {
        if (isEmpty()) error("no solution for empty crab crew")

        val max = maxOrNull() ?: 0
        val costs = (0..max).map { pos -> pos to this.countFuelCostToMoveTo(pos) }

        return costs.minByOrNull { it.second } ?: error("no solution found!")
    }
}