package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.OrderDetailAdapter
import com.android.shop.shopapp.model.network.RetrofitHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_group.*

/**
 * @author a488606
 * @since 3/22/18
 */
class OrderDetailActivity : BaseActivity() {

    var list = arrayListOf<ProductModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        swipeRefreshLayout.setOnRefreshListener {
            getOrders()
        }
        swipeRefreshLayout.isRefreshing = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrderDetailActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@OrderDetailActivity, DividerItemDecoration.HORIZONTAL))
            adapter = OrderDetailAdapter(this@OrderDetailActivity, list)
        }
        getOrders()
    }

    private fun getOrders() {
        val orderService = RetrofitHelper().getOrdersService()

        mCompositeDisposable.add(orderService.getOrderDetail(intent.getStringExtra("OrderNum"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    list = t.products!!
                    (recyclerView.adapter as OrderDetailAdapter).contents = list
                    recyclerView.adapter?.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing = false
                }, { _ ->
                    Toast.makeText(this@OrderDetailActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                }))
    }

}