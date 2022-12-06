fun main() {
    Day01.run {
        solve1(24000) // 74711
        solve2(45000) // 209481
    }
}

object Day01 : Day.GroupInput<List<Int>, Int>("01") {

    override fun parse(input: List<List<String>>) = input.map { elf -> elf.sumOf { cal -> cal.toInt() } }

    override fun part1(data: List<Int>) = data.max()

    override fun part2(data: List<Int>) = data.sortedDescending().take(3).sum()
}