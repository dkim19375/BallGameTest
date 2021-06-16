package me.dkim19375.tag.view

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.SCOPE
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.file.Profile
import me.dkim19375.tag.main
import me.dkim19375.tag.util.toHash
import tornadofx.View
import tornadofx.hide
import tornadofx.observableListOf
import tornadofx.show

class ProfileView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val profileList: ListView<String> by fxid()
    private val passwordField: PasswordField by fxid()
    private val backButton: Button by fxid()
    private val createButton: Button by fxid()
    private val deleteButton: Button by fxid()
    private val createField: TextField by fxid()
    private val errorLabel: Label by fxid()
    private var job: Job? = null

    init {
        main.profileView = this
        backButton.setOnAction {
            val selected = profileList.selectionModel.selectedItem ?: run {
                showError("You must select a profile!")
                return@setOnAction
            }
            val pass = passwordField.text.trim()
            val profile = main.dataFile.getProfile(selected)
            if (!profile.password.hashEquals(pass)) {
                showError("Incorrect password!")
                return@setOnAction
            }
            main.dataFile.setCurrentProfile(selected)
            replaceWith<StartView>()
            main.startView.updateCoinsLabel()
            main.startView.updateSelectedCircle()
            main.startView.updateProfileButton()
        }
        deleteButton.setOnAction {
            val selected = profileList.selectionModel.selectedItem ?: run {
                showError("You must select a profile!")
                return@setOnAction
            }
            if (selected == "Public") {
                showError("You cannot delete the Public profile!")
                return@setOnAction
            }
            val pass = passwordField.text.trim()
            val profile = main.dataFile.getProfile(selected)
            if (!profile.password.hashEquals(pass)) {
                showError("Incorrect password!")
                return@setOnAction
            }
            main.dataFile.deleteProfile(selected)
            updateProfileList()
        }
        createButton.setOnAction {
            val text = createField.text.trim()
            val pass = passwordField.text.trim()
            if (text.isBlank()) {
                showError("The profile must not be blank!")
                return@setOnAction
            }
            if (main.dataFile.getProfiles().map(Profile::name).contains(text)) {
                showError("The profile already exists!")
                return@setOnAction
            }
            val hash = pass.toHash()
            main.dataFile.setProfile(main.dataFile.getProfile(text).copy(password = hash))
            updateProfileList()
        }
    }

    fun start() {
        errorLabel.hide()
        passwordField.text = ""
        createField.text = ""
        updateProfileList()
    }

    private fun updateProfileList() {
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