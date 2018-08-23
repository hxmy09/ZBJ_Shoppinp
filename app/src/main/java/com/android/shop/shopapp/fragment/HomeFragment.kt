package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.activity.ProductShowActivity
import com.android.shop.shopapp.activity.SearchableActivity
import com.android.shop.shopapp.data.HomeAdapter
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.network.services.ProductParameterRequest
import com.android.shop.shopapp.util.DEFAULT_ITEM_SIZE
import com.android.shop.shopapp.util.MSG_CODE_LOADMORE
import com.android.shop.shopapp.util.MSG_CODE_REFRESH
import com.android.shop.shopapp.util.QUERY_TEXT
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import com.android.shop.shopapp.util.*
import kotlinx.android.synthetic.main.naviation_item.*

/**
 * Created by myron on 3/28/18.
 */
class HomeFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.findItem(R.id.action_settings)?.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    val mCompositeDisposable = CompositeDisposable()
    var list = arrayListOf<ProductModel>()
    //
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageIndicator = PageIndicator.HOME_PAGE
        findViews()
        getNewsProducts(MSG_CODE_REFRESH)
        pullLoadMoreRecyclerView.setRefreshing(true)
        searchBtn.setOnClickListener {
            val intent = Intent(activity, SearchableActivity::class.java)
            intent.putExtra(QUERY_TEXT, search_view.query.toString())
            startActivity(intent)
        }

        bind()
    }

    fun bind() {
        baihuoLayout.setOnClickListener(this)
        nvzhuangLayout.setOnClickListener(this)
        nanzhuangLayout.setOnClickListener(this)
        xidiLayout.setOnClickListener(this)
        xiangbaoLayout.setOnClickListener(this)
        meizhuangLayout.setOnClickListener(this)
        neiyiLayout.setOnClickListener(this)
        wanjuLayout.setOnClickListener(this)
        wenjuLayout.setOnClickListener(this)
        wujinLayout.setOnClickListener(this)
        xieziLayout.setOnClickListener(this)
        fangzhipinLayout.setOnClickListener(this)
        imgTejia.setOnClickListener(this)
        tejia.setOnClickListener(this)
    }
    lateinit var mAdapter: HomeAdapter
    private fun findViews() {

//        pullLoadMoreRecyclerView.setLinearLayout()
        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
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
        val request = ProductParameterRequest()
//        var userState = (activity.application as ShopApplication).sharedPreferences?.getInt("userState", 0)
        val userName = (activity.application as ShopApplication).sharedPreferences?.getString("userName", "")
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
                        val cusPos = mAdapter.contents.size - 1
                        mAdapter.contents.addAll(t.products!!)
                        mAdapter.notifyItemRangeChanged(cusPos, t.products!!.size)
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

    override fun onClick(v: View?) {
        val group = when (v?.id) {
            R.id.baihuoLayout -> G_BAIHUO
            R.id.nvzhuangLayout -> G_NVZHUANG
            R.id.nanzhuangLayout -> G_NANZHUANG
            R.id.xidiLayout -> G_XIDI
            R.id.xiangbaoLayout -> G_XIANGBAO
            R.id.meizhuangLayout -> G_MEIZHUANG
            R.id.neiyiLayout -> G_NEIYI
            R.id.wanjuLayout -> G_WANJU
            R.id.wenjuLayout -> G_WENJU
            R.id.wujinLayout -> G_WUJIN
            R.id.xieziLayout -> G_XIEZI
            R.id.fangzhipinLayout -> G_FANGZHIPIN
            R.id.imgTejia -> G_TEJIA
            R.id.tejia -> G_LINGSHOU
            else -> {
                ""
            }
        }
        val intent = Intent(activity, ProductShowActivity::class.java).apply {

            putExtra(GROUP, group)
        }
        activity.startActivity(intent)
    }

//    class ViewHolderNavigation(itemView: View) : View.OnClickListener {
//        override fun onClick(v: View?) {
//
//        }
//
//        var baihuoLayout: LinearLayout? = null
//        var nvzhuangLayout: LinearLayout? = null
//        var nanzhuangLayout: LinearLayout? = null
//        var xidiLayout: LinearLayout? = null
//        var xiangbaoLayout: LinearLayout? = null
//        var meizhuangLayout: LinearLayout? = null
//        var neiyiLayout: LinearLayout? = null
//        var wanjuLayout: LinearLayout? = null
//        var wenjuLayout: LinearLayout? = null
//        var wujinLayout: LinearLayout? = null
//        var xieziLayout: LinearLayout? = null
//        var fangzhipinLayout: LinearLayout? = null
//        var tejia: AppCompatImageView? = null
//
//        init {
//            baihuoLayout = itemView.findViewById<LinearLayout>(R.id.baihuoLayout)
//            nvzhuangLayout = itemView.findViewById<LinearLayout>(R.id.nvzhuangLayout)
//            nanzhuangLayout = itemView.findViewById<LinearLayout>(R.id.nanzhuangLayout)
//            xidiLayout = itemView.findViewById<LinearLayout>(R.id.xidiLayout)
//            xiangbaoLayout = itemView.findViewById<LinearLayout>(R.id.xiangbaoLayout)
//            meizhuangLayout = itemView.findViewById<LinearLayout>(R.id.meizhuangLayout)
//            neiyiLayout = itemView.findViewById<LinearLayout>(R.id.neiyiLayout)
//            wanjuLayout = itemView.findViewById<LinearLayout>(R.id.wanjuLayout)
//            wenjuLayout = itemView.findViewById<LinearLayout>(R.id.wenjuLayout)
//            wujinLayout = itemView.findViewById<LinearLayout>(R.id.wujinLayout)
//            xieziLayout = itemView.findViewById<LinearLayout>(R.id.xieziLayout)
//            fangzhipinLayout = itemView.findViewById<LinearLayout>(R.id.fangzhipinLayout)
//            tejia = itemView.findViewById<AppCompatImageView>(R.id.imgTejia)
//
//        }
//
//
//    }
}