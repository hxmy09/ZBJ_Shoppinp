package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.ProductManagementAdapter
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.ProductIdsReqeust
import com.android.shop.shopapp.network.services.ProductParameterRequest
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_product_management.*

/**
 * Created by myron on 3/31/18.
 */
open class ProductManagementFragment : Fragment() {

    var list: List<ProductModel> = arrayListOf()

    private val mCompositeDisposable = CompositeDisposable()
    var adapter: ProductManagementAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_product_management, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        list = DBUtil(activity).mAppDatabase.productDao()?.findAll()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter = ProductManagementAdapter(activity, this@ProductManagementFragment, list)
        //Use this now
        recyclerView.addItemDecoration(MaterialViewPagerHeaderDecorator())
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            fetchProducts()
        }
        swipeRefreshLayout.isRefreshing = true

        selectAll.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->

            list.map { it.isSelected = b }
            adapter!!.contents = list
            adapter!!.notifyDataSetChanged()

        })

        delete.setOnClickListener {
            var productIds = arrayListOf<String>()
            list.filter { it.isSelected }.forEach {
                productIds.add(it.productId!!)
            }
            if (productIds.size == 0) {
                Toast.makeText(activity, "请选择至少一条数据", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            deleteProduct(productIds);
        }

        setHasOptionsMenu(true)
        fetchProducts()
    }


    private fun deleteProduct(productIds: ArrayList<String>) {
        var pIds = ProductIdsReqeust()
        pIds.productIds = productIds
        val deleteService = RetrofitHelper().getDeleteProductService()

        mCompositeDisposable.add(deleteService.deleteProducts(pIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {
                        swipeRefreshLayout.isRefreshing = true
                        fetchProducts()
                        Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(activity, "删除失败", Toast.LENGTH_LONG).show()
                    }

                }, { _ ->
                    Toast.makeText(activity, "删除失败", Toast.LENGTH_LONG).show()
                }))
    }


    private fun fetchProducts() {
        val productsService = RetrofitHelper().getProductsService()
        var userState = (activity.application as ShopApplication).sharedPreferences?.getInt("userState", 0)
        var userName = (activity.application as ShopApplication).sharedPreferences?.getString("userName", "")

        var request = ProductParameterRequest()
        request.userName = userName
        request.userState = userState
        mCompositeDisposable.add(productsService.getAllProductByUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {
                        list = t.products!!
                        (recyclerView.adapter as ProductManagementAdapter).contents = list
                        recyclerView.adapter?.notifyDataSetChanged()

                        swipeRefreshLayout.isRefreshing = false
                    }
                }, { t ->
                    Toast.makeText(activity, "网络错误,请重新刷新", Toast.LENGTH_LONG).show()
//                    swipeRefreshLayout.isRefreshing = false
                }))
    }


    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}

