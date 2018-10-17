package com.tim.schupp.retroesapereloaded.GameClasses.AllTiles

import com.tim.schupp.retroesapereloaded.GameClasses.Map
import com.tim.schupp.retroesapereloaded.GameClasses.Tile
import com.tim.schupp.retroesapereloaded.MainGame

class Shield(x:Int, y:Int,map:Map, game: MainGame) : Tile(x,y,map,game){

    override var tileID = 5
    override var name = "Shield"
    override var texture = game.getTileTexture("shieldTile")

    init {
        layer = 1
        allowTileOnOtherLayer = true
    }

    override fun enter(x_dir: Int, y_dir: Int): Boolean {
        return true //False since it cant be entered
    }

    override fun leave(x_dir: Int, y_dir: Int): Boolean {
        return true
    }


    override fun entered(x_dir: Int, y_dir: Int){
        game.log(tileInfo) //Logs the Text given in the Tile Data
    }

    override fun left(x_dir: Int, y_dir: Int) {

    }

    override fun canBeDropped(map: Map): Boolean {
        return true
    }

}