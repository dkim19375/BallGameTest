package me.dkim19375.tag.view

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.PasswordField
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
import tornadofx.View
import tornadofx.hide
import tornadofx.observableListOf
import tornadofx.show

class ProfileView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val profileList: ListView<String> by fxid()
    private val passwordField: PasswordField by fxid()
    private val backButton: Button by fxid()
    private val selectButton: Button by fxid()
    private val createButton: Button by fxid()
    private val deleteButton: Button by fxid()
    private val errorLabel: Label by fxid()
    private var job: Job? = null

    init {
        main.profileView = this
        root.applyBackgroundSettings()
        selectButton.setOnPress {
            val selected = profileList.selectionModel.selectedItem ?: run {
                showError("You must select a profile!")
                return@setOnPress
            }
            val pass = passwordField.text.trim()
            val profile = main.dataFile.getProfile(selected)
            if (!profile.password.hashEquals(pass)) {
                showError("Incorrect password!")
                return@setOnPress
            }
            main.dataFile.setCurrentProfile(selected)
            replaceWith<StartView>()
            main.startView.updateCoinsLabel()
            main.startView.updateHighscoreLabel()
            main.startView.updateSelectedCircle()
            main.startView.updateProfileButton()
        }
        backButton.setOnPress {
            replaceWith<StartView>()
            main.startView.updateCoinsLabel()
            main.startView.updateHighscoreLabel()
            main.startView.updateSelectedCircle()
            main.startView.updateProfileButton()
        }
        deleteButton.setOnPress {
            val selected = profileList.selectionModel.selectedItem ?: run {
                showError("You must select a profile!")
                return@setOnPress
            }
            if (selected == "Public") {
                showError("You cannot delete the Public profile!")
                return@setOnPress
            }
            val pass = passwordField.text.trim()
            val profile = main.dataFile.getProfile(selected)
            if (!profile.password.hashEquals(pass)) {
                showError("Incorrect password!")
                return@setOnPress
            }
            val current = main.profile.name
            main.dataFile.deleteProfile(selected)
            updateProfileList()
            if (current == selected) {
                main.dataFile.setCurrentProfile("Public")
            }
        }
        createButton.setOnPress {
            replaceWith<CreateAccountView>()
            main.createAccountView.reset()
        }
    }

    fun start() {
        errorLabel.hide()
        passwordField.text = ""
        updateProfileList()
    }

    fun updateProfileList() {
        profileList.items = observableListOf(main.dataFile.getProfiles().map(Profile::name))
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