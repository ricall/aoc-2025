import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05Test {
    val testInput = """
        |3-5
        |10-14
        |16-20
        |12-18
        |
        |1
        |5
        |8
        |11
        |17
        |32""".toInput()

    val input = readInput("day05")

    fun LongRange.overlaps(first: Long, last: Long) = first <= this.last && last >= this.first
    fun List<LongRange>.findOverlappingRange(first: Long, last: Long) = this.find { it.overlaps(first, last) }

    fun List<String>.parseRanges(): List<LongRange> {
        val ranges = mutableListOf<LongRange>()
        this.forEach { line ->
            var (first, last) = line.split("-").map { it.toLong() }
            var overlap = ranges.findOverlappingRange(first, last)
            while (overlap != null) {
                ranges.remove(overlap)
                first = Math.min(first, overlap.first)
                last = Math.max(last, overlap.last)
                overlap = ranges.findOverlappingRange(first, last)
            }
            ranges.add(first..last)
        }
        return ranges
    }

    fun part1(input: List<String>): Int {
        val ranges = input.firstPart().parseRanges()

        return input.secondPart().map { line ->
            val id = line.toLong()
            ranges.any { it.contains(id) }
        }.count { it }
    }

    fun part2(input: List<String>) = input.firstPart()
        .parseRanges()
        .sumOf { it.last - it.first + 1 }

    @Test
    fun `part 1 test`() = assertEquals(3, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(821, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(14, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(344771884978261, part2(input))
}