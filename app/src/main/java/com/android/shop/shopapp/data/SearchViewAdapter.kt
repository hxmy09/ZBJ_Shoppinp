package com.android.shop.shopapp.data

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.model.ProductModel
import com.squareup.picasso.Picasso

class SearchViewAdapter(var list: MutableList<ProductModel>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        lateinit var holder: ViewHolder

        lateinit var view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.searchview_item, null, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.bind(list.get(index = position))
        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: android.support.v7.widget.AppCompatImageView? = null
        var desc: TextView? = null
        var price: TextView? = null

        init {
            imageView = itemView.findViewById(R.id.imageView)
            desc = itemView.findViewById(R.id.desc)
            price = itemView.findViewById(R.id.price)
        }

        fun bind(model: ProductModel) {
            Picasso.get().load("").into(imageView)
            desc?.text = model.desc
            price?.text = model.price.toString()
        }
    }
}