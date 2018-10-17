package com.tim.schupp.retroesapereloaded.GameClasses.AllTiles

import com.tim.schupp.retroesapereloaded.GameClasses.Map
import com.tim.schupp.retroesapereloaded.GameClasses.Tile
import com.tim.schupp.retroesapereloaded.MainGame

class END(x: Int,y: Int,map:Map, game: MainGame): Tile(x,y,map,game){ //Special Marking Tile For the Map editor

    override var tileID = 2
    override var name = "End"
    override var texture = game.getTileTexture("finishTile")

    override fun enter(x_dir: Int, y_dir: Int): Boolean {
        return true
    }

    override fun leave(x_dir: Int, y_dir: Int): Boolean {
        return false
    }

    override fun entered(x_dir: Int, y_dir: Int) {
        game.log("Yes entered")
    }

    override fun left(x_dir: Int, y_dir: Int) {

    }

    override fun canBeDropped(map: Map): Boolean {
        if(map.layers[1].containsTileWithId(tileID)){
            game.log("There can only be one end.")
            return false
        }
        return  true
    }

}