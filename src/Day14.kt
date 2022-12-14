import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day14.run {
            solve1(24) // 674
            solve2(93) // 24958
        }
    }.let { println("Total: $it ms") }
}

object Day14 : Day.LineInput<Day14.Data, Int>("14") {

    class Data(val map: Array<BooleanArray>, val startX: Int)

    override fun parse(input: List<String>) = input.map { path ->
        path.split(" -> ").map { it.split(",").let { (x, y) -> x.toInt() to y.toInt() } }
    }.let { paths ->
        val (minX, maxX, maxY) = paths.fold(intArrayOf(Int.MAX_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)) { acc, path ->
            for ((x, y) in path) {
                acc[0] = minOf(acc[0], x)
                acc[1] = maxOf(acc[1], x)
                acc[2] = maxOf(acc[2], y)
            }
            acc
        }
        val map = Array(maxY + 1) { BooleanArray(maxX - minX + 1) }
        for (path in paths) {
            for (i in 0 until path.lastIndex) {
                val (x1, y1) = path[i]
                val (x2, y2) = path[i + 1]
                for (x in minOf(x1, x2) - minX..maxOf(x1, x2) - minX) {
                    for (y in minOf(y1, y2)..maxOf(y1,y2)) {
                        map[y][x] = true
                    }
                }
            }
        }
        Data(map, 500 - minX)
    }

    override fun part1(data: Data): Int {
        var res = 0
        val startX = data.startX
        val blocked = Array(data.map.size) { data.map[it].copyOf() }

        fun fall(): Boolean {
            if (blocked[0][startX]) return false
            var currX = startX
            for (y in 0 until blocked.lastIndex) {
                currX = when {
                    !blocked[y + 1][currX] -> currX
                    currX == 0 -> return false
                    !blocked[y + 1][currX - 1] -> currX - 1
                    currX == blocked[y + 1].lastIndex -> return false
                    !blocked[y + 1][currX + 1] -> currX + 1
                    else -> {
                        blocked[y][currX] = true
                        return true
                    }
                }
            }
            return false
        }

        while (fall()) ++res

        return res
    }

    override fun part2(data: Data): Int {
        val map = data.map
        val mid = data.startX

        val noSand = BooleanArray(map.first().size)
        fun updateNoSand() {
            var left = 0
            while (left < noSand.size) {
                while (left < noSand.size && !noSand[left]) ++left
                var right = left
                while (right < noSand.size && noSand[right]) ++right
                if (left < noSand.size) noSand[left] = false
                noSand[right - 1] = false
                left = right
            }
        }

        var res = 0
        for (y in map.indices) {
            updateNoSand()
            for (x in map[y].indices) {
                if (map[y][x] && x in mid - y..mid + y) noSand[x] = true
            }
            res += y * 2 + 1 - noSand.count { it }
        }
        updateNoSand()
        res += map.size * 2 + 1 - noSand.count { it }

        return res.also(::println)
    }
}