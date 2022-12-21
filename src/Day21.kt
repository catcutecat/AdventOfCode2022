import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day21.run {
            solve1(152L) // 309248622142100L
            solve2(301L) // 3757272361782L
        }
    }.let { println("Total: $it ms") }
}

object Day21 : Day.LineInput<Day21.Monkeys, Long>("21") {

    override fun parse(input: List<String>) = input.associate {
        it.split(": ").let { (name, job) ->
            name to job.split(" ")
        }
    }.let(::Monkeys)

    class Monkeys(private val data: Map<String, List<String>>) {
        private val cache = mutableMapOf<String, Long>()
        private val humanMonkeys = mutableSetOf("humn")

        fun getMonkeyValue(name: String): Long = cache[name] ?: data[name]!!.let { job ->
            if (job.size == 1) {
                job.first().toLong()
            } else {
                val (monkey1, monkey2) = job[0] to job[2]
                when (job[1]) {
                    "+" -> getMonkeyValue(monkey1) + getMonkeyValue(monkey2)
                    "-" -> getMonkeyValue(monkey1) - getMonkeyValue(monkey2)
                    "*" -> getMonkeyValue(monkey1) * getMonkeyValue(monkey2)
                    "/" -> getMonkeyValue(monkey1) / getMonkeyValue(monkey2)
                    else -> error("Should not reach here.")
                }.also { if (monkey1 in humanMonkeys || monkey2 in humanMonkeys) humanMonkeys.add(name) }
            }.also { cache[name] = it }
        }

        fun getHumanMonkeyValue(): Long {
            fun getHumanValue(name: String, expectValue: Long): Long = if (name == "humn") {
                expectValue
            } else {
                val job = data[name]!!
                val (monkey1, monkey2) = job[0] to job[2]
                when {
                    job[1] == "+" && monkey1 in humanMonkeys -> getHumanValue(monkey1, expectValue - getMonkeyValue(monkey2))
                    job[1] == "-" && monkey1 in humanMonkeys -> getHumanValue(monkey1, expectValue + getMonkeyValue(monkey2))
                    job[1] == "*" && monkey1 in humanMonkeys -> getHumanValue(monkey1, expectValue / getMonkeyValue(monkey2))
                    job[1] == "/" && monkey1 in humanMonkeys -> getHumanValue(monkey1, expectValue * getMonkeyValue(monkey2))
                    job[1] == "+" -> getHumanValue(monkey2, expectValue - getMonkeyValue(monkey1))
                    job[1] == "-" -> getHumanValue(monkey2, getMonkeyValue(monkey1) - expectValue)
                    job[1] == "*" -> getHumanValue(monkey2, expectValue / getMonkeyValue(monkey1))
                    job[1] == "/" -> getHumanValue(monkey2, getMonkeyValue(monkey1) / expectValue)
                    else -> error("Should not reach here.")
                }
            }

            val job = data["root"]!!
            val (monkey1, monkey2) = job[0] to job[2]
            return if (monkey1 in humanMonkeys) getHumanValue(monkey1, getMonkeyValue(monkey2))
            else getHumanValue(monkey2, getMonkeyValue(monkey1))
        }
    }

    override fun part1(data: Monkeys) = data.getMonkeyValue("root")

    override fun part2(data: Monkeys) = data.getHumanMonkeyValue()
}