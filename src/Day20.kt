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
            mod > 0 || return
            val step = ((value % mod + mod) * mul % mod).toInt()
            step > 0 || return

            var targetNode = this
            repeat(step) { targetNode = targetNode.next!! }
            prev!!.next = next
            next!!.prev = prev
            prev = targetNode
            next = targetNode.next
            next!!.prev = this
            prev!!.next = this
        }
    }

    private fun linkedList(values: List<Int>) = values.map { Node(it) }.also {
        for ((i, node) in it.withIndex()) {
            node.next = if (i + 1 < it.size) it[i + 1] else it[0]
            node.prev = if (i > 0) it[i - 1] else it.last()
        }
    }

    private fun getSum(nodes: List<Node>): Long {
        var curr = nodes.first { it.value == 0 }
        var sum = 0L
        repeat(3) {
            repeat(1000 % nodes.size) {
                curr = curr.next!!
            }
            sum += curr.value
        }
        return sum
    }

    override fun part1(data: List<Int>) = linkedList(data).onEach { it.move(data.size - 1) }.let(::getSum)

    override fun part2(data: List<Int>) = linkedList(data).apply {
        repeat(10) { forEach { it.move(data.size - 1, 811589153L) } }
    }.let(::getSum) * 811589153L
}