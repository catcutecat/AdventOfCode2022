import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day18.run {
            solve1(64) // 4192
            solve2(58) // 2520
        }
    }.let { println("Total: $it ms") }
}

object Day18 : Day.LineInput<Day18.Data, Int>("18") {

    class Data(val cubes: Array<Array<BooleanArray>>, val positions: List<List<Int>>)

    override fun parse(input: List<String>): Data {
        val positions = input.map { it.split(",").map { it.toInt() + 1 } }
        val (maxX, maxY, maxZ) = (0..2).map { i -> positions.maxOf { it[i] } }
        val cubes = Array(maxX + 2) { Array(maxY + 2) { BooleanArray(maxZ + 2) } }
        for ((x, y, z) in positions) {
            cubes[x][y][z] = true
        }
        return Data(cubes, positions)
    }

    private val directions = arrayOf(
        intArrayOf(1, 0, 0), intArrayOf(-1, 0, 0),
        intArrayOf(0, 1, 0), intArrayOf(0, -1, 0),
        intArrayOf(0, 0, 1), intArrayOf(0, 0, -1)
    )

    override fun part1(data: Data) = data.run {
        positions.sumOf { (x, y, z) ->
            directions.count { (dx, dy, dz) -> !cubes[x + dx][y + dy][z + dz] }
        }
    }

    override fun part2(data: Data) = data.run {
        var res = 0
        val visited = Array(cubes.size) { x -> Array(cubes[x].size) { y -> BooleanArray(cubes[x][y].size) } }
        var next = listOf(intArrayOf(0, 0, 0))
        while (next.isNotEmpty()) {
            val newNext = mutableListOf<IntArray>()
            for ((x, y, z) in next) for ((dx, dy, dz) in directions) {
                val (nx, ny, nz) = intArrayOf(x + dx, y + dy, z + dz)
                if (nx in visited.indices && ny in visited[nx].indices && nz in visited[nx][ny].indices) {
                    if (cubes[nx][ny][nz]) {
                        ++res
                    } else if (!visited[nx][ny][nz]) {
                        visited[nx][ny][nz] = true
                        newNext.add(intArrayOf(nx, ny, nz))
                    }
                }
            }
            next = newNext
        }
        res
    }
}