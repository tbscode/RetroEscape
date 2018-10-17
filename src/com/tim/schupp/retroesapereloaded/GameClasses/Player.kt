package com.tim.schupp.retroesapereloaded.GameClasses

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.tim.schupp.retroesapereloaded.MainGame

class Player(var x: Int, var y: Int,var map: Map, var game: MainGame){

    //Real Screen Position
    var realX = x.toFloat()*game.tileSize
    var realY = y.toFloat()*game.tileSize

    //Moving Variables
    var moving = false
    var dirX = 0
    var dirY = 0

    //Animation Stuff
    lateinit var animation: Array<Animation<TextureRegion>?>
    lateinit var animationFrame: Array<com.badlogic.gdx.utils.Array<TextureRegion>>
    var animationDuration = 1/16f
    var timePassed = 0f
    lateinit var currentFrame: TextureRegion
    var label = "abdd"

    var playerItems: IntArray   //Bomb|Red|Green|Blue

    init {
        loadAnimation() //Loads the Animation Sprite Sheet.
        playerItems = map.playerItems
    }

    fun loadAnimation(){
        animationFrame = Array(4){com.badlogic.gdx.utils.Array<TextureRegion>(arrayOfNulls(4))}

        for (num in 1..animationFrame.size){
            for(num2 in 1..animationFrame[(num-1)].size){
                animationFrame[(num-1)][(num2-1)] = TextureRegion()
            }
        }

        animation = arrayOfNulls(4)
        for(num in 0..(animationFrame[0].size-1)){
            animationFrame[0][num] = game.playerAnimationPack.findRegion("up${label[num]}")
            animationFrame[1][num] = game.playerAnimationPack.findRegion("left${label[num]}")
            animationFrame[2][num] = game.playerAnimationPack.findRegion("down${label[num]}")
            animationFrame[3][num] = game.playerAnimationPack.findRegion("right${label[num]}")
        }

        for(num in 0..(animation.size-1)){
            animation[num] = Animation(animationDuration,animationFrame[num])
            animation[num]!!.playMode = Animation.PlayMode.LOOP
        }

        currentAnimation = animation[0]!! //Starting

    }

    fun updateMap(gameMap: Map){
        map = gameMap
    }

    private var batch = SpriteBatch()
    fun draw(cam: OrthographicCamera){ //The player has his own sprite batch
        batch.projectionMatrix = cam.combined
        batch.begin()
        batch.draw(currentFrame,realX,realY)
        batch.end()
    }

    lateinit var currentAnimation: Animation<TextureRegion>
    fun act(deltaTime: Float, cam: OrthographicCamera){  //Loop Method for the Player to Act every Turn
        timePassed += deltaTime
        if(moving) {
            currentFrame = currentAnimation.getKeyFrame(timePassed)
        } else {
            currentFrame = currentAnimation.keyFrames[0]
        }
        move() //Move the player according to current
        draw(cam)
    }

    fun reguestMove(x_dir: Int, y_dir: Int){ // (0,0) would be stopping
        if(!moving){
            //Checking if the Tile even Exists:
            var newX = x + x_dir
            var newY = y + y_dir
            if(newX<map.width&&newY<map.height&&0<=newX&&0<=newY){
                //YES it exists so lets start moving
                var tileNew = map.get(newX,newY)
                var tileOld = map.get(x,y)
                if(     tileNew[0].enter(newX,newY)&&
                        tileNew[1].enter(newX,newY)&&
                        tileOld[0].leave(x,y)&&
                        tileOld[1].leave(x,y)){
                    moving = true
                    dirX = x_dir
                    dirY = y_dir

                    //Start the Animation
                    if(dirX==0&&dirY==1){//Move UP
                        currentAnimation = animation[0]!!
                    }else if(dirX==-1&&dirY==0){//Move LEFT
                        currentAnimation = animation[1]!!
                    }else if(dirX==0&&dirY==-1){//Move DOWN
                        currentAnimation = animation[2]!!
                    }else if(dirX==1&&dirY==0){//Move RIGHT
                        currentAnimation = animation[3]!!
                    }
                }
            }


        }
    }

    //All stuff for moving
    //TODO: Meke a running Speed option and buttoon
    var stepAmount = 8 //Divide by 4 to get circle Amount
    var stepDistance = game.tileSize/stepAmount.toFloat()
    var passedDistance = 0f
    fun move(){
        if(moving){
            realY += dirY.toFloat()*stepDistance
            realX += dirX.toFloat()*stepDistance
            passedDistance += stepDistance
            if(passedDistance>=game.tileSize.toFloat()){//The Movement is done
                moving = false
                passedDistance = 0f
                x += dirX
                y += dirY
                realX = x.toFloat()*game.tileSize.toFloat()
                realY = y.toFloat()*game.tileSize.toFloat()

                //End the Animation
            }
        }
    }
}