import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day07Test {
    val testInput = """
        |.......S.......
        |...............
        |.......^.......
        |...............
        |......^.^......
        |...............
        |.....^.^.^.....
        |...............
        |....^.^...^....
        |...............
        |...^.^...^.^...
        |...............
        |..^...^.....^..
        |...............
        |.^.^.^.^.^...^.
        |...............""".toInput()

    val input = readInput("Day07")

    fun List<String>.initialStartBeamIndexAndSplits() = setOf(this[0].indexOf('S')) to 0

    fun String.toPotentialSplitIndexes(index: Int) = when (this[index]) { '^' -> listOf(index - 1, index + 1) else -> listOf(index) }

    fun part1(input: List<String>): Int = input.fold(input.initialStartBeamIndexAndSplits()) { (beams, splits), line ->
        beams.flatMap { index -> line.toPotentialSplitIndexes(index) }
            .let { it.toSet() to (splits + it.size - beams.size) }
    }.second

    fun List<String>.initialStartBeamIndexAndCount() = mapOf(this[0].indexOf('S') to 1L)

    fun part2(input: List<String>): Long = input.fold(input.initialStartBeamIndexAndCount()) { beamCounts, line ->
        beamCounts.entries.fold(mutableMapOf<Int, Long>()) { nextBeamCount, (index, count) ->
            line.toPotentialSplitIndexes(index).forEach { index ->
                nextBeamCount[index] = nextBeamCount.getOrDefault(index, 0L) + count
            }
            nextBeamCount
        }
    }.values.sum()

    @Test
    fun `part 1 test`() = assertEquals(21, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(1687, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(40L, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(390684413472684, part2(input))
}