package com.android.shop.shopapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.data.OrdersAdapter
import com.android.shop.shopapp.model.ShoppingModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.response.ProductOrder
import com.android.shop.shopapp.util.*
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_orders.*
import com.android.shop.shopapp.util.*

@Deprecated("OrderListActivity instead")
class AgentUserOrdersListActivity : BaseActivity() {

    var list = mutableListOf<ProductOrder>()
    // var productState: Int? = null

    var userState = 0
    lateinit var mAdapter: OrdersAdapter
    private fun findViews() {

        pullLoadMoreRecyclerView.setLinearLayout()
//        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
//        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数
        userState = (application as ShopApplication).userState
        var productState = intent.getIntExtra("ProductState", WEI_FU_KUAN)
        mAdapter = OrdersAdapter(this@AgentUserOrdersListActivity, list, userState!!, productState)
        pullLoadMoreRecyclerView.setAdapter(mAdapter)
        pullLoadMoreRecyclerView.setFooterViewText("加载。。。");
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.primaryColor)
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(object : PullLoadMoreRecyclerView.PullLoadMoreListener {
            override fun onRefresh() {
                getOrders(MSG_CODE_REFRESH)
            }

            override fun onLoadMore() {
                getOrders(MSG_CODE_LOADMORE)
            }
        })

    }

    var isSearchSellerOrders = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val collapsingToolbarTayout = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayout)
        //如果有collapsingToolbarLayout ，如果需要设置toolbar title , 需要设置如下
        collapsingToolbarTayout.title = intent?.getStringExtra("Title")

//        supportActionBar?.title = intent?.getStringExtra("Title")
        //productState = intent.getIntExtra("ProductState", 0)
        findViews()
        getOrders(MSG_CODE_REFRESH)


        //default 显示买家数据， 卖家按钮高亮
        floatingBuyer.isEnabled = false
        floatingSeller.isEnabled = true
        if (userState == USER_STATE_ADMIN) {
            floatingBuyer.visibility = View.VISIBLE
            floatingSeller.visibility = View.VISIBLE
            floatingBuyer.setOnClickListener {
                isSearchSellerOrders = false

                //default 显示买家数据， 卖家按钮高亮
                floatingBuyer.isEnabled = false
                floatingSeller.isEnabled = true

                Toast.makeText(this@AgentUserOrdersListActivity, "查询买家订单信息", Toast.LENGTH_SHORT).show()
                getOrders(MSG_CODE_REFRESH)
            }

            floatingSeller.setOnClickListener {
                isSearchSellerOrders = true
                //default 显示买家数据， 卖家按钮高亮
                floatingBuyer.isEnabled = true
                floatingSeller.isEnabled = false
                Toast.makeText(this@AgentUserOrdersListActivity, "查询厂家订单信息", Toast.LENGTH_SHORT).show()
                getOrders(MSG_CODE_REFRESH)
            }
        } else {
            floatingBuyer.visibility = View.GONE
            floatingSeller.visibility = View.GONE
        }
    }

    private fun getOrders(loadingType: Int) {
        val orderService = RetrofitHelper().getOrdersService()
        val userName = (application as ShopApplication).sharedPreferences?.getString("userName", "")
        //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员
        val request = ShoppingModel()
        when (userState) {
            USER_STATE_MANAGER -> request.seller = userName
            USER_STATE_USER -> request.buyer = userName
            USER_STATE_AGENT -> request.buyer = userName
            USER_STATE_ADMIN -> {

            }
        //如果等于1 查询出所有商家订单

        /**
         * 用户状态分为1.2.3.
         * 如果是1- 超级管理员  那么根据is_search_seller 来区分查询出卖家。还是买家的所有订单信息。
         * 如果是2 - 普通管理员 也就是卖家。。。 这里查看的是他自己卖出的订单信息。
         * 3 买家。  查看自己的订单
         */
        //根据查询条件卖家还是买家查询
        //0购物车1未付款2代发货3已发货4售后
        }
        request.userState = userState //如果等于1 查询出所有商家订单

        /**
         * 用户状态分为1.2.3.
         * 如果是1- 超级管理员  那么根据is_search_seller 来区分查询出卖家。还是买家的所有订单信息。
         * 如果是2 - 普通管理员 也就是卖家。。。 这里查看的是他自己卖出的订单信息。
         * 3 买家。  查看自己的订单
         */
        request.isSearchSellerOrders = isSearchSellerOrders   //根据查询条件卖家还是买家查询


        request.orderState = intent.getIntExtra("ProductState", WEI_FU_KUAN)//0购物车1未付款2代发货3已发货4售后
        if (loadingType == MSG_CODE_LOADMORE) {
            request.start = mAdapter.contents.size
            request.end = mAdapter.contents.size + DEFAULT_ITEM_SIZE
        } else {
            request.start = 0
            request.end = DEFAULT_ITEM_SIZE
        }
        mCompositeDisposable.add(orderService.getAllOrders(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (loadingType != MSG_CODE_LOADMORE) {
                        mAdapter.contents = t.orders!!
                        mAdapter.notifyDataSetChanged()
                        list = t.orders!!
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                    } else {
                        val cusPos = mAdapter.contents.size - 1
                        mAdapter.contents.addAll(t.orders!!)
                        list = mAdapter.contents
                        mAdapter.notifyItemRangeChanged(cusPos, t.orders!!.size)
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, { e ->
                    Toast.makeText(this@AgentUserOrdersListActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            pullLoadMoreRecyclerView.refresh()
        }
    }
}