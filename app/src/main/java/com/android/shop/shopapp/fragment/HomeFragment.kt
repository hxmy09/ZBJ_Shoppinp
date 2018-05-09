package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.*
import com.android.shop.shopapp.R

/**
 * Created by myron on 3/28/18.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        childFragmentManager.beginTransaction().add(R.id.hot, HotFragment(), "Hot").commit()
        setHasOptionsMenu(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.findItem(R.id.action_settings)?.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

}