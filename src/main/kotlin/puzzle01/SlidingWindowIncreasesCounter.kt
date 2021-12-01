package puzzle01

import puzzle01.SlidingWindowIncreasesCounter.Companion.createSlidingWindows
import puzzle01.SlidingWindowIncreasesCounter.Companion.sums

class SlidingWindowIncreasesCounter {

    companion object {
        fun List<Int>.createSlidingWindows(): List<Triple<Int, Int, Int>> {
            if (this.size < 3) {
                return emptyList()
            }

            val first = this.dropLast(2)
            val second = this.drop(1).dropLast(1)
            val third = this.drop(2)

            check(first.size == second.size && second.size == third.size) {
                "sizes must match: ${first.size}, ${second.size}, ${third.size}"
            }

            return first.mapIndexed { idx, _ ->
                val f = first[idx]
                val s = second[idx]
                val t = third[idx]
                Triple(f, s, t)
            }
        }

        fun List<Triple<Int, Int, Int>>.sums(): List<Int> = this.map { (f, s, t) -> f + s + t }
    }
}

fun main() {
    val sums = puzzleInput.createSlidingWindows().sums()
    println("Puzzle has total of ${IncreasesCounter.countIncreases(sums)} increases")
}