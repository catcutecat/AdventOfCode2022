fun main() {
    Day06.run {
        solve1(listOf(7, 5, 6, 10, 11)) // 1480
        solve2(listOf(19, 23, 23, 29, 26)) // 2746
    }
}

object Day06 : Day.LineInput<List<String>, List<Int>>("06") {

    private fun findStartMaker(size: Int, s: String): Int {
        val exist = BooleanArray(26)
        var start = -1
        for (end in s.indices) {
            while (exist[s[end] - 'a']) exist[s[++start] - 'a'] = false
            exist[s[end] - 'a'] = true
            if (end - start == size) return end + 1
        }
        error("Invalid input: size=$size, string=$s")
    }

    override fun parse(input: List<String>) = input

    override fun part1(data: List<String>) = data.map { findStartMaker(4, it) }

    override fun part2(data: List<String>) = data.map { findStartMaker(14, it) }
}