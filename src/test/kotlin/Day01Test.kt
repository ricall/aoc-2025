import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day01Test {
    companion object {
        const val STARTING_POSITION = 50
        const val DIAL_SIZE = 100
    }

    val testInput = """
            |L68
            |L30
            |R48
            |L5
            |R60
            |L55
            |L1
            |L99
            |R14
            |L82""".toInput()

    val input = readInput("Day01")

    fun String.parseMove() = when (this[0]) {
        'L' -> this.substring(1).toInt() * -1
        'R' -> this.substring(1).toInt()
        else -> throw IllegalArgumentException(this)
    }

    fun Int.toStep() = when {
        this > 0 -> 1
        this < 0 -> -1
        else -> 0
    }

    fun part1(input: List<String>): Int {
        var position = STARTING_POSITION
        val parts = input.map { command ->
            position = (position + command.parseMove()).mod(DIAL_SIZE)
            position
        }
        return parts.count { it == 0 }
    }

    fun part2(input: List<String>): Int {
        var position = STARTING_POSITION
        var count = 0
        input.forEach { command ->
            val amount = command.parseMove()
            val step = amount.toStep()
            val stepCount = amount.absoluteValue

            repeat(stepCount) {
                position = (position + step).mod(DIAL_SIZE)
                if (position == 0) {
                    count++
                }
            }
        }
        return count
    }

    @Test
    fun `part 1 test`() = assertEquals(3, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(969, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(6, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(5887, part2(input))
}