package com.android.shop.shopapp.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.data.GroupAdapter
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.network.services.ProductParameterRequest
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_group.*
import shopping.hxmy.com.shopping.util.DEFAULT_ITEM_SIZE
import shopping.hxmy.com.shopping.util.GROUP
import shopping.hxmy.com.shopping.util.MSG_CODE_LOADMORE
import shopping.hxmy.com.shopping.util.MSG_CODE_REFRESH


/**
 * Created by myron on 3/29/18.
 */
class ProductShowActivity : BaseActivity() {

    lateinit var mAdapter: GroupAdapter

    var queryText: String? = null

    private fun findViews() {

        pullLoadMoreRecyclerView.setLinearLayout()
//        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
//        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数

        mAdapter = GroupAdapter(this@ProductShowActivity, list, true)
        pullLoadMoreRecyclerView.setAdapter(mAdapter)
        pullLoadMoreRecyclerView.setFooterViewText("加载。。。");
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.primaryColor)
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(object : PullLoadMoreRecyclerView.PullLoadMoreListener {
            override fun onRefresh() {
                getProductsByGroup(mGroup!!, MSG_CODE_REFRESH, queryText)
            }

            override fun onLoadMore() {
                getProductsByGroup(mGroup!!, MSG_CODE_LOADMORE, queryText)
            }
        })

    }

    var mGroup: String? = null
    var list = arrayListOf<ProductModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        //显示哪个组的信息
        mGroup = intent.getStringExtra(GROUP)
        title = mGroup
        findViews()
        setSupportActionBar(findViewById(R.id.toolbar))

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getProductsByGroup(mGroup!!, MSG_CODE_REFRESH, queryText)

        //设置searchview . 点击suggestion 直接查询
//        search_view.setSubmitOnClick(true)
//        search_view.showSearch(false)
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                queryText = query
                getProductsByGroup(mGroup!!, MSG_CODE_REFRESH, queryText)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                queryText = newText
                getProductsByGroup(mGroup!!, MSG_CODE_REFRESH, queryText)
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
        list = mAdapter.contents
        var filter = arrayListOf<ProductModel>()
        for (p in list) {
            if (p.desc!!.contains(queryText)) {
                filter.add(p)
            }
        }
        mAdapter.contents = filter
        mAdapter.notifyDataSetChanged()
    }

    private fun getProductsByGroup(group: String, loadingType: Int, keyword: String?) {
        val productService = RetrofitHelper().getProductsService()
        var request = ProductParameterRequest()
        request.groupName = group

        if (loadingType == MSG_CODE_LOADMORE) {
            request.start = mAdapter.contents.size
            request.end = mAdapter.contents.size + DEFAULT_ITEM_SIZE
        } else {
            request.start = 0
            request.end = DEFAULT_ITEM_SIZE
        }
        request.keyWord = keyword
        mCompositeDisposable.add(productService.getAllProductGroup(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (loadingType != MSG_CODE_LOADMORE) {
                        mAdapter.contents = t.products!!
                        mAdapter.notifyDataSetChanged()
                        list = t.products!!
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                    } else {
                        val cusPos = mAdapter.contents.size - 1
                        mAdapter.contents.addAll(t.products!!)
                        list = mAdapter.contents
                        mAdapter.notifyItemRangeChanged(cusPos, t.products!!.size)
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, { e ->
                    Toast.makeText(this@ProductShowActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val item = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(item)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (android.R.id.home == item?.itemId) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (search_view!!.isSearchOpen) {
            search_view!!.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

}