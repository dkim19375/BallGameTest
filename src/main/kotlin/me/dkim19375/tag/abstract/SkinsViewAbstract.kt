package me.dkim19375.tag.abstract

import javafx.application.Platform
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
import me.dkim19375.tag.enumclass.SkinType
import me.dkim19375.tag.main
import me.dkim19375.tag.util.VIEW_TITLE
import me.dkim19375.tag.util.applyBackgroundSettings
import me.dkim19375.tag.util.setOnPress
import me.dkim19375.tag.view.StartView
import tornadofx.*
import kotlin.reflect.KClass

@Suppress("LeakingThis", "UNCHECKED_CAST")
abstract class SkinsViewAbstract : View(VIEW_TITLE) {
    override val root: VBox by fxml("/${javaClass.name.replace('.', '/')}.fxml")
    private val coinsLabel: Label by fxid()
    private val setup = mutableSetOf<Int>()
    protected abstract val circles: List<Circle>
    protected abstract val costs: List<Label>
    protected abstract val firstItem: Int
    protected open val previousButton: Button? = null
    protected open val nextButton: Button? = null
    protected abstract val backButton: Button
    private val current = javaClass.simpleName[9].toString().toInt()

    init {
        Platform.runLater {
            backButton.setOnPress {
                replaceWith<StartView>()
            }
            previousButton?.setOnAction {
                onChangeButton(-1)
            }
            nextButton?.setOnAction {
                onChangeButton(1)
            }
            onStart()
            setupItems()
        }
    }

    private fun onChangeButton(change: Int) {
        replaceWith(
            Class.forName(
                javaClass.name.replace(
                    current.toString()[0],
                    (current + change).toString()[0]
                )
            ).kotlin as KClass<out UIComponent>
        )
    }

    open fun start() {
        root.applyBackgroundSettings()
        onUpdate()
        updateCoinsLabel()
        setupItems()
    }

    private fun updateCoinsLabel() {
        coinsLabel.text = "Coins: ${main.coins}"
    }

    private fun setupItems() {
        for (type in SkinType.values()) {
            val circle = circles[type.intValue - firstItem]
            setupItem(circle, type)
        }
    }

    private fun setupItem(circle: Circle, type: SkinType) {
        val number = type.intValue - firstItem
        val paint = type.image
        val cost = type.cost
        val isDefault = type == SkinType.DEFAULT
        if (!isDefault) {
            circle.fill = ImagePattern(Image("images/${if (type == SkinType.CUSTOM) "custom_" else ""}lock.png"))
        }
        costs[number].text = if (isDefault) "Free!" else "Cost: $cost Coins"
        costs[number].textFill = if (cost > main.coins) Color.RED else Color.DARKGREEN
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
                    onUpdate()
                    return@setOnMouseClicked
                }
                // buying
                if (cost > main.coins) {
                    showSelectedColor(circle, Color.RED)
                    onUpdate()
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
                onUpdate()
            }
        }
        if (!main.owned.contains(type) && !isDefault) {
            return
        }
        circle.fill = paint()
    }

    protected open fun onUpdate() {}

    protected open fun onStart() {}

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