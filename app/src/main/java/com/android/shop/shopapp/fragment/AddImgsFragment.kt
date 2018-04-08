package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shop.shopapp.R
import com.android.shop.shopapp.data.AddImgsAdapter
import com.android.shop.shopapp.model.request.AddImgsRequest
import kotlinx.android.synthetic.main.fragment_property_add.*

/**
 * Created by myron on 4/7/18.
 */

class AddImgsFragment : Fragment() {

    //    val mCompositeDisposable = CompositeDisposable()
    var list = arrayListOf<AddImgsRequest>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.fragment_property_add, container, false)!!
    }

    fun setData(data: ArrayList<AddImgsRequest>) {
        list = data
        (recyclerView.adapter as AddImgsAdapter).contents = list
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.apply {
            layoutManager = GridLayoutManager(activity, 3)
            setHasFixedSize(true)
            // addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL))
            adapter = AddImgsAdapter(activity, list, layoutManager as GridLayoutManager)
        }
        //   getNewsProducts()
    }

//    private fun getNewsProducts() {
//        val productService = RetrofitHelper().getProductsService()
//        var request = ProductParameterRequest()
//        var userState = (activity.application as ShopApplication).sharedPreferences?.getInt("userState", 0)
//        var userName = (activity.application as ShopApplication).sharedPreferences?.getString("userName", "")
//        request.userState = 1//超级管理员，查询所有数据
//        request.userName = userName
//
//
////        mCompositeDisposable.add(productService.getAllProductByUser(request)
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe({ t ->
////                    list = t.products!!
////                    (recyclerView.adapter as GroupAdapter).contents = list
////                    recyclerView.adapter?.notifyDataSetChanged()
////                }, { _ ->
////                    Toast.makeText(activity, "请求数据失败", Toast.LENGTH_LONG).show()
////                }))
//    }

//    override fun onDestroy() {
//        // DO NOT CALL .dispose()
//        mCompositeDisposable.clear()
//        super.onDestroy()
//    }
}