package me.dkim19375.tag.view.skin

import com.jfoenix.controls.JFXButton
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.shape.Circle
import me.dkim19375.tag.abstract.SkinsViewAbstract
import me.dkim19375.tag.main
import me.dkim19375.tag.util.kfxButton

class SkinsView1 : SkinsViewAbstract() {
    private val image0: Circle by fxid()
    private val image1: Circle by fxid()
    private val image2: Circle by fxid()
    private val image3: Circle by fxid()
    private val costLabel0: Label by fxid()
    private val costLabel1: Label by fxid()
    private val costLabel2: Label by fxid()
    private val costLabel3: Label by fxid()
    override val circles = listOf(image0, image1, image2, image3)
    override val costs =
        listOf(costLabel0, costLabel1, costLabel2, costLabel3)
    override val firstItem: Int = 0
    override val backBox: HBox by fxid()
    override val nextBox: HBox by fxid()
    override val backButton: JFXButton = kfxButton("Back", backBox)
    override val nextButton: JFXButton = kfxButton("Next Page", nextBox)

    init {
        main.skinsView1 = this
    }
}