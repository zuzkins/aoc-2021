package puzzle18

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import puzzle18.SnailMathUtil.findNextReduceOperation
import puzzle18.SnailMathUtil.findNumbersToExplode
import puzzle18.SnailMathUtil.findNumbersToSplit
import puzzle18.SnailMathUtil.parseSnailNumber
import puzzle18.SnailMathUtil.reduce
import puzzle18.SnailMathUtil.snailSum
import java.util.*

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
        val explodingNumber = num.findNextReduceOperation()!!
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[[[[0,9],2],3],4]")
    }

    @Test
    fun `can explode number 2`() {
        val num = "[7,[6,[5,[4,[3,2]]]]]".parseSnailNumber()
        val explodingNumber = num.findNextReduceOperation()!!
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[7,[6,[5,[7,0]]]]")
    }

    @Test
    fun `can explode number 3`() {
        val num = "[[6,[5,[4,[3,2]]]],1]".parseSnailNumber()
        val explodingNumber = num.findNextReduceOperation()!!
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[[6,[5,[7,0]]],3]")
    }

    @Test
    fun `can explode number 4`() {
        val num = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]".parseSnailNumber()
        val explodingNumber = num.findNextReduceOperation()!!
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    }

    @Test
    fun `can explode number 5`() {
        val num = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]".parseSnailNumber()
        val explodingNumber = num.findNextReduceOperation()!!
        explodingNumber.reduceStep(num)
        assertThat(num.print()).isEqualTo("[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
    }

    @Test
    fun `can explode example 1`() {
        val input = "[[[[0,7],4],[15,[0,13]]],[1,1]]".parseSnailNumber()
        input.findNextReduceOperation()!!.reduceStep(input)
        assertThat(input.print()).isEqualTo("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]")
        input.findNextReduceOperation()!!.reduceStep(input)
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

    @Test
    fun `can add numbers 4`() {
        val n1 = "[[[[4,3],4],4],[7,[[8,4],9]]]".parseSnailNumber()
        val n2 = "[1,1]".parseSnailNumber()
        val res = n1 + n2
        res.reduce()
        assertThat(res.print()).isEqualTo("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
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
    @Disabled
    fun `find out priority of operations`() {
        val n = "[[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]".parseSnailNumber()
        n.findNumbersToExplode().first().reduceStep(n)
        println("01-E" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("02-E" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("03-E" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("04-E" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("05-E" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("06-E" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("07-E" + n.print())
        n.findNumbersToSplit().first().reduceStep(n)
        println("08-S" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("09-E" + n.print())
        n.findNumbersToSplit().first().reduceStep(n)
        println("10-S" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("11-E" + n.print())
        n.findNumbersToSplit().first().reduceStep(n)
        println("12-S" + n.print())
        n.findNumbersToSplit().first().reduceStep(n)
        println("13-S" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("14-E" + n.print())
        n.findNumbersToSplit().first().reduceStep(n)
        println("15-S" + n.print())
        n.findNumbersToSplit().first().reduceStep(n)
        println("16-S" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("17-E" + n.print())
        n.findNumbersToSplit().first().reduceStep(n)
        println("18-S" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("19-E" + n.print())
        n.findNumbersToSplit().first().reduceStep(n)
        println("20-S" + n.print())
        n.findNumbersToExplode().first().reduceStep(n)
        println("21-E" + n.print())
    }

    @Test
    fun `can produce magnitude`() {
        assertThat("[9,1]".parseSnailNumber().magnitude).isEqualTo(29)
        assertThat("[[9,1],[1,9]]".parseSnailNumber().magnitude).isEqualTo(129)
        assertThat("[[1,2],[[3,4],5]]".parseSnailNumber().magnitude).isEqualTo(143)
        assertThat("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]".parseSnailNumber().magnitude).isEqualTo(1384)
        assertThat("[[[[1,1],[2,2]],[3,3]],[4,4]]".parseSnailNumber().magnitude).isEqualTo(445)
        assertThat("[[[[3,0],[5,3]],[4,4]],[5,5]]".parseSnailNumber().magnitude).isEqualTo(791)
        assertThat("[[[[5,0],[7,4]],[5,5]],[6,6]]".parseSnailNumber().magnitude).isEqualTo(1137)
        assertThat("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]".parseSnailNumber().magnitude).isEqualTo(3488)
    }

    @Test
    fun `can add numbers - large`() {
        val nums = exampleInputLarge()
        val result = nums.snailSum()

        assertThat(result.print()).isEqualTo("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
    }

    @Test
    fun `example solution 1`() {
        val nums = """
        [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
        [[[5,[2,8]],4],[5,[[9,9],0]]]
        [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
        [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
        [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
        [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
        [[[[5,4],[7,7]],8],[[8,3],8]]
        [[9,3],[[9,9],[6,[4,9]]]]
        [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
        [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
        """.trimIndent().lines().map { it.parseSnailNumber() }
        val res = nums.snailSum()
        assertThat(res.print()).isEqualTo("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]")
        assertThat(res.magnitude).isEqualTo(4140L)
    }

    fun puzzleInput() = """
        [2,[[7,4],[5,[3,9]]]]
        [[[[1,4],[0,1]],4],[3,[8,5]]]
        [[0,[2,8]],[[[3,7],0],[7,[3,1]]]]
        [[8,[5,[2,3]]],[[3,[9,2]],[[3,8],[0,2]]]]
        [[[1,[3,6]],6],[[[8,7],[2,2]],[7,6]]]
        [6,[[[5,7],6],[[7,8],2]]]
        [[1,3],[[[0,1],[9,4]],1]]
        [[[[0,6],5],7],[7,[[0,7],[9,6]]]]
        [[[7,7],4],[[5,7],[[2,3],[4,4]]]]
        [[5,[0,[7,3]]],[7,[[6,1],4]]]
        [[[8,9],[3,[5,3]]],[[2,8],[[0,8],6]]]
        [[[[6,4],[2,5]],[[2,1],[3,3]]],[3,6]]
        [6,[[5,[8,3]],[[2,3],[9,1]]]]
        [[[[5,6],1],[7,[8,1]]],[2,[7,0]]]
        [[[2,4],[[8,8],6]],[[[3,4],[4,5]],[0,[8,5]]]]
        [1,[[6,6],[9,1]]]
        [[[[6,0],8],[[3,2],[5,8]]],[[6,1],[[6,4],5]]]
        [[4,[1,[8,1]]],[7,[4,[0,8]]]]
        [[[3,3],[[1,5],[5,2]]],[[[4,5],[8,5]],[[5,8],5]]]
        [[[0,4],[[5,0],2]],[[7,[9,3]],5]]
        [[[7,[4,4]],[4,5]],[5,2]]
        [[[[9,2],[7,4]],[[4,4],9]],[[[2,2],[9,7]],[[8,0],[0,9]]]]
        [[[[6,2],3],[4,[6,9]]],5]
        [[[8,4],[1,6]],6]
        [[1,1],[[[8,9],[5,2]],[1,5]]]
        [[[[3,8],5],[6,5]],2]
        [[4,[6,8]],[[1,[5,2]],[[6,3],[7,9]]]]
        [9,[5,[1,9]]]
        [[[[5,2],[4,1]],[[8,6],[4,1]]],[[0,7],[1,8]]]
        [[[0,4],[[6,8],[4,8]]],[[0,[7,0]],[6,9]]]
        [[[[7,1],[6,1]],[[2,2],[9,9]]],[[[7,1],[5,1]],[[3,0],[9,6]]]]
        [[[1,6],[7,[1,6]]],[[1,[7,3]],[[7,4],3]]]
        [[[[1,6],[7,0]],[[4,5],2]],4]
        [[[7,2],[7,5]],[[8,[1,8]],[[8,5],9]]]
        [[[7,8],[[7,5],[8,0]]],[[9,5],0]]
        [[[5,2],8],[1,[2,2]]]
        [[[[3,1],[2,7]],[8,4]],[[4,[6,1]],2]]
        [[4,[7,[7,0]]],5]
        [[[7,5],[[1,2],[7,4]]],[[7,[3,9]],9]]
        [[[[8,6],[7,1]],8],[[9,7],7]]
        [[7,[[9,0],5]],[[[0,9],[7,5]],[[8,1],9]]]
        [[[8,9],[[4,2],[3,3]]],[[3,8],2]]
        [[[[6,9],1],[8,[9,1]]],[[5,9],[1,5]]]
        [[[[8,4],[9,6]],[[9,1],7]],[[0,3],[[6,3],8]]]
        [[[[2,0],[3,7]],[[3,4],9]],[0,[[0,5],4]]]
        [[3,[3,[5,2]]],[[[1,0],7],1]]
        [[[6,7],[0,3]],[6,3]]
        [[[[1,1],[6,6]],3],[[8,[6,9]],[5,[1,3]]]]
        [[[[3,3],8],5],[[[7,7],[8,2]],[[1,2],7]]]
        [6,[[2,4],[4,7]]]
        [[[[3,4],[6,2]],8],[[9,7],0]]
        [[8,[9,[8,1]]],[4,4]]
        [[[[9,8],4],[[3,9],[4,3]]],[[7,[7,4]],3]]
        [[2,[3,[3,1]]],[[2,[5,8]],2]]
        [[[[1,8],[7,9]],[7,[4,4]]],[9,[[1,9],5]]]
        [[[[5,9],[5,8]],[7,[5,6]]],[[[5,4],[7,7]],[[0,4],0]]]
        [[[6,[4,3]],6],[[1,[5,0]],[[5,9],[3,5]]]]
        [[[[3,5],[9,4]],[7,[2,7]]],8]
        [[[8,[7,8]],1],[7,[0,4]]]
        [[7,4],[0,0]]
        [1,[5,[6,[7,2]]]]
        [[[7,[0,5]],[4,3]],[4,[6,5]]]
        [[3,[[3,0],[2,8]]],[[[5,7],5],[7,6]]]
        [5,[4,[[4,8],[0,3]]]]
        [[[3,[5,8]],6],[9,3]]
        [[[7,[3,9]],2],[[2,6],[[3,5],[0,2]]]]
        [[[[7,6],[4,2]],[[9,2],2]],[[[6,1],8],[[8,8],1]]]
        [[[[9,3],1],2],[1,[[7,4],1]]]
        [[[[0,1],[1,4]],3],[[[7,6],1],[[2,8],[8,4]]]]
        [[[1,[3,5]],5],[0,[[8,4],[7,2]]]]
        [[[[0,2],[3,7]],[0,0]],3]
        [[[[1,5],[0,0]],[6,[3,7]]],[[9,4],5]]
        [[[[9,4],4],5],[[6,[6,4]],1]]
        [[1,[3,[5,9]]],[[1,[7,0]],5]]
        [[[0,[9,8]],[[4,3],6]],[[[2,3],5],[6,[1,3]]]]
        [[1,[7,[9,7]]],[7,[[0,5],[8,9]]]]
        [[9,[[6,4],[2,2]]],[9,[[7,5],[6,2]]]]
        [[[9,[4,7]],[6,0]],2]
        [[7,[[0,9],[3,6]]],[1,[[8,5],0]]]
        [[[9,4],[[0,9],[7,7]]],[[[2,8],8],[4,6]]]
        [1,[[[5,7],6],2]]
        [[5,[6,[6,3]]],[6,[[0,3],6]]]
        [[[2,9],[[8,7],0]],[[[7,6],8],[[8,2],4]]]
        [0,[[2,3],[4,4]]]
        [[[[6,6],[5,4]],5],1]
        [[[[5,5],8],0],[9,0]]
        [[0,4],[[2,[0,1]],9]]
        [[[[7,3],3],4],[[[8,3],[0,9]],[6,9]]]
        [[[[3,9],[6,5]],6],[9,2]]
        [9,[8,[7,3]]]
        [[[6,6],[[3,6],2]],[[[4,6],5],[3,4]]]
        [6,[[7,[4,6]],1]]
        [[[[1,8],5],[1,0]],9]
        [[6,[[8,8],[9,6]]],[6,[8,2]]]
        [[8,[7,3]],[[[2,2],[5,8]],8]]
        [[[0,[4,3]],[[5,5],0]],[8,[1,[1,2]]]]
        [[[[3,7],[5,5]],[1,0]],[4,5]]
        [[8,[3,[3,1]]],[[3,4],[[5,4],3]]]
        [[[3,[9,9]],[[6,0],[4,5]]],[[9,[3,5]],[[2,8],[6,0]]]]
        [[5,[[7,2],5]],5]
        """.trimIndent().lines().map { it.parseSnailNumber() }

    @Test
    fun `puzzle solution 1`() {
        val result = puzzleInput().snailSum()
        assertThat(result.magnitude).isEqualTo(3869)
    }
}