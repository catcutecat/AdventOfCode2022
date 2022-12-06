fun main() {
    Day06.run {
        solve1(listOf(7, 5, 6, 10, 11)) // 1480
        solve2(listOf(19, 23, 23, 29, 26)) // 2746
    }
}

object Day06 : Day.LineInput<String, List<Int>>("06", { it }) {

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

    override fun part1(input: List<String>) = input.map { findStartMaker(4, it) }

    override fun part2(input: List<String>) = input.map { findStartMaker(14, it) }

}