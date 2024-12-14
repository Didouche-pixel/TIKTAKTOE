package tictactoe

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily



fun main() = application {
    // Charge l'image à partir d'un fichier local

    Window(
        onCloseRequest = ::exitApplication,
        title = "Jeu du Pendu"
    ) {
        GameWindow()
    }
}

val customTextStyle = TextStyle(
    fontFamily = FontFamily.Serif, // Tu peux définir une police ici
    fontSize = 18.sp,             // Taille de la police
    color = Color.Black           // Couleur du texte
)
@Composable
fun GameWindow() {
    val wordList = listOf("frontend", "backend", "thread", "kotlin", "algorithme", "java")
    var currentWordIndex by remember { mutableStateOf(0) }
    val gameState = remember { mutableStateOf(startGame(wordList[currentWordIndex])) }
    var showHint by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var gameResult by remember { mutableStateOf("") }

    val hints = mapOf(
        "frontend" to "La partie d'une application où l'utilisateur interagit, souvent développée avec des technologies web. (en anglais)",
        "backend" to "La partie qui gère la logique métier et les bases de données d'une application.(en anglais)",
        "thread" to "Unité d'exécution dans un programme permettant un traitement parallèle.(en anglais)",
        "kotlin" to "Langage moderne, conçu pour remplacer Java dans les applications Android.",
        "algorithme" to "Suite d'étapes définies pour résoudre un problème ou accomplir une tâche.",
        "java" to "Langage de programmation très utilisé dans les systèmes d'entreprise et les applications mobiles multiplateformes."
    )


    // Utilisation de Column avec un contenu centré et scrollable
    Column(
        modifier = Modifier
            .fillMaxSize() // Prend toute la taille disponible
            .padding(16.dp) // Ajoute un padding général
            .verticalScroll(rememberScrollState()), // Permet le scroll vertical
        horizontalAlignment = Alignment.CenterHorizontally, // Centre horizontalement
        verticalArrangement = Arrangement.Center // Centre verticalement
    ) {
        // Conteneur avec largeur maximale limitée
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp), // Limite la largeur maximale
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espace uniforme entre les éléments
            ) {
                Text(
                    "Bienvenue dans le jeu du Pendu!",
                    style = customTextStyle,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Dans ce jeu, les mots à deviner sont liés à l'informatique. Bonne chance !",
                    style = customTextStyle.copy(fontSize = 14.sp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Contenu principal du jeu
                if (!gameOver) {
                    Text(
                        "Mot à deviner : ${getFormattedWordWithSpaces(gameState.value)}",
                        style = customTextStyle
                    )
                    Text(
                        "Tentatives restantes : ${gameState.value.remainingAttempts}",
                        style = customTextStyle
                    )
                    Text(
                        "Lettres proposées : ${gameState.value.guessedLetters.joinToString(", ")}",
                        style = customTextStyle
                    )

                    DisplayHangman(gameState.value.remainingAttempts)

                    // Bouton d'indice
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { showHint = !showHint },
                            modifier = Modifier.size(50.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF87CEFA),
                                contentColor = Color.White
                            )
                        ) {
                            Text("!", style = MaterialTheme.typography.h5)
                        }

                        if (showHint) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(hints[gameState.value.secretWord] ?: "Pas d'indice disponible.")
                        }
                    }
                }

                // Résultat du jeu
                if (gameResult.isNotEmpty()) {
                    Text(
                        gameResult,
                        style = customTextStyle.copy(
                            fontSize = 18.sp,
                            color = if(gameResult.contains("gagné")) Color.Green else Color.Red
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                // Boutons de lettres
                if (!gameOver && gameState.value.remainingAttempts > 0 && gameState.value.guessedWord.contains("_")) {
                    LetterButtons(gameState.value) { letter ->
                        proposeLetter(letter, gameState)

                        // Logique de progression du jeu (identique à votre code précédent)
                        if (!gameState.value.guessedWord.contains("_")) {
                            score++
                            if (currentWordIndex == wordList.size - 1) {
                                gameOver = true
                                gameResult = "Vous avez gagné ! Score final : $score/6\n${getScoreComment(score)}"
                            } else {
                                gameResult = "Vous avez gagné ce mot !"
                            }
                        } else if (gameState.value.remainingAttempts <= 0) {
                            if (currentWordIndex == wordList.size - 1) {
                                gameOver = true
                                gameResult = "Vous avez perdu. Score final : $score/6\n${getScoreComment(score)}"
                            } else {
                                gameResult = "Vous avez perdu ce mot. Le mot était : ${gameState.value.secretWord}"
                            }
                        }
                    }
                }

                // Bouton de progression
                if (gameResult.isNotEmpty()) {
                    Button(
                        onClick = {
                            showHint = false

                            if (currentWordIndex < wordList.size - 1 && !gameOver) {
                                currentWordIndex++
                                gameState.value = startGame(wordList[currentWordIndex])
                                gameResult = ""
                            } else {
                                currentWordIndex = 0
                                score = 0
                                gameState.value = startGame(wordList[currentWordIndex])
                                gameOver = false
                                gameResult = ""
                            }
                        }
                    ) {
                        Text(if (!gameOver) "Mot Suivant" else "Recommencer")
                    }
                }
            }
        }
    }
}

fun getFormattedWordWithSpaces(gameState: GameState): String {
    // Ajouter un espace entre chaque caractère (_ ou lettre devinée)
    return gameState.guessedWord.toList().joinToString(" ")
}

