import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03Test {
    val testInput = """
        |987654321111111
        |811111111111119
        |234234234234278
        |818181911112111""".toInput()

    val input = readInput("Day03")

    fun String.nextJolt(): Pair<Int, String> {
        ('9'.downTo('1')).forEach { search ->
            val index = this.indexOf(search)
            if (index >= 0) {
                return@nextJolt index to search.toString()
            }
        }
        error("Should not happen")
    }

    fun String.largestJolt(digits: Int): Long {
        var start = 0
        return (digits-1 downTo 0).joinToString("") { index ->
            val (newStart, value) = this.substring(start, this.length - index).nextJolt()
            start += newStart + 1
            value
        }.toLong()
    }

    fun findLargestJoltFor(digits: Int) = { input: List<String> -> input.sumOf { it.largestJolt(digits) } }

    val part1 = findLargestJoltFor(2)

    val part2 = findLargestJoltFor( 12)

    @Test
    fun `part 1 test`() = assertEquals(357, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(17100, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(3121910778619, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(170418192256861, part2(input))
}