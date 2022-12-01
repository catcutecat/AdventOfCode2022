fun main() {
    object : Day<Int, Int>("01") {
        override fun part1(): Int {
            return inputGroups.maxOf { elf -> elf.sumOf { it.toInt() } }
        }

        override fun part2(): Int {
            return inputGroups.fold(IntArray(3)) { acc, elf ->
                acc[2] = maxOf(acc[2], elf.sumOf { it.toInt() })
                if (acc[2] > acc[1]) acc[2] = acc[1].also { acc[1] = acc[2] }
                if (acc[1] > acc[0]) acc[1] = acc[0].also { acc[0] = acc[1] }
                acc
            }.sum()
        }
    }.run {
        solve1(24000)
        solve2(45000)
    }
}