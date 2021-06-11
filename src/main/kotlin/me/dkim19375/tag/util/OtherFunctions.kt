package me.dkim19375.tag.util

import javafx.application.Platform
import me.dkim19375.tag.THREAD
import me.dkim19375.tag.main
import me.dkim19375.tag.view.GameEndView
import me.dkim19375.tag.view.GameView
import me.dkim19375.tag.view.JoinLobbyView
import me.dkim19375.tag.view.LobbyView
import me.dkim19375.tag.view.StartView
import tornadofx.View
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KProperty0

fun KProperty0<*>.isInit(): Boolean = try {
    get()
    true
} catch (_: UninitializedPropertyAccessException) {
    false
}

inline fun <reified T : View> changeRoot() {
    if (main::gameEndView.changeView<GameEndView, T>()) {
        return
    }
    if (main::gameView.changeView<GameView, T>()) {
        return
    }
    if (main::joinLobbyView.changeView<JoinLobbyView, T>()) {
        return
    }
    if (main::lobbyView.changeView<LobbyView, T>()) {
        return
    }
    if (main::startView.changeView<StartView, T>()) {
        return
    }
    throw IllegalStateException("No views are initialized!")
}

inline fun <T : View, reified C : View> KProperty0<T>.changeView() : Boolean {
    if (!isInit()) {
        return false
    }
    if (onMainThread()) {
        println("view changed to ${C::class.simpleName ?: ""}")
        get().replaceWith<C>()
        return true
    }
    val future = CompletableFuture<Unit>()
    Platform.runLater {
        println("view changed to ${C::class.simpleName ?: ""}")
        get().replaceWith<C>()
        future.complete(Unit)
    }
    future.get()
    return true
}

fun onMainThread(): Boolean = Thread.currentThread().id == THREAD.id