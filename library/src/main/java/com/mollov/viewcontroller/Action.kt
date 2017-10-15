package com.mollov.viewcontroller

import android.support.annotation.AnimRes
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

/**
 * Created by Svetlin Mollov on 15.10.2017 г..
 */
interface Action {

    fun now()
}

abstract class AnimatedAction : Action {

    protected var animationRes: Int = 0

    fun animate(@AnimRes animationRes: Int): Action {
        this.animationRes = animationRes

        return this
    }
}

class AddAction(private val parent: ViewGroup, private val view: View, private val index: Int) : AnimatedAction() {

    override fun now() {
        parent.addView(view, index)

        if (animationRes != 0) {
            val enterAnim = AnimationUtils.loadAnimation(view.context, animationRes)
            view.startAnimation(enterAnim)
        }
    }
}

class RemoveAction(private val view: View, private val onExecute: (() -> Unit)? = null) : AnimatedAction() {

    override fun now() {

        fun executeAction() {
            var parent = view.parent as ViewGroup
            parent.removeView(view)

            onExecute?.invoke()
        }

        if (animationRes != 0) {
            val enterAnim = AnimationUtils.loadAnimation(view.context, animationRes)
            enterAnim.setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    executeAction()
                }
            })
            view.startAnimation(enterAnim)
        } else {
            executeAction()
        }
    }
}