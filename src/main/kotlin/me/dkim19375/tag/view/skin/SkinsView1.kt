package me.dkim19375.tag.view.skin

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.shape.Circle
import javafx.stage.FileChooser
import me.dkim19375.tag.abstract.SkinsViewAbstract
import me.dkim19375.tag.main
import me.dkim19375.tag.enumclass.SkinType
import me.dkim19375.tag.util.setOnPress
import tornadofx.*

class SkinsView1 : SkinsViewAbstract() {
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
    override val circles = listOf(image0, image1, image2, image3, image4, image5, image6, customImage)
    override val costs =
        listOf(costLabel0, costLabel1, costLabel2, costLabel3, costLabel4, costLabel5, costLabel6, customCostLabel)
    override val firstItem: Int = 0
    override val backButton: Button by fxid()

    init {
        main.skinsView1 = this
    }

    override fun onStart() {
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
                main.startView.updateSelectedCircle()
            }
        }
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