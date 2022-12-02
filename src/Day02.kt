fun main() {
    object : Day<Int, Int, Pair<Int, Int>>("02") {
        init {
            parse = { it.map { s -> s[0] - 'A' to s[2] - 'X' } }
        }

        override fun part1() = parsedInput.sumOf { (a, b) ->
            (b - a + 1 + 3) % 3 * 3 + b + 1
        }

        override fun part2() = parsedInput.sumOf { (a, b) ->
            b * 3 + (a + b - 1 + 3) % 3 + 1
        }
    }.run {
        solve1(15) // 14375
        solve2(12) // 10274
    }
}