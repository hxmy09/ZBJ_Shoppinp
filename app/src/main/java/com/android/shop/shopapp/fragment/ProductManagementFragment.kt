package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.data.ProductManagementAdapter
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.ProductIdsReqeust
import com.android.shop.shopapp.network.services.ProductParameterRequest
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_product_management.*
import kotlinx.android.synthetic.main.item_grid_colors.view.*
import shopping.hxmy.com.shopping.util.DEFAULT_ITEM_SIZE
import shopping.hxmy.com.shopping.util.MSG_CODE_LOADMORE
import shopping.hxmy.com.shopping.util.MSG_CODE_REFRESH

/**
 * Created by myron on 3/31/18.
 */
open class ProductManagementFragment : Fragment() {
    var list: MutableList<ProductModel> = arrayListOf()

    lateinit var search_view: MaterialSearchView
    private val mCompositeDisposable = CompositeDisposable()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_product_management, container, false)
    }

    var queryText: String? = null
    lateinit var mAdapter: ProductManagementAdapter
    private fun findViews() {

        pullLoadMoreRecyclerView.setLinearLayout()
//        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
//        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数

        mAdapter = ProductManagementAdapter(activity, this@ProductManagementFragment, list)
        pullLoadMoreRecyclerView.setAdapter(mAdapter)
        pullLoadMoreRecyclerView.setFooterViewText("加载。。。");
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.primaryColor)
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(object : PullLoadMoreRecyclerView.PullLoadMoreListener {
            override fun onRefresh() {
                fetchProducts(MSG_CODE_REFRESH, queryText)
            }

            override fun onLoadMore() {
                fetchProducts(MSG_CODE_LOADMORE, queryText)
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        search_view = activity.findViewById(R.id.search_view)
        findViews()
////        list = DBUtil(activity).mAppDatabase.productDao()?.findAll()
//        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.setHasFixedSize(true)
//        adapter = ProductManagementAdapter(activity, this@ProductManagementFragment, list)
//        //Use this now
//        recyclerView.addItemDecoration(MaterialViewPagerHeaderDecorator())
//        recyclerView.adapter = adapter
//
//        swipeRefreshLayout.setOnRefreshListener {
//            start -= PAGE_NUMBER
//            end = start + PAGE_NUMBER
//
//            if (start <= 0) {
//                start = 0
//                end = PAGE_NUMBER
//            }
//            fetchProducts()
//        }
//        swipeRefreshLayout.isRefreshing = true

        selectAll.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->

            mAdapter.contents.map { it.isSelected = b }
            mAdapter!!.contents = list
            mAdapter!!.notifyDataSetChanged()

        })

        delete.setOnClickListener {
            var productIds = arrayListOf<String>()
            mAdapter.contents.filter { it.isSelected }.forEach {
                productIds.add(it.productId!!)
            }
            if (productIds.size == 0) {
                Toast.makeText(activity, "请选择至少一条数据", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            deleteProduct(productIds);
        }

        setHasOptionsMenu(true)
        fetchProducts(MSG_CODE_REFRESH, queryText)
        search_view.text

        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                queryText = query
                fetchProducts(MSG_CODE_REFRESH, queryText)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                queryText = newText
                fetchProducts(MSG_CODE_REFRESH, queryText)
                return false
            }
        })

        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                //Do some magic
            }

            override fun onSearchViewClosed() {
                //Do some magic
            }
        })
    }

    private fun search(queryText: String) {
        var filter = arrayListOf<ProductModel>()
        for (p in list) {
            if (p.desc!!.contains(queryText)) {
                filter.add(p)
            }
        }
//        if (filter.size <= 0) {
//            return
//        }
        mAdapter?.contents = filter
        mAdapter?.notifyDataSetChanged()
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
//                        swipeRefreshLayout.isRefreshing = true
                        fetchProducts(MSG_CODE_REFRESH, queryText)
                        Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(activity, "删除失败", Toast.LENGTH_LONG).show()
                    }

                }, { _ ->
                    Toast.makeText(activity, "删除失败", Toast.LENGTH_LONG).show()
                }))
    }


    private fun fetchProducts(loadingType: Int, keyWord: String?) {
        val productsService = RetrofitHelper().getProductsService()
        var userState = (activity.application as ShopApplication).sharedPreferences?.getInt("userState", 0)
        var userName = (activity.application as ShopApplication).sharedPreferences?.getString("userName", "")

        var request = ProductParameterRequest()
        request.userName = userName
        request.userState = userState
        request.keyWord = keyWord
        if (loadingType == MSG_CODE_LOADMORE) {
            request.start = mAdapter.contents.size
            request.end = mAdapter.contents.size + DEFAULT_ITEM_SIZE
        } else {
            request.start = 0
            request.end = DEFAULT_ITEM_SIZE
        }
        mCompositeDisposable.add(productsService.getAllProductByUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {

                        if (loadingType != MSG_CODE_LOADMORE) {
                            mAdapter.contents = t.products!!
                            list = t.products!!
                            mAdapter.notifyDataSetChanged()

                        } else {
                            mAdapter.contents.addAll(t.products!!)
                            list = mAdapter.contents
                            mAdapter.notifyDataSetChanged()

                        }
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, { t ->
                    Toast.makeText(activity, "网络错误,请重新刷新", Toast.LENGTH_LONG).show()
//                    swipeRefreshLayout.isRefreshing = false
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }))
    }


    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}

