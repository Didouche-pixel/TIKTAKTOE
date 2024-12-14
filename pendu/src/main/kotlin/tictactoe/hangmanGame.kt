package tictactoe

import tictactoe.View

class HangmanGame(private val secretWord: String, private var remainingAttempts: Int) {

    private var guessedWord: String = "_ ".repeat(secretWord.length).trim()
    private val guessedLetters = mutableListOf<Char>()

    fun start() {
        // Affichage initial du jeu
        View.displayIntro(secretWord.length)

        // Boucle du jeu
        while (remainingAttempts > 0 && guessedWord.contains("_")) {
            // Affiche l'état actuel du jeu (avant la saisie de la lettre)
            View.displayWord(guessedWord, guessedLetters, remainingAttempts)

            // Demander une nouvelle lettre à l'utilisateur
            val guess = View.askForLetter()

            // Vérifier si la lettre a déjà été proposée
            if (guessedLetters.contains(guess)) {
                View.displayMessage("Tu as déjà proposé cette lettre.")
            } else {
                guessedLetters.add(guess)

                if (secretWord.contains(guess)) {
                    guessedWord = updateGuessedWord(guess)
                    View.displayMessage("Bonne réponse!")
                } else {
                    remainingAttempts--
                    View.displayMessage("Mauvaise réponse.")
                }
            }

            // Afficher l'état du pendu (après la tentative)
            View.displayHangman(remainingAttempts)
        }

        // Afficher le message de fin
        if (remainingAttempts == 0) {
            View.displayMessage("GAME OVER! Le mot était: $secretWord")
        } else {
            View.displayMessage("Félicitations! Vous avez trouvé le mot: $secretWord")
        }
    }

    private fun updateGuessedWord(guess: Char): String {
        val guessedWordArray = guessedWord.split(" ").toMutableList()
        for (i in secretWord.indices) {
            if (secretWord[i] == guess) {
                guessedWordArray[i] = guess.toString()
            }
        }
        return guessedWordArray.joinToString(" ")
    }
}
