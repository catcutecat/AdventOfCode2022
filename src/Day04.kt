fun main() {
    Day04.run {
        solve1(2) // 448
        solve2(4) // 794
    }
}

object Day04 : Day.LineInput<Day04.Data, Int>("04", { Data.fromString(it) }) {

    data class Data(val elf1: IntRange, val elf2: IntRange) {
        companion object {
            fun fromString(s: String) = s.split(",", "-").map(String::toInt)
                .let { (a, b, c, d) ->
                    Data(a..b, c..d)
                }
        }
    }

    private operator fun IntRange.contains(other: IntRange) =
        first <= other.first && last >= other.last

    private infix fun IntRange.overlaps(other: IntRange) =
        first <= other.last && other.first <= last

    override fun part1(input: List<Data>) = input.count { (a, b) -> a in b || b in a }

    override fun part2(input: List<Data>) = input.count { (a, b) -> a overlaps b }
}