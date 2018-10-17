package com.tim.schupp.retroesapereloaded

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.tim.schupp.retroesapereloaded.GameClasses.Tiles
import com.tim.schupp.retroesapereloaded.Screens.LevelBuilder
import com.tim.schupp.retroesapereloaded.Screens.MainMenu
import com.tim.schupp.retroesapereloaded.Screens.PlayScreen

class MainGame : Game(), Thread.UncaughtExceptionHandler{

    //Map for Debugging
    var testLevel2 ="[10,10,\"[0,0,0,0]\",\"[[4,4,4,4,4,4,4,4,4,4],[4,3,3,3,3,3,3,3,3,4],[4,3,3,3,3,3,3,3,3,4],[4,3,3,3,12,3,3,3,3,4],[4,3,3,14,3,3,3,3,3,4],[4,3,3,3,3,3,3,3,3,4],[4,3,3,15,3,3,3,3,3,4],[4,3,3,3,3,3,3,3,3,4],[4,2,3,3,3,3,3,3,3,4],[4,4,4,4,4,4,4,4,4,4]]\",\"[[0,0,0,0,0,0,0,0,0,5],[0,0,6,0,0,0,0,0,1,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0]]\",\"[[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"]]\",\"[[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"],[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"]]\"]\n"
    //All Game Tiles
    var iconNames = arrayOf("editIcon","moveIcon","eraserIcon","settingsIcon")

    var tiles = Tiles() //Kind of tile manager can be used to Create Tiles

    lateinit var assets: AssetManager
    lateinit var uiPack: TextureAtlas       //All game ui elements
    lateinit var tilePack: TextureAtlas     //All Game Tiles
    lateinit var dimensions: Vector3    //z ist (y/x) whole dimension vectors and relation vector
    lateinit var skin: Skin             //Skin contains all UI skins (call void loadSkin())

    //For the Player Animation
    lateinit var playerAnimationPack: TextureAtlas

    //GAME CONSTANTS:
    val BackgroundColor: Color = Color(0f,0f,0f,1f)
    val tileSize = 64
    val iconSize = 64

    //ToDo Android cant handle the threats solve?

    override fun create() {
        assets = AssetManager() //Will be used to manage All  Kinds of Assets

        assets.load("TILES/TilesPack.pack",TextureAtlas::class.java)
        assets.load("UI/UiPack.pack",TextureAtlas::class.java)

        assets.load("PLAYER/playeranimation.pack",TextureAtlas::class.java)

        assets.finishLoading() //assets.update() if I want to load them async
        //for getting the loaded asses use: assets.get("buttons.pack", Texture...
        uiPack = assets.get("UI/UiPack.pack")
        tilePack = assets.get("TILES/TilesPack.pack")
        playerAnimationPack = assets.get("PLAYER/playeranimation.pack")

        dimensions = Vector3(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(),
                (Gdx.graphics.height.toFloat())/(Gdx.graphics.width.toFloat())) //Obtaining the screen dimensions

        loadSkin()  //IMPORTANT: Loads all UI Skins

        //Setting the Game Cursor
        var pixmap = Pixmap(Gdx.files.internal("UI/cursor.png"))
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap,0,0))

        //LevelBuilder(this,10,10)
        setScreen(LevelBuilder(this,10,10) ) //Opens the Main Menu and Passes the Game Controller it self.
    }

    fun clearScreen(){
        Gdx.gl.glClearColor(this.BackgroundColor.r, this.BackgroundColor.g,
                this.BackgroundColor.b, this.BackgroundColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height) //ToDO: Make Game Fucking Resizable man
        //Changing the screen dimensions
        dimensions.set(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(),
                (Gdx.graphics.height.toFloat())/(Gdx.graphics.width.toFloat()))
    }

    fun <T: Any> getAsset(name: String): T?{
        return when(assets.assetNames.contains(name)){
            true -> assets.get(name)
            false -> null //Returns noting if not contained
        }
    }

    fun getUiTexture(name: String): TextureRegion{
        return uiPack.findRegion(name)
    }

    fun getTileTexture(name: String): TextureRegion{
        return tilePack.findRegion(name)
    }

    lateinit var windowSize: Vector3 //Again the third on ist the relation
    lateinit var tileButtonSize: Vector2
    lateinit var optionButtonSize: Vector3

    private fun loadSkin(){
        skin = Skin()

        var textBtnStyle = TextButton.TextButtonStyle()
        val up_button = this.getUiTexture("button_pos1")
        val down_button = this.getUiTexture("button_pos3")
        textBtnStyle.up = TextureRegionDrawable(up_button)
        textBtnStyle.down = TextureRegionDrawable(down_button)

        //GameFont
        var font = BitmapFont(Gdx.files.internal("FONT/game_font.fnt"),Gdx.files.internal("FONT/game_font.png"),false)

        textBtnStyle.font = font
        textBtnStyle.checkedOffsetY = 0f
        textBtnStyle.pressedOffsetY = 2f
        textBtnStyle.fontColor = Color.GOLD

        skin.add("default",textBtnStyle)

        var windowStyle = Window.WindowStyle()
        font.data.scale(0.2f)
        windowStyle.titleFont = font
        var windowBackgroudnRegion = this.getUiTexture("window")
        windowStyle.background = TextureRegionDrawable(windowBackgroudnRegion)
        windowSize = Vector3(
                windowBackgroudnRegion.regionWidth.toFloat(),
                windowBackgroudnRegion.regionHeight.toFloat(),
                windowBackgroudnRegion.regionWidth.toFloat()/windowBackgroudnRegion.regionHeight.toFloat()
        )

        skin.add("default",windowStyle)

        var optionButtonStyle = ImageButton.ImageButtonStyle()
        var textureOptionsButton = this.getUiTexture("optionsButton")
        optionButtonStyle.imageDown = TextureRegionDrawable(this.getUiTexture("optionsButtonDown"))
        optionButtonStyle.imageUp = TextureRegionDrawable(textureOptionsButton)

        optionButtonSize = Vector3(
                textureOptionsButton.regionWidth.toFloat(),
                textureOptionsButton.regionHeight.toFloat(),
                textureOptionsButton.regionWidth.toFloat()/textureOptionsButton.regionHeight.toFloat()
        )

        skin.add("options",optionButtonStyle)

        var tileButtonStyle = ImageButton.ImageButtonStyle()
        var tileButtonTexture = this.getUiTexture("tileButton")

        tileButtonStyle.imageUp = TextureRegionDrawable(tileButtonTexture)
        tileButtonStyle.imageDown = TextureRegionDrawable(tileButtonTexture)
        tileButtonStyle.imageChecked = TextureRegionDrawable(this.getUiTexture("tileButtonSelected"))

        tileButtonSize = Vector2(
                tileButtonTexture.regionWidth.toFloat(),
                tileButtonTexture.regionHeight.toFloat()
        )
        skin.add("tileButton",tileButtonStyle)


    }

    override fun uncaughtException(p0: Thread?, p1: Throwable?) {
        log("nice")
    }

    fun log(message: String){
        Gdx.app.log("DBG",message)
    }

}
