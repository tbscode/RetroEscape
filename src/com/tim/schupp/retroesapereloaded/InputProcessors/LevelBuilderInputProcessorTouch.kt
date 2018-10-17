package com.tim.schupp.retroesapereloaded.InputProcessors

import com.badlogic.gdx.InputProcessor
import com.tim.schupp.retroesapereloaded.Screens.LevelBuilder
import sun.java2d.InvalidPipeException

class LevelBuilderInputProcessorTouch(var levelBuilder: LevelBuilder) : InputProcessor {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        levelBuilder.touchUp(screenX.toFloat(),screenY.toFloat())
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        levelBuilder.scrolled(amount)
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        levelBuilder.touchDown(screenX.toFloat(),screenY.toFloat())
        return true
    }
}