import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day17.run {
            solve1(3068L) // 3133L
            solve2(1514285714288L) // 1547953216393L
        }
    }.let { println("Total: $it ms") }
}

object Day17 : Day.RowInput<Day17.Data, Long>("17") {

    data class Data(private val directions: List<Direction>) {

        private val rockSequence = listOf(
            Rock(listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3), 1),
            Rock(listOf(1 to 1, 0 to 1, 2 to 1, 1 to 0, 1 to 2), 3),
            Rock(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2), 3),
            Rock(listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0), 4),
            Rock(listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1), 2),
        )

        fun fallingRocks(targetRockCount: Long): Long {
            var directionIndex = 0
            fun nextDirection() = directions[directionIndex].also { directionIndex = (directionIndex + 1) % directions.size }

            var rockIndex = 0
            fun nextRock() = rockSequence[rockIndex].also { rockIndex = (rockIndex + 1) % rockSequence.size }

            var rockCount = 0L
            var totalHeight = 0
            val chamber = mutableListOf<BooleanArray>()

            fun fallRock() {
                val rock = nextRock()
                while (chamber.size < totalHeight + rock.height + 3) chamber.add(BooleanArray(7))

                val position = intArrayOf(totalHeight + 3, 2)
                rock.move(position, Direction.INIT, chamber)
                do {
                    rock.move(position, nextDirection(), chamber)
                } while (rock.move(position, Direction.DOWN, chamber))

                totalHeight = maxOf(totalHeight, position[0] + rock.height)
                ++rockCount
            }

            val records = mutableMapOf<Int, Pair<Int, Long>>() // [directionIndex] = (diff, rockCount)
            var prevTotalHeight = 0

            fun findPattern(): Boolean {
                val diff = totalHeight - prevTotalHeight

                if (rockIndex != 0) return false
                if (records[directionIndex]?.first == diff) return true

                records[directionIndex] = diff to rockCount
                prevTotalHeight = totalHeight

                return false
            }

            while (rockCount < targetRockCount && !findPattern()) {
                fallRock()
            }

            val patternRockCount = rockCount - records[directionIndex]!!.second
            val patternHeight = records.values
                .filter { (_, prevRockCount) -> prevRockCount >= records[directionIndex]!!.second }
                .sumOf { it.first }

            val remainRockCount = targetRockCount - rockCount
            val needPatternRound = remainRockCount / patternRockCount
            val needSingleRockCount = (remainRockCount % patternRockCount).toInt()

            repeat(needSingleRockCount) {
                fallRock()
            }

            return totalHeight.toLong() + patternHeight * needPatternRound
        }
    }

    override fun parse(input: String) = Data(input.map { if (it == '<') Direction.LEFT else Direction.RIGHT })

    override fun part1(data: Data) = data.fallingRocks(2022L)

    override fun part2(data: Data) = data.fallingRocks(1000000000000L)

    enum class Direction(val dy: Int, val dx: Int) {
        LEFT(0, -1), RIGHT(0, 1), DOWN(-1, 0), INIT(0, 0);
    }

    class Rock(private val points: List<Pair<Int, Int>>, val height: Int) {

        fun move(position: IntArray, direction: Direction, chamber: List<BooleanArray>): Boolean {
            set(position, chamber, false)
            val canMove = canMove(position, direction, chamber)
            if (canMove) {
                position[0] += direction.dy
                position[1] += direction.dx
            }
            set(position, chamber, true)
            return canMove
        }

        private fun canMove(position: IntArray, direction: Direction, chamber: List<BooleanArray>) = points
            .map { (dy, dx) -> position[0] + dy + direction.dy to position[1] + dx + direction.dx }
            .all { (newY, newX) ->
                newY in chamber.indices && newX in chamber[newY].indices && !chamber[newY][newX]
            }

        private fun set(position: IntArray, chamber: List<BooleanArray>, put: Boolean) {
            val (y, x) = position
            for ((dy, dx) in points) {
                chamber[y + dy][x + dx] = put
            }
        }
    }
}