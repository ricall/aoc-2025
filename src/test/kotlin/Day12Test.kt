import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Test {
    val testInput = """
        |0:
        |###
        |##.
        |##.
        |
        |1:
        |###
        |##.
        |.##
        |
        |2:
        |.##
        |###
        |##.
        |
        |3:
        |##.
        |###
        |##.
        |
        |4:
        |###
        |#..
        |###
        |
        |5:
        |###
        |.#.
        |###
        |
        |4x4: 0 0 0 0 2 0
        |12x5: 1 0 1 0 2 2
        |12x5: 1 0 1 0 3 2""".trimMargin("|").parts()

    val input = readRaw("Day12").parts()

    fun List<String>.parseInput(): List<List<Int>> {
        val lastPart = this[lastIndex].lines()
        return lastPart.map { line ->
            val (dimensions, counts) = line.split(": ")
            val (x, y) = dimensions.split('x').map { it.toInt() }
            listOf(x, y, counts.split(" ").sumOf { it.toInt() })
        }
    }

    val checkers: List<(x: Int, y: Int, count: Int) -> Boolean> = listOf(
        { x, y, count -> x == 4 && y == 4 && count == 2 },
        { x, y, count -> x == 12 && y == 5 && count == 6 },
        { x, y, count ->
            val xSlots: Int = x / 3
            val ySlots: Int = y / 3
            (xSlots * ySlots) >= count
        }
    )

    fun part1(input: List<String>): Int = input.parseInput().count { q -> checkers.any { it(q[0], q[1], q[2]) } }

    @Test
    fun `part 1 test`() = assertEquals(2, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(427, part1(input))
}