package com.android.shop.shopapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.android.shop.shopapp.R


class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //去掉Activity上面的状态栏
        window.setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN)
        setContentView(R.layout.activity_launch)
        supportActionBar?.hide()

        Handler().postDelayed({
            var intent = Intent(this@LaunchActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}