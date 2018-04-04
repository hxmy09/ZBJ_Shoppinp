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
import com.android.shop.shopapp.dao.ProductModel
import kotlinx.android.synthetic.main.fragment_mine.*

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
        audit.setOnClickListener(this)
        upload.setOnClickListener(this)
        exit.setOnClickListener(this)
        manageProduct.setOnClickListener(this)
        orderList.setOnClickListener(this)


        var userNameStr = (activity.application as ShopApplication).sharedPreferences?.getString("userName", "")
        var addressStr = (activity.application as ShopApplication).sharedPreferences?.getString("address", "")
        var phoneStr = (activity.application as ShopApplication).sharedPreferences?.getString("phone", "")
        var userState = (activity.application as ShopApplication).sharedPreferences?.getInt("userState", 0)
        address.text = addressStr
        phone.text = phoneStr
        userName.text = userNameStr

        //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员
        when (userState) {
            0 -> Toast.makeText(activity, "未审核用户", Toast.LENGTH_SHORT).show()
            1 -> {
                audit.visibility = View.VISIBLE
                upload.visibility = View.VISIBLE
                manageProduct.visibility = View.VISIBLE
            }
            2 -> {
                //没有审核的权限
                audit.visibility = View.GONE
                upload.visibility = View.VISIBLE
                manageProduct.visibility = View.VISIBLE
            }
            3 -> {
                audit.visibility = View.GONE
                upload.visibility = View.GONE
                manageProduct.visibility = View.GONE
            }
            else -> {
                audit.visibility = View.GONE
                upload.visibility = View.GONE
                manageProduct.visibility = View.GONE
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
            R.id.orderList -> {
                var intent = Intent(activity, OrdersListActivity::class.java)
                startActivity(intent)
            }
        }
    }
}