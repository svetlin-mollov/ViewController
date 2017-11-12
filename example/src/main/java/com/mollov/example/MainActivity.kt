package com.mollov.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mollov.example.viewcontroller.LoginViewController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginViewController = LoginViewController(this)

        switchLayout.setOnClickListener { _ ->
            loginViewController
                    .attachTo(findViewById(if (count % 2 == 0) R.id.container2 else R.id.container1))
                    .withAnimator(R.animator.fade_in)
                    .now()

            count++
        }
    }
}
