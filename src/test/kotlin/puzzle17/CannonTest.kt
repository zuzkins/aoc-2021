package puzzle17

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle05.Point
import puzzle17.Cannon.dragX
import puzzle17.Cannon.gravity
import puzzle17.Cannon.highestShot
import puzzle17.Cannon.shootWith
import puzzle17.Cannon.showerThemWithNukes

internal class CannonTest {

    val exampleTarget = Target(20..30, -10..-5)
    val puzzleTarget = Target(201..230, -99..-65)

    @Test
    fun `can compute drag on X`() {
        var x = 8
        val drags = generateSequence {
            val next = x.dragX()
            x = next
            next
        }

        assertThat(drags.take(9).toList()).isEqualTo(listOf(7, 6, 5, 4, 3, 2, 1, 0, 0))
    }

    @Test
    fun `can compute gravity on Y`() {
        var y = 5
        val drags = generateSequence {
            val next = y.gravity()
            y = next
            next
        }
        assertThat(drags.take(9).toList()).isEqualTo(listOf(4, 3, 2, 1, 0, -1, -2, -3, -4))
    }

    @Test
    fun `can hit target`() {
        assertThat(exampleTarget.hit(1, 2)).isFalse
        assertThat(exampleTarget.hit(19, -11)).isFalse
        assertThat(exampleTarget.hit(31, -4)).isFalse
        assertThat(exampleTarget.hit(20, -4)).isFalse
        assertThat(exampleTarget.hit(30, -11)).isFalse

        assertThat(exampleTarget.hit(25, -7)).isTrue
        assertThat(exampleTarget.hit(20, -10)).isTrue
        assertThat(exampleTarget.hit(30, -5)).isTrue
    }

    @Test
    fun `can compute example 1`() {
        assertThat(exampleTarget.shootWith(7, 2)).isEqualTo(
            listOf(
                0 + 7 to 2,
                7 + 6 to 3,
                13 + 5 to 3,
                18 + 4 to 2,
                22 + 3 to 0,
                25 + 2 to -3,
                27 + 1 to -3 - 4
            )
        )
    }

    @Test
    fun `can compute example 2`() {
        assertThat(exampleTarget.shootWith(6, 3)).isEqualTo(
            listOf(
                6 to 3,
                6 + 5 to 3 + 2,
                11 + 4 to 5 + 1,
                15 + 3 to 6 + 0,
                18 + 2 to 6 - 1,
                20 + 1 to 5 - 2,
                21 + 0 to 3 - 3,
                21 + 0 to 0 - 4,
                21 + 0 to -4 - 5,
            )
        )
    }

    @Test
    fun `verify example 3`() {
        assertThat(exampleTarget.shootWith(9, 0)).isNotEmpty
    }

    @Test
    fun `can overshoot target`() {
        assertThat(exampleTarget.shootWith(17, -4)).isEmpty()
    }

    @Test
    fun `find initial velocities`() {
        val shots = exampleTarget.showerThemWithNukes()
        assertThat(shots).isNotEmpty
        val initialVelocities = shots.initialVelocities()
        assertThat(initialVelocities).containsAll(
            listOf(
                7 to 2,
                6 to 3,
                9 to 0
            )
        )
    }

    @Test
    fun `can find shot with highest elevation`() {
        val shots = exampleTarget.showerThemWithNukes()
        val shot = shots.highestShot()
        assertThat(shot.first()).isEqualTo(6 to 9)
        assertThat(shot.maxOf { it.second }).isEqualTo(45)
        assertThat(shots).hasSize(112)
    }

    @Test
    fun `puzzle solution 1`() {
        val shots = puzzleTarget.showerThemWithNukes()
        val shot = shots.highestShot()
        assertThat(shot.maxOf { it.second }).isEqualTo(4851)
        assertThat(shots).hasSize(1739)
    }


    fun List<List<Point>>.initialVelocities() = map { it.first() }
}