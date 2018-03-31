package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.DBUtil
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.MineAdapter
import kotlinx.android.synthetic.main.fragment_shopping_trolley.*

/**
 * @author a488606
 * @since 3/30/18
 */

class MineActivity : BaseActivity() {

    var list = arrayListOf<ProductModel>()
    var adapter: MineAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_hot)
        list = DBUtil(this@MineActivity).mAppDatabase.productDao().findAll() as ArrayList<ProductModel>
//        adapter = MineAdapter(this@MineActivity, list!!)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MineActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@MineActivity, DividerItemDecoration.HORIZONTAL))
            adapter = MineAdapter(this@MineActivity, list!!)
        }
    }


}