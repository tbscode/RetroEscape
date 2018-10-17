package com.tim.schupp.retroesapereloaded.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.tim.schupp.retroesapereloaded.Extentions.AnimationThread
import com.tim.schupp.retroesapereloaded.MainGame
import kotlin.concurrent.thread

class PlayerLevelMenu(var game: MainGame): Screen {

    //UI Elements
    lateinit var createBtn: TextButton
    lateinit var ui_table: Table
    //UI Stuff
    lateinit var ui_stage: Stage
    lateinit var ui_cam: OrthographicCamera
    lateinit var cam_dim: Vector2

    //Constants
    val fixed_with = 200f

    var animator = AnimationThread()

    override fun show() {

        //Camera to Change Rendering Perspective on the UI Elements
        ui_cam = OrthographicCamera()
        ui_cam.setToOrtho(false,fixed_with, fixed_with*game.dimensions.z)
        cam_dim = Vector2(ui_cam.viewportWidth,ui_cam.viewportHeight)

        //Stage to house all UI Elements
        ui_stage = Stage()
        ui_stage.viewport.camera = ui_cam //Telling the Stage to render in Camera Viewport

        createBtn = TextButton("NEW",game.skin)
        createBtn.labelCell.pad(2f,20f,2f,20f)

        createBtn.addListener(object : ClickListener(){
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                //game.screen = LevelBuilder(game)
            }
        })

        ui_table = Table()
        ui_table.setPosition(ui_table.width/2, cam_dim.y/2)
        ui_table.add(createBtn)
        ui_table.row()

        ui_stage.addActor(ui_table)

        ui_stage.addActor(animator)

        Gdx.input.inputProcessor = ui_stage

        ui_cam.position.x += (cam_dim.x)

        openAnimation()
    }

    override fun hide() {

    }

    override fun render(delta: Float) {
        game.clearScreen()
        ui_stage.draw()
        ui_stage.act()
    }

    fun openAnimation(){
        var animation_duration = 500
        var end_pos = 0
        var step_distance = (cam_dim.x)/animation_duration
        animator.addAnimation(
                thread {
                    while (end_pos <= ui_cam.position.x){
                        Thread.sleep(1)
                        ui_cam.position.x -= step_distance
                    }
                }
        ) {game.log("HI")}
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        ui_table = Table()
        ui_table.setPosition(ui_table.width/2, cam_dim.y/2)

        ui_cam.setToOrtho(false,fixed_with, fixed_with*game.dimensions.z)
        cam_dim = Vector2(ui_cam.viewportWidth,ui_cam.viewportHeight)
        ui_cam.position.x = 0f
        ui_cam.position.y = 0f

        ui_cam.update()
    }

    override fun dispose() {
    }
}