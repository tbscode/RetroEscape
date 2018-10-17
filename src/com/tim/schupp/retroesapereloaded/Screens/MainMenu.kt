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
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.tim.schupp.retroesapereloaded.Extentions.AnimationThread
import com.tim.schupp.retroesapereloaded.MainGame
import kotlin.concurrent.thread


class MainMenu (var game: MainGame): Screen {

    lateinit var animator: AnimationThread

    //The UI Elements
    lateinit var starBtn: TextButton
    lateinit var makeBtn: TextButton
    lateinit var window: Window

    //Stage and Camera
    lateinit var ui_stage: Stage
    lateinit var ui_cam: OrthographicCamera
    lateinit var cam_dim: Vector2

    //Constants
    val fixed_with = 200f

    override fun show() {

        //Camera to Change Rendering Perspective on the UI Elements
        ui_cam = OrthographicCamera()
        ui_cam.setToOrtho(false,fixed_with, fixed_with*game.dimensions.z)
        cam_dim = Vector2(ui_cam.viewportWidth,ui_cam.viewportHeight)

        //Stage to house all UI Elements
        ui_stage = Stage()
        ui_stage.viewport.camera = ui_cam //Telling the Stage to render in Camera Viewport


        //Preparing the UI Elements:
        starBtn = TextButton("Play",game.skin)
        starBtn.labelCell.pad(2f,20f,2f,20f)

        starBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                exitMenue { game.screen = LevelBuilder(game,10,10)}
            }
        })

        makeBtn = TextButton("Make",game.skin)
        makeBtn.labelCell.pad(2f,20f,2f,20f)

        makeBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                exitMenue {game.screen = PlayerLevelMenu(game)}
            }
        })

        window = Window("Menu",game.skin)
        window.setSize(cam_dim.x,cam_dim.x*(1/game.windowSize.z))
        window.setPosition(0f,0f)

        //Setting up a table to Render elements in:



        window.add(starBtn)
        window.row()
        window.add(makeBtn)

        ui_stage.addActor(window)

        animator = AnimationThread()

        ui_stage.addActor(animator)

        //Make the Stage Process Inputs
        Gdx.input.inputProcessor = ui_stage

        ui_cam.zoom = 2f
        enterMenu()

    }

    override fun render(delta: Float) {
        game.clearScreen()
        ui_stage.act()
        ui_stage.draw()

    }

    //Animation when Menu is entered
    fun enterMenu(){
        var start_val = 0f
        var end_val = 1f
        ui_cam.zoom = start_val
        var animation_duration = 750
        animator.addAnimation(
        thread {
            for(num: Int in 1..animation_duration){
                Thread.sleep(1)
                ui_cam.zoom += end_val/animation_duration
            }
        }) {game.log("HI")}
    }

    //Animation when menu is exited
    fun exitMenue(do_after: () -> Unit) {
        var animation_duration = 500
        var end_pos = cam_dim.x
        var step_distance = (end_pos-Math.min(starBtn.x,makeBtn.x))/animation_duration
        animator.addAnimation(
        thread {
            while (end_pos >= Math.min(starBtn.x,makeBtn.x)){
                Thread.sleep(1)
                starBtn.x += step_distance
                makeBtn.x += step_distance

            }
        },do_after)
    }

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {}

    override fun hide() {}
}