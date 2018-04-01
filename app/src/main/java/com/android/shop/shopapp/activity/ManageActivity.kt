package com.android.shop.shopapp.activity

import android.os.Bundle
import android.os.PersistableBundle
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.ProductManagementFragment

/**
 * @author a488606
 * @since 3/22/18
 */
class ManageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fragmentManager.beginTransaction().add(R.id.fra_management, ProductManagementFragment(),"MANAGE").commit()
    }






}