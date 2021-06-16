package me.dkim19375.tag.view

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dkim19375.dkimcore.extension.SCOPE
import me.dkim19375.tag.VIEW_TITLE
import me.dkim19375.tag.main
import me.dkim19375.tag.util.SkinType
import tornadofx.View

class SkinsView : View(VIEW_TITLE) {
    override val root: VBox by fxml()
    private val setup = mutableSetOf<Int>()
    private val image1: Circle by fxid()
    private val image2: Circle by fxid()
    private val image3: Circle by fxid()
    private val image4: Circle by fxid()
    private val image5: Circle by fxid()
    private val image6: Circle by fxid()
    private val costLabel1: Label by fxid()
    private val costLabel2: Label by fxid()
    private val costLabel3: Label by fxid()
    private val costLabel4: Label by fxid()
    private val costLabel5: Label by fxid()
    private val costLabel6: Label by fxid()
    private val backButton: Button by fxid()
    private val coinsLabel: Label by fxid()
    private val circles = listOf(image1, image2, image3, image4, image5, image6)
    private val costs = listOf(costLabel1, costLabel2, costLabel3, costLabel4, costLabel5, costLabel6)

    init {
        main.skinsView = this
        backButton.setOnAction {
            replaceWith<StartView>()
        }
    }

    fun start() {
        updateCoinsLabel()
        setupItems()
    }

    private fun updateCoinsLabel() {
        coinsLabel.text = "Coins: ${main.coins}"
    }

    private fun setupItems() {
        for (type in SkinType.purchasableValues()) {
            val circle = circles[type.intValue - 1]
            setupItem(circle, type)
        }
    }

    private fun setupItem(circle: Circle, type: SkinType) {
        val number = type.intValue
        val imagePath = type.imagePath
        val cost = type.cost
        circle.fill = ImagePattern(Image("images/lock.png"))
        costs[type.intValue - 1].text = "Cost: $cost Coins"
        costs[type.intValue - 1].textFill = if (cost > main.coins) Color.RED else Color.DARKGREEN
        if (!setup.contains(number)) {
            setup.add(number)
            circle.setOnMouseClicked { event ->
                if (event.button != MouseButton.PRIMARY) {
                    return@setOnMouseClicked
                }
                if (main.owned.contains(type)) {
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
            }
        }
        if (!main.owned.contains(type)) {
            return
        }
        circle.fill = ImagePattern(
            try {
                Image(imagePath)
            } catch (_: IllegalArgumentException) {
                return
            }
        )
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