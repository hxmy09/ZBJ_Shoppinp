package com.android.shop.shopapp.fragment
//
//import android.widget.TextView
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.view.ViewGroup
//import android.view.LayoutInflater
//
//
//
//class OrderPageFragment : Fragment {
//    val ARG_PAGE = "ARG_PAGE"
//
//    private var mPage: Int = 0
//
//    fun newInstance(page: Int): PageFragment {
//        val args = Bundle()
//        args.putInt(ARG_PAGE, page)
//        val fragment = PageFragment()
//        fragment.setArguments(args)
//        return fragment
//    }
//
//    fun onCreate(savedInstanceState: Bundle) {
//        super.onCreate(savedInstanceState)
//        mPage = getArguments().getInt(ARG_PAGE)
//    }
//
//    fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
//                     savedInstanceState: Bundle): View {
//        val view = inflater.inflate(R.layout.fragment_page, container, false)
//        val textView = view as TextView
//        textView.text = "Fragment #$mPage"
//        return view
//    }
//}