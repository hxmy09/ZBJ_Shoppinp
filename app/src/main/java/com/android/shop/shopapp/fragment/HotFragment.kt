package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.GroupAdapter
import com.android.shop.shopapp.model.network.RetrofitHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * Created by myron on 3/31/18.
 */
class HotFragment : Fragment() {
    val mCompositeDisposable = CompositeDisposable()
    var list = arrayListOf<ProductModel>()
//
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_hot, container, false)
    }

    //
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL))
            adapter = GroupAdapter(activity, list)
        }
        getNewsProducts()
    }

    private fun getNewsProducts() {
        val productService = RetrofitHelper().getProductsService()
        mCompositeDisposable.add(productService.getAllProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    list = t.products!!
                    (recyclerView.adapter as GroupAdapter).contents = list
                    recyclerView.adapter?.notifyDataSetChanged()
                }, { _ ->
                    Toast.makeText(activity, "请求数据失败", Toast.LENGTH_LONG).show()
                }))
    }
    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}