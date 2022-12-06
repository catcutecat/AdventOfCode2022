fun main() {
    Day03.run {
        solve1(157) // 7845
        solve2(70) // 2790
    }
}

object Day03 : Day.LineInput<List<String>, Int>("03") {

    private val Char.priority get() = when(this) {
        in 'a'..'z' -> this - 'a' + 1
        in 'A'..'Z' -> this - 'A' + 27
        else -> 0
    }

    private fun findCommon(input: List<String>) = input
        .map { it.toSet() }
        .reduce { acc, chars -> acc intersect chars }
        .single().priority

    override fun parse(input: List<String>) = input

    override fun part1(data: List<String>) = data.sumOf { it.chunked(it.length / 2).let(::findCommon) }

    override fun part2(data: List<String>) = data.chunked(3).sumOf(::findCommon)
}