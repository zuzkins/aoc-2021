package puzzle18

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle18.SnailMathUtil.findNextReduceOp
import puzzle18.SnailMathUtil.findNumbersToExplode
import puzzle18.SnailMathUtil.findNumbersToSplit
import puzzle18.SnailMathUtil.parseSnailNumber

internal class SnailNumberTest {
    fun testNumber() = SnailNumber.Node(
        right = SnailNumber.Number(4),
        left = SnailNumber.Node(
            right = SnailNumber.Number(3),
            left = SnailNumber.Node(
                right = SnailNumber.Number(2),
                left = SnailNumber.Node(
                    right = SnailNumber.Number(1),
                    left = SnailNumber.Node(
                        left = SnailNumber.Number(9),
                        right = SnailNumber.Number(8)
                    )
                )
            )
        )
    )

    @Test
    fun `can parse number`() {
        assertThat("[[[[[9,8],1],2],3],4]".parseSnailNumber()).isEqualTo(testNumber())
        assertThat("[7,[6,[5,[4,[3,2]]]]]".parseSnailNumber().print()).isEqualTo("[7,[6,[5,[4,[3,2]]]]]")
        assertThat("[[6,[5,[4,[3,2]]]],1]".parseSnailNumber().print()).isEqualTo("[[6,[5,[4,[3,2]]]],1]")
        assertThat(
            "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]".parseSnailNumber().print()
        ).isEqualTo("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")
        assertThat(
            "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]".parseSnailNumber().print()
        ).isEqualTo("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    }

    @Test
    fun `can render number`() {
        assertThat(testNumber().print()).isEqualTo("[[[[[9,8],1],2],3],4]")
    }

    @Test
    fun `can find numbers to explode`() {
        assertThat(testNumber().findNumbersToExplode()).isEqualTo(
            listOf(
                SnailNumberToExplode(
                    parent = SnailNumber.Node(
                        right = SnailNumber.Number(1),
                        left = SnailNumber.Node(
                            left = SnailNumber.Number(9),
                            right = SnailNumber.Number(8)
                        )
                    ),
                    number = SnailNumber.Node(
                        left = SnailNumber.Number(9),
                        right = SnailNumber.Number(8)
                    )
                )
            )
        )
    }

    @Test
    fun `can explode number`() {
        val num = testNumber()
        val explodingNumber = num.findNumbersToExplode().first()
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[[[[0,9],2],3],4]")
    }

    @Test
    fun `can explode number 2`() {
        val num = "[7,[6,[5,[4,[3,2]]]]]".parseSnailNumber()
        val explodingNumber = num.findNumbersToExplode().first()
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[7,[6,[5,[7,0]]]]")
    }

    @Test
    fun `can explode number 3`() {
        val num = "[[6,[5,[4,[3,2]]]],1]".parseSnailNumber()
        val explodingNumber = num.findNumbersToExplode().first()
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[[6,[5,[7,0]]],3]")
    }

    @Test
    fun `can explode number 4`() {
        val num = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]".parseSnailNumber()
        val explodingNumber = num.findNumbersToExplode().first()
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    }

    @Test
    fun `can explode number 5`() {
        val num = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]".parseSnailNumber()
        val explodingNumber = num.findNumbersToExplode().first()
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
    }

    @Test
    fun `can explode example 1`() {
        val input = "[[[[0,7],4],[15,[0,13]]],[1,1]]".parseSnailNumber()
        val nums = input.findNumbersToSplit()
        assertThat(nums).hasSize(2)
        nums.first().reduceStep(input)
        assertThat(input.print()).isEqualTo("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]")
        input.findNumbersToSplit().first().reduceStep(input)
        assertThat(input.print()).isEqualTo("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]")
    }

    @Test
    fun `can add numbers`() {
        val nums = """
            [1,1]
            [2,2]
            [3,3]
            [4,4]
            """.trimIndent().lines().map { it.parseSnailNumber() }

        val f = nums.first()
        val rest = nums.drop(1)
        val result = rest.fold(f) { acc, num ->
            acc + num
        }

        assertThat(result.print()).isEqualTo("[[[[1,1],[2,2]],[3,3]],[4,4]]")
    }

    fun exampleInput2() = """
        [1,1]
        [2,2]
        [3,3]
        [4,4]
        [5,5]
        """.trimIndent().lines().map { it.parseSnailNumber() }

    @Test
    fun `can add numbers 2`() {
        val (first, second, third, fourth, fifth) = exampleInput2()
        val res1 = first + second
        assertThat(res1.print()).isEqualTo("[[1,1],[2,2]]")
        val res2 = res1 + third
        assertThat(res2.print()).isEqualTo("[[[1,1],[2,2]],[3,3]]")
        val res3 = res2 + fourth
        assertThat(res3.print()).isEqualTo("[[[[1,1],[2,2]],[3,3]],[4,4]]")
        val res4 = res3 + fifth
        res4.reduce()
        assertThat(res4.print()).isEqualTo("[[[[3,0],[5,3]],[4,4]],[5,5]]")
    }

    fun exampleInput3() = """
        [1,1]
        [2,2]
        [3,3]
        [4,4]
        [5,5]
        [6,6]
        """.trimIndent().lines().map { it.parseSnailNumber() }

    @Test
    fun `can add numbers 3`() {
        val nums = exampleInput3()
        val result = nums.drop(1).fold(nums.first()) { acc, n ->
            val res = acc + n
            res.reduce()
            res
        }

        assertThat(result.print()).isEqualTo("[[[[5,0],[7,4]],[5,5]],[6,6]]")
    }

    fun exampleInputLarge() = """
        [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
        [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
        [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
        [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
        [7,[5,[[3,8],[1,4]]]]
        [[2,[2,2]],[8,[8,1]]]
        [2,9]
        [1,[[[9,3],9],[[9,0],[0,7]]]]
        [[[5,[7,4]],7],1]
        [[[[4,2],2],6],[8,7]]
        """.trimIndent().lines().map { it.parseSnailNumber() }

    @Test
    fun `can add numbers - large`() {
        val nums = exampleInputLarge()
        val start = nums.first()
        val result = nums.drop(1).fold(start) { acc, n ->
            print("\t${acc.print()} ")
            acc.reduce()
            println(" (${acc.print()})")
            print("+\t${n.print()}")
            n.reduce()
            println(" (${n.print()})")
            val res = acc + n
            res.reduce()
            println("=\t${res.print()}")
            println()
            res
        }

        assertThat(result.print()).isEqualTo("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
    }

    fun SnailNumber.Node.reduce(): SnailNumber.Node {
        var op = findNextReduceOp()
        while (op != null) {
            op.reduceStep(this)
            op = findNextReduceOp()
        }
        return this
    }
}