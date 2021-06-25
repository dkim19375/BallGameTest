package me.dkim19375.tag.view

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXTextField
import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.control.TextInputControl
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import kfoenix.jfxpasswordfield
import kfoenix.jfxtextfield
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.SCOPE
import me.dkim19375.tag.file.Profile
import me.dkim19375.tag.main
import me.dkim19375.tag.util.*
import tornadofx.*

class CreateAccountView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val usernameBox: HBox by fxid()
    private val passwordBox: HBox by fxid()
    private val createBox: HBox by fxid()
    private val backBox: HBox by fxid()
    private val applyRegion: Region.() -> Unit = {
        val borderRadius = 5.0
        background = Background(BackgroundFill(Color.BLACK, CornerRadii(borderRadius * 1.2), null))
        val textColor = TEXT_COLOR
        style = "-fx-text-fill: $TEXT_COLOR_HEX"
        (this as? TextInputControl)?.font = Font.font("System", 15.0)
        border = Border(
            BorderStroke(
                textColor,
                BorderStrokeStyle.SOLID,
                CornerRadii(borderRadius),
                BorderWidths(5.0)
            )
        )
    }
    private val usernameField: JFXTextField = usernameBox.jfxtextfield { applyRegion() }
    private val passwordField: JFXPasswordField = passwordBox.jfxpasswordfield { applyRegion() }
    private val createButton: JFXButton = kfxButton("Create Profile", createBox, 50.0, 200.0)
    private val backButton: JFXButton = kfxButton("Back", backBox, 50.0, 200.0)
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