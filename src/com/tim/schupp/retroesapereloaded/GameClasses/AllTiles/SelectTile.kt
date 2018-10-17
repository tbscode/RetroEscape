package com.tim.schupp.retroesapereloaded.GameClasses.AllTiles

import com.tim.schupp.retroesapereloaded.GameClasses.Map
import com.tim.schupp.retroesapereloaded.GameClasses.Tile
import com.tim.schupp.retroesapereloaded.MainGame

class SelectTile(x: Int,y: Int,map:Map,  game: MainGame): Tile(x,y,map,game){ //Special Marking Tile For the Map editor

    override var tileID = -1
    override var name = "Empty"
    override var texture = game.getTileTexture("selectTile")

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