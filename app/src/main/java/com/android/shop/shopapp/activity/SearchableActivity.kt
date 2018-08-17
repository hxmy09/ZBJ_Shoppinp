package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.data.SearchViewAdapter
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.network.services.ProductParameterRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import shopping.hxmy.com.shopping.util.DEFAULT_ITEM_SIZE

class SearchableActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backImg.setOnClickListener {
            super.onBackPressed()
        }

        initSearchView()
    }

    fun initSearchView() {
        search_view.isSubmitButtonEnabled = true
        search_view.suggestionsAdapter = this.mAdapter
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
            }

            override fun onQueryTextChange(newText: String?): Boolean {
            }

        })
//              search_view..setSubmitOnClick(true)
//        search_view.showSearch(false)
//        search_view.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                queryText = query
//                getProductsByGroup(mGroup!!, MSG_CODE_REFRESH, queryText)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                queryText = newText
//                getProductsByGroup(mGroup!!, MSG_CODE_REFRESH, queryText)
//                return false
//            }
//        })
    }


    private val mAdapter: SearchViewAdapter? = null
    private fun getProductsByGroup(group: String, keyword: String?) {
        val productService = RetrofitHelper().getProductsService()
        var request = ProductParameterRequest()
        request.groupName = group
        request.start = 0
        request.end = DEFAULT_ITEM_SIZE
        request.keyWord = keyword
        mCompositeDisposable.add(productService.getAllProductGroup(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    mAdapter?.list = t.products!!
                    mAdapter?.notifyDataSetChanged()
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }, { e ->
                    Toast.makeText(this@SearchableActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }))
    }
}