abstract class Day<T, E>(index: String) {
    private val puzzleInput = "Day${index}"
    private val testInput = "Day${index}_test"

    abstract fun part1(): T
    abstract fun part2(): E

    private var testing = false
    private val puzzleInputLines: List<String> by lazy { readInputLines(puzzleInput) }
    private val puzzleInputGroups: List<List<String>> by lazy { readInputGroups(puzzleInput) }
    private val testInputLines: List<String> by lazy { readInputLines(testInput) }
    private val testInputGroups: List<List<String>> by lazy { readInputGroups(testInput) }

    protected val inputLines: List<String> get() = if (testing) testInputLines else puzzleInputLines
    protected val inputGroups: List<List<String>> get() = if (testing) testInputGroups else puzzleInputGroups

    fun solve1(testResult: T) {
        testing = true
        check(part1() == testResult)
        testing = false
        println("Part One: ${part1()}")
    }

    fun solve2(testResult: E) {
        testing = true
        check(part2() == testResult)
        testing = false
        println("Part Two: ${part2()}")
    }
}