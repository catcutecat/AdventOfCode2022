import kotlin.math.abs
import kotlin.math.sign
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day09.run {
            solve1(listOf(13, 88)) // 6090
            solve2(listOf(1, 36)) // 2566
        }
    }.let { println("Total: $it ms") }
}

object Day09 : Day.GroupInput<List<Day09.Data>, List<Int>>("09") {

    data class Data(val steps: List<Step>) {
        fun simulate(length: Int): Int {
            val visited = mutableSetOf<List<Int>>()
            val positions = Array(length) { IntArray(2) }
            for ((dx, dy, move) in steps) {
                repeat(move) {
                    positions[0][0] += dx
                    positions[0][1] += dy
                    for (i in 1..positions.lastIndex) {
                        val diffX = positions[i - 1][0] - positions[i][0]
                        val diffY = positions[i - 1][1] - positions[i][1]
                        if (abs(diffX) == 2 || abs(diffY) == 2) {
                            positions[i][0] += diffX.sign
                            positions[i][1] += diffY.sign
                        }
                    }
                    visited.add(positions.last().toList())
                }
            }
            return visited.size
        }
    }

    data class Step(val dx: Int, val dy: Int, val move: Int)

    override fun parse(input: List<List<String>>) = input.map { rawSteps ->
        rawSteps.map { step ->
            val (dir, move) = step.split(" ")
            val (dx, dy) = when (dir) {
                "U" -> 0 to 1
                "D" -> 0 to -1
                "L" -> -1 to 0
                "R" -> 1 to 0
                else -> error("Invalid input.")
            }
            Step(dx, dy, move.toInt())
        }.let(::Data)
    }

    override fun part1(data: List<Data>) = data.map { it.simulate(2) }

    override fun part2(data: List<Data>) = data.map { it.simulate(10) }
}