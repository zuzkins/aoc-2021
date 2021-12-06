package puzzle06

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle06.Lanterfish.asPopulation
import puzzle06.Lanterfish.countFish
import puzzle06.Lanterfish.daysOf
import puzzle06.Lanterfish.nextDay

internal class LanterfishTest {

    @Test
    fun `can create initial population`() {
        val population = listOf(3, 4, 3, 1, 2).asPopulation()

        assertThat(population).isEqualTo(
            mapOf(
                0 to 0L,
                1 to 1L,
                2 to 1L,
                3 to 2L,
                4 to 1L,
                5 to 0L,
                6 to 0L,
                7 to 0L,
                8 to 0L,
            )
        )
    }

    @Test
    fun `first generation`() {
        val population = listOf(3, 4, 3, 1, 2).asPopulation().nextDay()

        assertThat(population).isEqualTo(listOf(2, 3, 2, 0, 1).asPopulation())
    }

    @Test
    fun `second generation`() {
        val population = listOf(3, 4, 3, 1, 2).asPopulation().nextDay().nextDay()

        assertThat(population).isEqualTo(listOf(1, 2, 1, 6, 0, 8).asPopulation())
    }

    @Test
    fun `can use shortcut population`() {
        val population = 2.daysOf(listOf(3, 4, 3, 1, 2).asPopulation())
        assertThat(population).isEqualTo(listOf(1, 2, 1, 6, 0, 8).asPopulation())
    }

    val exampleInput = "3,4,3,1,2".split(",").map { it.toInt() }.asPopulation()

    @Test
    fun `example solution`() {
        val population18 = 18.daysOf(exampleInput)
        assertThat(population18.countFish()).isEqualTo(26)

        val population80 = 80.daysOf(exampleInput)
        assertThat(population80.countFish()).isEqualTo(5934)
    }

    @Test
    fun `puzzle solution`() {
        val input = Utils.readResource("/puzzle06/input").split(",").map { it.toInt() }.asPopulation()
        val population = 80.daysOf(input)
        assertThat(population.countFish()).isEqualTo(354564)
    }
}