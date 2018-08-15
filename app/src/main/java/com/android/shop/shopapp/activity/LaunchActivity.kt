package com.android.shop.shopapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.android.shop.shopapp.R


class LaunchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        supportActionBar?.hide()

        Handler().postDelayed({
            var intent = Intent(this@LaunchActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}