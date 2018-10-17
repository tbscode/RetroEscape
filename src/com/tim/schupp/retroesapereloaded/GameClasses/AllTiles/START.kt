package com.tim.schupp.retroesapereloaded.GameClasses.AllTiles

import com.tim.schupp.retroesapereloaded.GameClasses.Map
import com.tim.schupp.retroesapereloaded.GameClasses.Tile
import com.tim.schupp.retroesapereloaded.MainGame

class START(x: Int,y: Int, map:Map,game: MainGame): Tile(x,y,map,game){ //Special Marking Tile For the Map editor

    override var tileID = 1
    override var name = "Start"
    override var texture = game.getTileTexture("startTile")

    init {
        layer = 1
        visible = false
        allowTileOnOtherLayer = true
    }
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
        if(map.containsTileWithID(tileID)){
            game.log("There can only be one start.")
            return false
        }
        return  true
    }
}