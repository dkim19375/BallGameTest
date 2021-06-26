package me.dkim19375.tag.view

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXListCell
import com.jfoenix.controls.JFXListView
import com.jfoenix.controls.JFXPasswordField
import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.util.Callback
import kfoenix.jfxlistview
import kfoenix.jfxpasswordfield
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.SCOPE
import me.dkim19375.tag.file.Profile
import me.dkim19375.tag.main
import me.dkim19375.tag.util.*
import tornadofx.*

class ProfileView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val profileBox: HBox by fxid()
    @Suppress("UNCHECKED_CAST")
    private val profiles: JFXListView<String> = profileBox.jfxlistview {
        prefWidth = 400.0
        prefHeight = Region.USE_COMPUTED_SIZE
        val borderRadius = 15.0
        border =
            Border(BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, CornerRadii(borderRadius), BorderWidths(3.0)))
        background = Background(BackgroundFill(BORDER_COLOR, CornerRadii(borderRadius * 1.2), null))
        cellFactory = Callback<JFXListView<String>, JFXListCell<String>> callback@{
            object : JFXListCell<String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    cellRippler.ripplerFill = Color.web("#232c70")
                    border = Border(
                        BorderStroke(
                            BORDER_COLOR,
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            BorderWidths(3.0),
                        ),
                    )
                    textFill = TEXT_COLOR
                    text = if (item == null || empty) null else item
                    font = Font.font("System", FontWeight.BOLD, 15.0)
                    style = "-fx-control-inner-background: BLACK; " +
                            "-fx-text-fill: $TEXT_COLOR_HEX; " +
                            "-fx-accent: #0f185c; " +
                            "-fx-focus-color: #0f185c;" +
                            "-fx-cell-hover-color: #0f185c;"
                    if (item == this@jfxlistview.selectionModel.selectedItem && item != null) {
                        style += "-fx-background-color: #0f185c"
                    }
                }
            }
        } as Callback<ListView<String>, ListCell<String>>
    }
    private val passwordBox: HBox by fxid()
    @Suppress("UsePropertyAccessSyntax")
    private val passwordField: JFXPasswordField = passwordBox.jfxpasswordfield {
        val borderRadius = 5.0
        background = Background(BackgroundFill(Color.BLACK, CornerRadii(borderRadius * 1.2), null))
        border =
            Border(BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, CornerRadii(borderRadius), BorderWidths(5.0)))
        this.prefHeight = 25.0
        this.prefWidth = 250.0
        setStyle("-fx-text-inner-color: $TEXT_COLOR_HEX;")
    }
    private val backBox: HBox by fxid()
    private val selectBox: HBox by fxid()
    private val createBox: HBox by fxid()
    private val deleteBox: HBox by fxid()
    private val backButton: JFXButton = kfxButton("Back", backBox, 50.0, 200.0)
    private val selectButton: JFXButton = kfxButton("Select", selectBox, 50.0, 200.0)
    private val createButton: JFXButton = kfxButton("Create Profile", createBox, 50.0, 200.0)
    private val deleteButton: JFXButton = kfxButton("Delete profile", deleteBox, 50.0, 200.0)
    private val errorLabel: Label by fxid()
    private var job: Job? = null

    init {
        main.profileView = this
        root.applyBackgroundSettings()
        selectButton.setOnPress {
            val selected = profiles.selectionModel.selectedItem ?: run {
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
            main.startView.update()
        }
        backButton.setOnPress {
            replaceWith<StartView>()
            main.startView.update()
        }
        deleteButton.setOnPress {
            val selected = profiles.selectionModel.selectedItem ?: run {
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
        profiles.selectionModel.select(main.profile.name)
    }

    fun updateProfileList() {
        profiles.items = observableListOf(main.dataFile.getProfiles().map(Profile::name))
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