import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day16.run {
            solve1(1651) // 1638
            solve2(1707) // 2400
        }
    }.let { println("Total: $it ms") }
}

object Day16 : Day.LineInput<Day16.Data, Int>("16") {

    data class Data(private val valves: List<Valve>, private val start: Int, private val minDistance: List<List<Int>>) {

        fun start(totalTime: Int, totalRounds: Int): Int {
            var res = 0
            val turnedOn = BooleanArray(valves.size)

            fun traverse(curr: Int, remainMinutes: Int, totalPressure: Int, remainRounds: Int) {
                if (remainMinutes == 0 || remainRounds == 0) return

                for ((next, isTurnedOn) in turnedOn.withIndex()) {
                    val flowRate = valves[next].flowRate
                    val distance = minDistance[curr][next]
                    if (!isTurnedOn && flowRate > 0 && remainMinutes > distance) {
                        turnedOn[next] = true
                        val nextRemainMinutes = remainMinutes - distance - 1
                        val nextTotalPressure = totalPressure + flowRate * nextRemainMinutes
                        res = maxOf(res, nextTotalPressure)
                        traverse(next, nextRemainMinutes, nextTotalPressure, remainRounds)
                        traverse(start, 26, nextTotalPressure, remainRounds - 1)
                        turnedOn[next] = false
                    }
                }
            }
            traverse(start, totalTime, 0, totalRounds)

            return res
        }
    }

    data class Valve(val flowRate: Int, val tunnels: List<Int>)

    override fun parse(input: List<String>): Data {
        val matchResults = input.map {
            Regex("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? ([\\s\\S]+)")
            .matchEntire(it)!!.groupValues
        }
        val valveIndex = matchResults.withIndex().associate { it.value[1] to it.index }
        val valves = matchResults.map {
            Valve(it[2].toInt(), it[3].split(", ").map { valveIndex[it]!! })
        }

        val minDistance = MutableList(valves.size) { MutableList(valves.size) { valves.size } }
        for ((from, valve) in valves.withIndex()) {
            for (neighbor in valve.tunnels) {
                minDistance[from][neighbor] = 1
            }
        }
        for (mid in valves.indices) {
            for (from in valves.indices) {
                for (to in valves.indices) {
                    minDistance[from][to] = minOf(minDistance[from][to], minDistance[from][mid] + minDistance[mid][to])
                }
            }
        }
        return Data(valves, valveIndex["AA"]!!, minDistance)
    }

    override fun part1(data: Data) = data.start(30, 1)

    override fun part2(data: Data) = data.start(26, 2)
}