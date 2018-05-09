package com.android.shop.shopapp.activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.data.OrderDetailAdapter
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.ShoppingModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_orderdetail.*
import shopping.hxmy.com.shopping.util.*


/**
 * Created by myron on 3/29/18.
 */
class OrderDetailsActivity : BaseActivity() {

    lateinit var mAdapter: OrderDetailAdapter

    var products: List<ShoppingModel>? = null

    var currentData: MutableList<ShoppingModel>? = mutableListOf()
    private fun findViews() {

        pullLoadMoreRecyclerView.setLinearLayout()
//        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
//        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数

        mAdapter = OrderDetailAdapter(this@OrderDetailsActivity, currentData!!)
        pullLoadMoreRecyclerView.setAdapter(mAdapter)
        pullLoadMoreRecyclerView.setFooterViewText("加载。。。");
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.primaryColor)
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(object : PullLoadMoreRecyclerView.PullLoadMoreListener {
            override fun onRefresh() {
                getOrderDetails(MSG_CODE_REFRESH)
            }

            override fun onLoadMore() {
                getOrderDetails(MSG_CODE_LOADMORE)
            }
        })

    }

    var list = arrayListOf<ProductModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderdetail)
        //显示哪个组的信息
        products = intent.getParcelableArrayListExtra("orders")
        findViews()

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getOrderDetails(MSG_CODE_REFRESH)

        var userState = (application as ShopApplication).sharedPreferences?.getInt("userState", 0) //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员
        if (userState == USER_STATE_ADMIN || userState == USER_STATE_MANAGER) {
            superManager.visibility = View.VISIBLE
        } else {
            superManager.visibility = View.GONE
        }
        var shoppingModel = products?.get(0)

        sellerPhone.text = shoppingModel?.sellerPhone
        sellerAddress.text = shoppingModel?.sellerAddress
        sellerUserName.text = shoppingModel?.seller
        buyerPhone.text = shoppingModel?.buyerPhone
        buyerAddress.text = shoppingModel?.buyerAddress
        buyerUserName.text = shoppingModel?.buyer
        orderTime.text = shoppingModel?.orderTime
        total.text = intent.getDoubleExtra("total", 0.00).toString()
//        request.productState = intent.getIntExtra("ProductState", 1)
        var productState = intent.getIntExtra("ProductState", 0)//0购物车1未付款2代发货3已发货4售后

        if (userState == USER_STATE_USER) {
            if (productState == WEI_FU_KUAN) {
                action.text = "付款"
                action.visibility = View.VISIBLE
            } else if (productState == DAI_FA_HUO) {
                action.visibility = View.GONE
            } else if (productState == DAI_SHOU_HUO) {
                action.text = "已收货"
                action.visibility = View.VISIBLE
            } else if (productState == SHOU_HOU) {
                action.text = "售后"
                action.visibility = View.VISIBLE
            } else {
                action.visibility = View.GONE
            }
        } else if (userState == USER_STATE_MANAGER) {
            if (productState == WEI_FU_KUAN) {
                action.visibility = View.GONE
            } else if (productState == DAI_FA_HUO) {
                action.visibility = View.VISIBLE
                action.text = "发货"
            } else if (productState == DAI_SHOU_HUO) {
                action.visibility = View.GONE
            } else {
                action.visibility = View.GONE
            }
        } else if (userState == USER_STATE_ADMIN) {
            action.visibility = View.GONE
        }


        var orderState = 0
        var message = if (action.text == "付款") {
            orderState = DAI_FA_HUO
            "付款啦！"
        } else if (action.text == "已收货") {
            orderState = SHOU_HOU
            "你的宝贝已经收到？"
        } else if (action.text == "发货") {
            orderState = DAI_SHOU_HUO
            "你确定准备发货吗？"
        } else if (action.text == "售后") {
//            orderState = DAI_SHOU_HUO
            "请拨打电话0579-85876692"
        } else {
            ""
        }
        action.setOnClickListener {
            if (action.text == "售后") {
                MaterialDialog.Builder(this)
                        .content(message)
                        .positiveText("确定")
                        .show()
            } else {
                MaterialDialog.Builder(this)
                        .content(message)
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive { dialog, which ->
                            val orderService = RetrofitHelper().getOrdersService()
                            var request = ShoppingModel()
                            request.orderNumber = intent.getStringExtra("OrderNum")
                            request.orderState = orderState//0购物车100未付款200代发货300已发货400售后
                            mCompositeDisposable.add(orderService.updateOrderStatus(request)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ t ->
                                        if (t.code == "100") {
                                            Toast.makeText(this@OrderDetailsActivity, "操作成功", Toast.LENGTH_LONG).show()
                                            action.visibility = View.GONE
                                            setResult(Activity.RESULT_OK)
                                            finish()
                                        } else {
                                            Toast.makeText(this@OrderDetailsActivity, "操作失败", Toast.LENGTH_LONG).show()
                                        }

                                    }, { e ->
                                        Toast.makeText(this@OrderDetailsActivity, "操作失败", Toast.LENGTH_LONG).show()
                                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                    }))
                        }
                        .show()
            }

        }
    }

    var start = 0;
    var end = DEFAULT_ITEM_SIZE
    private fun getOrderDetails(loadingType: Int) {
        if (loadingType == MSG_CODE_LOADMORE) {
            start = mAdapter.contents.size
            end = mAdapter.contents.size + DEFAULT_ITEM_SIZE
        } else {
            start = 0
            end = DEFAULT_ITEM_SIZE
        }

        if (end >= products?.size!!) {
            end = products?.size!!
        }
        currentData?.clear()
        for (index in start until end) {
            currentData?.add(products?.get(index)!!)
        }
        if (currentData?.size!! > 0) {
            mAdapter.contents = currentData!!
            mAdapter.notifyDataSetChanged()
        }
        pullLoadMoreRecyclerView.setPullLoadMoreCompleted()

    }
}