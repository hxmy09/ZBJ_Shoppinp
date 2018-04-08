package com.android.shop.shopapp.data

import android.content.Context
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.android.shop.shopapp.R
import com.android.shop.shopapp.model.request.AddImgsRequest
import com.squareup.picasso.Picasso
import java.io.File


open class AddImgsAdapter(var context: Context, list: ArrayList<AddImgsRequest>, var layoutManager: GridLayoutManager) : RecyclerView.Adapter<AddImgsAdapter.ViewHolder>() {

    var contents: ArrayList<AddImgsRequest> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.add_imgs_item, parent, false)
        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contents.get(position), context, layoutManager)
    }

    override fun getItemCount(): Int {
        return contents.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img: AppCompatImageView? = null
        var r: Button? = null

        init {

            img = itemView.findViewById(R.id.img)
        }

        fun bind(model: AddImgsRequest, context: Context, layoutManager: GridLayoutManager) {

//            val parm = img!!.getLayoutParams() //获取button背景的LayoutParams实例
            var width = layoutManager.width / layoutManager.spanCount - img!!.paddingLeft

            img?.layoutParams = ViewGroup.LayoutParams(width , width)

            Picasso.get().load(Uri.fromFile(File(model.path!!))).into(img)
        }
    }

}