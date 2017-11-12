package com.mollov.viewcontroller

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.support.annotation.AnimRes
import android.support.annotation.AnimatorRes
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

/**
 * Created by Svetlin Mollov on 15.10.2017 г..
 */
interface Action {

    /**
     * Executes the pending action
     */
    fun now()
}

abstract class AnimatedAction(private val view: View) : Action {

    private var animationRes: Int = 0
    private var animatorRes: Int = 0

    /**
     * @param animationRes The resource ID of the view animation to play
     */
    fun withAnimation(@AnimRes animationRes: Int): Action {
        this.animationRes = animationRes

        return this
    }

    /**
     * @param animatorRes The resource ID of the animation to play
     */
    fun withAnimator(@AnimatorRes animatorRes: Int): Action {
        this.animatorRes = animatorRes

        return this
    }

    internal fun playAnimation(finishCallback: (() -> Unit)? = null) {
        when {
            animationRes != 0 -> {
                val animation = AnimationUtils.loadAnimation(view.context, animationRes)
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationRepeat(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        finishCallback?.invoke()
                    }
                })
                view.startAnimation(animation)
            }
            animatorRes != 0 -> {
                val animator = AnimatorInflater.loadAnimator(view.context, animatorRes)
                animator.addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)

                        finishCallback?.invoke()
                    }
                })
                animator.setTarget(view)
                animator.start()
            }
            else -> finishCallback?.invoke()
        }
    }
}

class AttachAction(
        private val parent: ViewGroup,
        private val view: View,
        private val index: Int,
        private val onPreExecute: () -> Unit) : AnimatedAction(view) {

    private var isExecuted: Boolean = false

    override fun now() {
        // Handle multiple calls of now()
        if (isExecuted) {
            throw IllegalStateException("Calling AttachAction.now() more than once!")
        }
        isExecuted = true

        onPreExecute.invoke()

        parent.addView(view, index)

        playAnimation()
    }
}

class DetachAction(
        private val view: View,
        private val onPostExecute: () -> Unit) : AnimatedAction(view) {

    private var isExecuted: Boolean = false

    override fun now() {
        // Handle multiple calls of now()
        if (isExecuted) {
            throw IllegalStateException("Calling DetachAction.now() more than once!")
        }
        isExecuted = true

        playAnimation({
            val parent = view.parent as ViewGroup
            parent.removeView(view)

            onPostExecute?.invoke()
        })
    }
}