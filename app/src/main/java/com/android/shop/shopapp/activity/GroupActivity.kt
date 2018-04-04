package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.GroupAdapter
import com.android.shop.shopapp.model.network.RetrofitHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_group.*
import shopping.hxmy.com.shopping.util.GROUP

/**
 * Created by myron on 3/29/18.
 */
class GroupActivity : BaseActivity() {
    var list = arrayListOf<ProductModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        //显示哪个组的信息
        var group = intent.getStringExtra(GROUP)

        title = group
        swipeRefreshLayout.setOnRefreshListener {
            getProductsByGroup(group)
            swipeRefreshLayout.isRefreshing = false

        }
        swipeRefreshLayout.isRefreshing = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


//        list = DBUtil(this@GroupActivity).mAppDatabase.productDao().findByGroup(group) as ArrayList<ProductModel>
//        adapter = MineAdapter(this@MineActivity, list!!)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GroupActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@GroupActivity, DividerItemDecoration.HORIZONTAL))
            adapter = GroupAdapter(this@GroupActivity, list)
        }
        getProductsByGroup(group)
    }

    private fun getProductsByGroup(group: String) {
        val productService = RetrofitHelper().getProductsService()
        mCompositeDisposable.add(productService.getAllProductGroup(group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    list = t.products!!
                    (recyclerView.adapter as GroupAdapter).contents = list
                    recyclerView.adapter?.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing = false
                }, { _ ->
                    Toast.makeText(this@GroupActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                }))
    }

}