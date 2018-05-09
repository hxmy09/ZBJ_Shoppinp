package com.android.shop.shopapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.LayoutType
import com.android.shop.shopapp.model.request.GridLayoutModel
import com.squareup.picasso.Picasso
import java.io.File


open class GridLayoutAdapter(var context: Context, list: ArrayList<GridLayoutModel>, var layoutManager: StaggeredGridLayoutManager) : RecyclerView.Adapter<GridLayoutAdapter.ViewHolder>() {

    private var contents: ArrayList<GridLayoutModel> = list

    private var mType: LayoutType = LayoutType.IMAGE

    @SuppressLint("SupportAnnotationUsage")
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        @LayoutRes var redId = when (mType) {

            LayoutType.IMAGE -> R.layout.item_grid_imgs
            LayoutType.COLOR -> R.layout.item_grid_colors
            LayoutType.SIZE -> R.layout.item_grid_sizes
        }

        var view = LayoutInflater.from(parent.context)
                .inflate(redId, parent, false)
        return ViewHolder(view!!, mType)
    }

    fun setData(ct: ArrayList<GridLayoutModel>, type: LayoutType) {
        contents = ct
        mType = type

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contents.get(position), context, layoutManager, mType)
    }

    override fun getItemCount(): Int {
        return contents.size
    }


    class ViewHolder(itemView: View, type: LayoutType) : RecyclerView.ViewHolder(itemView) {

        var img: AppCompatImageView? = null
        var text: TextView? = null
        var r: Button? = null

        init {
            when (type) {

                LayoutType.IMAGE -> {
                    img = itemView.findViewById(R.id.img)
                }
                LayoutType.COLOR -> {
                    text = itemView.findViewById(R.id.text)
                }
                LayoutType.SIZE -> {
                    text = itemView.findViewById(R.id.text)
                }
            }

        }

        fun bind(model: GridLayoutModel, context: Context, layoutManager: StaggeredGridLayoutManager, type: LayoutType) {

//            val parm = img!!.getLayoutParams() //获取button背景的LayoutParams实例
            var width = layoutManager.width / layoutManager.spanCount - img!!.paddingLeft

            img?.layoutParams = ViewGroup.LayoutParams(width, width)
            when (type) {

                LayoutType.IMAGE -> {
                    Picasso.get().load(Uri.fromFile(File(model.path!!))).into(img)
                }
                LayoutType.COLOR -> text?.text = model.text
                LayoutType.SIZE -> text?.text = model.text
            }

        }
    }

}