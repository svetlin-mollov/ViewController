package com.mollov.viewcontroller

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Svetlin Mollov on 15.10.2017 г..
 */
abstract class ViewController(val context: Context) : View.OnAttachStateChangeListener {

    private var view: View? = null

    /**
     * @return The layout resource ID of the ViewController's view
     */
    @LayoutRes
    abstract protected fun getLayoutId(): Int

    /**
     * Adds the ViewController's view to the [parent]'s layout hierarchy. If the view
     * already has a parent, it will be removed from it and added in the new container
     *
     * @param parent The [ViewGroup] to add the ViewController's view to
     * @param index The position at which to add the ViewController's view
     *
     * @see remove
     */
    fun addIn(parent: ViewGroup, index: Int = -1): AddAction {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false)

            onCreated()
        } else {
            remove().now()
        }

        val view = getView()
        view.addOnAttachStateChangeListener(this)

        return AddAction(parent, view, index)
    }

    /**
     * Removes the ViewController's view from it's parent
     *
     * @throws [IllegalStateException] if the view is not added in parent
     *
     * @see isAdded
     * @see addIn
     */
    fun remove(): RemoveAction {
        val view = getView()

        return RemoveAction(view) {
            view.removeOnAttachStateChangeListener(this)
        }
    }

    /**
     * @return The ViewController's root [View]
     *
     * @throws [IllegalStateException] if the view is not added in parent
     *
     * @see isAdded
     */
    fun getView(): View {
        return view ?: throw IllegalStateException("${javaClass.simpleName} not added, yet!")
    }

    /**
     * @return true of the ViewController's view is added to parent ViewGroup, false otherwise
     *
     * @see addIn
     */
    fun isAdded(): Boolean = view?.isAttachedToWindow ?: false

    final override fun onViewAttachedToWindow(v: View) {
        onAdded()
    }

    final override fun onViewDetachedFromWindow(v: View) {
        onRemoved()
    }

    fun <T : View> findViewById(@IdRes id: Int): T? = view?.findViewById(id)

    /**
     * Called the first time when the ViewController's view is added to a parent ViewGroup.
     * Should be used to initialize UI state
     */
    protected abstract fun onCreated()

    /**
     *Called every time the ViewController's view is added to a parent ViewGroup
     */
    protected open fun onAdded() {}

    /**
     * Called every time the ViewController's view is removed from it's parent ViewGroup
     */
    protected open fun onRemoved() {}
}