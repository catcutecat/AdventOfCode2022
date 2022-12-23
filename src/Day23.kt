import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day23.run {
            solve1(110) // 3766
            solve2(20) // 954
        }
    }.let { println("Total: $it ms") }
}

object Day23 : Day.LineInput<Set<Pair<Int, Int>>, Int>("23") {

    override fun parse(input: List<String>) = mutableSetOf<Pair<Int, Int>>().apply {
        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] == '#') add(row to col)
            }
        }
    }

    private val directions = arrayOf(
        (-1 to 0) to arrayOf(-1 to -1, -1 to 0, -1 to 1), // N
        (1 to 0) to arrayOf(1 to -1, 1 to 0, 1 to 1), // S
        (0 to -1) to arrayOf(-1 to -1, 0 to -1, 1 to -1), // W
        (0 to 1) to arrayOf(-1 to 1, 0 to 1, 1 to 1) // E
    )

    private fun move(elves: MutableSet<Pair<Int, Int>>, round: Int): Boolean {
        val move = mutableMapOf<Pair<Int, Int>, MutableList<Pair<Int, Int>>>() // [dest] = listOf(from)
        for (elf in elves) (round until round + directions.size)
            .map { directions[it % directions.size] }
            .filter { (_, checkpoints) -> checkpoints.all { point -> (point.first + elf.first to point.second + elf.second) !in elves } }
            .takeIf { it.size in 1 until directions.size }
            ?.let {
                val (dir, _) = it.first()
                val dest = elf.first + dir.first to elf.second + dir.second
                move.getOrPut(dest) { mutableListOf() }.add(elf)
            }
        for ((k, v) in move) if (v.size == 1) elves.apply {
            remove(v.single())
            add(k)
        }
        return move.any { it.value.size == 1 }
    }

    override fun part1(data: Set<Pair<Int, Int>>): Int = data.toMutableSet().run {
        repeat(10) { move(this, it) }
        intArrayOf(minOf { it.first }, maxOf { it.first }, minOf { it.second }, maxOf { it.second })
            .let { (minRow, maxRow, minCol, maxCol) ->
                (maxRow - minRow + 1) * (maxCol - minCol + 1) - size
            }
    }

    override fun part2(data: Set<Pair<Int, Int>>): Int = data.toMutableSet().run {
        var res = 0
        while (move(this, res)) ++res
        res + 1
    }
}