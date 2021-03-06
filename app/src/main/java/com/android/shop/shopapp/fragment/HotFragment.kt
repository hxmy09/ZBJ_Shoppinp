package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.data.HomeAdapter
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.network.services.ProductParameterRequest
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_hot.*
import shopping.hxmy.com.shopping.util.DEFAULT_ITEM_SIZE
import shopping.hxmy.com.shopping.util.MSG_CODE_LOADMORE
import shopping.hxmy.com.shopping.util.MSG_CODE_REFRESH


/**
 * Created by myron on 3/31/18.
 */
class HotFragment : Fragment() {


    val mCompositeDisposable = CompositeDisposable()
    var list = arrayListOf<ProductModel>()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_hot, container, false)
    }

    //
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        findViews()
        getNewsProducts(1)
        pullLoadMoreRecyclerView.setRefreshing(true);
    }

    lateinit var mAdapter: HomeAdapter
    private fun findViews() {

        pullLoadMoreRecyclerView.setLinearLayout()
//        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
//        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数

        mAdapter = HomeAdapter(activity, list, true)
        pullLoadMoreRecyclerView.setAdapter(mAdapter)
        pullLoadMoreRecyclerView.setFooterViewText("加载。。。");
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.primaryColor)
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(object : PullLoadMoreRecyclerView.PullLoadMoreListener {
            override fun onRefresh() {
                getNewsProducts(MSG_CODE_REFRESH)
            }

            override fun onLoadMore() {
                getNewsProducts(MSG_CODE_LOADMORE)
            }
        })

    }

    private fun getNewsProducts(loadingType: Int) {
        val productService = RetrofitHelper().getProductsService()
        var request = ProductParameterRequest()
        var userState = (activity.application as ShopApplication).sharedPreferences?.getInt("userState", 0)
        var userName = (activity.application as ShopApplication).sharedPreferences?.getString("userName", "")
        request.userState = 1//超级管理员，查询所有数据
        request.userName = userName

        if (loadingType == MSG_CODE_LOADMORE) {
            request.start = mAdapter.contents.size
            request.end = mAdapter.contents.size + DEFAULT_ITEM_SIZE
        } else {
            request.start = 0
            request.end = DEFAULT_ITEM_SIZE
        }
        mCompositeDisposable.add(productService.getAllProductByUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (loadingType != MSG_CODE_LOADMORE) {
                        mAdapter.contents = t.products!!
                        mAdapter.notifyDataSetChanged()
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                    } else {
                        mAdapter.contents.addAll(t.products!!)
                        mAdapter.notifyDataSetChanged()
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, { _ ->
                    Toast.makeText(activity, "请求数据失败", Toast.LENGTH_LONG).show()
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }))
    }

    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}