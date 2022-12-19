import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day19.run {
            solve1(33) // 1199
            solve2(3348) // 3510
        }
    }.let { println("Total: $it ms") }
}

object Day19 : Day.LineInput<List<Day19.Blueprint>, Int>("19") {

    class Blueprint(val id: Int, private val costs: Array<IntArray>) {

        fun maxGeodes(totalTime: Int): Int {
            var res = 0

            val resource = intArrayOf(0, 0, 0, 0)
            val robot = intArrayOf(1, 0, 0, 0)
            val robotMax = IntArray(4) { resId -> costs.maxOf { it[resId] } }.also { it[3] = totalTime }

            fun traverse(currTime: Int) {
                if (currTime == totalTime) {
                    res = maxOf(res, resource.last())
                    return
                }

                for (robotId in robot.indices.reversed()) {
                    val canBuildRobot = costs[robotId].withIndex().all { (resId, resNeed) ->
                        resource[resId] >= resNeed
                    } && robot[robotId] < robotMax[robotId]

                    if (canBuildRobot) {
                        resource.indices.forEach { resource[it] += robot[it] }
                        resource.indices.forEach { resource[it] -= costs[robotId][it] }
                        robot[robotId] += 1

                        traverse(currTime + 1)

                        robot[robotId] -= 1
                        resource.indices.forEach { resource[it] += costs[robotId][it] }
                        resource.indices.forEach { resource[it] -= robot[it] }

                        if (robotId >= 2) {
                            return
                        }
                    }
                }
                resource.indices.forEach { resource[it] += robot[it] }
                traverse(currTime + 1)
                resource.indices.forEach { resource[it] -= robot[it] }
            }
            traverse(0)

            return res
        }
    }

    override fun parse(input: List<String>) = input.map { line ->
        Regex("""Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""")
            .matchEntire(line)!!.destructured.toList().map(String::toInt).let {
                Blueprint(
                    it[0],
                    arrayOf(
                        intArrayOf(it[1], 0, 0, 0),
                        intArrayOf(it[2], 0, 0, 0),
                        intArrayOf(it[3], it[4], 0, 0),
                        intArrayOf(it[5], 0, it[6], 0)
                    )
                )
            }
    }

    override fun part1(data: List<Blueprint>) = data.sumOf { it.maxGeodes(24) * it.id }

    override fun part2(data: List<Blueprint>) = data.take(3).fold(1) { acc, it -> acc * it.maxGeodes(32) }
}