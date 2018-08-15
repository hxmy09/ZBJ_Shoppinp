package com.android.shop.shopapp.activity

import android.os.Bundle
import com.android.shop.shopapp.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchableActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backImg.setOnClickListener {
            super.onBackPressed()
        }
    }
}