package com.android.shop.shopapp.data

import android.content.Context
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.fragment.ProductManagementFragment
import com.squareup.picasso.Picasso
import java.io.File

/**
 * @author a488606
 * @since 3/20/18
 */

class ProductManagementAdapter(var context: Context?, var fragment: ProductManagementFragment, list: List<ProductModel>) : RecyclerView.Adapter<ProductManagementAdapter.ViewHolder>() {

    var contents: List<ProductModel> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductManagementAdapter.ViewHolder? {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_management_item, parent, false)
        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contents.get(position), fragment)
    }

    override fun getItemCount(): Int {
        return contents.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var price: TextView? = null
        var desc: TextView? = null
        var imageView: AppCompatImageView? = null
        var checkBox: CheckBox? = null
        var add: ImageButton? = null

        init {
            price = itemView.findViewById<TextView>(R.id.price)
            desc = itemView.findViewById<TextView>(R.id.desc)
            imageView = itemView.findViewById<AppCompatImageView>(R.id.img)
            checkBox = itemView.findViewById<CheckBox>(R.id.ck)


        }

        fun bind(model: ProductModel?, fragment: ProductManagementFragment) {
            price?.text = model?.price.toString()
            desc?.text = model?.desc
            Picasso.with(itemView.context).load(Uri.fromFile(File(model?.imageUrl))).into(imageView)
            checkBox?.isChecked = model?.isSelected!!
            checkBox?.setOnCheckedChangeListener({ compoundButton: CompoundButton, b: Boolean ->
                model.isSelected = b
            })

        }
    }

}