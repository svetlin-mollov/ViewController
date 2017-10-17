package com.mollov.example.viewcontroller

import android.content.Context
import android.widget.Button
import android.widget.Toast
import com.mollov.example.R
import com.mollov.viewcontroller.ViewController

/**
 * Created by Svetlin Mollov on 15.10.2017 г..
 */
class LoginViewController(context: Context) : ViewController(context) {

    override fun getLayoutId(): Int = R.layout.view_login

    override fun onCreated() {

        findViewById<Button>(R.id.confirmButton)?.setOnClickListener {
            Toast.makeText(context, "Login!", Toast.LENGTH_SHORT).show()
        }
    }
}