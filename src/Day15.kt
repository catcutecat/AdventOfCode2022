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

        fun coveredIntervals(y: Int): List<Pair<Int, Int>> {
            val res = mutableListOf<Pair<Int, Int>>()
            for ((sx, sy, bx, by) in positions) {
                val distance = abs(sx - bx) + abs(sy - by)
                val distanceX = distance - abs(sy - y)
                if (distanceX >= 0) {
                    res.add(sx - distanceX to sx + distanceX)
                }
            }
            res.sortWith(compareBy({ it.first }, { it.second }))
            return res
        }

        fun beaconCount(y: Int) = positions.filter { it[3] == y }.map { it[2] }.toSet().size
    }

    override fun parse(input: List<List<String>>): Data {
        val positions = input[0].map {
            Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
                .matchEntire(it)!!.groupValues.drop(1).map(String::toInt)
        }
        val (targetY, searchArea) = input[1].map(String::toInt)
        return Data(positions, targetY, searchArea)
    }

    override fun part1(data: Data) = data.run {
        val intervals = coveredIntervals(targetY) + listOf(Int.MAX_VALUE to Int.MAX_VALUE)
        var currStart = intervals.first().first
        var currEnd = intervals.first().second
        var coveredLength = 0L
        for ((start, end) in intervals) {
            if (start > currEnd) {
                coveredLength += currEnd - currStart + 1
                currStart = start
            }
            currEnd = maxOf(currEnd, end)
        }

        coveredLength - beaconCount(targetY)
    }

    override fun part2(data: Data) = data.run {
        for (y in 0..searchArea) {
            val intervals = coveredIntervals(y)
            var currEnd = intervals.first().second
            for ((start, end) in intervals) {
                if (start > currEnd + 1) {
                    return@run (start - 1) * 4000000L + y
                }
                currEnd = maxOf(currEnd, end)
            }
        }
        error("Should not reach here.")
    }
}