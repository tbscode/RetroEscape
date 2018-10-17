package com.tim.schupp.retroesapereloaded.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.tim.schupp.retroesapereloaded.GameClasses.Map
import com.tim.schupp.retroesapereloaded.GameClasses.MapRender
import com.tim.schupp.retroesapereloaded.GameClasses.Player
import com.tim.schupp.retroesapereloaded.InputProcessors.PlayScreenInputProcessorGestures
import com.tim.schupp.retroesapereloaded.InputProcessors.PlayScreenInputProcessorTouch
import com.tim.schupp.retroesapereloaded.MainGame
import java.util.*

class PlayScreen(var game: MainGame): Screen {

    //UI Stuff
    lateinit var ui_cam: OrthographicCamera
    lateinit var cam_dim: Vector2

    //Game Map Stuff
    lateinit var gameCam: OrthographicCamera
    lateinit var game_dim: Vector2
    lateinit var mapRenderer: MapRender
    lateinit var map: Map

    //Constants
    val fixed_with = 800f
    val fixed_with_mapCam = 1000f

    //The Game Player
    lateinit var player: Player

    override fun show() {
        //Camera to Change Rendering Perspective on the UI Elements
        ui_cam = OrthographicCamera()
        ui_cam.setToOrtho(false,fixed_with, fixed_with*game.dimensions.z)
        cam_dim = Vector2(ui_cam.viewportWidth,ui_cam.viewportHeight)

        //The Camera to be connected with the Map:
        gameCam = OrthographicCamera()
        gameCam.setToOrtho(false,fixed_with_mapCam, fixed_with_mapCam*game.dimensions.z)
        game_dim = Vector2(gameCam.viewportWidth,gameCam.viewportHeight)

        //Rendering Prep
        map = Map(game,0,0)
        mapRenderer = MapRender(map, game)
        mapRenderer.setCamera(gameCam)

        map.loadMapFromString(game.testLevel2)//Loading a Level for Editing
        var startTile = map.findTilesWithId(6)
        game.log("Spawn at: ${startTile[0].x}, ${startTile[0].y}")
        player = Player(startTile[0].x,startTile[0].y,map,game)

        updateCamera()

        var inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(GestureDetector(PlayScreenInputProcessorGestures(this))) //For Pan and zoom
        inputMultiplexer.addProcessor(PlayScreenInputProcessorTouch(this))

        Gdx.input.inputProcessor = inputMultiplexer

    }

    override fun render(delta: Float) {
        game.clearScreen()
        mapRenderer.draw()
        player.act(delta,gameCam)
        updateCamera()//Makes the cam follow the player

        clickProcessing() //Handles Key Inputs
        //game.log("FPS: "+Gdx.graphics.framesPerSecond)
    }

    var keyPressed = Stack<Int>()

    fun clickProcessing(){
        if(!player.moving){
            if(!keyPressed.empty()){
                when(keyPressed.peek()){
                        Input.Keys.W->{
                            player.reguestMove(0,1)
                        }
                        Input.Keys.A->{
                            player.reguestMove(-1,0)
                        }
                        Input.Keys.S->{
                            player.reguestMove(0,-1)
                        }
                        Input.Keys.D->{
                            player.reguestMove(1,0)
                        }
                }
            }
        }
    }

    fun keyUp(keyCode: Int){
        when(keyCode){
            Input.Keys.W->{
                keyPressed.remove(keyCode)
            }
            Input.Keys.A->{
                keyPressed.remove(keyCode)
            }
            Input.Keys.S->{
                keyPressed.remove(keyCode)
            }
            Input.Keys.D->{
                keyPressed.remove(keyCode)
            }
        }
    }

    fun keyDown(keyCode: Int){
        when(keyCode){
            Input.Keys.W->{
                keyPressed.add(keyCode)
            }
            Input.Keys.A->{
                keyPressed.add(keyCode)
            }
            Input.Keys.S->{
                keyPressed.add(keyCode)
            }
            Input.Keys.D->{
                keyPressed.add(keyCode)
            }
        }
    }

    fun updateCamera(){
        gameCam.position.x = player.realX
        gameCam.position.y = player.realY
        gameCam.update()
    }

    override fun hide() {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {

    }
}