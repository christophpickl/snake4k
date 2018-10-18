package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.view.KeyboardWatcher
import com.github.christophpickl.snake4k.view.QuitEvent
import com.github.christophpickl.snake4k.view.RestartEvent
import com.google.common.eventbus.DeadEvent
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.application.Platform
import javax.inject.Inject

class ApplicationManager @Inject constructor(
//    private val window: Window,
    private val keyboard: KeyboardWatcher,
    private val engine: GameEngine,
    bus: EventBus
) {

    init {
        println("Register game manager to: $bus")
        bus.register(this)
    }

    fun start() {
//        window.addWindowListener(object : WindowAdapter() {
//            override fun windowClosing(e: WindowEvent) {
//                onQuitEvent()
//            }
//        })
//        window.addKeyListener(keyboard)
//        window.isVisible = true
        onRestartEvent()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onRestartEvent(event: RestartEvent = RestartEvent) {
        Log.debug { "Restart game." }
        engine.restart()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onQuitEvent(event: QuitEvent = QuitEvent) {
        Log.debug { "Quit application ..." }
        engine.stop()
        Platform.exit()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onDeadEvent(event: DeadEvent) {
        Log.debug { "Warning: Unused event found: $event" }
    }

}