fun getScoreComment(score: Int): String {
    return when (score) {
        0 -> "Courage, vous pouvez faire mieux !"
        1 -> "Bon début, continuez à pratiquer !"
        2 -> "Pas mal, vous êtes sur la bonne voie !"
        3 -> "Bien joué ! Vous commencez à maîtriser !"
        4 -> "Excellent travail ! Vous êtes presque un expert !"
        5 -> "Impressionnant ! Vous êtes très proche du score parfait !"
        6 -> "Félicitations ! Score parfait ! Vous êtes un véritable expert !"
        else -> ""
    }
}
@Composable
fun LetterButtons(gameState: GameState, onLetterClicked: (Char) -> Unit) {
    val letters = ('a'..'z').toList()
    val chunkedLetters = letters.chunked(8) // Diviser les lettres en 3 groupes

    // Créer une Row pour chaque ligne de lettres
    chunkedLetters.forEach { chunk ->
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            chunk.forEach { letter ->
                Button(
                    onClick = { onLetterClicked(letter) },
                    enabled = !gameState.guessedLetters.contains(letter), // Désactiver les lettres déjà proposées
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF87CEFA), contentColor = Color.Black) // Changer couleur des boutons
                ) {
                    Text(letter.toString())
                }
            }
        }
    }
}
@Composable
fun DisplayHangman(remainingAttempts: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Dessin du support du pendu (toujours affiché)
            drawLine(
                color = Color.Black,
                start = center.copy(x = center.x - 100f, y = center.y + 100f), // Base gauche
                end = center.copy(x = center.x + 100f, y = center.y + 100f),   // Base droite
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Black,
                start = center.copy(x = center.x - 50f, y = center.y + 100f), // Colonne verticale
                end = center.copy(x = center.x - 50f, y = center.y - 150f),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Black,
                start = center.copy(x = center.x - 50f, y = center.y - 150f), // Barre horizontale
                end = center.copy(x = center.x + 50f, y = center.y - 150f),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Black,
                start = center.copy(x = center.x + 50f, y = center.y - 150f), // Corde
                end = center.copy(x = center.x + 50f, y = center.y - 100f),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )

            // Dessin des étapes du pendu selon les tentatives restantes
            if (remainingAttempts <= 5) {
                // Tête
                drawCircle(
                    color = Color.Black,
                    center = center.copy(x = center.x + 50f, y = center.y - 80f),
                    radius = 20f,
                    style = Stroke(width = 4f)
                )
            }
            if (remainingAttempts <= 4) {
                // Corps
                drawLine(
                    color = Color.Black,
                    start = center.copy(x = center.x + 50f, y = center.y - 60f),
                    end = center.copy(x = center.x + 50f, y = center.y),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
            if (remainingAttempts <= 3) {
                // Bras gauche
                drawLine(
                    color = Color.Black,
                    start = center.copy(x = center.x + 50f, y = center.y - 50f),
                    end = center.copy(x = center.x + 30f, y = center.y - 30f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
            if (remainingAttempts <= 2) {
                // Bras droit
                drawLine(
                    color = Color.Black,
                    start = center.copy(x = center.x + 50f, y = center.y - 50f),
                    end = center.copy(x = center.x + 70f, y = center.y - 30f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
            if (remainingAttempts <= 1) {
                // Jambe gauche
                drawLine(
                    color = Color.Black,
                    start = center.copy(x = center.x + 50f, y = center.y),
                    end = center.copy(x = center.x + 30f, y = center.y + 30f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
            if (remainingAttempts <= 0) {
                // Jambe droite
                drawLine(
                    color = Color.Black,
                    start = center.copy(x = center.x + 50f, y = center.y),
                    end = center.copy(x = center.x + 70f, y = center.y + 30f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

fun startGame(secretWord: String): GameState {
    return GameState(secretWord, "_".repeat(secretWord.length), mutableListOf(), 6)
}
fun proposeLetter(letter: Char, gameState: MutableState<GameState>) {
    // Cette fonction met à jour l'état du jeu en fonction de la lettre proposée :
    // elle vérifie si la lettre a été devinée, met à jour les lettres devinées, le mot et les tentatives restantes.

    if (gameState.value.guessedLetters.contains(letter)) return

    val currentGameState = gameState.value

    // Met à jour les lettres devinées
    val updatedGuessedLetters = currentGameState.guessedLetters.toMutableList().apply {
        add(letter)
    }

    // Met à jour le mot deviné ou diminue les tentatives
    val updatedGuessedWord = if (currentGameState.secretWord.contains(letter)) {
        updateGuessedWord(letter, currentGameState)
    } else {
        currentGameState.guessedWord
    }

    val updatedAttempts = if (!currentGameState.secretWord.contains(letter)) {
        currentGameState.remainingAttempts - 1
    } else {
        currentGameState.remainingAttempts
    }

    // Met à jour l'état du jeu avec une nouvelle instance de GameState
    gameState.value = currentGameState.copy(
        guessedLetters = updatedGuessedLetters,
        guessedWord = updatedGuessedWord,
        remainingAttempts = updatedAttempts
    )
}

fun updateGuessedWord(letter: Char, gameState: GameState): String {
    val guessedWordArray = gameState.guessedWord.toCharArray()
    for (i in gameState.secretWord.indices) {
        if (gameState.secretWord[i] == letter) {
            guessedWordArray[i] = letter
        }
    }
    return String(guessedWordArray)
}

