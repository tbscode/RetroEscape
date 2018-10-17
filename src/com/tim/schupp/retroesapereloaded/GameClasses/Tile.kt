package com.tim.schupp.retroesapereloaded.GameClasses

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.tim.schupp.retroesapereloaded.MainGame

abstract class Tile(var x: Int, var y:Int, var map: Map, var game: MainGame) {
    abstract var name: String
    abstract var texture: TextureRegion
    abstract var tileID: Int
    var visible = true  //If that Tile type is shown in the Map Rendering

    var isItem = false  //If The Tile is a Item the player can pick up

    var allowTileOnOtherLayer = false

    var layer = 0 //To separate Tile and Item Layer

    var tileInfo = ""

    var position: Vector2 = Vector2(x.toFloat()*game.tileSize,y.toFloat()*game.tileSize)

    fun draw(bach: SpriteBatch){
        bach.draw(texture,position.x,position.y)
    }

    fun act(){ //Refreshing and exchanging information withe the game

    }

    //When trying to Enter or trying to leave
    abstract fun enter(x_dir: Int, y_dir: Int): Boolean
    abstract fun leave(x_dir: Int, y_dir: Int): Boolean

    //If actually leaving or entering:
    abstract fun entered(x_dir: Int, y_dir: Int)
    abstract fun left(x_dir: Int, y_dir: Int)

    abstract fun canBeDropped(map: Map): Boolean
}