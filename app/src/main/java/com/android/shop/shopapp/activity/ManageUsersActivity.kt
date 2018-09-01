package com.android.shop.shopapp.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.UserManagementFragment
import kotlinx.android.synthetic.main.activity_manager.*

class ManageUsersActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction().add(R.id.fra_management, UserManagementFragment(), "MANAGE").commit()
//        backImg.setOnClickListener{
//            onBackPressed()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val item = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(item)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (android.R.id.home == item?.itemId) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (search_view!!.isSearchOpen) {
            search_view!!.closeSearch()
        } else {
            super.onBackPressed()
        }
    }
}