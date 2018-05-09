package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shop.shopapp.R
import com.android.shop.shopapp.data.GridLayoutAdapter
import com.android.shop.shopapp.model.request.GridLayoutModel
import kotlinx.android.synthetic.main.fragment_grid.*

/**
 * Created by myron on 4/7/18.
 */


enum class LayoutType {
    IMAGE, SIZE, COLOR
}

class StaggeredGridLayoutFragment : Fragment() {


    var mSpanCount = 3;
    var list = arrayListOf<GridLayoutModel>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.fragment_grid, container, false)!!
    }

    fun setData(data: ArrayList<GridLayoutModel>, type: LayoutType, spanCount: Int = 3) {
        list = data
        mSpanCount = spanCount
        (recyclerView.adapter as GridLayoutAdapter).setData(list, type)
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(mSpanCount, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = GridLayoutAdapter(activity, list, layoutManager as StaggeredGridLayoutManager)
        }
    }
}