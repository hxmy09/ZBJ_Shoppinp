package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.DBUtil
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.GroupAdapter
import com.android.shop.shopapp.data.MineAdapter
import kotlinx.android.synthetic.main.activity_group.*
import shopping.hxmy.com.shopping.util.GROUP

/**
 * Created by myron on 3/29/18.
 */
class GroupActivity : BaseActivity() {
    var list = arrayListOf<ProductModel>()
    var adapter: MineAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false

        }
        swipeRefreshLayout.isRefreshing = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //显示哪个组的信息
        var group = intent.getStringExtra(GROUP)

        title = group

        list = DBUtil(this@GroupActivity).mAppDatabase.productDao().findByGroup(group) as ArrayList<ProductModel>
//        adapter = MineAdapter(this@MineActivity, list!!)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GroupActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@GroupActivity, DividerItemDecoration.HORIZONTAL))
            adapter = GroupAdapter(this@GroupActivity, list!!)
        }
        //doto service  好了就要删掉这个
        swipeRefreshLayout.isRefreshing = false
    }

}