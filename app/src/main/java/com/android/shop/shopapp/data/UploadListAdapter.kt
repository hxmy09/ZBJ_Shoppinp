package com.android.shop.shopapp.data

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.model.request.ProductReqeust

/**
 * @author a488606
 * @since 3/20/18
 */

class UploadListAdapter(contents: List<ProductReqeust>?) : RecyclerView.Adapter<UploadListAdapter.ViewHolder>() {

    private var contents: List<ProductReqeust>? = contents

    private val typeHeader = 0
    private val typeCell = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        var view: View?

        when (viewType) {
            typeHeader -> {
                view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_card_big, parent, false)
                return ViewHolder(view)
            }
            typeCell -> {
                view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_card_small, parent, false)
                return ViewHolder(view)
            }
        }
        return null
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            typeHeader -> {
                holder.bind(contents!!.get(position))
            }
            typeCell -> {
                holder.bind(contents!!.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return contents?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            typeHeader -> typeHeader
            else -> typeCell
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var image: AppCompatImageView? = null
        var price: TextView? = null
        var desc: TextView? = null

        init {
            image = view.findViewById<AppCompatImageView>(R.id.img)
            price = view.findViewById<TextView>(R.id.price)
            desc = view.findViewById<TextView>(R.id.desc)
        }

        fun bind(product: ProductReqeust) {

//            image.
            price?.text = product.price.toString()
            desc?.text = product.desc
        }

    }
}