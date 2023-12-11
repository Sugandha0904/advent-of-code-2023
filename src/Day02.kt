fun main() {
    fun getMaxColourValueYet(
        colourName: Pair<Int, String>,
        y: String,
        currentColourValue: Int?,
        currentColour: String
    ): Int? {
        if (colourName.second == currentColour) {
            val cubes = y.substring(0, colourName.first).trim().toIntOrNull()
            if (cubes != null) {
                return if (currentColourValue != null) maxOf(currentColourValue, cubes) else cubes
            }
        }
        return currentColourValue
    }

    fun calculateCubeColourNumbers(it: String, gameFactors: GameFactors) {
        val games = it.substringAfter(":").split(';')

        games.forEach { x ->
            val colours = x.split(',')
            colours.forEach { y ->
                val colourName = y.findAnyOf(globalColours, ignoreCase = true)
                if (colourName != null) {
                    gameFactors.redCubes = getMaxColourValueYet(colourName, y, gameFactors.redCubes, "Red")
                    gameFactors.blueCubes =
                        getMaxColourValueYet(colourName, y, gameFactors.blueCubes, "Blue")
                    gameFactors.greenCubes =
                        getMaxColourValueYet(colourName, y, gameFactors.greenCubes, "Green")
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val gameFactors = GameFactors()
            if (it.startsWith("game", true)) {
                gameFactors.gameNumber = it.substringAfter("Game").substringBefore(':').trim().toIntOrNull()
                if (gameFactors.gameNumber != null) {
                    calculateCubeColourNumbers(it, gameFactors)

                    if (gameFactors.redCubes != null && gameFactors.redCubes!! <= 12 &&
                        gameFactors.blueCubes != null && gameFactors.blueCubes!! <= 14 &&
                        gameFactors.greenCubes != null && gameFactors.greenCubes!! <= 13
                    ) {
                        gameFactors.isValid = true
                        sum += gameFactors.gameNumber!!
                    }
                }
            }
        }

        return sum
    }


    fun part2(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val gameFactors = GameFactors()
            if (it.startsWith("game", true)) {
                gameFactors.gameNumber = it.substringAfter("Game").substringBefore(':').trim().toIntOrNull()
                if (gameFactors.gameNumber != null) {
                    calculateCubeColourNumbers(it, gameFactors)
                    // there's a potential that one of the cubes required are zero, in that case what would the power be? For now, our test input doesn't have such data, so ignoring this case.
                    if (gameFactors.redCubes != null &&
                        gameFactors.blueCubes != null &&
                        gameFactors.greenCubes != null
                    ) {
                        gameFactors.power = gameFactors.redCubes!! * gameFactors.blueCubes!! * gameFactors.greenCubes!!
                        sum += gameFactors.power
                    }
                }
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    part1(testInput).println()
    part2(testInput).println()

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class GameFactors (
    var gameNumber: Int? = null,
    var redCubes: Int? = null,
    var greenCubes: Int? = null,
    var blueCubes: Int? = null,
    var isValid: Boolean = false,
    var power: Int = 0
)

private val globalColours = listOf<String>(
    "Red", "Green", "Blue"
)