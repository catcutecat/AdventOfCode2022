import java.util.*
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day15.run {
            solve1(26L) // 4907780L
            solve2(56000011L) // 13639962836448L
        }
    }.let { println("Total: $it ms") }
}

object Day15 : Day.GroupInput<Day15.Data, Long>("15") {

    class Data(private val positions: List<List<Int>>, val targetY: Int, val searchArea: Int) {

        fun coveredIntervalsAt(y: Int): Stack<Pair<Int, Int>> = positions.mapNotNull { (sx, sy, bx, by) ->
            val distance = abs(sx - bx) + abs(sy - by)
            val distanceX = distance - abs(sy - y)
            distanceX.takeIf { it >= 0 }?.let { sx - it to sx + it + 1 }
        }.sortedBy { it.first }.fold(Stack<Pair<Int, Int>>()) { acc, (start, end) ->
            acc.apply {
                if (isEmpty() || peek().second < start) add(start to end)
                else if (peek().second < end) push(pop().first to end)
            }
        }

        fun beaconCountAt(y: Int): Int = positions.map { it[2] to it[3] }.toSet().count { it.second == y }
    }

    override fun parse(input: List<List<String>>): Data {
        val positions = input[0].map {
            Regex("""Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""")
                .matchEntire(it)!!.destructured.toList().map(String::toInt)
        }
        val (targetY, searchArea) = input[1].map(String::toInt)
        return Data(positions, targetY, searchArea)
    }

    override fun part1(data: Data) = with(data) {
        coveredIntervalsAt(targetY).sumOf { it.second - it.first } - beaconCountAt(targetY).toLong()
    }

    override fun part2(data: Data) = with(data) {
        (0..searchArea).firstNotNullOf { y ->
            coveredIntervalsAt(y).takeIf { it.size == 2 }?.let { (a, _) ->
                a.second * 4000000L + y
            }
        }
    }
}