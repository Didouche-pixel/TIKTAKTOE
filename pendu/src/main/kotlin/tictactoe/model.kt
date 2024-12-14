package tictactoe

data class GameState(
    val secretWord: String,
    var guessedWord: String,
    var guessedLetters: MutableList<Char>,
    var remainingAttempts: Int
)

