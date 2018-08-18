package com.android.shop.shopapp.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.data.GroupAdapter
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.network.services.ProductParameterRequest
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import shopping.hxmy.com.shopping.util.*


class SearchableActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        val group = when (v?.id) {
            R.id.baihuo -> {
                G_BAIHUO
            }
            R.id.nvzhuang -> {
                G_NVZHUANG
            }
            R.id.xiangbao -> {
                G_XIANGBAO
            }
            R.id.shipin -> {
                G_MEIZHUANG
            }
            R.id.neiyi -> {
                G_NEIYI
            }
            R.id.wanju -> {
                G_WANJU
            }
            R.id.wenju -> {
                G_WENJU
            }
            R.id.xidi -> {
                G_XIDI
            }
            R.id.tongzhuang -> {
                G_WUJIN
            }
            R.id.xiezi -> {
                G_XIEZI
            }
            R.id.fangzhipin -> {
                G_FANGZHIPIN
            }
            R.id.tejia -> {
                G_TEJIA
            }
            else -> {
                G_TEJIA
            }
        }

        val intent = Intent(this, ProductShowActivity::class.java).apply {

            putExtra(GROUP, group)
        }
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backImg.setOnClickListener {
            super.onBackPressed()
        }
        mGroup = intent.getStringExtra(GROUP) ?: ""
        initSearchView()
        findViews()
        bindEvents()
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            getProductsByGroup(mGroup!!, MSG_CODE_REFRESH, query)
        }
    }

    fun initSearchView() {
        search_view.isSubmitButtonEnabled = true
        search_view.queryHint = mGroup ?: "搜索你喜欢的主题"
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                queryText = query
                getProductsByGroup(mGroup!!, MSG_CODE_REFRESH, queryText)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                queryText = newText
                getProductsByGroup(mGroup!!, MSG_CODE_REFRESH, queryText)
                return true
            }

        })
    }

    lateinit var mAdapter: GroupAdapter

    var queryText: String? = null

    private fun findViews() {

//        pullLoadMoreRecyclerView.setLinearLayout()
        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
//        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数

        mAdapter = GroupAdapter(this@SearchableActivity, list, true)
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

    private fun bindEvents() {
        baihuo.setOnClickListener(this)
        nanzhuang.setOnClickListener(this)
        nvzhuang.setOnClickListener(this)
        xiangbao.setOnClickListener(this)
        shipin.setOnClickListener(this)
        neiyi.setOnClickListener(this)
        wanju.setOnClickListener(this)
        wenju.setOnClickListener(this)
        xidi.setOnClickListener(this)
        tongzhuang.setOnClickListener(this)
        xiezi.setOnClickListener(this)
        fangzhipin.setOnClickListener(this)
        tejia.setOnClickListener(this)
    }

    var mGroup: String? = null
    var list = arrayListOf<ProductModel>()
//
//    private var mAdapter: CursorAdapter? = null
//    private fun getProductsByGroup(group: String, keyword: String?) {
//        val productService = RetrofitHelper().getProductsService()
//        val request = ProductParameterRequest()
//        request.groupName = group
//        request.start = 0
//        request.end = DEFAULT_ITEM_SIZE
//        request.keyWord = keyword
//        mCompositeDisposable.add(productService.getAllProductGroup(request)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ t ->
//                    (application as ShopApplication).db.dbProductDao().deleteAll()
//                    t.products?.forEach { model ->
//                        (application as ShopApplication).db.dbProductDao().insertModel(DbProductModel(
//                                imgUrl = model.imageUrl?.let { model.imageUrls!![0] },
//                                desc = model.desc,
//                                price = model.price.toString(),
//                                productId = model.productId!!
//                        ))
//                    }
//                    mAdapter = SearchableAdapter(this, (application as ShopApplication).db.dbProductDao().getAll())
//                    search_view.suggestionsAdapter = this.mAdapter
//                    mAdapter?.notifyDataSetChanged()
//
//                }, { e ->
//                    Toast.makeText(this@SearchableActivity, "请求数据失败", Toast.LENGTH_LONG).show()
//                }))
//    }

    private fun getProductsByGroup(group: String, loadingType: Int, keyword: String?) {
        val productService = RetrofitHelper().getProductsService()
        var request = ProductParameterRequest()
        if (group.isEmpty()) {

            request.userState = 1
//            request.userName
        } else {
            request.groupName = group
        }


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
                    Toast.makeText(this@SearchableActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }))
    }

}