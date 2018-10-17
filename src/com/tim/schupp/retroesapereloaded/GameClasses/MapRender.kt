package com.tim.schupp.retroesapereloaded.GameClasses

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.tim.schupp.retroesapereloaded.MainGame

class MapRender(var map: Map,game: MainGame){
    var batch = SpriteBatch()
    lateinit var cam: OrthographicCamera

    fun setCamera(cam: OrthographicCamera){
        this.cam = cam
        batch.projectionMatrix = cam.combined
    }

    fun draw() {
        batch.projectionMatrix = cam.combined
        batch.begin()
        map.layers[0].draw(batch)
        map.layers[1].draw(batch)
        batch.end()
    }


}