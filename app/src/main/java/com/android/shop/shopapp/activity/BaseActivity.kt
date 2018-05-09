package com.android.shop.shopapp.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.reactivex.disposables.CompositeDisposable


open class BaseActivity : AppCompatActivity() {
    val mCompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (android.R.id.home == item?.itemId) {
            onBackPressed()
        }
//        else if (R.id.action_search == item?.itemId) {
//            var intent = Intent(this@BaseActivity, SearchActivity::class.java)
//            startActivity(intent)
//        }
        return super.onOptionsItemSelected(item)
    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
////        val inflater = menuInflater
////        inflater.inflate(R.menu.search, menu)
////        return true
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        //  Toast.makeText(this@BaseActivity, "user..", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}