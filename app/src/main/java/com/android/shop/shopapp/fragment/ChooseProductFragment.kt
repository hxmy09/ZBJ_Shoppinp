package com.android.shop.shopapp.fragment

import android.app.DialogFragment
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shop.shopapp.R

/**
 * Created by myron on 4/8/18.
 */
class ChooseProductFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_choose_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var sizeFragment = StaggeredGridLayoutFragment()
        sizeFragment.setData(arrayListOf(), LayoutType.SIZE)
        fragmentManager.beginTransaction().add(R.id.sizeLayout, sizeFragment).commit()

        //color
        var colorFragment = StaggeredGridLayoutFragment()
        colorFragment.setData(arrayListOf(), LayoutType.COLOR)
        fragmentManager.beginTransaction().add(R.id.sizeLayout, colorFragment).commit()
    }

}