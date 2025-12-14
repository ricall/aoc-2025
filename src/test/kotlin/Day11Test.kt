import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {
    val testInput = """
        |aaa: you hhh
        |you: bbb ccc
        |bbb: ddd eee
        |ccc: ddd eee fff
        |ddd: ggg
        |eee: out
        |fff: out
        |ggg: out
        |hhh: ccc fff iii
        |iii: out""".toInput()

    val testInput2 = """
        |svr: aaa bbb
        |aaa: fft
        |fft: ccc
        |bbb: tty
        |tty: ccc
        |ccc: ddd eee
        |ddd: hub
        |hub: fff
        |eee: dac
        |dac: fff
        |fff: ggg hhh
        |ggg: out
        |hhh: out""".toInput()

    val input = readInput("Day11")

    fun List<String>.parseInput() = this.associate { line ->
        val (node, nodes) = line.split(": ")
        node to nodes.trim().split(' ')
    }

    private val pathCache = mutableMapOf<Pair<String, String>, Long>()

    fun Map<String, List<String>>.countPaths(path: Pair<String, String>): Long {
        val (start, end) = path
        if (start == end) {
            return 1L
        }

        val map = this
        return pathCache.getOrPut(path, {
            map.getOrDefault(start, emptyList()).sumOf { newStart -> countPaths(newStart to end) }
        })
    }

    fun part1(input: List<String>): Long = input.parseInput().countPaths("you" to "out")

    fun part2(input: List<String>): Long = input.parseInput().run {
        countPaths("svr" to "dac") * countPaths("dac" to "fft") * countPaths("fft" to "out")
            + countPaths("svr" to "fft") * countPaths("fft" to "dac") * countPaths("dac" to "out")
    }

    @Test
    fun `part 1 test`() = assertEquals(5, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(649, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(2, part2(testInput2))

    @Test
    fun `part 2`() = assertEquals(458948453421420, part2(input))
}