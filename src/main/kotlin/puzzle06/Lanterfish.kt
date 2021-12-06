package puzzle06

object Lanterfish {

    fun day(population: Map<Int, Long>): Map<Int, Long> {
        val newPopulation = prepareEmptyPopulation()

        val breeders = population.getOrDefault(0, 0)
        (1..8).forEach { stage ->
            newPopulation[stage - 1] = population.getOrDefault(stage, 0)
        }
        newPopulation[8] = breeders
        newPopulation[6] = newPopulation.getValue(6) + breeders
        return newPopulation
    }

    private fun prepareEmptyPopulation() = (0..8).associateWith { 0L }.toMutableMap()

    fun List<Int>.asPopulation(): Map<Int, Long> = this.fold(prepareEmptyPopulation()) { pop, stage ->
        pop[stage] = pop.getOrDefault(stage, 0L) + 1L
        pop
    }

    fun Map<Int, Long>.nextDay(): Map<Int, Long> = day(this)

    fun Int.daysOf(population: Map<Int, Long>): Map<Int, Long> {
        return (1..this).fold(population) { p, day ->
            p.nextDay()
        }
    }

    fun Map<Int, Long>.countFish(): Long = values.sum()
}