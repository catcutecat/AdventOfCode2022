import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day12.run {
            solve1(31) // 330
            solve2(29) // 321
        }
    }.let { println("Total: $it ms") }
}

object Day12 : Day.LineInput<Day12.Data, Int>("12") {

    data class Data(val map: List<String>, val start: Pair<Int, Int>, val end: Pair<Int, Int>)
    private val directions = arrayOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

    override fun parse(input: List<String>): Data {
        var start = 0 to 0
        var end = 0 to 0
        for (row in input.indices) {
            for ((col, elevation) in input[row].withIndex()) {
                if (elevation == 'S') start = row to col
                else if (elevation == 'E') end = row to col
            }
        }
        return Data(input, start, end)
    }

    override fun part1(data: Data): Int {
        val (map, start, end) = data
        val (startRow, startCol) = start
        val steps = Array(map.size) { IntArray(map[it].length) { -1 } }.also { it[startRow][startCol] = 0 }
        val (targetRow, targetCol) = end
        val next = ArrayDeque<Pair<Int, Int>>().apply {
            directions.map { (r, c) -> startRow + r to startCol + c }.forEach { (newRow, newCol) ->
                if (newRow in steps.indices && newCol in steps[newRow].indices && map[newRow][newCol] <= 'b') {
                    steps[newRow][newCol] = 1
                    addLast(newRow to newCol)
                }
            }
        }
        while (next.isNotEmpty()) {
            val (row, col) = next.removeFirst()
            directions.map { (r, c) -> row + r to col + c }.forEach { (newRow, newCol) ->
                if (newRow in steps.indices && newCol in steps[newRow].indices && steps[newRow][newCol] == -1) {
                    if (newRow == targetRow && newCol == targetCol) {
                        if (map[row][col] >= 'y') return steps[row][col] + 1
                    } else if (map[newRow][newCol] <= map[row][col] + 1) {
                        steps[newRow][newCol] = steps[row][col] + 1
                        next.addLast(newRow to newCol)
                    }
                }
            }
        }
        error("Cannot reach the destination.")
    }

    override fun part2(data: Data): Int {
        val (map, _, end) = data
        val (endRow, endCol) = end
        val steps = Array(map.size) { IntArray(map[it].length) { -1 } }.also { it[endRow][endCol] = 0 }
        val next = ArrayDeque<Pair<Int, Int>>().apply {
            directions.map { (r, c) -> endRow + r to endCol + c }.forEach { (row, col) ->
                if (row in steps.indices && col in steps[row].indices && map[row][col] >= 'y') {
                    steps[row][col] = 1
                    addLast(row to col)
                }
            }
        }
        while (next.isNotEmpty()) {
            val (row, col) = next.removeFirst()
            directions.map { (r, c) -> row + r to col + c }.forEach { (newRow, newCol) ->
                if (newRow in steps.indices && newCol in steps[newRow].indices && steps[newRow][newCol] == -1) {
                    if (map[newRow][newCol] == 'a' || map[newRow][newCol] == 'S') {
                        if (map[row][col] == 'b') return steps[row][col] + 1
                    } else if (map[newRow][newCol] + 1 >= map[row][col]) {
                        steps[newRow][newCol] = steps[row][col] + 1
                        next.addLast(newRow to newCol)
                    }
                }
            }
        }
        error("Cannot reach the destination.")
    }
}