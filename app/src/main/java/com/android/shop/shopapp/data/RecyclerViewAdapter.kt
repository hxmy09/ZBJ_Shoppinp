package com.android.shop.shopapp.data

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.ShoppingModel

/**
 * @author a488606
 * @since 3/20/18
 */

class RecyclerViewAdapter(contents: ArrayList<ShoppingModel>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var contents: List<ShoppingModel> = contents


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_card_small, parent, false)
        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contents.get(position))
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var price: TextView? = null
        var desc: TextView? = null
        var imageView: AppCompatImageView? = null

        init {
            price = itemView.findViewById<TextView>(R.id.price)
            desc = itemView.findViewById<TextView>(R.id.title)
            imageView = itemView.findViewById<AppCompatImageView>(R.id.img)


        }

        fun bind(model: ShoppingModel?) {
            price?.text = model?.price.toString()
            desc?.text = model?.desc

        }
    }
}