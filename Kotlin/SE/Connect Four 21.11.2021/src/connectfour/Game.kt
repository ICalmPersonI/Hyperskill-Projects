package connectfour

class Game {

    private var board: GameBoard
    private val numbers: Regex = Regex("\\d+")

    private val firstPlayer: String
    private val secondPlayer: String

    private var numberOfGames: Int
    private val singleMode: Boolean

    private var firstPlayerScore: Int = 0
    private var secondPlayerScore: Int = 0

    init {
        println("Connect Four")
        println("First player's name:")
        firstPlayer = readLine()!!
        println("Second player's name:")
        secondPlayer = readLine()!!

        board = GameBoard()

        while (true) {
            println(
                "Do you want to play single or multiple games?\n" +
                        "For a single game, input 1 or press Enter\n" +
                        "Input a number of games:"
            )
            val input: String = readLine()!!
            when {
                input.isEmpty() -> {
                    numberOfGames = 1
                    break
                }
                input.matches(numbers) && input.toInt() > 0 -> {
                    numberOfGames = input.toInt()
                    break
                }
                else -> println("Invalid input")
            }
        }

        println("$firstPlayer VS $secondPlayer")
        println("${board.getNumberOfRow()} X ${board.getNumberOfColumn()} board")
        if (numberOfGames > 1) {
            singleMode = false
            println("Total $numberOfGames games")
        } else {
            singleMode = true
            println("Single game")
        }
    }

    fun start() {
        var gameNumber: Int = 1
        var player: String = firstPlayer
        repeat(numberOfGames) {
            board.resetBoard()
            if (!singleMode) println("Game #$gameNumber")
            gameNumber++
            board.printBoard()

            while (true) {
                println("$player's turn:")
                val input: String = readLine()!!
                when {
                    input == "end" -> break
                    !input.matches(numbers) -> println("Incorrect column number")
                    input.toInt() > board.getNumberOfColumn() || input.toInt() < 1 ->
                        println("The column number is out of range (1 - ${board.getNumberOfRow()})")
                    board.isColumnFull(input.toInt() - 1) -> println("Column $input is full")
                    else -> {
                        board.addDisk(getPlayerNumber(player), input.toInt() - 1)
                        board.printBoard()
                        var exitLoop: Boolean = false
                        if (board.checkWinningCondition(getPlayerNumber(player))) {
                            if (player == firstPlayer) firstPlayerScore += 2
                            else secondPlayerScore += 2
                            println("Player $player won")
                            exitLoop = true
                        } else if (board.isBoardFull()) {
                            firstPlayerScore++
                            secondPlayerScore++
                            println("It is a draw")
                            exitLoop = true
                        }
                        player = playerChange(player)
                        if (exitLoop) break
                    }
                }
            }

            if (!singleMode)
                if (numberOfGames != 0) {
                    println("Score")
                    println("$firstPlayer: $firstPlayerScore $secondPlayer: $secondPlayerScore")
                }

        }
        println("Game over!")
    }

    private fun getPlayerNumber(player: String): Int = if (player == firstPlayer) 1 else 2

    private fun playerChange(player: String) = if (player == firstPlayer) secondPlayer else firstPlayer
}