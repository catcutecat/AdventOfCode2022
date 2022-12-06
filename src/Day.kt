abstract class Day<DATA, RESULT>(index: String) {

    protected val testInputName = "Day${index}_test"
    protected val puzzleInputName = "Day${index}"

    protected abstract fun part1(data: DATA): RESULT
    protected abstract fun part2(data: DATA): RESULT

    protected abstract val testInput: DATA
    protected abstract val puzzleInput: DATA

    abstract class RowInput<DATA, RESULT>(index: String) : Day<DATA, RESULT>(index) {
        abstract fun parse(input: String): DATA

        override val testInput = readInputText(testInputName).let(::parse)
        override val puzzleInput = readInputText(puzzleInputName).let(::parse)
    }

    abstract class LineInput<DATA, RESULT>(index: String) : Day<DATA, RESULT>(index) {
        abstract fun parse(input: List<String>): DATA

        override val testInput = readInputLines(testInputName).let(::parse)
        override val puzzleInput = readInputLines(puzzleInputName).let(::parse)
    }

    abstract class GroupInput<DATA, RESULT>(index: String) : Day<DATA, RESULT>(index) {
        abstract fun parse(input: List<List<String>>): DATA

        override val testInput = readInputGroups(testInputName).let(::parse)
        override val puzzleInput = readInputGroups(puzzleInputName).let(::parse)
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