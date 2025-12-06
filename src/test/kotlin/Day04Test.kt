import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day04Test {
    typealias Room = Set<Point>

    val testInput = """
        |..@@.@@@@.
        |@@@.@.@.@@
        |@@@@@.@.@@
        |@.@@@@..@.
        |@@.@@@@.@@
        |.@@@@@@@.@
        |.@.@.@.@@@
        |@.@@@.@@@@
        |.@@@@@@@@.
        |@.@.@@@.@.""".toInput()

    val input = readInput("Day04")

    data class Point(val x: Int, val y: Int) {
        fun neighbours(): List<Point> = listOf(
            Point(x - 1, y - 1),
            Point(x, y - 1),
            Point(x + 1, y - 1),
            Point(x - 1, y),
            Point(x + 1, y),
            Point(x - 1, y + 1),
            Point(x, y + 1),
            Point(x + 1, y + 1),
        )
    }

    fun List<String>.parseToRoom(): Room {
        val rolls = HashSet<Point>()
        this.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '@' -> rolls.add(Point(x, y))
                }
            }
        }
        return rolls
    }

    fun Room.findRemovableRolls() = this.filter { it.neighbours().count { this.contains(it) } < 4 }

    fun part1(input: List<String>) = input.parseToRoom()
        .findRemovableRolls()
        .size

    fun part2(input: List<String>): Int {
        var room = input.parseToRoom()
        var count = 0
        do {
            val removable = room.findRemovableRolls()
            count += removable.size
            room = room - removable
        } while (!removable.isEmpty())
        return count
    }

    @Test
    fun `part 1 test`() = assertEquals(13, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(1516, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(43, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(9122, part2(input))
}