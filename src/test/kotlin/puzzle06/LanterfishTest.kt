package puzzle06

import Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle06.Lanterfish.daysOf
import puzzle06.Lanterfish.nextDay

internal class LanterfishTest {

    @Test
    fun `first generation`() {
        val population = listOf(3, 4, 3, 1, 2).nextDay()

        assertThat(population).isEqualTo(listOf(2, 3, 2, 0, 1))
    }

    @Test
    fun `second generation`() {
        val population = listOf(3, 4, 3, 1, 2).nextDay().nextDay()

        assertThat(population).isEqualTo(listOf(1, 2, 1, 6, 0, 8))
    }

    val exampleInput = "3,4,3,1,2".split(",").map { it.toInt() }

    @Test
    fun `example solution`() {
        val population18 = 18.daysOf(exampleInput)
        assertThat(population18.size).isEqualTo(26)

        val population80 = 80.daysOf(exampleInput)
        assertThat(population80.size).isEqualTo(5934)
    }

    @Test
    fun `puzzle solution`() {
        val input = Utils.readResource("/puzzle06/input").split(",").map { it.toInt() }
        val population = 80.daysOf(input)
        assertThat(population.size).isEqualTo(354564)
    }
}