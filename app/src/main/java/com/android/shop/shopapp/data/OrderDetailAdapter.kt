package com.android.shop.shopapp.data

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.ProductDetailActivity
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.ShoppingModel
import com.squareup.picasso.Picasso


class OrderDetailAdapter(var context: Context?, list: MutableList<ShoppingModel>) : RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    var contents: List<ShoppingModel> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_detail_item, parent, false)
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
        var count: TextView? = null
        var color: TextView? = null
        var size: TextView? = null
        var add: ImageButton? = null

        init {
            price = itemView.findViewById<TextView>(R.id.price)
            desc = itemView.findViewById<TextView>(R.id.desc)
            count = itemView.findViewById<TextView>(R.id.count)
            color = itemView.findViewById<TextView>(R.id.color)
            size = itemView.findViewById<TextView>(R.id.size)
            imageView = itemView.findViewById<AppCompatImageView>(R.id.img)


        }

        fun bind(model: ShoppingModel?) {
            price?.text = model?.price.toString()
            desc?.text = model?.desc
            count?.text = model?.orderAmount.toString()
            color?.text = model?.color
            size?.text = model?.size
            Picasso.get().load(model?.imageUrl).into(imageView)

            itemView.setOnClickListener {
                var intent = Intent(itemView.context, ProductDetailActivity::class.java).apply {

                    var productModel = ProductModel()
                    productModel.price = model?.price
                    productModel.desc= model?.desc
                    productModel.productId = model?.productId
                    putExtra("Details", productModel)
                }
                itemView?.context?.startActivity(intent)
            }
        }
    }

}