package com.tim.schupp.retroesapereloaded.InputProcessors

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.tim.schupp.retroesapereloaded.Screens.LevelBuilder

class LevelBuilderInputProcessorGestures(var levelBuilder: LevelBuilder): GestureDetector.GestureListener{


    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        return false
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        levelBuilder.zoom(initialDistance,distance)
        return false
    }

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        levelBuilder.pan(x,y,deltaX,deltaY)
        return true
    }

    override fun pinchStop() {
        levelBuilder.zoomStopped()
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        return false
    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun longPress(x: Float, y: Float): Boolean {
        return false
    }

    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
        return false
    }
}