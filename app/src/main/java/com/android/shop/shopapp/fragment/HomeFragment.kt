package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.GroupActivity
import kotlinx.android.synthetic.main.fragment_home.*
import shopping.hxmy.com.shopping.util.*

/**
 * Created by myron on 3/28/18.
 */
class HomeFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        childFragmentManager.beginTransaction().add(R.id.adv, AdvFragment(), "ADV").commit()
        baihuoLayout.setOnClickListener(this)
        nvzhuangLayout.setOnClickListener(this)
        nanzhuangLayout.setOnClickListener(this)
        xidiLayout.setOnClickListener(this)
        xiangbaoLayout.setOnClickListener(this)
        meizhuangLayout.setOnClickListener(this)
        neiyiLayout.setOnClickListener(this)
        wanjuLayout.setOnClickListener(this)
        wenjuLayout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        var group = when (v?.id) {
            R.id.baihuoLayout -> G_BAIHUO
            R.id.nvzhuangLayout -> G_NVZHUANG
            R.id.nanzhuangLayout -> G_NANZHUANG
            R.id.xidiLayout -> G_XIDI
            R.id.xiangbaoLayout -> G_XIANGBAO
            R.id.meizhuangLayout -> G_MEIZHUANG
            R.id.neiyiLayout -> G_NEIYI
            R.id.wanjuLayout -> G_WANJU
            R.id.wenjuLayout -> G_WENJU
            else -> {
                ""
            }
        }
        var intent = Intent(activity, GroupActivity::class.java).apply {

            putExtra(GROUP, group)
        }
        startActivity(intent)
    }

}