import java.util.*
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day08.run {
            solve1(21) // 1851
            solve2(8) // 574080
        }
    }.let { println("Total: $it ms") }
}

object Day08 : Day.LineInput<Day08.Data, Int>("08") {

    class Data(val rowSize: Int, val colSize: Int, val visibleCounts: Array<Array<IntArray>>)

    override fun parse(input: List<String>): Data {
        val rowSize = input.size
        val colSize = input.first().length
        val visibleCounts = Array(4) { Array(rowSize) { IntArray(colSize) } }
        val ranges = arrayOf(
            (0 until rowSize) to (0 until colSize),
            (rowSize - 1 downTo 0) to (colSize - 1 downTo 0)
        )

        fun count(rows: IntProgression, cols: IntProgression, dest1: Array<IntArray>, dest2: Array<IntArray>) {
            val prevNotLowerRow = Array(colSize) { Stack<Int>() }
            for (row in rows) {
                val prevNotLowerCol = Stack<Int>()
                for (col in cols) {
                    val curr = input[row][col]
                    while (prevNotLowerCol.isNotEmpty() && input[row][prevNotLowerCol.peek()] < curr) prevNotLowerCol.pop()
                    dest2[row][col] = if (prevNotLowerCol.isEmpty()) -1 else abs(col - prevNotLowerCol.peek())
                    while (prevNotLowerRow[col].isNotEmpty() && input[prevNotLowerRow[col].peek()][col] < curr) prevNotLowerRow[col].pop()
                    dest1[row][col] = if (prevNotLowerRow[col].isEmpty()) -1 else abs(row - prevNotLowerRow[col].peek())
                    prevNotLowerCol.push(col)
                    prevNotLowerRow[col].push(row)
                }
            }
        }
        for ((i, range) in ranges.withIndex()) {
            count(range.first, range.second, visibleCounts[i * 2], visibleCounts[i * 2 + 1])
        }
        return Data(rowSize, colSize, visibleCounts)
    }

    override fun part1(data: Data) = data.run {
        (0 until rowSize).sumOf { row ->
            (0 until colSize).count { col ->
                visibleCounts.any { it[row][col] == -1 }
            }
        }
    }

    override fun part2(data: Data) = data.run {
        (0 until rowSize).maxOf { row ->
            (0 until colSize).maxOf { col ->
                visibleCounts.indices.fold(1) { acc, dir ->
                    acc * (visibleCounts[dir][row][col].takeIf { it != -1 } ?: when (dir) {
                        0 -> row
                        1 -> col
                        2 -> rowSize - row - 1
                        3 -> colSize - col - 1
                        else -> error("Should not reach here.")
                    })
                }
            }
        }
    }
}