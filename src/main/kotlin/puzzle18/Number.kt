package puzzle18

import java.lang.Math.ceil
import java.lang.Math.floor
import java.util.*

sealed class SnailNumberReduceOp {
    abstract fun reduceStep(root: SnailNumber.Node)
}

data class SnailNumberToSplit(val parent: SnailNumber.Node, val number: SnailNumber.Number) : SnailNumberReduceOp() {

    override fun reduceStep(root: SnailNumber.Node) {
        val split = SnailNumber.Node(
            left = SnailNumber.Number(
                floor(number.value / 2.0).toInt()
            ),
            right = SnailNumber.Number(
                ceil(number.value / 2.0).toInt()
            ),
        )

        if (parent.left === number) {
            parent.left = split
        } else {
            parent.right = split
        }
    }
}

data class SnailNumberToExplode(val parent: SnailNumber.Node, val number: SnailNumber.Node) : SnailNumberReduceOp() {
    override fun reduceStep(root: SnailNumber.Node) {
        val (left, right) = number
        check(left is SnailNumber.Number)
        check(right is SnailNumber.Number)
        val numbers = SnailMathUtil.linearizeNumbers(root)
        val toTheLeft = numbers.takeWhile { it.first !== left }
        toTheLeft.lastOrNull()?.let { (n, p) ->
            n.value = n.value + left.value
        }
        val toTheRight = numbers.dropWhile { it.first !== right }.drop(1)
        toTheRight.firstOrNull()?.let { (n, p) ->
            n.value = n.value + right.value
        }
        parent.reset(number)
    }
}

sealed class SnailNumber {
    abstract fun print(): String

    data class Node(var left: SnailNumber, var right: SnailNumber) : SnailNumber() {
        override fun print() = "[${left.print()},${right.print()}]"
        fun reset(number: Node) {
            if (left === number) {
                left = Number(0)
            } else if (right === number) {
                right = Number(0)
            }
        }

        operator fun plus(other: Node): Node {
            return Node(left = this, right = other)
        }
    }

    data class Number(var value: Int) : SnailNumber() {
        override fun print() = value.toString()
    }
}

object SnailMathUtil {

    fun linearizeNumbers(
        node: SnailNumber.Node,
        res: LinkedList<Pair<SnailNumber.Number, SnailNumber.Node>> = LinkedList()
    ): List<Pair<SnailNumber.Number, SnailNumber.Node>> {
        when (val l = node.left) {
            is SnailNumber.Number -> res += l to node
            is SnailNumber.Node -> linearizeNumbers(l, res)
        }
        when (val r = node.right) {
            is SnailNumber.Number -> res += r to node
            is SnailNumber.Node -> linearizeNumbers(r, res)
        }

        return res
    }

    fun findNumbersToExplode(current: SnailNumber.Node, level: Int): LinkedList<SnailNumberToExplode> {
        val result = LinkedList<SnailNumberToExplode>()
        return when {
            level > 4 -> {
                result
            }
            level == 3 -> {
                val children = listOf(current.left, current.right).filterIsInstance<SnailNumber.Node>()
                children.all { n ->
                    n.left is SnailNumber.Number && n.right is SnailNumber.Number
                }
                result += children.map { ch -> SnailNumberToExplode(parent = current, number = ch) }
                result
            }
            else -> {
                val nodes = listOf(current.left, current.right).filterIsInstance<SnailNumber.Node>()
                nodes.forEach { n ->
                    result += findNumbersToExplode(n, level + 1)
                }
                result
            }
        }
    }

    fun SnailNumber.findNumbersToExplode(): List<SnailNumberReduceOp> {
        check(this is SnailNumber.Node)
        return findNumbersToExplode(this, 0)
    }

    fun SnailNumber.findNumbersToSplit(): List<SnailNumberReduceOp> {
        check(this is SnailNumber.Node)
        return linearizeNumbers(this).filter { (num, parent) ->
            num.value > 9
        }.map { (n, p) -> SnailNumberToSplit(p, n) }
    }

    fun SnailNumber.Node.findNextReduceOp(level: Int = 0): SnailNumberReduceOp? {
        val l = left
        val r = right

        if (level == 3) {
            val children = listOf(l, r).filterIsInstance<SnailNumber.Node>()
            check(
                children.all { n ->
                    n.left is SnailNumber.Number && n.right is SnailNumber.Number
                }
            )
            if (children.isNotEmpty()) {
                return SnailNumberToExplode(parent = this, number = children.first())
            }
        }
        if (l is SnailNumber.Number && l.value > 9) {
            return SnailNumberToSplit(this, l)
        }
        if (l is SnailNumber.Node) {
            val op = l.findNextReduceOp(level + 1)
            if (op != null) return op
        }
        if (r is SnailNumber.Number && r.value > 9) {
            return SnailNumberToSplit(this, r)
        }
        if (r is SnailNumber.Node) {
            val op = r.findNextReduceOp(level + 1)
            if (op != null) return op
        }
        return null
    }

    fun String.parseSnailNumber(): SnailNumber.Node {
        val (node, rest) = parseSnailNumberNode()
        check(rest.isEmpty())
        return node
    }

    fun String.parseSnailNumberNode(): Pair<SnailNumber.Node, String> {
        check(first() == '[')
        val rest = drop(1)
        val (left, leftRest) = parseLeft(rest)
        val (right, rightRest) = parseRight(leftRest)
        check(rightRest.isEmpty() || rightRest.first() in setOf(']', ','))
        return SnailNumber.Node(left = left, right = right) to rightRest.drop(1)
    }

    private fun parseLeft(rest: String): Pair<SnailNumber, String> = when (val n = rest.first()) {
        '[' -> rest.parseSnailNumberNode()
        else -> {
            check(n.isDigit()) {
                "expected to find a digit, but found: $n, in: $this"
            }
            val num = rest.takeWhile { it.isDigit() }
            val res = SnailNumber.Number(num.toInt())
            check(rest.drop(num.length).first() == ',') {
                "expected to find a digit followed by a ',', was: $rest"
            }
            res to rest.drop(num.length + 1)
        }
    }

    private fun parseRight(rest: String): Pair<SnailNumber, String> = when (val n = rest.first()) {
        '[' -> rest.parseSnailNumberNode()
        else -> {
            check(n.isDigit()) {
                "expected to find a digit, but found: $n, in $this"
            }
            val num = rest.takeWhile { it.isDigit() }
            val res = SnailNumber.Number(num.toInt())
            check(rest.drop(num.length).first() == ']') {
                "expected to find a digit followed by a ']', was: $rest"
            }
            res to rest.drop(num.length + 1)
        }
    }
}