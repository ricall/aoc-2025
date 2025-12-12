import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Stack
import kotlin.collections.forEach
import kotlin.collections.lastIndex
import kotlin.math.abs

class Day09Test {
    val testInput = """
        |7,1
        |11,1
        |11,7
        |9,7
        |9,5
        |2,5
        |2,3
        |7,3""".toInput()

    val input = readInput("Day09")

    data class Point(val x: Long, val y: Long) {
        companion object {
            fun fromString(input: String): Point {
                val (x, y) = input.split(',').map(String::toLong)
                return Point(x, y)
            }
        }

        fun neighbours() = listOf(Point(x - 1, y), Point(x + 1, y), Point(x, y - 1), Point(x, y + 1))
    }

    fun List<String>.parsePoints() = this.map(Point::fromString)

    fun part1(input: List<String>): Long = input.parsePoints()
        .pairs()
        .maxBy { it.size() }.size()

    class CompressedGrid(val points: List<Point>) {
        val gridx = compressDimension { p -> p.x }
        val gridy = compressDimension { p -> p.y }
        val grid = mutableMapOf<Point, Char>().let { grid ->
            (points + points[0]).zipWithNext().forEach { (start, end) ->
                val (startx, endx) = minmax(gridx[start.x], gridx[end.x])
                val (starty, endy) = minmax(gridy[start.y], gridy[end.y])
                (startx..endx).forEach { x ->
                    (starty..endy).forEach { y ->
                        grid[Point(x, y)] = '#'
                    }
                }
            }
            grid
        }

        fun compressDimension(dimension: (Point) -> Long): Map<Long, Long> {
            val candidates = points
                .map { point -> dimension(point) }
                .toSet()
                .sorted()
                .toMutableList()

            // Expand the dimension either side so flood fill can fill around the shape
            candidates.add(0, -1)
            candidates.add(candidates[candidates.lastIndex] + 1)
            return candidates.withIndex().associate { (index, value) -> value to index.toLong() }
        }

        fun minmax(v1: Long?, v2: Long?): Pair<Long, Long> {
            if (v1 == null || v2 == null) error("illegal state")
            return when (v1 < v2) {
                true -> v1 to v2
                else -> v2 to v1
            }
        }

        fun floodfill() {
            val gridXRange = 0..gridx.size - 1
            val gridYRange = 0..gridy.size - 1

            val candidates = Stack<Point>()
            candidates.push(Point(0, 0))
            while (candidates.isNotEmpty()) {
                val point = candidates.pop()
                grid[point] = ' '
                point.neighbours()
                    .filter { p -> p.x in gridXRange && p.y in gridYRange && grid[p] == null }
                    .forEach { candidates.push(it) }
            }
        }

        fun display() {
            (0..<gridy.size).forEach { y ->
                (0..<gridx.size).forEach { x ->
                    print(grid.getOrDefault(Point(x.toLong(), y.toLong()), '.'))
                }
                print('\n')
            }
        }

        fun perimeter(start: Point, end: Point): Set<Point> {
            val (startx, endx) = minmax(gridx[start.x], gridx[end.x])
            val (starty, endy) = minmax(gridy[start.y], gridy[end.y])

            return mutableSetOf<Point>().apply {
                for (x in startx..endx) {
                    add(Point(x, starty))
                    add(Point(x, endy))
                }
                for (y in starty..endy) {
                    add(Point(startx, y))
                    add(Point(endx, y))
                }
            }
        }

        fun isTileOnly(start: Point, end: Point) = perimeter(start, end).all { grid[it] != ' ' }

        fun largestTileOnlyRectangleBetweenPoints() = points.pairs()
            .filter { (start, end) -> isTileOnly(start, end) }
            .maxBy { it.size() }.size()
    }

    fun part2(input: List<String>) = CompressedGrid(input.parsePoints()).run {
        floodfill()
//        display()
        largestTileOnlyRectangleBetweenPoints()
    }

    @Test
    fun `part 1 test`() = assertEquals(50L, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(4745816424L, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(24L, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(1351617690L, part2(input))

    companion object {
        fun List<Point>.pairs(): List<Pair<Point, Point>> {
            val pairs = mutableListOf<Pair<Point, Point>>()
            for (i in 0 until size) {
                for (j in i + 1 until size) {
                    pairs.add(Pair(this[i], this[j]))
                }
            }
            return pairs
        }

        fun Pair<Point, Point>.size(): Long {
            val width = abs(first.x - second.x) + 1
            val height = abs(first.y - second.y) + 1
            return width * height
        }
    }

}