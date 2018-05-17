package com.android.shop.shopapp.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.ProductDetailActivity
import com.android.shop.shopapp.model.Detail
import com.android.shop.shopapp.model.ProductModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.File


class UploadAdapter(var context: Context?, list: ArrayList<Detail>) : RecyclerView.Adapter<UploadAdapter.ViewHolder>() {

    var contents: ArrayList<Detail> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.upload_item, parent, false)

        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contents.get(position), this)
    }

    override fun getItemCount(): Int {
        return contents.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var color: TextView? = null
        var store: TextView? = null
        var delete: TextView? = null
        var imageView: AppCompatImageView? = null

        init {
            color = itemView.findViewById<TextView>(R.id.color)
            store = itemView.findViewById<TextView>(R.id.store)
            delete = itemView.findViewById<TextView>(R.id.delete)
            imageView = itemView.findViewById<AppCompatImageView>(R.id.img)


        }

        fun bind(model: Detail?, adapter: UploadAdapter) {
            color?.text = model?.color
            Picasso.get().load(Uri.fromFile(File(model?.img))).into(imageView)

            var sb = StringBuffer()
            model?.store?.forEach {

                sb.append(it.size)
//                sb.append("/")
//                sb.append(it.amount)
//                sb.append(",")
            }
            store?.text = sb.toString()
            delete?.setOnClickListener {
                adapter.contents.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
//                adapter.notifyDataSetChanged()
            }
        }
    }

}