package com.android.shop.shopapp.data

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.ProductDetailActivity
import com.android.shop.shopapp.dao.ProductModel
import com.squareup.picasso.Picasso


class GroupAdapter(var context: Context?, list: ArrayList<ProductModel>) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    var contents: ArrayList<ProductModel> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.group_card_item, parent, false)
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
            desc = itemView.findViewById<TextView>(R.id.desc)
            imageView = itemView.findViewById<AppCompatImageView>(R.id.img)


        }

        fun bind(model: ProductModel?) {
            price?.text = model?.price.toString()
            desc?.text = model?.desc
            Picasso.get().load(model?.imageUrl).into(imageView)

            itemView.setOnClickListener {
                var intent = Intent(itemView.context, ProductDetailActivity::class.java).apply {
                    putExtra("Details", model)
                }

                itemView?.context?.startActivity(intent)
            }
        }
    }

}