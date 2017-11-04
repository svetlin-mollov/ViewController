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

    override fun getLayoutRes(): Int = R.layout.view_login

    override fun onViewCreated() {

        findViewById<Button>(R.id.confirmButton)?.setOnClickListener {
            Toast.makeText(getContext(), "Login!", Toast.LENGTH_SHORT).show()
        }
    }
}