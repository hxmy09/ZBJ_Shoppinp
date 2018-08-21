package com.android.shop.shopapp.data

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.ProductShowActivity
import com.android.shop.shopapp.activity.ProductDetailActivity
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.util.*
import com.squareup.picasso.Picasso
import com.android.shop.shopapp.util.*


class HomeAdapter(var context: Context?, list: ArrayList<ProductModel>, var hot: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contents: ArrayList<ProductModel> = list
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View? = null

        when (viewType) {
            1 -> {
                v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.naviation_item, parent, false)
                return ViewHolderNavigation(v!!)
            }
            2 -> {
                v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.group_card_item, parent, false)
                return ViewHolder(v!!)
            }
//            3 -> {
//                v = LayoutInflater.from(parent.context)
//                        .inflate(R.layout.avd_item, parent, false)
//                return ViewHolderAV(v!!)
//            }
            else -> {
                v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.group_card_item, parent, false)
                return ViewHolder(v!!)
            }
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var type = getItemViewType(position)
        when (type) {
            1 -> {
                (holder as ViewHolderNavigation).bind()
            }
            2 -> {
                (holder as ViewHolder).bind(contents.get(position - 1))
            }
//            3 -> {
//                (holder as ViewHolderAV).bind()
//            }
            else -> {
                (holder as ViewHolder).bind(contents.get(position - 1))
            }
        }
    }

    override fun getItemCount(): Int {
        return contents.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 1
        } else {
            return 2
        }

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

    class ViewHolderAV(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
        }
    }

    class ViewHolderNavigation(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var group = when (v?.id) {
                R.id.baihuoLayout -> G_BAIHUO
                R.id.nvzhuangLayout -> G_NVZHUANG
                R.id.nanzhuangLayout -> G_NANZHUANG
                R.id.xidiLayout -> G_XIDI
                R.id.xiangbaoLayout -> G_XIANGBAO
                R.id.meizhuangLayout -> G_MEIZHUANG
                R.id.neiyiLayout -> G_NEIYI
                R.id.wanjuLayout -> G_WANJU
                R.id.wenjuLayout -> G_WENJU
                R.id.wujinLayout -> G_WUJIN
                R.id.xieziLayout -> G_XIEZI
                R.id.fangzhipinLayout -> G_FANGZHIPIN
                R.id.imgTejia -> G_TEJIA
                else -> {
                    ""
                }
            }
            var intent = Intent(itemView.context, ProductShowActivity::class.java).apply {

                putExtra(GROUP, group)
            }
            itemView.context.startActivity(intent)
        }

        var baihuoLayout: LinearLayout? = null
        var nvzhuangLayout: LinearLayout? = null
        var nanzhuangLayout: LinearLayout? = null
        var xidiLayout: LinearLayout? = null
        var xiangbaoLayout: LinearLayout? = null
        var meizhuangLayout: LinearLayout? = null
        var neiyiLayout: LinearLayout? = null
        var wanjuLayout: LinearLayout? = null
        var wenjuLayout: LinearLayout? = null
        var wujinLayout: LinearLayout? = null
        var xieziLayout: LinearLayout? = null
        var fangzhipinLayout: LinearLayout? = null
        var tejia: AppCompatImageView? = null

        init {
            baihuoLayout = itemView.findViewById<LinearLayout>(R.id.baihuoLayout)
            nvzhuangLayout = itemView.findViewById<LinearLayout>(R.id.nvzhuangLayout)
            nanzhuangLayout = itemView.findViewById<LinearLayout>(R.id.nanzhuangLayout)
            xidiLayout = itemView.findViewById<LinearLayout>(R.id.xidiLayout)
            xiangbaoLayout = itemView.findViewById<LinearLayout>(R.id.xiangbaoLayout)
            meizhuangLayout = itemView.findViewById<LinearLayout>(R.id.meizhuangLayout)
            neiyiLayout = itemView.findViewById<LinearLayout>(R.id.neiyiLayout)
            wanjuLayout = itemView.findViewById<LinearLayout>(R.id.wanjuLayout)
            wenjuLayout = itemView.findViewById<LinearLayout>(R.id.wenjuLayout)
            wujinLayout = itemView.findViewById<LinearLayout>(R.id.wujinLayout)
            xieziLayout = itemView.findViewById<LinearLayout>(R.id.xieziLayout)
            fangzhipinLayout = itemView.findViewById<LinearLayout>(R.id.fangzhipinLayout)
            tejia = itemView.findViewById<AppCompatImageView>(R.id.imgTejia)

        }

        fun bind() {
            baihuoLayout?.setOnClickListener(this)
            nvzhuangLayout?.setOnClickListener(this)
            nanzhuangLayout?.setOnClickListener(this)
            xidiLayout?.setOnClickListener(this)
            xiangbaoLayout?.setOnClickListener(this)
            meizhuangLayout?.setOnClickListener(this)
            neiyiLayout?.setOnClickListener(this)
            wanjuLayout?.setOnClickListener(this)
            wenjuLayout?.setOnClickListener(this)
            wujinLayout?.setOnClickListener(this)
            xieziLayout?.setOnClickListener(this)
            fangzhipinLayout?.setOnClickListener(this)
            tejia?.setOnClickListener(this)
        }
    }

}