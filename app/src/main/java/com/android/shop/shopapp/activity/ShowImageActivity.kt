package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.android.shop.shopapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_image.*


const val EXTRA_IMAGEURL = "imageUrl"

class ShowImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        window.setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN)
        setContentView(R.layout.activity_show_image)
        supportActionBar?.hide()
        val imageUrl = intent.extras.getString(EXTRA_IMAGEURL)
        Picasso.get().load(imageUrl).into(fullImage)
    }
}