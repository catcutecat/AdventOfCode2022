import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day20.run {
            solve1(3L) // 3473L
            solve2(1623178306L) // 7496649006261L
        }
    }.let { println("Total: $it ms") }
}

object Day20 : Day.LineInput<List<Int>, Long>("20") {

    override fun parse(input: List<String>) = input.map(String::toInt)

    private class Node(val value: Int, var next: Node? = null, var prev: Node? = null) {

        fun move(mod: Int, mul: Long = 1L) {
            require(mod > 0) { "Value of mod $mod is less than or equal to zero." }
            val step = ((value % mod + mod) * mul % mod).toInt().takeIf { it > 0 } ?: return
            val targetNode = getNext(step)
            prev!!.next = next
            next!!.prev = prev
            prev = targetNode
            next = targetNode.next
            next!!.prev = this
            prev!!.next = this
        }

        fun getNext(step: Int): Node = generateSequence(this) { it.next!! }.take(step + 1).last()
    }

    private fun linkedList(values: List<Int>) = values.map(::Node).also {
        for ((i, node) in it.withIndex()) {
            node.next = it[(i + 1) % it.size]
            node.prev = it[(i + it.size - 1) % it.size]
        }
    }

    private fun List<Node>.mix(count: Int, mul: Long) = repeat(count) { onEach { it.move(size - 1, mul) } }

    private fun List<Node>.getSum() = generateSequence(first { it.value == 0 }) { it.getNext(1000) }
        .take(4).drop(1).sumOf { it.value }

    override fun part1(data: List<Int>) = linkedList(data).apply { mix(1, 1L) }.getSum() * 1L

    override fun part2(data: List<Int>) = linkedList(data).apply { mix(10, 811589153L) }.getSum() * 811589153L
}