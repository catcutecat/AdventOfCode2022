import java.util.*

fun main() {
    Day05.run {
        solve1("CMZ") // "CWMTGHBDW"
        solve2("MCD") // "SSCGWJCRB"
    }
}

object Day05 : Day.RowInput<Day05.SupplyStacks, String>("05") {

    data class SupplyStacks(val rawStacks: String, val steps: List<Step>) {

        fun moveOneAtATime(): String = Stacks(rawStacks).apply { steps.forEach(::moveOneAtATime) }.status

        fun moveMultipleAtOnce(): String = Stacks(rawStacks).apply { steps.forEach(::moveMultipleAtOnce) }.status
    }

    class Stacks(s: String) {
        private val stacks: Array<Stack<Char>>
        private val tmpStack = Stack<Char>()

        init {
            val lines = s.split(System.lineSeparator())
            val size = lines.last().split(" ").filter { it.isNotEmpty() }.size
            stacks = Array(size) { Stack() }
            for (i in lines.lastIndex - 1 downTo 0) {
                val line = lines[i]
                for (j in 1..line.lastIndex step 4) { // 1, 5, 9
                    if (line[j].isLetter()) stacks[j / 4].push(line[j])
                }
            }
        }

        val status: String get() = stacks.joinToString("") { it.last().toString() }

        fun moveOneAtATime(step: Step) {
            val (move, from, to) = step
            repeat(move) { stacks[to].push(stacks[from].pop()) }
        }

        fun moveMultipleAtOnce(step: Step) {
            val (move, from, to) = step
            repeat(move) { tmpStack.push(stacks[from].pop()) }
            repeat(move) { stacks[to].push(tmpStack.pop()) }
        }
    }

    data class Step(val move: Int, val from: Int, val to: Int) {
        companion object {
            fun fromString(s: String) = s.split(" ").let {
                Step(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1)
            }
        }
    }

    override fun parse(input: String): SupplyStacks = input
        .split(System.lineSeparator() + System.lineSeparator())
        .let { (a, b) ->
            SupplyStacks(a, b.split(System.lineSeparator()).map(Step::fromString))
        }

    override fun part1(data: SupplyStacks) = data.moveOneAtATime()

    override fun part2(data: SupplyStacks) = data.moveMultipleAtOnce()
}