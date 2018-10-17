package com.tim.schupp.retroesapereloaded.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.tim.schupp.retroesapereloaded.Extentions.AnimationThread
import com.tim.schupp.retroesapereloaded.Extentions.DoubleInt
import com.tim.schupp.retroesapereloaded.GameClasses.*
import com.tim.schupp.retroesapereloaded.GameClasses.Map
import com.tim.schupp.retroesapereloaded.InputProcessors.LevelBuilderInputProcessorGestures
import com.tim.schupp.retroesapereloaded.InputProcessors.LevelBuilderInputProcessorTouch
import com.tim.schupp.retroesapereloaded.MainGame

class LevelBuilder(var game: MainGame, var width: Int, var height: Int): Screen{

    //When Called the Dimensions of the new map are being passed over

    //UI Stuff
    lateinit var ui_stage: Stage
    lateinit var ui_cam: OrthographicCamera
    lateinit var cam_dim: Vector2

    lateinit var optionsButton: ImageButton

    //Rendering all tile buttons
    lateinit var tileButtons: Array<ImageButton>
    lateinit var tileImages: Array<Image>
    val rowSize = 8

    lateinit var toolButtons: Array<ImageButton>
    lateinit var toolImages: Array<Image>

    //Game Map Stuff
    lateinit var gameCam: OrthographicCamera
    lateinit var game_dim: Vector2
    lateinit var mapRenderer: MapRender
    lateinit var map: Map

    lateinit var fieldsMap: Map
    lateinit var fieldsMapRender: MapRender

    //Constants
    val fixed_with = game.optionButtonSize.x+rowSize*game.tileButtonSize.x
    val fixed_with_mapCam = 400f

    var animator = AnimationThread()

    //Current Selected Tool and Tile
    var currentTool = game.iconNames[0] //The first Tool Icon is the Draw Tool
    var currentTile = 0//game.tiles.tileNames[0] //The first Tile is the Ground Tile

    override fun show() {
        //TODO: Implement mew selection Design
        //TODO: Make eraser layer dependent
        //TODO: Make Item Bar
        //ToDO: Make Move Tool Faster
        //ToDo: Start not always removable

        //Camera to Change Rendering Perspective on the UI Elements
        ui_cam = OrthographicCamera()
        ui_cam.setToOrtho(false,fixed_with, fixed_with*game.dimensions.z)
        cam_dim = Vector2(ui_cam.viewportWidth,ui_cam.viewportHeight)

        //The Camera to be connected with the Map:
        gameCam = OrthographicCamera()
        gameCam.setToOrtho(false,fixed_with_mapCam, fixed_with_mapCam*game.dimensions.z)
        game_dim = Vector2(gameCam.viewportWidth,gameCam.viewportHeight)

        game.tiles.updateGame(game)

        //Lower map to mark fields
        fieldsMap = Map(game, width,height)
        fieldsMapRender = MapRender(fieldsMap,game)
        fieldsMapRender.setCamera(gameCam)

        //Rendering Prep
        map = Map(game,width,height)
        mapRenderer = MapRender(map, game)
        mapRenderer.setCamera(gameCam)

        map.loadMapFromString(game.testLevel2)//Loading a Level for Editing
        game.tiles.changeMap(map)


        //So that the normally invisible Tiles are drawn
        map.layers[0].justDrawVisibleTiles = false
        map.layers[1].justDrawVisibleTiles = false

        //Filling all Fields with selectTiles
        for(x in 0..(width-1)){
            for (y in 0..(height-1)){
                //map.layers[0].setTile(Tiles.EmptyTile(x,y,game))
                fieldsMap.layers[0].setTile(game.tiles.newTileById(x,y,-1))        //-1 is the Id of the select Tile
            }
        }

        //Stage to house all UI Elements
        ui_stage = Stage()
        ui_stage.viewport.camera = ui_cam //Telling the Stage to render in Camera Viewport


        var tileButtonGroup = Table()
        var tileTable = Table()


        //Vertical Tool Area
        var toolTable = Table()
        var toolImageTable = Table()

        //Preparing the Tables
        toolTable.top()
        toolTable.setPosition(game.optionButtonSize.x/2,cam_dim.y - game.optionButtonSize.y)
        toolImageTable.top()
        toolImageTable.setPosition(game.optionButtonSize.x/2,cam_dim.y - game.optionButtonSize.y)

        //Initializing Images and Buttons
        toolButtons = Array(game.iconNames.size) { ImageButton(game.skin,"tileButton") }
        toolImages = Array(game.iconNames.size) {Image()}

        //Horizontal Tile Area

        tileButtonGroup.left()
        tileButtonGroup.setPosition(game.optionButtonSize.x,cam_dim.y - game.optionButtonSize.y/2-game.tileButtonSize.y/2)

        tileTable.left()
        tileTable.setPosition(game.optionButtonSize.x,cam_dim.y - game.optionButtonSize.y/2-game.tileButtonSize.y/2)

        optionsButton = ImageButton(game.skin,"options")
        optionsButton.setPosition(0f, cam_dim.y - game.optionButtonSize.y)

        optionsButton.addListener(object : ClickListener(){
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if(optionsButton.isChecked){
                    tileTable.isVisible = false
                    tileButtonGroup.isVisible = false
                    game.log(map.convertToText(null))
                }else{
                    tileTable.isVisible = true
                    tileButtonGroup.isVisible = true
                }
            }
        })

