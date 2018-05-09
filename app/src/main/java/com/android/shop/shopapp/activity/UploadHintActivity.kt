package com.android.shop.shopapp.activity

import android.os.Bundle
import com.android.shop.shopapp.R
import kotlinx.android.synthetic.main.activity_hint.*

/**
 * Created by myron on 4/14/18.
 */
class UploadHintActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hint)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        webview.loadUrl("file:///android_asset/hint.html")
    }
}