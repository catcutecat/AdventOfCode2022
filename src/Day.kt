abstract class Day<T, E, P>(index: String) {
    private val puzzleInput = "Day${index}"
    private val testInput = "Day${index}_test"

    abstract fun part1(): T
    abstract fun part2(): E

    private var testing = false
    private val puzzleInputLines: List<String> by lazy { readInputLines(puzzleInput) }
    private val puzzleInputGroups: List<List<String>> by lazy { readInputGroups(puzzleInput) }
    private val testInputLines: List<String> by lazy { readInputLines(testInput) }
    private val testInputGroups: List<List<String>> by lazy { readInputGroups(testInput) }

    protected lateinit var parseInput: (List<String>) -> List<P>
    private val parsedTestInput: List<P> by lazy { parseInput(testInputLines) }
    private val parsedPuzzleInput: List<P> by lazy { parseInput(puzzleInputLines) }

    protected val inputLines: List<String> get() = if (testing) testInputLines else puzzleInputLines
    protected val inputGroups: List<List<String>> get() = if (testing) testInputGroups else puzzleInputGroups
    protected val parsedInput: List<P> get() = if (testing) parsedTestInput else parsedPuzzleInput

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