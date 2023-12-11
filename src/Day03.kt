fun main() {
    val validSymbols = mutableListOf<GearSymbol>()

    fun isValid(validNumber: ValidNumbers, list: List<List<Char>>): Boolean {
        for (i in validNumber.startIndex - 1 until validNumber.endIndex + 1) {
            if (i > -1 && i < list[0].size) {
                for (j in validNumber.column - 1 until validNumber.column + 2) {
                    if(j > -1 && j < list.size) {
                        if(!list[j][i].isDigit() && !list[j][i].isWhitespace()) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun getMatrixInLists(input: List<String>, updateGearSymbol: Boolean = false): MutableList<MutableList<Char>> {
        val list = mutableListOf<MutableList<Char>>()

        input.forEach {
            val row = mutableListOf<Char>()

            it.forEach { c ->
                if (c != '.') {
                    row.add(c)
                    if (updateGearSymbol && c == '*') {
                        validSymbols.add(GearSymbol(
                            symbol = '*',
                            index = row.size - 1,
                            rowNumber = list.size
                        ))
                    }
                } else row.add(' ')
            }
            list += row
        }
        return list
    }

    fun part1(input: List<String>): Int {
        val list = getMatrixInLists(input)

        val validNumbers = mutableListOf<ValidNumbers>()

        for (i in 0 until list.size) {
            val row = list[i]
            var startIndex: Int = -1
            var number = ""
            for (j in 0 until row.size) {
                if (row[j].isDigit()) {
                    if (startIndex == -1) {
                        startIndex = j
                    }
                    number += row[j]
                } else {
                    if (number.isNotBlank()) {
                        val num = ValidNumbers(
                            number = number.toInt(),
                            startIndex = startIndex,
                            endIndex = j,
                            column = i,
                        )
                        num.isValid = isValid(num, list)
                        validNumbers.add(num)
                        startIndex = -1
                        number = ""
                    }
                }
            }
            if (number.isNotBlank()) {
                val num = ValidNumbers(
                    number = number.toInt(),
                    startIndex = startIndex,
                    endIndex = row.size,
                    column = i,
                )
                num.isValid = isValid(num, list)
                validNumbers.add(num)
            }
        }
        return validNumbers.filter { it.isValid }.sumOf { it.number }
    }

    fun findNumbersAroundSymbol(list: List<List<Char>>) : Long {
        var sum = 0L
        println(validSymbols)

        validSymbols.forEach {
            var digitCount = 0
            val locations = mutableListOf<Location>()
            for (i in it.rowNumber - 1 until it.rowNumber + 2) {
                for (j in it.index - 1 until it.index + 2) {
                    if (j > -1 && i > -1 && j < list[0].size - 1 && i < list.size) {
                        if ((list[i][j].isDigit() && !list[i][j + 1].isDigit()) || (list[i][j].isDigit() && j == it.index + 1)) {
                            digitCount++
                            locations.add(Location(i, j))
                        }
                    }
                }
            }
            println(locations)

            if (digitCount == 2) {
                val validNumbers = mutableListOf<Int>()
                var gearRatio = 1
                locations.forEach { x ->
                    var number = ""
                    // get number 2 spaces before
                    var numberStartingAt = 0
                    beforeLoop@ for (index in x.yCoordinate - 1 downTo x.yCoordinate - 3) {
                        if (x.xCoordinate > -1 && index > -1 && index < list[0].size - 1 && x.xCoordinate < list.size) {
                            if (!list[x.xCoordinate][index].isDigit()) {
                                numberStartingAt = index + 1
                                break@beforeLoop
                            }
                        }
                    }

                    loop@ for (i in numberStartingAt  until numberStartingAt + 4) { // assuming all maximum 3 digit numbers
                        println("i - ${x.xCoordinate} - j - $i ")
                        if (i < list[x.xCoordinate].size && list[x.xCoordinate][i].isDigit()) {
                            number += list[x.xCoordinate][i]
                        } else {
                            if (number.isNotBlank()) {
                                validNumbers.add(number.toInt())
                                gearRatio *= number.toInt()
                                number = ""
                                break@loop
                            }
                        }
                    }
                    if (number.isNotBlank()) {
                        validNumbers.add(number.toInt())
                        gearRatio *= number.toInt()
                    }

                }
                it.validNumbers = validNumbers
                it.gearRatio = gearRatio.toLong()
                sum += it.gearRatio
//                println(it.validNumbers + " " + it.gearRatio + " " + sum)
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val list = getMatrixInLists(input, true)

        return findNumbersAroundSymbol(list)
    }


//    val testInput = readInput("Day03_test")
//    println(part1(testInput))
//    println(part2(testInput))
//
    val input = readInput("Day03")
//    println(part1(input))
    println(part2(input))
}

data class ValidNumbers (
    var number: Int = 0,
    var startIndex: Int = 0,
    var endIndex: Int = 0,
    var column: Int = 0,
    var isValid: Boolean = false
)

data class Location (
    var xCoordinate: Int,
    var yCoordinate: Int
)

data class GearSymbol (
    val symbol: Char = '*',
    var index: Int = 0,
    var rowNumber: Int = 0,
    var validNumbers: List<Int> = mutableListOf(),
    var gearRatio: Long = 0
)