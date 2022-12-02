fun main() {
    Day01.run {
        solve1(24000) // 74711
        solve2(45000) // 209481
    }
}

object Day01 : Day.GroupInput<Int, Int>("01", { elf -> elf.sumOf { cal -> cal.toInt() } }) {
    override fun part1(input: List<Int>) = input.max()

    override fun part2(input: List<Int>) = input.sortedDescending().take(3).sum()
}