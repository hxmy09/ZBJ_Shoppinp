package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.DBUtil
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.ProductManagementAdapter
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import kotlinx.android.synthetic.main.fragment_product_management.*

/**
 * Created by myron on 3/31/18.
 */
open class ProductManagementFragment : Fragment() {

    lateinit var list: List<ProductModel>


    var adapter: ProductManagementAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_product_management, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list = DBUtil(activity).mAppDatabase.productDao()?.findAll()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter = ProductManagementAdapter(activity, this@ProductManagementFragment, list!!)
        //Use this now
        recyclerView.addItemDecoration(MaterialViewPagerHeaderDecorator())
        recyclerView.adapter = adapter

        selectAll.setOnCheckedChangeListener({ compoundButton: CompoundButton, b: Boolean ->

            list.forEach {
                it.isSelected = b
                DBUtil(activity).mAppDatabase.productDao()?.insert(it)
            }
            adapter!!.contents = list
            adapter!!.notifyDataSetChanged()

        })

        delete.setOnClickListener {
            list.filter { it.isSelected }.forEach { it -> DBUtil(activity).mAppDatabase.productDao()?.deleteByGroupId(it.groupId!!) }
            list = DBUtil(activity).mAppDatabase.productDao()?.findAll()
            if (list.size == adapter!!.itemCount) {
                Toast.makeText(activity, "请选择至少一条数据", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            adapter!!.contents = list
            adapter!!.notifyDataSetChanged()

        }

        setHasOptionsMenu(true)
    }
}

