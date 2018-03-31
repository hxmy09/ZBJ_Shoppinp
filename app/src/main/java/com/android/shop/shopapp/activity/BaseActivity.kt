package com.android.shop.shopapp.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast

/**
 * @author a488606
 * @since 3/22/18
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (android.R.id.home == item?.itemId) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
      //  Toast.makeText(this@BaseActivity, "user..", Toast.LENGTH_SHORT).show()
    }
}