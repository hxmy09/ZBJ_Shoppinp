package com.android.shop.shopapp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.ShoppingModel
import com.android.shop.shopapp.data.RecyclerViewAdapter
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import kotlinx.android.synthetic.main.fragment_recyclerview.*

/**
 * @author a488606
 * @since 3/20/18
 */
class RecyclerViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var items = ArrayList<ShoppingModel>()

//        items = arguments?.getParcelableArrayList<ShoppingModel>("shoppingList")!!
//        items = arguments.getParcelableArrayList("")!!
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        //Use this now
        recyclerView.addItemDecoration(MaterialViewPagerHeaderDecorator())
        recyclerView.adapter = RecyclerViewAdapter(items)
    }
}