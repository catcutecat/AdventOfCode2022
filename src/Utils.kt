import java.io.File
import java.lang.System.lineSeparator
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInputLines(name: String) = File("input", name)
    .readLines()

fun readInputGroups(name: String) = File("input", name)
    .readText()
    .split(lineSeparator() + lineSeparator())
    .map { it.split(lineSeparator()) }

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')
