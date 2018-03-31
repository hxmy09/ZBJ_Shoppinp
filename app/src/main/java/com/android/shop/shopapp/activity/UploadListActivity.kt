package com.android.shop.shopapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.data.UploadListAdapter
import com.android.shop.shopapp.model.request.ProductReqeust
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import kotlinx.android.synthetic.main.activity_upload_list.*

/**
 * @author a488606
 * @since 3/22/18
 */

class UploadListActivity : BaseActivity() {
    val REQUEST_CODE = 0x11

//    var  uploadListAdapter : UploadListAdapter
    var list = arrayListOf<ProductReqeust>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_list)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@UploadListActivity) as RecyclerView.LayoutManager?
            addItemDecoration(MaterialViewPagerHeaderDecorator())
            adapter = UploadListAdapter(list)
            setHasFixedSize(true)
        }
        fab.setOnClickListener {
            var intent = Intent(this@UploadListActivity, UploadActivity::class.java).apply {

            }
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE && data != null) {
            var productReqeust = data.getParcelableExtra<ProductReqeust>("Product")
            list.add(productReqeust)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}