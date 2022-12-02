abstract class Day<DATA, RESULT>(index: String) {

    protected val testInputName = "Day${index}_test"
    protected val puzzleInputName = "Day${index}"

    abstract fun part1(input: List<DATA>): RESULT
    abstract fun part2(input: List<DATA>): RESULT

    abstract val testInput: List<DATA>
    abstract val puzzleInput: List<DATA>

    abstract class LineInput<DATA, RESULT>(index: String, parse: (String) -> DATA) : Day<DATA, RESULT>(index) {
        override val testInput = readInputLines(testInputName).map { parse(it) }
        override val puzzleInput = readInputLines(puzzleInputName).map { parse(it) }
    }

    abstract class GroupInput<DATA, RESULT>(index: String, parse: (List<String>) -> DATA) : Day<DATA, RESULT>(index) {
        override val testInput = readInputGroups(testInputName).map { parse(it) }
        override val puzzleInput = readInputGroups(puzzleInputName).map { parse(it) }
    }

    fun solve1(testResult: RESULT) {
        check(part1(testInput) == testResult)
        println("Part One: ${part1(puzzleInput)}")
    }

    fun solve2(testResult: RESULT) {
        check(part2(testInput) == testResult)
        println("Part Two: ${part2(puzzleInput)}")
    }
}