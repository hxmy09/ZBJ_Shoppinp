package com.android.shop.shopapp.data

import android.content.Context
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.ProductModel
import com.squareup.picasso.Picasso
import java.io.File

/**
 * @author a488606
 * @since 3/20/18
 */

class MineAdapter(context: Context?, list: List<ProductModel>) : RecyclerView.Adapter<MineAdapter.ViewHolder>() {

    var contents: List<ProductModel> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MineAdapter.ViewHolder? {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.shopping_trolley_card_item, parent, false)
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
        private var imageView: AppCompatImageView? = null

        init {
            price = itemView.findViewById<TextView>(R.id.price)
            desc = itemView.findViewById<TextView>(R.id.title)
            imageView = itemView.findViewById<AppCompatImageView>(R.id.img)
        }

        fun bind(model: ProductModel?) {
            price?.text = model?.price.toString()
            desc?.text = model?.desc
//            Picasso.with(itemView.context).load(model?.imageUrl).into(imageView)
            Picasso.with(itemView.context).load(Uri.fromFile(File(model?.imageUrl))).into(imageView)
        }
    }

}