        tileButtons = Array(game.tiles.tileAmount) { ImageButton(game.skin,"tileButton") }
        tileImages = Array(game.tiles.tileAmount) {Image()}

        var rowCount = 0
        tileButtons.forEachIndexed { count, button ->
            if(count == 0) button.isChecked = true
            button.addListener(object : ClickListener(){
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    tileButtons.forEachIndexed{ otherBtnCount, otherBtn ->
                        if(otherBtnCount!=count)
                            otherBtn.isChecked = false
                    }
                    button.isChecked = true
                    //The Action for the particular Tile Selection
                    currentTile = count+1//Because Tile 0 is the empty tile
                }
            })
            if(count>(rowSize*rowCount+rowSize-1)){
                rowCount++
                tileButtonGroup.row()
            }
            tileButtonGroup.add(button)
        }

        toolButtons.forEachIndexed { count, toolBtn ->
            if(count==0)toolBtn.isChecked = true    //There is always a Button currently selected
            toolBtn.addListener(object : ClickListener(){
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    toolButtons.forEachIndexed{ otherBtnCount, otherBtn ->
                        if(otherBtnCount!=count)
                            otherBtn.isChecked = false
                    }
                    toolBtn.isChecked = true
                    currentTool = game.iconNames[count]

                }
            })
            toolTable.add(toolBtn)
            toolTable.row()
        }

        toolImages.forEachIndexed { count, toolImg ->
            toolImg.drawable = TextureRegionDrawable(game.getUiTexture(game.iconNames[count]))
            toolImg.touchable = Touchable.disabled
            toolImageTable.add(toolImg).size(game.iconSize.toFloat()/2,game.iconSize.toFloat()/2)
                    .pad(game.iconSize.toFloat()/4,game.iconSize.toFloat()/4,game.iconSize.toFloat()/4,game.iconSize.toFloat()/4)
            toolImageTable.row()
        }

        var rowCount2 = 0
        tileImages.forEachIndexed{ count, image ->
            image.drawable = TextureRegionDrawable(TextureRegion( game.tiles.newTileById(0,0,count+1).texture))
            image.touchable = Touchable.disabled
            if(count>(rowSize+rowCount2*rowSize-1)){
                rowCount2++
                tileTable.row()
                game.log("Rowed")
            }
            tileTable.add(image).size(game.tileSize.toFloat()/2,game.tileSize.toFloat()/2)
                    .pad(game.tileSize.toFloat()/4,game.tileSize.toFloat()/4,game.tileSize.toFloat()/4,game.tileSize.toFloat()/4)
        }

        ui_stage.addActor(optionsButton)
        ui_stage.addActor(tileButtonGroup)
        ui_stage.addActor(tileTable)
        ui_stage.addActor(toolTable)
        ui_stage.addActor(toolImageTable)

        //Using a Multiplexer allows to use a Gesture Detector
        var inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(ui_stage)
        inputMultiplexer.addProcessor(GestureDetector(LevelBuilderInputProcessorGestures(this))) //For Pan and zoom
        inputMultiplexer.addProcessor(LevelBuilderInputProcessorTouch(this))

        Gdx.input.inputProcessor = inputMultiplexer

    }

    override fun hide() {

    }

    override fun render(delta: Float) {
        game.clearScreen()
        fieldsMapRender.draw() //Draws the selection grid
        mapRenderer.draw()
        ui_stage.draw()
        ui_stage.act()
        //game.log("FPS: "+Gdx.graphics.framesPerSecond)
    }

    val acceleration = 0.5f
    fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float){
        when(currentTool){
            "moveIcon" -> {
                oldCords = null //So that no tiles are Changed
                //Calculating the distance passed in the Stage
                var delY = (deltaY / game.dimensions.y) * game_dim.y
                var delX = (deltaX / game.dimensions.x) * game_dim.x
                gameCam.translate(-delX, delY)
                gameCam.update()
            }
            else -> {
                var cords = gameCam.unproject(Vector3(x,y,0f))
                var blockCord = DoubleInt((cords.x/game.tileSize).toInt(), (cords.y/game.tileSize).toInt())
                if(blockCord.a in 0..(width-1) &&
                        blockCord.b in 0..(height-1)){
                    if(currentTool.equals("eraserIcon")){
                        map.layers[0].setTile(game.tiles.newTileById(blockCord.a,blockCord.b,0))
                    }else {
                        map.putTile(
                                game.tiles.newTileById(blockCord.a,blockCord.b,
                                currentTile))
                    }
                }
            }
        }

    }


    var initialScale = 1f

    var zoomStep = 2f
    fun zoom(initialDistance: Float, distance: Float){
        //ToDo: Block Drawing during zoom
        val ratio = initialDistance / distance
        gameCam.zoom = initialScale * ratio
        gameCam.update()
    }

    fun zoomStopped(){
        initialScale = gameCam.zoom
    }

    //Using the Mouse wheel
    var scale = 0.1f
    fun scrolled(amount: Int){
        gameCam.zoom += amount*scale
        gameCam.update()
    }

    var oldCords: DoubleInt? = null
    fun touchDown(x: Float, y: Float){
        var cords = gameCam.unproject(Vector3(x,y,0f))
        if(cords.x>0 && cords.y>0)  //So that drawing beside the Grid doesn't create a Tile
        oldCords = DoubleInt((cords.x/game.tileSize).toInt(), (cords.y/game.tileSize).toInt())
    }

    fun touchUp(x: Float,y:Float){
        var cords = gameCam.unproject(Vector3(x,y,0f))
        var blockCord = DoubleInt((cords.x/game.tileSize).toInt(), (cords.y/game.tileSize).toInt())
        if(oldCords!=null && blockCord.equals(oldCords!!)&&
                blockCord.a in 0..(width-1) &&
                blockCord.b in 0..(height-1)) {
            if(currentTool == "eraserIcon"){
                singleErase(blockCord.a,blockCord.b)
            }else if(currentTool == "settingsIcon"){

            }else {
                //If in Any tool selection other to the listed above a tile can be placed
                map.putTile(
                        game.tiles.newTileById(blockCord.a,blockCord.b,
                                currentTile))
            }
        }
    }

    //Functions for the Tool actions
    fun singleErase(x: Int, y: Int){

    }

    fun placeSingleTile(x: Int, y: Int){

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {
        ui_cam.setToOrtho(false,fixed_with, fixed_with*game.dimensions.z)
        cam_dim = Vector2(ui_cam.viewportWidth,ui_cam.viewportHeight)
        ui_cam.update()
    }

    override fun dispose() {

    }
}