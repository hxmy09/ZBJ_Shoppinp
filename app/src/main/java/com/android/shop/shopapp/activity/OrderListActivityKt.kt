package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.data.OrdersAdapter
import com.android.shop.shopapp.model.ShoppingModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.response.ProductOrder
import com.android.shop.shopapp.util.*
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_order_kt.*
import kotlinx.android.synthetic.main.fragment_page_order_kt.*

class OrderListActivityKt : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_kt)
        tablayout.setupWithViewPager(viewPager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val position = when (intent.extras.getInt("ProductState", 0)) {
            WEI_FU_KUAN -> 0
            DAI_FA_HUO -> 1
            DAI_SHOU_HUO -> 2
            SHOU_HOU -> 3
            else -> 0
        }
        viewPager.adapter = SimpleFragmentPageAdapter(supportFragmentManager)
        viewPager.setCurrentItem(position, true)
    }

}

class SimpleFragmentPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val state = when (position) {
            0 -> WEI_FU_KUAN
            1 -> DAI_FA_HUO
            2 -> DAI_SHOU_HUO
            3 -> SHOU_HOU
            else -> {
                WEI_FU_KUAN
            }
        }
        val fragment = PageFragment()
        val bundle = Bundle()
        bundle.putInt("ProductState", state)
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "待付款"
            1 -> "待发货"
            2 -> "待收货"
            3 -> "售后"
            else -> {
                ""
            }
        }
    }

}

class PageFragment : Fragment() {

    val mCompositeDisposable = CompositeDisposable()

    var list = mutableListOf<ProductOrder>()
    // var productState: Int? = null

    var userState = 0
    var userStateForCurrentBuyer = 0
    lateinit var mAdapter: OrdersAdapter

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        findViews()
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_page_order_kt, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        findViews()
        getOrders(MSG_CODE_REFRESH)
    }

    private fun findViews() {
        pullLoadMoreRecyclerView.setLinearLayout()
//        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
//        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数
        userState = (activity?.application as ShopApplication).userState
        userStateForCurrentBuyer = activity?.intent?.getIntExtra("userState", -1) ?: 0
        val productState = activity?.intent?.getIntExtra("ProductState", WEI_FU_KUAN) ?: 0
        mAdapter = OrdersAdapter(activity, list, userState, productState)
        pullLoadMoreRecyclerView.setAdapter(mAdapter)
        pullLoadMoreRecyclerView.setFooterViewText("加载。。。");
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.primaryColor)
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(object : PullLoadMoreRecyclerView.PullLoadMoreListener {
            override fun onRefresh() {
                getOrders(MSG_CODE_REFRESH)
            }

            override fun onLoadMore() {
                getOrders(MSG_CODE_LOADMORE)
            }
        })

    }


    private fun getOrders(loadingType: Int) {
        val orderService = RetrofitHelper().getOrdersService()
        val userName = (activity?.application as ShopApplication).sharedPreferences?.getString("userName", "")
        //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员
        val request = ShoppingModel()
        when (userState) {
            USER_STATE_MANAGER -> request.seller = userName
            USER_STATE_USER,USER_STATE_AGENT_SUB_USER -> request.buyer = userName
            USER_STATE_AGENT -> request.buyer = if (userState == USER_STATE_AGENT && userStateForCurrentBuyer != -1) {
                activity?.intent?.getStringExtra("userName")
            } else userName//如果等于1 查询出所有商家订单
            USER_STATE_ADMIN -> {

            }
        //如果等于1 查询出所有商家订单

        /**
         * 用户状态分为1.2.3.4
         * 如果是1- 超级管理员  那么根据is_search_seller 来区分查询出卖家。还是买家的所有订单信息。
         * 如果是2 - 普通管理员 也就是卖家。。。 这里查看的是他自己卖出的订单信息。
         * 3 买家。  查看自己的订单
         * 4 代理商
         */
        //根据查询条件卖家还是买家查询
        //0购物车1未付款2代发货3已发货4售后
        }
        //TODO  如果是代理商，查询数据的时候需要设置用户状态为代理商下面用户status = 3
        request.userState = if (userState == USER_STATE_AGENT && userStateForCurrentBuyer != -1) {
            userStateForCurrentBuyer
        } else userState//如果等于1 查询出所有商家订单


        /**
         * 用户状态分为1.2.3.
         * 如果是1- 超级管理员  那么根据is_search_seller 来区分查询出卖家。还是买家的所有订单信息。
         * 如果是2 - 普通管理员 也就是卖家。。。 这里查看的是他自己卖出的订单信息。
         * 3 买家。  查看自己的订单
         */
        request.isSearchSellerOrders = false   //根据查询条件卖家还是买家查询

        request.orderState = arguments?.getInt("ProductState", WEI_FU_KUAN)//0购物车1未付款2代发货3已发货4售后
        if (loadingType == MSG_CODE_LOADMORE) {
            request.start = mAdapter.contents.size
            request.end = mAdapter.contents.size + DEFAULT_ITEM_SIZE
        } else {
            request.start = 0
            request.end = DEFAULT_ITEM_SIZE
        }
        mCompositeDisposable.add(orderService.getAllOrders(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (loadingType != MSG_CODE_LOADMORE) {
                        mAdapter.contents = t.orders!!
                        mAdapter.notifyDataSetChanged()
                        list = t.orders!!
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                    } else {
                        val cusPos = mAdapter.contents.size - 1
                        mAdapter.contents.addAll(t.orders!!)
                        list = mAdapter.contents
                        mAdapter.notifyItemRangeChanged(cusPos, t.orders!!.size)
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, { e ->
                    activity?.let {
                        Toast.makeText(it, "请求数据失败", Toast.LENGTH_LONG).show()
                    }

                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted()
                }))
    }


    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}