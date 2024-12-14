package tictactoe

import java.util.*
import kotlin.random.Random

fun main() {
    // Liste des mots à deviner
    val words = listOf("UBO", "france", "kotlin")

    // Lancer plusieurs parties
    var playAgain = true
    while (playAgain) {
        // Sélectionner un mot aléatoirement dans la liste
        val randomWord = words[Random.nextInt(words.size)]

        // Créer une instance du jeu avec le mot sélectionné
        val game = HangmanGame(randomWord, 6)

        // Démarrer le jeu
        game.start()

        // Demander à l'utilisateur s'il veut jouer à nouveau
        println("Voulez-vous jouer à nouveau ? (oui/non)")
        playAgain = readLine()?.trim()?.lowercase(Locale.getDefault()) == "oui"
    }

    println("Merci d'avoir joué !")
}
