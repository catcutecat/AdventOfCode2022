import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        Day07.run {
            solve1(95437) // 1749646
            solve2(24933642) // 1498966
        }
    }.let { println("Total: $it ms") }
}

object Day07 : Day.LineInput<Day07.Directory, Int>("07") {

    class Directory(
        val parent: Directory?
    ) {
        val directories: MutableMap<String, Directory> = mutableMapOf()
        val files: MutableList<File> = mutableListOf()
        var totalSize = 0

        fun updateTotalSize() {
            val dirSize = directories.values.sumOf {
                it.updateTotalSize()
                it.totalSize
            }
            val fileSize = files.sumOf { it.size }
            totalSize = dirSize + fileSize
        }

        fun allSize(): List<Int> = listOf(totalSize) + directories.values.flatMap(Directory::allSize)
    }

    data class File(val size: Int, val name: String)

    override fun parse(input: List<String>) = Directory(null).also { root ->
        input.fold(root) { curr, command ->
            val (a, b, c) = "$command ".split(" ")
            when {
                a == "$" && b == "cd" && c == "/" -> root
                a == "$" && b == "cd" && c == ".." -> curr.parent!!
                a == "$" && b == "cd" -> curr.directories[c]!!
                a == "$" && b == "ls" -> curr
                a == "dir" -> curr.apply { directories[b] = Directory(curr) }
                else -> curr.apply { files.add(File(a.toInt(), b)) }
            }
        }
    }.apply { updateTotalSize() }

    override fun part1(data: Directory) = data.allSize().sumOf { if (it <= 100_000) it else 0 }

    override fun part2(data: Directory) = (30_000_000 - (70_000_000 - data.totalSize)).let { min ->
        data.allSize().minOf { if (it >= min) it else Int.MAX_VALUE }
    }
}