package com.android.shop.shopapp.data

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.ProductManagementFragment
import com.android.shop.shopapp.model.ProductModel
import com.squareup.picasso.Picasso


class ProductManagementAdapter(var context: Context?, var fragment: ProductManagementFragment, list: MutableList<ProductModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contents: MutableList<ProductModel> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_management_item, parent, false)
        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductManagementAdapter.ViewHolder).bind(contents.get(position))
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var price: TextView? = null
        var desc: TextView? = null
        var userName: TextView? = null
        var imageView: AppCompatImageView? = null
        var checkBox: CheckBox? = null
        var add: ImageButton? = null

        init {
            price = itemView.findViewById<TextView>(R.id.price)
            desc = itemView.findViewById<TextView>(R.id.desc)
            userName = itemView.findViewById<TextView>(R.id.userName)
            imageView = itemView.findViewById<AppCompatImageView>(R.id.img)
            checkBox = itemView.findViewById<CheckBox>(R.id.ck)

        }

        fun bind(model: ProductModel?) {
            price?.text = model?.price.toString()
            desc?.text = model?.desc
            userName?.text = model?.userName?.let {
                it
            } ?: model?.userName_2

            try {
                if (!TextUtils.isEmpty(model?.imageUrl)) {
                    Picasso.get().load(model?.imageUrl).into(imageView)
                } else {
                    Picasso.get().load(model?.imageUrls!![0]).into(imageView)
                }
            } catch (e: Exception) {
                Picasso.get().load(model?.details!![0].img).into(imageView)
            }

            checkBox?.isChecked = model?.isSelected!!
            checkBox?.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
                model.isSelected = b
            }

        }
    }
}