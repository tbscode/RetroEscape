package com.tim.schupp.retroesapereloaded.GameClasses.AllTiles

import com.tim.schupp.retroesapereloaded.GameClasses.Map
import com.tim.schupp.retroesapereloaded.GameClasses.Tile
import com.tim.schupp.retroesapereloaded.MainGame

class Ground(x:Int, y:Int,map:Map, game: MainGame) : Tile(x,y,map,game){

    override var name = "Ground"
    override var texture = game.getTileTexture("ground")
    override var tileID = 3

    init {
        allowTileOnOtherLayer = true
    }

    override fun canBeDropped(map: Map): Boolean {
        return true
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
}