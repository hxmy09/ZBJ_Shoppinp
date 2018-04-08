package com.android.shop.shopapp.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.data.UsersAdapter
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.RegisterRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_group.*

/**
 * Created by myron on 3/29/18.
 */
class ManagerNewUserActivity : BaseActivity(), UserAuditCall {

    var list = arrayListOf<RegisterRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_user)

        swipeRefreshLayout.setOnRefreshListener {
            getAllUsers()
        }
        swipeRefreshLayout.isRefreshing = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ManagerNewUserActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@ManagerNewUserActivity, DividerItemDecoration.HORIZONTAL))
            adapter = UsersAdapter(this@ManagerNewUserActivity, list)
        }
        getAllUsers()
    }

    private fun getAllUsers() {
        val allUserService = RetrofitHelper().getAllUserService()
        mCompositeDisposable.add(allUserService.getAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {
                        list = t.data as ArrayList<RegisterRequest>
                        (recyclerView.adapter as UsersAdapter).contents = list
                        recyclerView.adapter?.notifyDataSetChanged()
                        swipeRefreshLayout.isRefreshing = false
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
                        getAllUsers()
                        swipeRefreshLayout.isRefreshing = true
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