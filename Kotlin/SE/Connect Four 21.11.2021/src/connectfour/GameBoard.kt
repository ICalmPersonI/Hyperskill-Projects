package connectfour

class GameBoard {
    private var board: Array<Array<Int>>

    private val separatingMark: Regex = Regex("\\s*x\\s*")
    private val inputFormat: Regex = Regex("\\d+$separatingMark\\d+")

    init {
        while (true) {
            println(
                "Set the board dimensions (Rows x Columns)\n" +
                        "Press Enter for default (6 x 7)"
            )
            val size: String = readLine()!!.trim().lowercase()
            when {
                size.isEmpty() -> {
                    board = Array(6) { Array(7) { 0 } }
                    break
                }
                !size.matches(inputFormat) -> println("Invalid input")
                else -> {
                    val temp: List<Int> = size.split(separatingMark).map { it.toInt() }
                    when {
                        temp[0] !in 5..9 -> println("Board rows should be from 5 to 9")
                        temp[1] !in 5..9 -> println("Board columns should be from 5 to 9")
                        else -> {
                            board = Array(temp[0]) { Array(temp[1]) { 0 } }
                            break
                        }
                    }
                }
            }
        }
    }

    fun addDisk(player: Int, column: Int) {
        for (i in board.lastIndex downTo 0)
            if (board[i][column] == 0) {
                board[i][column] = player
                return
            }
    }

    fun isBoardFull(): Boolean = board[0].filterIndexed { index, _ -> isColumnFull(index) }.count() == board[0].size

    fun isColumnFull(column: Int): Boolean = board[0][column] != 0

    fun getNumberOfRow() = board.size

    fun getNumberOfColumn() = board[0].size

    fun resetBoard() {
        for (i in board.indices) board[i].fill(0)
    }

    fun printBoard() {
        for (i in board[0].indices) {
            when (i) {
                0 -> print(" ${i + 1} ")
                board[0].lastIndex -> print("${i + 1} \n")
                else -> print("${i + 1} ")
            }
        }

        for (i in board.indices) {
            for (j in board[i].indices) {
                val value: Int = board[i][j]
                if (j == 0) print("|${if (value == 0) " " else if (value == 1) "o" else "*"}|")
                else print("${if (value == 0) " " else if (value == 1) "o" else "*"}|")
            }
            println()
        }

        for (i in board[0].indices) {
            when (i) {
                0 -> print("===")
                board[0].lastIndex -> print("==\n")
                else -> print("==")
            }
        }
    }

    fun checkWinningCondition(player: Int): Boolean {
        for (r in board.indices) {
            for (c in board[0].indices) {
                if (board[r][c] == player) {
                    for (case in 1..8) {
                        var count: Int = 1
                        var row: Int = r
                        var column: Int = c
                        while (true) {
                            if (count == 4) return true
                            when (case) {
                                1 -> {
                                    if (row - 1 > -1 && board[row - 1][column] == player) {
                                        count++
                                        row--
                                    } else break
                                }
                                2 -> {
                                    if (row - 1 > -1 && board[row - 1][column] == player) {
                                        count++
                                        row--
                                    } else break
                                }
                                3 -> {
                                    if (column - 1 > -1 && board[row][column - 1] == player) {
                                        count++
                                        column--
                                    } else break
                                }
                                4 -> {
                                    if (column + 1 < board[0].size && board[row][column + 1] == player) {
                                        count++
                                        column++
                                    } else break
                                }
                                5 -> {
                                    if ((row - 1 > -1 && column - 1 > -1)
                                        && board[row - 1][column - 1] == player
                                    ) {
                                        count++
                                        column--
                                        row--
                                    } else break
                                }
                                6 -> {
                                    if ((row - 1 > -1 && column + 1 < board[0].size)
                                        && board[row - 1][column + 1] == player
                                    ) {
                                        count++
                                        column++
                                        row--
                                    } else break
                                }
                                7 -> {
                                    if ((row + 1 < board.size && column - 1 > -1)
                                        && board[row + 1][column - 1] == player
                                    ) {
                                        count++
                                        column--
                                        row++
                                    } else break
                                }
                                8 -> {
                                    if ((row + 1 < board.size && column + 1 < board[0].size)
                                        && board[row + 1][column + 1] == player
                                    ) {
                                        count++
                                        column++
                                        row++
                                    } else break
                                }
                            }
                        }
                    }
                }
            }
        }
        return false
    }
}