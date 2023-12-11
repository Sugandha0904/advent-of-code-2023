import kotlin.enums.enumEntries

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun part1(input: List<String>): Long {
        var sum: Long = 0
        if (input.isNotEmpty()) {
            input.forEach {
                val out = it.find { x ->
                    x.isDigit()
                }.toString() + "" + it.findLast { x ->
                    x.isDigit()
                }
                sum += out.toIntOrNull() ?: 0

            }
        }
        return sum
    }

    fun findNumber(
        numberInStringIndexVal: Pair<Int, String>?,
        realNumberIndex: Int,
        it: String,
        ascending: Boolean
    ) = if (numberInStringIndexVal == null && realNumberIndex == -1) {
        0
    } else if (numberInStringIndexVal == null) {
        it[realNumberIndex]
    } else if (realNumberIndex == -1) {
        enumValueOf<Numbers>(numberInStringIndexVal.second.uppercase()).value
    } else if ((ascending && realNumberIndex < numberInStringIndexVal.first) || (!ascending && realNumberIndex > numberInStringIndexVal.first)) {
        it[realNumberIndex]
    } else {
        enumValueOf<Numbers>(numberInStringIndexVal.second.uppercase()).value
    }

    fun part2(input: List<String>): Long {
        var sum: Long = 0
        val stringNames = enumEntries<Numbers>().map { it.name }

        if (input.isNotEmpty()) {
            input.forEach {
                val firstString = it.findAnyOf(stringNames, 0, ignoreCase = true)
                val firstNumber = it.indexOfFirst { x -> x.isDigit() }
                val one =
                    findNumber(firstString, firstNumber, it, true)
                val lastString = it.findLastAnyOf(stringNames, ignoreCase = true)
                val lastNumber = it.indexOfLast { x -> x.isDigit() }
                val last =
                    findNumber(lastString, lastNumber, it, false)
                val output = ("" + one + "" + last)

                sum += output.toIntOrNull() ?: 0
            }
        }
        return sum
    }

    val testInput = readInput("Day01_test")
    println(part1(testInput))

    val testInput2 = readInput("Day01_part2_test")
    println(part2(testInput2))


    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

enum class Numbers(val value: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9)
}