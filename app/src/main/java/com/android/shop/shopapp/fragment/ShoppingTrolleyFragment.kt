package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.CompoundButton
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.DBUtil
import com.android.shop.shopapp.dao.ShoppingModel
import com.android.shop.shopapp.data.ShoppingAdapter
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import kotlinx.android.synthetic.main.fragment_shopping_trolley.*

/**
 * Created by myron on 3/31/18.
 */
class ShoppingTrolleyFragment : Fragment(), CountTotalCallBack {

    override fun checkAll(amount: Int, model: ShoppingModel) {
//        DBUtil(activity).mAppDatabase.shoppingDao()?.insert(model)
//        cal()
    }

    lateinit var list: List<ShoppingModel>

    override fun countTotal(number: Int, model: ShoppingModel) {
        //更新本地数据库 先更新数据库。在计算
        DBUtil(activity).mAppDatabase.shoppingDao()?.insert(model)
        cal()
    }

    private fun cal() {
        list = DBUtil(activity).mAppDatabase.shoppingDao()?.findAll()
//        adapter!!.contents = list
//        adapter!!.notifyDataSetChanged()
        var amount: Double = 0.00
        list.forEach {
            if (it.isSelected)
                amount += it.price!! * it.amount!!
        }
        total.text = amount.toString()
    }

    var adapter: ShoppingAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_shopping_trolley, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list = DBUtil(activity).mAppDatabase.shoppingDao()?.findAll()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter = ShoppingAdapter(activity, this@ShoppingTrolleyFragment, list!!)
        //Use this now
        recyclerView.addItemDecoration(MaterialViewPagerHeaderDecorator())
        recyclerView.adapter = adapter

        selectAll.setOnCheckedChangeListener({ compoundButton: CompoundButton, b: Boolean ->

            list.forEach {
                it.isSelected = b
                DBUtil(activity).mAppDatabase.shoppingDao()?.insert(it)
            }
            adapter!!.contents = list
            adapter!!.notifyDataSetChanged()

            cal()

        })
        result.setOnClickListener {
            MaterialDialog.Builder(activity)
                    .content("很抱歉，支付功能暂未实现。如需支付，请人工支付。所购商品合计￥${total.text.toString()}")
                    .show()

        }

        delete.setOnClickListener {
            list = DBUtil(activity).mAppDatabase.shoppingDao()?.findAll()
            list.filter { it.isSelected }.forEach { it -> DBUtil(activity).mAppDatabase.shoppingDao()?.deleteByGroupId(it.groupId!!) }
            list = DBUtil(activity).mAppDatabase.shoppingDao()?.findAll()
            if (list.size == adapter!!.itemCount) {
                Toast.makeText(activity, "请选择至少一条数据", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            adapter!!.contents = list
            adapter!!.notifyDataSetChanged()
            if (adapter!!.itemCount == 0) {
                editLayout.visibility = View.GONE
                resultLayout.visibility = View.VISIBLE
            }
            //更新显示
            cal()

        }
        cancel.setOnClickListener {
            editLayout.visibility = View.GONE
            resultLayout.visibility = View.VISIBLE
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.trolleymenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_settings -> {
                editLayout.visibility = View.VISIBLE
                resultLayout.visibility = View.GONE
            }

        }
        return super.onOptionsItemSelected(item)
    }
}

interface CountTotalCallBack {
    fun countTotal(amount: Int, model: ShoppingModel)
    fun checkAll(amount: Int, model: ShoppingModel)
}