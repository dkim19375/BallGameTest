package me.dkim19375.tag.view

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.stage.FileChooser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.SCOPE
import me.dkim19375.tag.main
import me.dkim19375.tag.util.SkinType
import me.dkim19375.tag.util.VIEW_TITLE
import me.dkim19375.tag.util.applyBackgroundSettings
import me.dkim19375.tag.util.setOnPress
import tornadofx.View
import tornadofx.hide
import tornadofx.show

class SkinsView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val setup = mutableSetOf<Int>()
    private val uploadHBox: HBox by fxid()
    private val uploadButton: Button by fxid()
    private val customImage: Circle by fxid()
    private val image0: Circle by fxid()
    private val image1: Circle by fxid()
    private val image2: Circle by fxid()
    private val image3: Circle by fxid()
    private val image4: Circle by fxid()
    private val image5: Circle by fxid()
    private val image6: Circle by fxid()
    private val customCostLabel: Label by fxid()
    private val costLabel0: Label by fxid()
    private val costLabel1: Label by fxid()
    private val costLabel2: Label by fxid()
    private val costLabel3: Label by fxid()
    private val costLabel4: Label by fxid()
    private val costLabel5: Label by fxid()
    private val costLabel6: Label by fxid()
    private val backButton: Button by fxid()
    private val coinsLabel: Label by fxid()
    private val circles = listOf(image0, image1, image2, image3, image4, image5, image6, customImage)
    private val costs =
        listOf(costLabel0, costLabel1, costLabel2, costLabel3, costLabel4, costLabel5, costLabel6, customCostLabel)

    init {
        main.skinsView = this
        backButton.setOnPress {
            replaceWith<StartView>()
        }
        updateUploadButton()
        uploadButton.setOnPress {
            uploadButton.run {
                if (!isVisible) {
                    return@setOnPress
                }
                val chooser = FileChooser()
                chooser.title = "Select Custom Image"
                chooser.extensionFilters.clear()
                chooser.extensionFilters.add(
                    FileChooser.ExtensionFilter("Image files", "*.jpeg", "*.jpg", "*.png")
                )
                val file = chooser.showOpenDialog(main.stage) ?: return@setOnPress
                main.dataFile.getCurrentProfile().setCustomSkin(file.absolutePath)
                setupItems()
                main.startView.updateSelectedCircle()
            }
        }
    }

    fun start() {
        root.applyBackgroundSettings()
        updateCoinsLabel()
        updateUploadButton()
        setupItems()
    }

    private fun updateCoinsLabel() {
        coinsLabel.text = "Coins: ${main.coins}"
    }

    private fun updateUploadButton() {
        if (!main.owned.contains(SkinType.CUSTOM)) {
            uploadButton.hide()
            uploadHBox.hide()
            return
        }
        uploadButton.show()
        uploadHBox.show()
    }

    private fun setupItems() {
        for (type in SkinType.values()) {
            val circle = circles[type.intValue]
            setupItem(circle, type)
        }
    }

    private fun setupItem(circle: Circle, type: SkinType) {
        val number = type.intValue
        val paint = type.image
        val cost = type.cost
        val isDefault = type == SkinType.DEFAULT
        if (!isDefault) {
            circle.fill = ImagePattern(Image("images/${if (type == SkinType.CUSTOM) "custom_" else ""}lock.png"))
        }
        costs[type.intValue].text = if (isDefault) "Free!" else "Cost: $cost Coins"
        costs[type.intValue].textFill = if (cost > main.coins) Color.RED else Color.DARKGREEN
        if (!setup.contains(number)) {
            setup.add(number)
            circle.setOnMouseClicked { event ->
                if (event.button != MouseButton.PRIMARY) {
                    return@setOnMouseClicked
                }
                if (main.owned.contains(type) || isDefault) {
                    main.selectedSkin = type
                    main.startView.updateSelectedCircle()
                    setupItems()
                    showSelectedColor(circle)
                    return@setOnMouseClicked
                }
                // buying
                if (cost > main.coins) {
                    showSelectedColor(circle, Color.RED)
                    return@setOnMouseClicked
                }
                main.coins -= cost
                main.startView.updateCoinsLabel()
                updateCoinsLabel()
                main.selectedSkin = type
                main.startView.updateSelectedCircle()
                setupItems()
                val new = main.owned.toMutableSet()
                new.add(type)
                main.owned = new
                setupItem(circle, type)
                showSelectedColor(circle)
                updateUploadButton()
            }
        }
        if (!main.owned.contains(type) && !isDefault) {
            return
        }
        circle.fill = paint()
    }

    private fun showSelectedColor(circle: Circle, color: Paint = Color.LIME) {
        circle.strokeWidth = 5.0
        circle.stroke = color
        SCOPE.launch {
            delay(500L)
            circle.strokeWidth = 1.0
            circle.stroke = Color.BLACK
        }
    }
}