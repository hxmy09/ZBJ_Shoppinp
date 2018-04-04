package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.BottomNavigationView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.HomeFragment
import com.android.shop.shopapp.fragment.MineFragment
import com.android.shop.shopapp.fragment.ShoppingTrolleyFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){

    var home = HomeFragment()
    var shopping = ShoppingTrolleyFragment()
    var mine = MineFragment()
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fragmentManager.beginTransaction().replace(R.id.home, home, "HOME").commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_shopping -> {
                fragmentManager.beginTransaction().replace(R.id.home, shopping, "Shopping").commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mine -> {
                fragmentManager.beginTransaction().replace(R.id.home, mine, "Mine").commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fragmentManager.beginTransaction().add(R.id.home, HomeFragment(), "HOME").commit()
        @IdRes var id = intent?.getIntExtra("selectedItemId", R.id.navigation_home)
                ?: R.id.navigation_home
        navigation.selectedItemId = id
    }
}
