package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.activity.*
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.util.*
import kotlinx.android.synthetic.main.fragment_mine.*
import com.android.shop.shopapp.util.*

/**
 * Created by myron on 3/31/18.
 */
class MineFragment : Fragment(), View.OnClickListener {


    var list = arrayListOf<ProductModel>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!(activity.application as ShopApplication).isLoggedIn) {
            startActivity(Intent(activity, LoginActivity::class.java))
            activity.finish()
            return
        }
        audit.setOnClickListener(this)
        upload.setOnClickListener(this)
        exit.setOnClickListener(this)
        manageProduct.setOnClickListener(this)
        daiFukuan.setOnClickListener(this)
        daiFahuo.setOnClickListener(this)
        daiShouHuo.setOnClickListener(this)
        tuihuoshouhou.setOnClickListener(this)
        manageUser.setOnClickListener(this)
        dailimanageUser.setOnClickListener(this)
        setHasOptionsMenu(false)
//        orderList.setOnClickListener(this)


//        var userNameStr = (activity.application as ShopApplication).sharedPreferences?.getString("userName", "")
//        var addressStr = (activity.application as ShopApplication).sharedPreferences?.getString("address", "")
//        var phoneStr = (activity.application as ShopApplication).sharedPreferences?.getString("phone", "")
        val userState = (activity.application as ShopApplication).userState
//        address.text = addressStr
//        phone.text = phoneStr
//        userName.text = userNameStr

        //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员 4 代理商
        when (userState) {
            USER_STATE_REGISTER -> Toast.makeText(activity, "未审核用户", Toast.LENGTH_SHORT).show()
            USER_STATE_ADMIN -> {
                muLayout.visibility = View.VISIBLE
                mProLayout.visibility = View.VISIBLE
                dailiLayout.visibility = View.GONE
            }
            USER_STATE_MANAGER -> {
                //没有审核的权限
                muLayout.visibility = View.GONE
                mProLayout.visibility = View.VISIBLE
                dailiLayout.visibility = View.GONE
            }
            USER_STATE_USER , USER_STATE_AGENT_SUB_USER-> {
                muLayout.visibility = View.GONE
                mProLayout.visibility = View.GONE
                dailiLayout.visibility = View.GONE
            }
            USER_STATE_AGENT -> {
                muLayout.visibility = View.GONE
                mProLayout.visibility = View.GONE
                dailiLayout.visibility = View.VISIBLE

                tuijianma.text = (activity.application as ShopApplication).id
            }
            else -> {
                mProLayout.visibility = View.GONE
                muLayout.visibility = View.GONE
                dailiLayout.visibility = View.GONE
            }
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.audit -> {
                var intent = Intent(activity, ManagerNewUserActivity::class.java)
                startActivity(intent)
            }
            R.id.upload -> {
                var intent = Intent(activity, UploadActivity::class.java)
                startActivity(intent)
            }

            R.id.exit -> {
                (activity.application as ShopApplication).sharedPreferences?.edit()?.remove("loggedin")?.apply()
                (activity.application as ShopApplication).sharedPreferences?.edit()?.remove("userState")?.apply() //1管理员，0 普通客户
                var intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(activity, "退出登录", Toast.LENGTH_SHORT).show()
                activity.finish()
            }
            R.id.manageProduct -> {
                var intent = Intent(activity, ManageProductsActivity::class.java)
                startActivity(intent)
            }
            R.id.daiFukuan -> {

                var intent = Intent(activity, OrderListActivityKt::class.java)
                intent.putExtra("ProductState", WEI_FU_KUAN)
                intent.putExtra("Title", "待付款清单")
                startActivity(intent)
            }
            R.id.daiFahuo -> {
                var intent = Intent(activity, OrderListActivityKt::class.java)
                intent.putExtra("ProductState", DAI_FA_HUO)
                intent.putExtra("Title", "待发货清单")
                startActivity(intent)
            }
            R.id.daiShouHuo -> {
                var intent = Intent(activity, OrderListActivityKt::class.java)
                intent.putExtra("ProductState", DAI_SHOU_HUO)
                intent.putExtra("Title", "待收货清单")
                startActivity(intent)
            }
            R.id.tuihuoshouhou -> {
                var intent = Intent(activity, OrderListActivityKt::class.java)
                intent.putExtra("ProductState", SHOU_HOU)
                intent.putExtra("Title", "售后清单")
                startActivity(intent)
            }
            R.id.manageUser -> {
                var intent = Intent(activity, ManageUsersActivity::class.java)
                startActivity(intent)

            }
            R.id.dailimanageUser -> {
                var intent = Intent(activity, AgentUsersActivity::class.java)
                startActivity(intent)

            }

//            R.id.orderList -> {
//                var intent = Intent(activity, OrdersListActivity::class.java)
//                startActivity(intent)
//            }
        }
    }
}