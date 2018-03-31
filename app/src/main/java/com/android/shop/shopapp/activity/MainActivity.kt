package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.HomeFragment
import com.android.shop.shopapp.fragment.MineFragment
import com.android.shop.shopapp.fragment.ShoppingTrolleyFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    var home = HomeFragment()
    var shopping = ShoppingTrolleyFragment()
    var mine = MineFragment()
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //if (fragmentManager.findFragmentByTag("HOME") == null) {
                fragmentManager.beginTransaction().replace(R.id.home, home, "HOME").commit()
                // }
//                home.view?.visibility = View.VISIBLE
//                shopping.view?.visibility = View.GONE
//                mine.view?.visibility = View.GONE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_shopping -> {
                // if (fragmentManager.findFragmentByTag("Shopping") == null) {
                fragmentManager.beginTransaction().replace(R.id.home, shopping, "Shopping").commit()
                // }
//                home.view?.visibility = View.GONE
//                shopping.view?.visibility = View.VISIBLE
//                mine.view?.visibility = View.GONE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mine -> {
                //  if (fragmentManager.findFragmentByTag("Mine") == null) {
                fragmentManager.beginTransaction().replace(R.id.home, mine, "Mine").commit()
                //}
//                home.view?.visibility = View.GONE
//                shopping.view?.visibility = View.GONE
//                mine.view?.visibility = View.VISIBLE
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
