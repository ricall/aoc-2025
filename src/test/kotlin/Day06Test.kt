import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.collections.map

class Day06Test {
    val testInput = """
        |123 328  51 64 
        | 45 64  387 23 
        |  6 98  215 314
        |*   +   *   +  """.toInput()

    val input = readInput("day06")

    fun parseWorksheet(input: List<String>): Sequence<Pair<Char, List<String>>> = sequence {
        val operatorLine = input[input.lastIndex]
        val lineLength = input[0].length
        var index = 0
        while (index >= 0) {
            val nextIndex = operatorLine.indexOfAny(listOf("+", "*"), index + 1)
            val endIndex = when (nextIndex > 0) {
                true -> nextIndex - 1
                else -> lineLength
            }
            yield(operatorLine[index] to (0..<input.lastIndex).map {
                input[it].substring(index, endIndex)
            })
            index = nextIndex
        }
    }

    fun evaluate(operator: Char, values: List<String>) = when (operator) {
        '+' -> values.fold(0L) { acc, value -> acc + value.trim().toLong() }
        '*' -> values.fold(1L) { acc, value -> acc * value.trim().toLong() }
        else -> error("Unknown operator $operator")
    }

    fun part1(input: List<String>) = parseWorksheet(input)
        .map { (operator, values) -> evaluate(operator, values) }.sum()

    fun List<String>.transposeRightToLeft() = (this[0].length - 1 downTo 0).map { column ->
        this.indices.joinToString("") { row -> this[row][column].toString() }
    }

    fun part2(input: List<String>): Long = parseWorksheet(input)
        .map { (operator, values) -> evaluate(operator, values.transposeRightToLeft()) }.sum()

    @Test
    fun `part 1 test`() = assertEquals(4277556, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(5381996914800, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(3263827, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(9627174150897, part2(input))
}