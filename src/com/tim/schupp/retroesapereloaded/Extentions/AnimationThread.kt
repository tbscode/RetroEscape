package com.tim.schupp.retroesapereloaded.Extentions

import com.badlogic.gdx.scenes.scene2d.Actor
import java.util.*

/** Nice little class to create in Scene Independent Animation Threads and
    after the Thread ends a method do_after is called **/

class AnimationThread: Actor() {

    var threadSteak: Queue<Thread> = LinkedList()
    var doAfterStack: Queue<() -> Unit> = LinkedList()

    private var active = false

    fun addAnimation(thread: Thread,do_after: () -> Unit){
        threadSteak.add(thread)
        doAfterStack.add(do_after)
        active = true
    }

    override fun act(delta: Float) {
        super.act(delta)
        if(threadSteak.size!=0)
        if(threadSteak.element().isAlive){
            //do nothing
        }else{
            threadSteak.remove()
            doAfterStack.element().invoke()
            doAfterStack.remove()
        }
    }

}