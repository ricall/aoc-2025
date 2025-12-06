import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {
    val testInput = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
            "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
            "824824821-824824827,2121212118-2121212124"

    val input = readInput("day02")[0]

    fun String.parseRange() = this.split("-").let {
        it[0].toLong()..it[1].toLong()
    }

    fun part(input: String, matcher: Regex) = input.split(",")
        .map { it.parseRange() }
        .flatMap { longRange -> longRange.filter { matcher.matches(it.toString()) } }
        .sum()

    fun part1(input: String) = part(input, Regex("([0-9]+)\\1"))

    fun part2(input: String) = part(input, Regex("([0-9]+)\\1+"))

    @Test
    fun `part 1 test`() = assertEquals(1227775554, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(21898734247, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(4174379265, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(28915664389, part2(input))
}