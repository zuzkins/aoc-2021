package puzzle14

import java.util.*

typealias InsertionRules = Map<Pair<Char, Char>, Char>

object Polymer {

    fun InsertionRules.polymerize(template: String): String {
        val characters = LinkedList<Char>().apply {
            addAll(template.toCharArray().asIterable())
        }
        val result = doPolymerization(template = characters)

        return result.joinToString("")
    }

    fun InsertionRules.polymerize(template: String, steps: Int): String {
        var characters = LinkedList<Char>().apply {
            addAll(template.toCharArray().asIterable())
        }
        (0 until steps).forEach {
            characters = doPolymerization(characters)
        }

        return characters.joinToString("")
    }


    private fun InsertionRules.doPolymerization(template: LinkedList<Char>): LinkedList<Char> {
        val result = LinkedList<Char>()
        var l = template.removeFirst()
        while (template.size != 0) {
            result.add(l)
            val r = template.first
            this[l to r]?.let { result.add(it) }
            l = template.removeFirst()
        }

        result.add(l)
        return result
    }

    fun InsertionRules.polymerizeSmart(template: MutableMap<Pair<Char, Char>, Long>): MutableMap<Pair<Char, Char>, Long> {
        val result = mutableMapOf<Pair<Char, Char>, Long>()
        for (entry in template.entries) {
            val pair = entry.key
            val count = entry.value
            val insertion = this[pair]
            if (insertion != null) {
                val left = pair.first to insertion
                val right = insertion to pair.second
                result.put(left, result.getOrDefault(left, 0L) + count)
                result.put(right, result.getOrDefault(right, 0L) + count)
            }
        }


        return result
    }

    fun parseInstructions(instructions: List<String>): InsertionRules {
        return instructions.associate {
            val (input, output) = it.split(" -> ")
            val f = input.first()
            val s = input.drop(1).first()
            val o = output.first()
            (f to s) to o
        }
    }

    fun polymerScore(result: String): Long {
        val frequencies = mutableMapOf<Char, Long>()
        result.forEach { ch ->
            frequencies[ch] = frequencies.getOrDefault(ch, 0) + 1
        }
        val max = frequencies.maxOf { it.value }
        val min = frequencies.minOf { it.value }
        return max - min
    }

    fun polymerScore(result: Map<Pair<Char, Char>, Long>, first: Char, last: Char): Long {
        val frequencies = mutableMapOf<Char, Long>()
        result.forEach { entry ->
            val (left, right) = entry.key
            val count = entry.value
            frequencies[left] = frequencies.getOrDefault(left, 0) + count
            frequencies[right] = frequencies.getOrDefault(right, 0) + count
        }
        val sorted = frequencies.entries.sortedBy { it.value }
        val minChar = sorted.first()
        val maxChar = sorted.last()
        var min = minChar.value
        var max = maxChar.value

        if (minChar.key in setOf(first, last)) {
            min = minChar.value + 1
        }

        if (maxChar.key in setOf(first, last)) {
            max = maxChar.value + 1
        }

        return max / 2 - min / 2
    }
}
