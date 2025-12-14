import java.math.BigInteger
import java.security.MessageDigest
import java.util.stream.Collectors
import kotlin.io.path.Path
import kotlin.io.path.readText

fun String.toInput() = this.trimMargin("|").lines()


fun readRaw(name: String) = Path("input/$name.txt").readText()
fun readInput(name: String) = readRaw(name).lines()
fun String.parts(): List<String> = this.split("\n\n")
fun List<String>.firstPart() = this.stream().takeWhile { !it.isBlank() }.collect(Collectors.toList())
fun List<String>.secondPart() = this.stream().dropWhile { !it.isBlank() }.skip(1).collect(Collectors.toList())

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
