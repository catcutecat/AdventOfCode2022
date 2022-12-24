import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day24.run {
            solve1(18) // 277
            solve2(54) // 877
        }
    }.let { println("Total: $it ms") }
}

object Day24 : Day.LineInput<Day24.Data, Int>("24") {

    class Data(val blizzards: Array<Array<Direction?>>, val start: Pair<Int, Int>, val goal: Pair<Int, Int>)

    enum class Direction(val dr: Int, val dc: Int) {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);
    }

    override fun parse(input: List<String>): Data {
        val blizzards = Array(input.size) { Array<Direction?>(input[it].length) { null } }
        for (row in input.indices) for (col in input[row].indices) {
            when (input[row][col]) {
                '>' -> blizzards[row][col] = Direction.RIGHT
                '<' -> blizzards[row][col] = Direction.LEFT
                '^' -> blizzards[row][col] = Direction.UP
                'v' -> blizzards[row][col] = Direction.DOWN
            }
        }
        return Data(blizzards, 0 to 1, blizzards.lastIndex to blizzards.last().lastIndex - 1)
    }

    private fun minSteps(blizzards: Array<Array<Direction?>>, targets: List<Pair<Int, Int>>): Int {
        fun isInDangerArea(row: Int, col: Int) =
            row in 1 until blizzards.lastIndex && col in 1 until blizzards[row].lastIndex

        fun hasBlizzard(row: Int, col: Int, mins: Int) = isInDangerArea(row, col) && Direction.values().any {
            val (rowSize, colSize) = blizzards.size - 2 to blizzards.first().size - 2
            val checkRow = (row - 1 + mins % rowSize * it.dr + rowSize) % rowSize + 1
            val checkCol = (col - 1 + mins % colSize * it.dc + colSize) % colSize + 1
            when (it) {
                Direction.UP -> blizzards[checkRow][checkCol] == Direction.DOWN
                Direction.DOWN -> blizzards[checkRow][checkCol] == Direction.UP
                Direction.LEFT -> blizzards[checkRow][checkCol] == Direction.RIGHT
                Direction.RIGHT -> blizzards[checkRow][checkCol] == Direction.LEFT
            }
        }

        var res = 0
        for (i in 1..targets.lastIndex) {
            var next = setOf(targets[i - 1])
            while (targets[i] !in next) {
                ++res
                val newNext = mutableSetOf<Pair<Int, Int>>()
                for ((row, col) in next) {
                    if (!hasBlizzard(row, col, res)) newNext.add(row to col)
                    Direction.values().filter { !hasBlizzard(row + it.dr, col + it.dc, res) }
                        .map { row + it.dr to col + it.dc }
                        .filter { it == targets[i] || isInDangerArea(it.first, it.second) }
                        .let { newNext.addAll(it) }
                }
                next = newNext
            }
        }
        return res.also { println(it) }
    }

    override fun part1(data: Data) = data.run { minSteps(blizzards, listOf(start, goal)) }

    override fun part2(data: Data) = data.run { minSteps(blizzards, listOf(start, goal, start, goal)) }
}