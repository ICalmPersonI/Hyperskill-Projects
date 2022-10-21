package chess

import java.lang.IndexOutOfBoundsException
import kotlin.math.abs
import kotlin.system.exitProcess

class Application {

    private val field: Array<Array<String>> = Array(8) { Array(8) { "   " } }

    private var yStart: Int
    private var xStart: Int
    private var yEnd: Int
    private var xEnd: Int

    private var blackCaptureAvailable: Boolean
    private var whiteCaptureAvailable: Boolean

    private val validCoordinates: Regex = Regex("[abcdefgh][1-8][abcdefgh][1-8]")

    init {
        this.field[1].fill(" B ")
        this.field[field.lastIndex - 1].fill(" W ")
        this.yStart = 0
        this.xStart = 0
        this.yEnd = 0
        this.xEnd = 0
        this.blackCaptureAvailable = false
        this.whiteCaptureAvailable = false
    }


    fun run() {
        println("Pawns-Only Chess")
        println("First Player's name:")
        val firstPlayer: String = readLine()!! + "'s"
        println("Second Player's name:")
        val secondPlayer: String = readLine()!! + "'s"
        val players: Map<String, String> = mutableMapOf(firstPlayer to secondPlayer, secondPlayer to firstPlayer)

        printField()

        var player: String = secondPlayer
        while (true) {
            player = players[player]!!
            while (true) {
                println("$player turn:")
                val coordinates: String = readLine()!!
                val side: String = if (player == firstPlayer) " W " else " B "
                if (validation(coordinates, side)) {
                    move(side)
                    printField()
                    when (checkingStage()) {
                        1 -> {
                            println("White Wins!")
                            exit()
                        }
                        2 -> {
                            println("Black Wins!")
                            exit()
                        }
                    }

                    if (checkingPossibilityOfMove(if (side == " W ") " B " else " W ")) {
                        println("Stalemate!")
                        exit()
                    }
                    break

                }
            }
        }
    }

    private fun validation(coordinates: String, side: String): Boolean {
        return when {
            validCoordinates.matches(coordinates) -> {
                yStart = getY(coordinates[1])
                xStart = getX(coordinates[0].code)
                yEnd = getY(coordinates[3])
                xEnd = getX(coordinates[2].code)
                when {
                    (side == " W " && yStart < yEnd) || (side == " B " && yStart > yEnd) -> return invalidInput()

                    field[yStart][xStart] != side -> {
                        println(
                            "No ${if (side == " B ") "black" else "white"}" +
                                    " pawn at ${coordinates[0].toString() + coordinates[1]}"
                        )
                        return false
                    }

                    xStart != xEnd && abs(xStart - xEnd) != 1 -> return invalidInput()

                    abs(xStart - xEnd) == 1 && field[yEnd][xEnd] == "   "
                            && !(blackCaptureAvailable || whiteCaptureAvailable) -> return invalidInput()

                    (side == " W " && field[yStart - 1][xStart] != "   ")
                            || side == " B " && field[yStart + 1][xStart] != "   " -> return invalidInput()

                    abs(yStart - yEnd) !in 1..2 -> return invalidInput()

                    abs(yStart - yEnd) == 2 && yStart != if (side == " W ") 6 else 1 -> return invalidInput()
                }

                return true
            }
            coordinates == "exit" -> exit()
            else -> {
                println("Invalid Input")
                false
            }
        }
    }

    private fun move(side: String) {
        if (blackCaptureAvailable && side == " W ") {
            field[yEnd + 1][xEnd] = "   "
        } else if (whiteCaptureAvailable && side == " B ") {
            field[yEnd - 1][xEnd] = "   "
        }


        field[yStart][xStart] = "   "
        field[yEnd][xEnd] = side

        blackCaptureAvailable = false
        whiteCaptureAvailable = false

        if (side == " W ") {
            if (field[yEnd][xEnd - if (xEnd > 0) 1 else 0] == " B "
                || field[yEnd][xEnd + if (xEnd < 6) 1 else 0] == " B "
            ) {
                whiteCaptureAvailable = true
            }
        } else if (side == " B ") {
            if (field[yEnd][xEnd - if (xEnd > 0) 1 else 0] == " W "
                || field[yEnd][xEnd + if (xEnd < 6) 1 else 0] == " W "
            ) {
                blackCaptureAvailable = true
            }
        }
    }

    private fun checkingPossibilityOfMove(side: String): Boolean {
        for (i in field.indices) {
            for (j in field[i].indices) {
                if (field[i][j] == side) {
                    try {
                        when {
                            field[if (side == " W ") i - 1 else i + 1][j] == "   " -> return false
                            field[if (side == " W ") i - 1 else i + 1][j + 1] != "   " -> return false
                            field[if (side == " W ") i - 1 else i + 1][j - 1] != "   " -> return false
                        }
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
            }
        }

        return true
    }

    private fun checkingStage(): Int {
        return when {
            field[0].contains(" W ") || !field.flatMap { it.toList() }.any { it.contains(" B ") } -> 1
            field[7].contains(" B ") || !field.flatMap { it.toList() }.any { it.contains(" W ") } -> 2
            else -> 0
        }
    }

    private fun invalidInput(): Boolean {
        println("Invalid Input")
        return false
    }

    private fun exit(): Boolean {
        println("Bye!")
        exitProcess(0)
        return true
    }

    private fun getX(coordX: Int): Int = coordX - 97
    private fun getY(coordY: Char): Int = abs(coordY.toString().toInt() - 8)

    private fun printField() {
        println("""  +---+---+---+---+---+---+---+---+
8 |${field[0][0]}|${field[0][1]}|${field[0][2]}|${field[0][3]}|${field[0][4]}|${field[0][5]}|${field[0][6]}|${field[0][7]}|
  +---+---+---+---+---+---+---+---+
7 |${field[1][0]}|${field[1][1]}|${field[1][2]}|${field[1][3]}|${field[1][4]}|${field[1][5]}|${field[1][6]}|${field[1][7]}|
  +---+---+---+---+---+---+---+---+
6 |${field[2][0]}|${field[2][1]}|${field[2][2]}|${field[2][3]}|${field[2][4]}|${field[2][5]}|${field[2][6]}|${field[2][7]}|
  +---+---+---+---+---+---+---+---+
5 |${field[3][0]}|${field[3][1]}|${field[3][2]}|${field[3][3]}|${field[3][4]}|${field[3][5]}|${field[3][6]}|${field[3][7]}|
  +---+---+---+---+---+---+---+---+
4 |${field[4][0]}|${field[4][1]}|${field[4][2]}|${field[4][3]}|${field[4][4]}|${field[4][5]}|${field[4][6]}|${field[4][7]}|
  +---+---+---+---+---+---+---+---+
3 |${field[5][0]}|${field[5][1]}|${field[5][2]}|${field[5][3]}|${field[5][4]}|${field[5][5]}|${field[5][6]}|${field[5][7]}|
  +---+---+---+---+---+---+---+---+
2 |${field[6][0]}|${field[6][1]}|${field[6][2]}|${field[6][3]}|${field[6][4]}|${field[6][5]}|${field[6][6]}|${field[6][7]}|
  +---+---+---+---+---+---+---+---+
1 |${field[7][0]}|${field[7][1]}|${field[7][2]}|${field[7][3]}|${field[7][4]}|${field[7][5]}|${field[7][6]}|${field[7][7]}|
  +---+---+---+---+---+---+---+---+
    a   b   c   d   e   f   g   h"""
        )
    }
}
