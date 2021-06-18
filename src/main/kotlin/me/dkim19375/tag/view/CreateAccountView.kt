package me.dkim19375.tag.view

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.SCOPE
import me.dkim19375.tag.file.Profile
import me.dkim19375.tag.main
import me.dkim19375.tag.util.VIEW_TITLE
import me.dkim19375.tag.util.applyBackgroundSettings
import me.dkim19375.tag.util.setOnPress
import me.dkim19375.tag.util.toHash
import tornadofx.View
import tornadofx.hide
import tornadofx.show

class CreateAccountView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val usernameField: TextField by fxid()
    private val passwordField: PasswordField by fxid()
    private val createButton: Button by fxid()
    private val backButton: Button by fxid()
    private val errorLabel: Label by fxid()
    private var job: Job? = null

    init {
        main.createAccountView = this
        root.applyBackgroundSettings()
        backButton.setOnPress {
            replaceWith<ProfileView>()
            main.profileView.start()
        }
        createButton.setOnPress {
            val text = usernameField.text.trim()
            val pass = passwordField.text.trim()
            if (text.isBlank()) {
                showError("The profile must not be blank!")
                return@setOnPress
            }
            if (main.dataFile.getProfiles().map(Profile::name).contains(text)) {
                showError("The profile already exists!")
                return@setOnPress
            }
            val hash = pass.toHash()
            main.dataFile.setProfile(main.dataFile.getProfile(text).copy(password = hash))
            showError("Successfully created profile: $text!")
            main.profileView.updateProfileList()
        }
    }

    fun reset() {
        usernameField.text = ""
        passwordField.text = ""
        errorLabel.hide()
    }

    private fun showError(text: String) {
        errorLabel.text = text
        errorLabel.show()
        job?.cancel()
        job = SCOPE.launch {
            delay(3000L)
            Platform.runLater {
                errorLabel.hide()
                job = null
            }
        }
    }
}