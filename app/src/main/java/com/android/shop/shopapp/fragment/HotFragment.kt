package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.DBUtil
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.GroupAdapter
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * Created by myron on 3/31/18.
 */
class HotFragment : Fragment() {

    var list = arrayListOf<ProductModel>()
    //    var adapter: MineAdapter? = null
//
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_hot, container, false)
    }

    //
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list = DBUtil(activity).mAppDatabase.productDao().findAll() as ArrayList<ProductModel>
//        adapter = MineAdapter(this@MineActivity, list!!)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL))
            adapter = GroupAdapter(activity, list!!)
        }
    }
}