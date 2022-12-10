import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day10.run {
            solve1(13140) // 14760
            solve2(0) // "EFGERURE"
        }
    }.let { println("Total: $it ms") }
}

object Day10 : Day.LineInput<List<Day10.Data>, Int>("10") {

    data class Data(val isAdd: Boolean, val num: Int)

    override fun parse(input: List<String>) = input.map {
        "$it 0".split(" ").let { (command, num) ->
            Data(command == "addx", num.toInt())
        }
    }

    override fun part1(data: List<Data>): Int {
        var res = 0
        var nextCheck = 20
        var x = 1
        var cycle = 0
        for ((isAdd, num) in data) {
            cycle += if (isAdd) 2 else 1
            if (cycle >= nextCheck) {
                res += x * nextCheck
                nextCheck += 40
            }
            x += num
        }
        return res
    }

    override fun part2(data: List<Data>): Int {
        val res = mutableListOf(BooleanArray(40))
        var x = 1
        var curr = 0

        for ((isAdd, num) in data) {
            repeat(if (isAdd) 2 else 1) {
                res.last()[curr] = curr in x - 1 .. x + 1
                if (++curr == 40) {
                    res.add(BooleanArray(40))
                    curr = 0
                }
            }
            x += num
        }
        for (row in res) row.joinToString("") { if (it) "⚽" else "⚫" }.let(::println)

        return 0
    }
}