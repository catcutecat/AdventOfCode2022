import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day04.run {
            solve1(2) // 448
            solve2(4) // 794
        }
    }.let { println("Total: $it ms") }
}

object Day04 : Day.LineInput<List<Day04.Assignment>, Int>("04") {

    data class Assignment(val elf1: IntRange, val elf2: IntRange)

    private operator fun IntRange.contains(other: IntRange) =
        first <= other.first && last >= other.last

    private infix fun IntRange.overlaps(other: IntRange) =
        first <= other.last && other.first <= last

    override fun parse(input: List<String>) = input.map {
        it.split(",", "-").map(String::toInt).let { (a, b, c, d) ->
            Assignment(a..b, c..d)
        }
    }

    override fun part1(data: List<Assignment>) = data.count { (a, b) -> a in b || b in a }

    override fun part2(data: List<Assignment>) = data.count { (a, b) -> a overlaps b }
}