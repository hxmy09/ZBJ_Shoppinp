package com.android.shop.shopapp.activity

import android.os.Bundle
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.ProductManagementFragment

/**
 * @author a488606
 * @since 3/22/18
 */
class ManageProductsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fragmentManager.beginTransaction().add(R.id.fra_management, ProductManagementFragment(),"MANAGE").commit()
    }

}