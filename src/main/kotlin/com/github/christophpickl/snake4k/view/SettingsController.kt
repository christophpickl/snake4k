package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.logic.ApplicationManager
import com.github.christophpickl.snake4k.model.Settings
import javafx.application.Platform
import mu.KotlinLogging
import tornadofx.*

class SettingsController : Controller() {

    private val logg = KotlinLogging.logger {}
    private val appManager: ApplicationManager by di()
    private val settings: Settings by di()
    private val settingsView = SettingsView(settings)

    init {
        subscribe<ApplicationReadyEvent> {
            println("aaaa")
            showAndRun()
        }
        subscribe<RestartFxEvent> {
            println("xxxxx")
            showAndRun()
        }
    }

    private fun showAndRun() {
        logg.debug { "show and run" }
        Platform.runLater {
            settingsView.showAndWait()
            appManager.start()
        }
    }

}
