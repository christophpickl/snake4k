package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.model.State
import com.github.christophpickl.snake4k.model.StateModel
import javafx.scene.input.KeyEvent
import tornadofx.*

class MainView : View() {
    private val board: Board by di()
    private val keyboard: KeyboardWatcher by di()
    private val state: State by di()
    private val stateModel = StateModel(state)

    override val root = borderpane {
        title = "Snake4k"
        addEventFilter(KeyEvent.KEY_PRESSED, keyboard)

        paddingAll = 0
        top {
            add(MyMenuBar(this@MainView, stateModel))
        }
        center {
            add(board)
        }
        bottom {
            hbox {
                label("Fruits eaten: ")
                label { bind(stateModel.fruitsEaten) }
            }
        }
    }

    override fun onDock() {
        // otherwise keyboard listener won't work
        root.requestFocus()
    }
}

