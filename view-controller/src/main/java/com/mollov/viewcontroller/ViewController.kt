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
abstract class ViewController(private val context: Context) {

    private var view: View? = null

    private val attachDetachListener = AttachDetachListener()

    /**
     * @return The layout resource ID of the ViewController's view
     */
    @LayoutRes
    abstract protected fun getLayoutRes(): Int

    /**
     * Attaches the ViewController's view to the [parent]'s layout hierarchy. If the view
     * already has a parent, it will be removed from it and attached to the new container
     *
     * @param parent The [ViewGroup] to attach the ViewController's view to
     * @param index The position at which to add the ViewController's view
     *
     * @see detach
     */
    @JvmOverloads
    fun attachTo(parent: ViewGroup, index: Int = -1): AttachAction {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(getLayoutRes(), parent, false)

            onViewCreated()
        } else {
            detach().now()
        }

        val view = getView()
        view.addOnAttachStateChangeListener(attachDetachListener)

        return AttachAction(parent, view, index)
    }

    /**
     * Detaches the ViewController's view from it's parent
     *
     * @throws [IllegalStateException] if the view is not attached
     *
     * @see isAttached
     * @see attachTo
     */
    fun detach(): DetachAction {
        val view = getView()

        return DetachAction(view) {
            view.removeOnAttachStateChangeListener(attachDetachListener)
        }
    }

    /**
     * @return true of the ViewController's view is attached to parent ViewGroup, false otherwise
     *
     * @see attachTo
     */
    fun isAttached(): Boolean = attachDetachListener.isAttached

    /**
     * @return The ViewController's root [View]
     *
     * @throws [IllegalStateException] if the view is not created, yet
     *
     * @see isAttached
     */
    fun getView(): View {
        return view ?: throw IllegalStateException("${javaClass.simpleName} not created, yet!")
    }

    /**
     * Returns the context the view is running in, through which it can
     * access the current theme, resources, etc.
     *
     * @return The view's Context.
     */
    protected fun getContext(): Context = context

    protected fun <T : View> findViewById(@IdRes id: Int): T? = view?.findViewById(id)

    /**
     * Called the first time the ViewController's view is attached to a parent ViewGroup.
     * Should be used to initialize UI state
     */
    protected abstract fun onViewCreated()

    /**
     *Called every time the ViewController's view is attached to a parent ViewGroup
     */
    protected open fun onViewAttached() {}

    /**
     * Called every time the ViewController's view is detached from it's parent ViewGroup
     */
    protected open fun onViewDetached() {}

    private inner class AttachDetachListener : View.OnAttachStateChangeListener {

        var isAttached: Boolean = false

        override fun onViewAttachedToWindow(v: View?) {
            isAttached = true

            onViewAttached()
        }

        override fun onViewDetachedFromWindow(v: View?) {
            isAttached = false

            onViewDetached()
        }
    }
}