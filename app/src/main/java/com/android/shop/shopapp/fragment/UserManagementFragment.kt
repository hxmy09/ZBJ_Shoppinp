package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.android.shop.shopapp.R
import com.android.shop.shopapp.data.UsersManagementAdapter
import com.android.shop.shopapp.model.UserModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import com.hxmy.sm.network.services.UserReqeust
import com.miguelcatalan.materialsearchview.MaterialSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_users_management.*

/**
 * Created by myron on 3/31/18.
 */
open class UserManagementFragment : Fragment() {

    var list: List<UserModel> = arrayListOf()
    private val usersList = arrayListOf<String>("普通会员", "普通管理员", "超级管理员")
    lateinit var search_view: MaterialSearchView
    private val mCompositeDisposable = CompositeDisposable()
    var adapter: UsersManagementAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_users_management, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        search_view = activity.findViewById(R.id.search_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter = UsersManagementAdapter(activity, this@UserManagementFragment, list)
        //Use this now
        recyclerView.addItemDecoration(MaterialViewPagerHeaderDecorator())
        recyclerView.adapter = adapter

        usersSelector.setItems(usersList)
        swipeRefreshLayout.setOnRefreshListener {
            fetchProducts()
        }
        swipeRefreshLayout.isRefreshing = true

        selectAll.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->

            list.map { it.isSelected = b }
            adapter!!.contents = list
            adapter!!.notifyDataSetChanged()

        })

        search.setOnClickListener {
            fetchProducts()
            swipeRefreshLayout.isRefreshing = true
        }
        delete.setOnClickListener {
            var ids = arrayListOf<String>()
            list.filter { it.isSelected!! }.forEach {
                ids.add(it.id!!)
            }
            if (ids.size == 0) {
                Toast.makeText(activity, "请选择至少一条数据", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            MaterialDialog.Builder(activity)
                    .content("你确定要删除此用户吗？")
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive { dialog, which ->
                        deleteUsers(ids)
                    }
//                .onNeutral { dialog, which ->
//                    // TODO
//                }
                    .onNegative { _, _ ->
                        // finish()
                    }
//                .onAny { dialog, which ->
//                    // TODO
//                }
                    .show()

        }

        setHasOptionsMenu(true)
        fetchProducts()

        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (TextUtils.isEmpty(query)) {
                    adapter?.contents = list
                    adapter?.notifyDataSetChanged()
                } else {
                    search(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                search(newText)
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
        var filter = arrayListOf<UserModel>()
        for (p in list) {
            if (p.userName!!.contains(queryText) || p.address!!.contains(queryText) || p.phone!!.contains(queryText)) {
                filter.add(p)
            }
        }
        adapter?.contents = filter
        adapter?.notifyDataSetChanged()
    }

    private fun deleteUsers(ids: ArrayList<String>) {
        var useRequest = UserReqeust()
        useRequest.ids = ids
        val userService = RetrofitHelper().getUsersService()

        mCompositeDisposable.add(userService.deleteUsers(useRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {
                        swipeRefreshLayout.isRefreshing = true
                        //TODO
                        fetchProducts()
                        Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(activity, "删除失败", Toast.LENGTH_LONG).show()
                    }

                }, { _ ->
                    Toast.makeText(activity, "删除失败", Toast.LENGTH_LONG).show()
                }))
    }


    private fun getUserState(): Int {
        return when (usersSelector?.text) {
            "普通会员" -> 3
            "普通管理员" -> 2
            "超级管理员" -> 1
            else -> {
                -1
            }
        }
    }

    private fun fetchProducts() {
        val usersServices = RetrofitHelper().getUsersService()
        var request = UserReqeust()
        request.userState = getUserState()
        request.start = 0
        request.end = 0
        mCompositeDisposable.add(usersServices.getAllUsers(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {
                        list = t.data!!
                        (recyclerView.adapter as UsersManagementAdapter).contents = list
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                    swipeRefreshLayout.isRefreshing = false
                }, { t ->
                    Toast.makeText(activity, "网络错误,请重新刷新", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                }))
    }


    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}

