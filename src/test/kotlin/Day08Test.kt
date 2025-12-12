import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.pow
import kotlin.math.sqrt

class Day08Test {
    val testInput = """
        |162,817,812
        |57,618,57
        |906,360,560
        |592,479,940
        |352,342,300
        |466,668,158
        |542,29,236
        |431,825,988
        |739,650,466
        |52,470,668
        |216,146,977
        |819,987,18
        |117,168,530
        |805,96,715
        |346,949,466
        |970,615,88
        |941,993,340
        |862,61,35
        |984,92,344
        |425,690,689""".toInput()

    val input = readInput("Day08")

    data class Point3D(val x: Int, val y: Int, val z: Int) {
        companion object {
            fun fromString(input: String): Point3D {
                val (x, y, z) = input.split(',').map(String::toInt)
                return Point3D(x, y, z)
            }
        }
        fun distanceTo(other: Point3D) = sqrt(listOf(x-other.x, y-other.y, z-other.z).sumOf { it.toDouble().pow(2.0) })
        fun closestPointTo(points: List<Point3D>) = points.map { it to it.distanceTo(this) }
            .minByOrNull { it.second }!!.first
    }

    fun List<String>.parsePoints() = this.map(Point3D::fromString)
    fun MutableList<MutableSet<Point3D>>.connectedTo(point: Point3D) = this.find { it.contains(point) } ?: error("Invalid State")
    fun List<Point3D>.pairs(): List<Pair<Point3D, Point3D>> {
        val pairs = mutableListOf<Pair<Point3D, Point3D>>()
        for (i in 0 until size) {
            for (j in i + 1 until size) {
                pairs.add(Pair(this[i], this[j]))
            }
        }
        return pairs
    }
    fun List<Point3D>.connectJunctions(junctionCount: Int): List<Int> {
        val list = this

        val pairs = list.pairs().sortedBy { (start, end) -> start.distanceTo(end) }
            .take(junctionCount)

        val circuits = list.map { mutableSetOf(it) }.toMutableList()
        pairs.forEach { (start, end) ->
            val startCircuit = circuits.connectedTo(start)
            if (!startCircuit.contains(end)) {
                circuits.remove(startCircuit)
                circuits.connectedTo(end).addAll(startCircuit)
            }
        }
        return circuits.map { it.size }.sortedBy { -it }
    }

    fun List<Point3D>.connectAllJunctions(): Long {
        val list = this
        val pairs = list.pairs().sortedBy { (start, end) -> start.distanceTo(end) }
        val circuits = list.map { mutableSetOf(it) }.toMutableList()

        pairs.forEach { (start, end) ->
            val endCircuit = circuits.connectedTo(end)
            if (!endCircuit.contains(start)) {
                circuits.remove(endCircuit)
                circuits.connectedTo(start).addAll(endCircuit)
            }
            if (circuits.size == 1) {
                return start.x.toLong() * end.x.toLong()
            }
        }
        throw error("Invalid State")
    }

    fun part1(input: List<String>, junctions: Int): Int = input.parsePoints()
        .connectJunctions(junctions)
        .take(3)
        .fold(1) { total, next -> total * next }

    fun part2(input: List<String>): Long = input.parsePoints().connectAllJunctions()

    @Test
    fun `part 1 test`() = assertEquals(40, part1(testInput, 10))

    @Test
    fun `part 1`() = assertEquals(171503, part1(input, 1000))

    @Test
    fun `part 2 test`() = assertEquals(25272L, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(9069509600L, part2(input))
}