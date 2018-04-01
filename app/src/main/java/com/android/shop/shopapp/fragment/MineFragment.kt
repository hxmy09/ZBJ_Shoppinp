package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.activity.*
import com.android.shop.shopapp.dao.DBUtil
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.MineAdapter
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_shopping_trolley.*

/**
 * Created by myron on 3/31/18.
 */
class MineFragment : Fragment(), View.OnClickListener {


    var list = arrayListOf<ProductModel>()
    var adapter: MineAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        list = DBUtil(activity).mAppDatabase.productDao().findAll() as ArrayList<ProductModel>
////        adapter = MineAdapter(this@MineActivity, list!!)
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(activity)
//            setHasFixedSize(true)
//            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL))
//            adapter = MineAdapter(activity, list)
//        }
        code.setOnClickListener(this)
        update.setOnClickListener(this)
        upload.setOnClickListener(this)
        exit.setOnClickListener(this)
        manageProduct.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.code -> {
                var intent = Intent(activity, CodeGenerateActivity::class.java)
                startActivity(intent)
            }
            R.id.update -> {
                var intent = Intent(activity, UpdateInfoActivity::class.java)
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
                var intent = Intent(activity, ManageActivity::class.java)
                startActivity(intent)
            }
        }
    }
}