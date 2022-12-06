fun main() {
    Day02.run {
        solve1(15) // 14375
        solve2(12) // 10274
    }
}

object Day02 : Day.LineInput<List<Pair<Int, Int>>, Int>("02") {

    override fun parse(input: List<String>) = input.map { s -> s[0] - 'A' to s[2] - 'X' }

    override fun part1(data: List<Pair<Int, Int>>) = data.sumOf { (a, b) ->
        (b - a + 1 + 3) % 3 * 3 + b + 1
    }

    override fun part2(data: List<Pair<Int, Int>>) = data.sumOf { (a, b) ->
        b * 3 + (a + b - 1 + 3) % 3 + 1
    }
}