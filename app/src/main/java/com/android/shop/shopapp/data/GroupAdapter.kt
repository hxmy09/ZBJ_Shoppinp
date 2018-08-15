package com.android.shop.shopapp.data

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.ProductDetailActivity
import com.android.shop.shopapp.model.ProductModel
import com.squareup.picasso.Picasso


class GroupAdapter(var context: Context?, list: ArrayList<ProductModel>, var hot: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contents: ArrayList<ProductModel> = list
    var isOver: Boolean = false
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View? = null
//        if (viewType == 1) {
        v = LayoutInflater.from(parent.context)
                .inflate(R.layout.grid_item, parent, false)
        return ViewHolder(v!!)
//        } else {
//            v = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.bottom_refresh, parent, false)
//            return ViewHolderBottom(v!!)
//        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        var type = getItemViewType(position)
        (holder as ViewHolder).bind(contents.get(position), hot)
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

        fun bind(model: ProductModel?, isGridLayout: Boolean) {
            price?.text = model?.price.toString()
            desc?.text = model?.desc

            try {
                if (!TextUtils.isEmpty(model?.imageUrl)) {
                    Picasso.get().load(model?.imageUrl).into(imageView)
                } else {
                    Picasso.get().load(model?.imageUrls!![0]).into(imageView)
                }
            } catch (e: Exception) {
                Picasso.get().load(model?.details!![0].img).into(imageView)
            }


            itemView.setOnClickListener {
                var intent = Intent(itemView.context, ProductDetailActivity::class.java).apply {
                    putExtra("Details", model)
                }

                itemView?.context?.startActivity(intent)
            }
        }
    }

//    class ViewHolderBottom(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        fun bind(context: Context?, isOver: Boolean) {
//            if (isOver) {
//                itemView.visibility = View.GONE
//            } else {
//                itemView.visibility = View.VISIBLE
//            }
//            itemView.setOnClickListener {
//                (context as? OnLoadMoreListener)?.refreshBottom()
//            }
//        }
//    }

}