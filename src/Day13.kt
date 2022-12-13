import kotlin.math.sign
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day13.run {
            solve1(13) // 6369
            solve2(140) // 25800
        }
    }.let { println("Total: $it ms") }
}

object Day13 : Day.GroupInput<List<Day13.Data>, Int>("13") {

    data class Data(val packet1: Packet, val packet2: Packet)

    sealed class Packet: Comparable<Packet> {
        data class DataList(val value: List<Packet>) : Packet()
        data class DataNumber(val value: Int) : Packet()

        override fun compareTo(other: Packet): Int = when {
            this is DataNumber && other is DataNumber -> (value - other.value).sign
            this is DataNumber -> DataList(listOf(this)).compareTo(other)
            other is DataNumber -> compareTo(DataList(listOf(other)))
            this is DataList && other is DataList -> run {
                for (i in 0 .. minOf(value.lastIndex, other.value.lastIndex)) {
                    val cmp = value[i].compareTo(other.value[i])
                    if (cmp != 0) return cmp
                }
                (value.size - other.value.size).sign
            }
            else -> error("Should not reach here.")
        }

        companion object {
            fun fromString(s: String): Packet {
                var i = 0

                fun parsePacket(): Packet = if (s[i] == '[') {
                    val list = mutableListOf<Packet>()
                    ++i
                    while (i < s.length && s[i] != ']') {
                        if (s[i] == ',') ++i
                        list.add(parsePacket())
                    }
                    ++i
                    DataList(list)
                } else {
                    var num = 0
                    while (s[i] in '0'..'9') num = num * 10 + (s[i++] - '0')
                    DataNumber(num)
                }

                return parsePacket()
            }
        }
    }

    override fun parse(input: List<List<String>>) = input.map { (a, b) ->
        Data(Packet.fromString(a), Packet.fromString(b))
    }

    override fun part1(data: List<Data>): Int = data.withIndex()
        .filter { (_, pair) -> pair.run { packet1 < packet2 } }
        .sumOf { it.index + 1 }

    override fun part2(data: List<Data>): Int {
        val flatList = data.flatMap { listOf(it.packet1, it.packet2) }.sorted()
        val index1 = -flatList.binarySearch(Packet.fromString("[[2]]")) - 1
        val index2 = -flatList.binarySearch(Packet.fromString("[[6]]")) - 1
        return (index1 + 1) * (index2 + 2)
    }
}