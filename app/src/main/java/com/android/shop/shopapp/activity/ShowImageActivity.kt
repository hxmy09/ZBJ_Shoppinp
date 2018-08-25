package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.FullImageFragment


const val EXTRA_IMAGEURL = "imageUrl"
const val EXTRA_SELECTED_IMAGE = "selectedimage"

class ShowImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        window.setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN)
        setContentView(R.layout.activity_show_image)
        supportActionBar?.hide()
        fragmentManager.beginTransaction().add(R.id.imgsContainer, FullImageFragment(), "IMAGES").commit()
    }
}