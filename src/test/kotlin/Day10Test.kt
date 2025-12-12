import com.github.shiguruikai.combinatoricskt.combinations
import com.microsoft.z3.Context
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status.SATISFIABLE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {
    val testInput = """
        |[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        |[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        |[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}""".toInput()

    val input = readInput("Day10")

    val lineRegex = Regex("""^\[([.#]+)] ([()\d, ]+)\{(.+)}$""")
    fun List<String>.parseInput() = this.map { line ->
        val (_lights, _buttons, _joltages) = lineRegex.matchEntire(line)!!.destructured

        val lights = _lights.mapIndexed { index, s -> when (s) { '#' -> index else -> -1 } }
            .filter { it != -1 }
            .toSet()
        val buttons = _buttons.trim().split(" ")
            .map { it.substring(1..it.lastIndex - 1) }
            .map { it.split(",").map { it.toInt() }.toSet() }
        val joltages = _joltages.split(",").map { it.toInt() }
        Triple(lights, buttons, joltages)
    }

    fun xor(a: Set<Int>, b: Set<Int>) = a.union(b) - a.intersect(b)

    fun calculateMinimumButtonPresses(target: Set<Int>, buttons: List<Set<Int>>): Int {
        (1..buttons.lastIndex).forEach { buttonPresses ->
            buttons.combinations(buttonPresses).forEach { buttons ->
                val lights = buttons.fold(emptySet<Int>()) { lights, button -> xor(lights, button) }
                if (lights == target) return buttonPresses
            }
        }
        error("No minimum button presses found")
    }

    fun part1(input: List<String>): Int = input.parseInput().sumOf { (lights, buttons) ->
        calculateMinimumButtonPresses(lights, buttons)
    }

    fun part2(input: List<String>): Int = input.parseInput().sumOf { (_, buttons, joltages) ->
        Context().run {
            val opt = mkOptimize()
            val vars = buttons.indices.map { mkIntConst("n$it") }
            vars.forEach { opt.Add(mkGe(it, mkInt(0))) }
            joltages.withIndex().forEach { (index, joltage) ->
                val terms = buttons.withIndex().filter { index in it.value }.map { vars[it.index] }
                val total = when (terms.size) {
                    0 -> error("Expected at least one button press for each joltage")
                    1 -> terms[0]
                    else -> mkAdd(*terms.toTypedArray())
                }
                opt.Add(mkEq(total, mkInt(joltage)))
            }
            opt.MkMinimize(mkAdd(*vars.toTypedArray()))
            when (opt.Check()) {
                SATISFIABLE -> vars.sumOf { (opt.model.evaluate(it, true) as IntNum).int }
                else -> error("Failed")
            }
        }
    }

    @Test
    fun `part 1 test`() = assertEquals(7, part1(testInput))

    @Test
    fun `part 1`() = assertEquals(375, part1(input))

    @Test
    fun `part 2 test`() = assertEquals(33, part2(testInput))

    @Test
    fun `part 2`() = assertEquals(15377, part2(input))
}