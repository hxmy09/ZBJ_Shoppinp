package com.android.shop.shopapp.activity

import android.os.Bundle
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.data.UsersAdapter
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.RegisterRequest
import com.android.shop.shopapp.network.services.ProductParameterRequest
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_manage_user.*
import com.android.shop.shopapp.util.DEFAULT_ITEM_SIZE
import com.android.shop.shopapp.util.MSG_CODE_LOADMORE
import com.android.shop.shopapp.util.MSG_CODE_REFRESH

/**
 * Created by myron on 3/29/18.
 */
class ManagerNewUserActivity : BaseActivity(), UserAuditCall {

    var list = arrayListOf<RegisterRequest>()

    lateinit var mAdapter: UsersAdapter
    private fun findViews() {

        pullLoadMoreRecyclerView.setLinearLayout()
//        pullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
//        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数

        mAdapter = UsersAdapter(this@ManagerNewUserActivity, list)
        pullLoadMoreRecyclerView.setAdapter(mAdapter)
        pullLoadMoreRecyclerView.setFooterViewText("加载。。。");
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.primaryColor)
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(object : PullLoadMoreRecyclerView.PullLoadMoreListener {
            override fun onRefresh() {
                getAllUsers(MSG_CODE_REFRESH)
            }

            override fun onLoadMore() {
                getAllUsers(MSG_CODE_LOADMORE)
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_user)
        findViews()
//        swipeRefreshLayout.setOnRefreshListener {
//            getAllUsers()
//        }
//        swipeRefreshLayout.isRefreshing = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(this@ManagerNewUserActivity)
//            setHasFixedSize(true)
//            addItemDecoration(DividerItemDecoration(this@ManagerNewUserActivity, DividerItemDecoration.HORIZONTAL))
//            adapter = UsersAdapter(this@ManagerNewUserActivity, list)
//        }
        getAllUsers(MSG_CODE_REFRESH)
    }

    private fun getAllUsers(loadingType: Int) {
        val allUserService = RetrofitHelper().getAllUserService()
        var request = ProductParameterRequest()
        if (loadingType == MSG_CODE_LOADMORE) {
            request.start = mAdapter.contents.size
            request.end = mAdapter.contents.size + DEFAULT_ITEM_SIZE
        } else {
            request.start = 0
            request.end = DEFAULT_ITEM_SIZE
        }
        mCompositeDisposable.add(allUserService.getAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {


                        if (loadingType != MSG_CODE_LOADMORE) {
                            mAdapter.contents = t.data!!
                            mAdapter.notifyDataSetChanged()
                            pullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                        } else {
                            val cusPos = mAdapter.contents.size - 1
                            mAdapter.contents.addAll(t.data!!)
                            mAdapter.notifyItemRangeChanged(cusPos, t.data!!.size)
                            pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }

//                        list = t.data as ArrayList<RegisterRequest>
//                        (recyclerView.adapter as UsersAdapter).contents = list
//                        recyclerView.adapter?.notifyDataSetChanged()
//                        swipeRefreshLayout.isRefreshing = false
                    } else {
                        Toast.makeText(this@ManagerNewUserActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                    }
                }, { t ->
                    Toast.makeText(this@ManagerNewUserActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                }))
    }

    override fun submit(user: RegisterRequest) {
        val registerService = RetrofitHelper().getRegisterService()
        mCompositeDisposable.add(registerService.validate(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    //成功
                    if (t.code == "100") {//101失败
                        Toast.makeText(this@ManagerNewUserActivity, "审核成功", Toast.LENGTH_LONG).show()
                        getAllUsers(MSG_CODE_REFRESH)
//                        swipeRefreshLayout.isRefreshing = true
                    } else {
                        Toast.makeText(this@ManagerNewUserActivity, "审核失败", Toast.LENGTH_LONG).show()
                    }
                }, { _ ->

                    //                    //TODO  need to remove
//                    var intent = Intent(this@RegisterActivity, MainActivity::class.java);
//                    startActivity(intent)
//                    finish()
                    Toast.makeText(this@ManagerNewUserActivity, "审核失败", Toast.LENGTH_LONG).show()

                }))

    }

}

interface UserAuditCall {
    fun submit(user: RegisterRequest)
}