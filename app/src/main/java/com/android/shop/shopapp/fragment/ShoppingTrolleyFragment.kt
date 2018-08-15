package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.activity.MainActivity
import com.android.shop.shopapp.data.ShoppingAdapter
import com.android.shop.shopapp.model.ShoppingModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.response.ProductOrder
import com.android.shop.shopapp.network.services.ProductParameterRequest
import com.android.shop.shopapp.pay.PayActivity
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_shopping_trolley.*
import shopping.hxmy.com.shopping.util.DEFAULT_ITEM_SIZE
import shopping.hxmy.com.shopping.util.MSG_CODE_LOADMORE
import shopping.hxmy.com.shopping.util.MSG_CODE_REFRESH
import java.text.DecimalFormat

/**
 * Created by myron on 3/31/18.
 */
class ShoppingTrolleyFragment : Fragment(), CountTotalCallBack {


    var list: MutableList<ShoppingModel> = arrayListOf()
    val mCompositeDisposable = CompositeDisposable()
    override fun countTotal(number: Int, model: ShoppingModel) {
        model.orderAmount = number
        cal()
    }


    lateinit var mAdapter: ShoppingAdapter
    private fun findViews() {

        pullLoadMoreRecyclerView.setLinearLayout()
//        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
//        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数

        mAdapter = ShoppingAdapter(activity, this, list)
        pullLoadMoreRecyclerView.setAdapter(mAdapter)
        pullLoadMoreRecyclerView.setFooterViewText("加载。。。");
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.primaryColor)
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(object : PullLoadMoreRecyclerView.PullLoadMoreListener {
            override fun onRefresh() {
                fetchData(MSG_CODE_REFRESH)
            }

            override fun onLoadMore() {
                fetchData(MSG_CODE_LOADMORE)
            }
        })

    }

    private fun startAnim() {
        avi.show();
    }

    private fun stopAnim() {
        avi.hide();
    }

    private fun cal() {
        var amount: Double = 0.00
        mAdapter.contents.forEach {
            if (it.isSelected)
                amount += it.price!! * it.orderAmount!!
        }
        var format = DecimalFormat("#.00")
        total.text = format.format(amount.toString().toDouble())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_shopping_trolley, container, false)
    }


    private fun fetchData(loadingType: Int) {
        //查询购物车商品。 需要传入userName 和 productState = 0
        var request = ProductParameterRequest()
        request.productState = 0 //购物车

        if (loadingType == MSG_CODE_LOADMORE) {
            request.start = mAdapter.contents.size
            request.end = mAdapter.contents.size + DEFAULT_ITEM_SIZE
        } else {
            request.start = 0
            request.end = DEFAULT_ITEM_SIZE
        }
//        startAnim()
        request.userName = (activity.application as ShopApplication).sharedPreferences?.getString("userName", "")
        val detailService = RetrofitHelper().getProductDetailService()
        mCompositeDisposable.add(detailService.queryShoppingTrolley(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {

                        if (loadingType != MSG_CODE_LOADMORE) {
                            mAdapter.contents = t.products!!
                            mAdapter.notifyDataSetChanged()
                            pullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                        } else {
                            val startPos = mAdapter.contents.size - 1
                            mAdapter.contents.addAll(t.products!!)
                            mAdapter.notifyItemRangeChanged(startPos, t.products!!.size)
                            pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }

                    } else {
                        Toast.makeText(activity, "请求数据失败", Toast.LENGTH_LONG).show()
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
//                    stopAnim()
                }, { e ->
                    Toast.makeText(activity, "请求数据失败", Toast.LENGTH_LONG).show()
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
//                    stopAnim()
                }))


    }


    //删除购物车内容的话，根据唯一的购物id
    private fun deleteByProductId(shoppingIds: List<String>) {
        startAnim()
        var request = ProductParameterRequest()
//        request.userName = (activity.application as ShopApplication).sharedPreferences?.getString("userName", "")
        request.shoppingIds = shoppingIds
        val detailService = RetrofitHelper().getProductDetailService()
        mCompositeDisposable.add(detailService.deleteShoppingTrolley(request)//.flatMap { fetchData() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {
                        Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show()
                        fetchData(MSG_CODE_REFRESH)

                    } else {
                        Toast.makeText(activity, "请求数据失败", Toast.LENGTH_LONG).show()
                    }
                    stopAnim()

                }, { e ->
                    Toast.makeText(activity, "请求数据失败", Toast.LENGTH_LONG).show()
                    stopAnim()
                }))

    }

    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setSupportActionBar(this.view.findViewById(R.id.toolbar))
//        (activity as MainActivity).title ="购物车"
        findViews()
        fetchData(MSG_CODE_REFRESH)

        selectAll.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->

            mAdapter.contents.forEach {
                it.isSelected = b
            }
            mAdapter.notifyDataSetChanged()

            cal()

        }
        result.setOnClickListener {
            startAnim()
            var userState = (activity.application as ShopApplication).sharedPreferences?.getInt("userState", 0) //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员

            if (userState != 3) {
                MaterialDialog.Builder(activity)
                        .content("对不起，你的客户端不支持购买商品，请注册其他账户，有任何问题，请你拨打电话0579-85876692")
                        .positiveText("确定")
                        .show()
                stopAnim()
                return@setOnClickListener
            }
            val orderList = mAdapter.contents.filter { it.isSelected }
            val orderService = RetrofitHelper().getOrdersService()
            val totalAmount = total.text.toString().toDouble()
            var order = ProductOrder(orderList, "", totalAmount)
            mCompositeDisposable.add(orderService.buyProducts(order)//.flatMap { fetchData() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        if (t.code == "100") {
                            val intent = Intent(activity, PayActivity::class.java).apply {
                                putExtra("order_number", t.data?.order_number)
                                putExtra("payAmount", totalAmount.toString())
                            }
                            startActivityForResult(intent, 0x11)
                        } else {
                            Toast.makeText(activity, "请求数据失败", Toast.LENGTH_LONG).show()
                        }
                        stopAnim()

                    }, { e ->
                        Toast.makeText(activity, "请求数据失败", Toast.LENGTH_LONG).show()
                        stopAnim()
                    }))
        }

        delete.setOnClickListener {
            var filterList = mAdapter.contents.filter { it.isSelected }

            if (filterList.size <= 0) {
                Toast.makeText(activity, "请选择至少一条数据", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var productsIds = arrayListOf<String>()
            filterList.forEach { productsIds.add(it.shoppingId!!) }
            deleteByProductId(productsIds)

        }
        cancel.setOnClickListener {
            editLayout.visibility = View.GONE
            resultLayout.visibility = View.VISIBLE
        }

        setHasOptionsMenu(true)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_settings -> {
                editLayout.visibility = View.VISIBLE
                resultLayout.visibility = View.GONE
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0x11) {
            fetchData(MSG_CODE_REFRESH)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}


interface CountTotalCallBack {
    fun countTotal(am: Int, model: ShoppingModel)
}
