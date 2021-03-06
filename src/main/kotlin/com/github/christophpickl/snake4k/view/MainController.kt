package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.ExceptionEvent
import com.github.christophpickl.snake4k.GameOverEvent
import com.github.christophpickl.snake4k.PauseEvent
import com.github.christophpickl.snake4k.QuitEvent
import com.github.christophpickl.snake4k.RestartFxEvent
import com.github.christophpickl.snake4k.RestartGuiceEvent
import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.State
import com.google.common.eventbus.EventBus
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import tornadofx.*

class MainController : Controller() {

    private val bus: EventBus by di()
    private val state: State by di()

    private var currentGameOverAlert: Alert? = null
    private var currentExceptionAlert: Alert? = null

    init {
        subscribe<GameOverEvent> { event ->
            currentGameOverAlert = buildAlert(
                type = Alert.AlertType.INFORMATION,
                owner = primaryStage,
                header = "Game over",
                content = "${event.detailMessage}\n" +
                    "Fruits eaten: ${event.fruitsEaten}\n" +
                    "Time survived: ${formatTime(event.secondsPlayed)}",
                buttons = *arrayOf(ButtonType("Restart"), ButtonType("Quit"))
            ).apply {
                dialogPane.addEventFilter(KeyEvent.KEY_PRESSED) {
                    if (it.code == KeyCode.ENTER) {
                        result = ButtonType("Restart")
                        close()
                    }
                }
            }

            currentGameOverAlert!!.let {
                val buttonClicked = it.showAndWait()
                if (buttonClicked.isPresent) {
                    when (buttonClicked.get().text) {
                        "Restart" -> bus.post(RestartGuiceEvent())
                        "Quit" -> bus.post(QuitEvent)
                    }
                }
            }
        }
        subscribe<RestartFxEvent> {
            closeDialogs()
        }
        subscribe<QuitEvent> {
            closeDialogs()
        }
        subscribe<PauseEvent> {
            if (state.gameState == GameState.NotRunning) {
                throw IllegalStateException("Game not running, nothing to pause :(")
            }
            state.gameState = if (state.gameState == GameState.Running) GameState.Paused else GameState.Running
        }
        subscribe<ExceptionEvent> {
            val e = it.exception
            currentExceptionAlert = buildAlert(
                type = Alert.AlertType.ERROR,
                owner = primaryStage,
                title = "",
                header = "Unhandled Exception!",
                content = "${e.javaClass.simpleName}: ${e.message}",
                buttons = *arrayOf(ButtonType.OK)
            )
        }

        state.fruitsEatenProperty.addListener { _, _, newValue ->
            val newVal = newValue.toInt()
            if (newVal > state.highscore) {
                state.highscore = newVal
            }
        }
    }


    private fun closeDialogs() {
        listOfNotNull(currentGameOverAlert, currentExceptionAlert).forEach {
            it.result = ButtonType("Abort")
            it.close()
        }
    }

    private fun formatTime(seconds: Int) =
        "${if (seconds >= 60) "${seconds / 60} Minutes " else ""}${seconds % 60} Second${if (seconds == 1) "" else "s"}"

}
