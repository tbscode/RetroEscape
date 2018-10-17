package com.tim.schupp.retroesapereloaded.GameClasses.AllTiles

import com.tim.schupp.retroesapereloaded.GameClasses.Map
import com.tim.schupp.retroesapereloaded.GameClasses.Tile
import com.tim.schupp.retroesapereloaded.MainGame

class Wall(x:Int, y:Int,map:Map, game: MainGame) : Tile(x,y,map,game){

    override var tileID = 4
    override var name = "Wall"
    override var texture = game.getTileTexture("wall")

    override fun enter(x_dir: Int, y_dir: Int): Boolean {
        return false //False since it cant be entered
    }

    override fun leave(x_dir: Int, y_dir: Int): Boolean {
        return false
    }


    override fun entered(x_dir: Int, y_dir: Int){

    }

    override fun left(x_dir: Int, y_dir: Int) {

    }

    override fun canBeDropped(map: Map): Boolean {
        return true
    }


}