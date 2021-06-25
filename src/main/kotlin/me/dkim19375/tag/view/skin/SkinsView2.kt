package me.dkim19375.tag.view.skin

import com.jfoenix.controls.JFXButton
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.shape.Circle
import javafx.stage.FileChooser
import me.dkim19375.tag.abstract.SkinsViewAbstract
import me.dkim19375.tag.enumclass.SkinType
import me.dkim19375.tag.main
import me.dkim19375.tag.util.kfxButton
import me.dkim19375.tag.util.setOnPress
import tornadofx.*

class SkinsView2 : SkinsViewAbstract() {
    private val uploadHBox: HBox by fxid()
    private val uploadButton: JFXButton = kfxButton("Upload Custom Skin", uploadHBox)
    private val customImage: Circle by fxid()
    private val image4: Circle by fxid()
    private val image5: Circle by fxid()
    private val image6: Circle by fxid()
    private val customCostLabel: Label by fxid()
    private val costLabel4: Label by fxid()
    private val costLabel5: Label by fxid()
    private val costLabel6: Label by fxid()
    override val circles = listOf(image4, image5, image6, customImage)
    override val costs = listOf(costLabel4, costLabel5, costLabel6, customCostLabel)
    override val firstItem: Int = 4
    override val backBox: HBox by fxid()
    override val previousBox: HBox by fxid()
    override val backButton: JFXButton = kfxButton("Back", backBox)
    override val previousButton: JFXButton = kfxButton("Previous Page", previousBox)

    init {
        main.skinsView2 = this
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
                main.startView.updateSelectedCircle()
            }
        }
    }

    override fun onStart() {
        updateUploadButton()
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

    override fun onUpdate() {
        updateUploadButton()
    }
}