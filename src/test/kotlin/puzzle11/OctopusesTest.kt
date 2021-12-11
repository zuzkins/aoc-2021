package puzzle11

import Utils.readResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle11.Octopuses.from
import puzzle11.Octopuses.nextStep
import puzzle11.Octopuses.print

internal class OctopusesTest {

    @Test
    fun `can step without flashing`() {
        assertThat(
            from(
                """
                000
                010
                000
                """.trimIndent()
            ).nextStep().first.print()
        ).isEqualTo(
            """
            111
            121
            111
            """.trimIndent()
        )
    }

    @Test
    fun `can flash without triggering others`() {
        assertThat(
            from(
                """
                123
                495
                670
                """.trimIndent()
            ).nextStep().first.print()
        ).isEqualTo(
            """
            345
            607
            892
            """.trimIndent()
        )
    }

    @Test
    fun `can trigger to flash others`() {
        val initial = from(
            """
            11111
            19991
            19191
            19991
            11111
            """.trimIndent()
        )
        val step1 = initial.nextStep()
        assertThat(step1.first.print()).isEqualTo(
            """
            34543
            40004
            50005
            40004
            34543
            """.trimIndent()
        )
    }

    @Test
    fun `can progress through example`() {
        val initial = from(
            """
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
            """.trimIndent()
        )
        val step1 = initial.nextStep()
        assertThat(step1.first.print()).isEqualTo(
            """
            6594254334
            3856965822
            6375667284
            7252447257
            7468496589
            5278635756
            3287952832
            7993992245
            5957959665
            6394862637
            """.trimIndent()
        )
        val step2 = step1.first.nextStep()
        assertThat(step2.first.print()).isEqualTo(
            """
            8807476555
            5089087054
            8597889608
            8485769600
            8700908800
            6600088989
            6800005943
            0000007456
            9000000876
            8700006848
            """.trimIndent()
        )
        val step3 = step2.first.nextStep()
        assertThat(step3.first.print()).isEqualTo(
            """
            0050900866
            8500800575
            9900000039
            9700000041
            9935080063
            7712300000
            7911250009
            2211130000
            0421125000
            0021119000
            """.trimIndent()
        )
    }

    fun exampleInput() = from(
        """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
        """.trimIndent()
    )

    @Test
    fun `example solution 1`() {
        val (lastStep, flashed) = steps(exampleInput())

        assertThat(lastStep.print()).isEqualTo(
            """
            0397666866
            0749766918
            0053976933
            0004297822
            0004229892
            0053222877
            0532222966
            9322228966
            7922286866
            6789998766
            """.trimIndent()
        )

        assertThat(flashed.size).isEqualTo(1656)
    }

    @Test
    fun `puzzle solution 1`() {
        val initial = from(readResource("/puzzle11/input"))
        val (lastStep, flashed) = steps(initial)
        assertThat(flashed.size).isEqualTo(1642)
    }

    private fun steps(initialLevels: Array<IntArray>) =
        (0 until 100).fold(initialLevels to emptyList<Pair<Int, Int>>()) { acc, _ ->
            val (input, flashed) = acc
            val step = input.nextStep()
            step.first to (flashed + step.second)
        }
}