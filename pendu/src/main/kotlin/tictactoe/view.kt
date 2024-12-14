package tictactoe

object View {
    fun displayIntro(wordLength: Int) {
        println("Bienvenue dans le jeu du Pendu!")
        println("Le mot à deviner a $wordLength lettres.")
    }

    fun displayWord(guessedWord: String, guessedLetters: List<Char>, remainingAttempts: Int) {
        // Affiche une seule fois l'état du jeu
        println("Mot secret : $guessedWord")
        println("Lettres déjà proposées : $guessedLetters")
        println("Tentatives restantes : $remainingAttempts")
    }

    fun askForLetter(): Char {
        print("Propose une lettre : ")
        return readln().first()
    }

    fun displayMessage(message: String) {
        println(message)
    }

    fun displayHangman(remainingAttempts: Int) {

        val hangmanStages = listOf(
            """
              ------
              |    |
                   |
                   |
                   |
                   |
            ------------
            """, // Stage 0 - pas de pendu
            """
              ------
              |    |
              O    |
                   |
                   |
                   |
            ------------
            """, // Stage 1 - tête
            """
              ------
              |    |
              O    |
              |    |
                   |
                   |
            ------------
            """, // Stage 2 - corps
            """
              ------
              |    |
              O    |
             /|    |
                   |
                   |
            ------------
            """, // Stage 3 - bras
            """
              ------
              |    |
              O    |
             /|\\   |
                   |
                   |
            ------------
            """, // Stage 4 - bras
            """
              ------
              |    |
              O    |
             /|\\   |
             /     |
                   |
            ------------
            """, // Stage 5 - jambes
            """
              ------
              |    |
              O    |
             /|\\   |
             / \\   |
            ------------
            """  // Stage 6 - pendu complet
        )
        println(hangmanStages[6 - remainingAttempts])
    }
}
