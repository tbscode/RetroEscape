package com.tim.schupp.retroesapereloaded.InputProcessors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.tim.schupp.retroesapereloaded.Screens.LevelBuilder
import com.tim.schupp.retroesapereloaded.Screens.PlayScreen
import sun.java2d.InvalidPipeException

class PlayScreenInputProcessorTouch(var playScreen: PlayScreen) : InputProcessor {

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        playScreen.keyUp(keycode)
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        playScreen.keyDown(keycode)
        return true
    }


    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }
}