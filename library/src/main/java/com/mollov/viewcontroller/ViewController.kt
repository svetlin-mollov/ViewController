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

            onViewCreated(view!!)
        } else {
            remove()?.now()
        }

        val nonNullView = view!!

        nonNullView.addOnAttachStateChangeListener(this)

        return AddAction(parent, nonNullView, index)
    }

    fun remove(): RemoveAction? {
        val finalView = view
        if (finalView == null || finalView.parent == null) {
            return null
        }

        return RemoveAction(finalView) { finalView.removeOnAttachStateChangeListener(this) }
    }

    fun getView(): View {
        return view ?: throw  IllegalStateException("${javaClass.simpleName} not added, yet!")
    }

    fun isAdded(): Boolean = view?.isAttachedToWindow ?: false

    abstract protected fun onViewCreated(view: View)

    override fun onViewAttachedToWindow(v: View) {}

    override fun onViewDetachedFromWindow(v: View) {}

    fun <T : View> findViewById(@IdRes id: Int): T? = view?.findViewById(id)
}