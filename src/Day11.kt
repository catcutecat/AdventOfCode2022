import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day11.run {
            solve1(10605L) // 58322L
            solve2(2713310158L) // 13937702909L
        }
    }.let { println("Total: $it ms") }
}

object Day11 : Day.GroupInput<List<Day11.Monkey>, Long>("11") {

    data class Monkey(val items: MutableList<Long>, val operation: (Long) -> Long, val next: (Long) -> Int, val div: Long) {
        var inspectedItems = 0L
    }

    override fun parse(input: List<List<String>>) = input.map { rawMonkey ->
        val items = rawMonkey[1].split(": ").last()
            .split(", ")
            .map(String::toLong)
            .let { ArrayDeque<Long>().apply { addAll(it) } }

        val (op, num) = rawMonkey[2].split(" ").takeLast(2)
        val operation: (Long) -> Long = when {
            op == "*" && num == "old" -> { { it * it } }
            op == "*" -> { { it * num.toLong() } }
            num == "old" -> { { it + it } }
            else -> { { it + num.toLong() } }
        }

        val div = rawMonkey[3].split(" ").last().toLong()
        val testTrue = rawMonkey[4].split(" ").last().toInt()
        val testFalse = rawMonkey[5].split(" ").last().toInt()
        val next: (Long) -> Int = { if (it % div == 0L) testTrue else testFalse }

        Monkey(items, operation, next, div)
    }

    private fun throwItems(data: List<Monkey>, round: Int, divide: Boolean): Long {
        val monkeys = data.map { it.copy(items = it.items.toMutableList()) }
        val mod = monkeys.fold(1L) { acc, monkey -> acc * monkey.div }
        repeat(round) {
            for (monkey in monkeys) monkey.apply {
                inspectedItems += items.size
                for (item in items) {
                    val worryLevel = operation(item).let { if (divide) it / 3 else it % mod }
                    val nextMonkey = next(worryLevel)
                    monkeys[nextMonkey].items.add(worryLevel)
                }
                items.clear()
            }
        }
        return monkeys.map { it.inspectedItems }.sorted().takeLast(2).let { (a, b) -> a * b }
    }

    override fun part1(data: List<Monkey>) = throwItems(data, 20, true)

    override fun part2(data: List<Monkey>) = throwItems(data, 10000, false)
}