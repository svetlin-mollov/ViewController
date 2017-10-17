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

    @LayoutRes
    abstract protected fun getLayoutId(): Int

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

    fun remove(): RemoveAction {
        val view = getView()

        return RemoveAction(view) {
            view.removeOnAttachStateChangeListener(this)
        }
    }

    fun getView(): View {
        return view ?: throw IllegalStateException("${javaClass.simpleName} not added, yet!")
    }

    fun isAdded(): Boolean = view?.isAttachedToWindow ?: false

    final override fun onViewAttachedToWindow(v: View) {
        onAdded()
    }

    final override fun onViewDetachedFromWindow(v: View) {
        onRemoved()
    }

    fun <T : View> findViewById(@IdRes id: Int): T? = view?.findViewById(id)

    protected abstract fun onCreated()

    protected open fun onAdded() {}

    protected open fun onRemoved() {}
}