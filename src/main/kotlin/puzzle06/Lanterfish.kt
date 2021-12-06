package puzzle06

object Lanterfish {

    fun day(population: List<Int>): List<Int> {
        var spawned = 0
        return population.map { age ->
            if (age == 0) {
                spawned++
                6
            } else {
                age - 1
            }
        } + generateSequence { 8 }.take(spawned)
    }


    fun List<Int>.nextDay(): List<Int> = day(this)

    fun Int.daysOf(population: List<Int>): List<Int> {
        return (1..this).fold(population) { p, day ->
            p.nextDay()
        }
    }
}