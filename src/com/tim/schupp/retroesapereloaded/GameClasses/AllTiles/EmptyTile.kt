package com.tim.schupp.retroesapereloaded.GameClasses.AllTiles

import com.tim.schupp.retroesapereloaded.GameClasses.Map
import com.tim.schupp.retroesapereloaded.GameClasses.Tile
import com.tim.schupp.retroesapereloaded.MainGame

class EmptyTile(x:Int, y:Int,map:Map, game: MainGame) : Tile(x,y,map,game){

    override var tileID = 0
    override var name = "Empty"
    override var texture = game.getTileTexture("emptyTile")

    override fun enter(x_dir: Int, y_dir: Int): Boolean {
        return true
    }

    override fun leave(x_dir: Int, y_dir: Int): Boolean {
        return true
    }

    override fun entered(x_dir: Int, y_dir: Int) {

    }

    override fun left(x_dir: Int, y_dir: Int) {

    }

    override fun canBeDropped(map: Map): Boolean {
        return true
    }
}