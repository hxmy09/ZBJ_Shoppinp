package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.HomeFragment
import com.android.shop.shopapp.fragment.MineFragment
import com.android.shop.shopapp.fragment.ShoppingTrolleyFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    var homeFragment = HomeFragment()
    var shoppingFragment = ShoppingTrolleyFragment()
    var mineFragment = MineFragment()
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (fragmentManager.findFragmentByTag("HOME") == null) {
                    fragmentManager.beginTransaction().replace(R.id.home, homeFragment, "HOME").commit()
                }
                shopping.visibility = View.GONE
                mine.visibility = View.GONE
                home.visibility = View.VISIBLE
                isShowMenu = false
                invalidateOptionsMenu()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_shopping -> {
                //if (fragmentManager.findFragmentByTag("Shopping") == null) {
                fragmentManager.beginTransaction().replace(R.id.shopping, shoppingFragment, "Shopping").commit()
                // }
                isShowMenu = true
                shopping.visibility = View.VISIBLE
                mine.visibility = View.GONE
                home.visibility = View.GONE
                invalidateOptionsMenu()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mine -> {
                if (fragmentManager.findFragmentByTag("Mine") == null) {
                    fragmentManager.beginTransaction().replace(R.id.mine, mineFragment, "Mine").commit()
                }
                isShowMenu = false
                shopping.visibility = View.GONE
                mine.visibility = View.VISIBLE
                home.visibility = View.GONE
                invalidateOptionsMenu()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_settings)?.isVisible = isShowMenu
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fragmentManager.beginTransaction().add(R.id.home, HomeFragment(), "HOME").commit()
        @IdRes var id = intent?.getIntExtra("selectedItemId", R.id.navigation_home)
                ?: R.id.navigation_home
        navigation.selectedItemId = id
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    var mMenu: Menu? = null
    var isShowMenu = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isShowMenu) {
            menuInflater.inflate(R.menu.trolleymenu, menu)
        }
        mMenu = menu
        return true
    }
//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        inflater?.inflate(R.menu.trolleymenu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
}
