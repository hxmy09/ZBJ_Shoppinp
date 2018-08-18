package com.android.shop.shopapp.data

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.shop.shopapp.R
import com.squareup.picasso.Picasso

//class SearchViewAdapter(var list: MutableList<ProductModel>) : BaseAdapter() {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
//
//        lateinit var holder: ViewHolder
//
//        lateinit var view: View
//        if (convertView == null) {
//            view = LayoutInflater.from(parent?.context).inflate(R.layout.searchview_item, null, false)
//            holder = ViewHolder(view)
//            view.tag = holder
//        } else {
//            holder = view.tag as ViewHolder
//        }
//
//        holder.bind(list.get(index = position))
//        return view
//    }
//
//    override fun getItem(position: Int): Any {
//        return position
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getCount(): Int {
//        return list.size
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var imageView: android.support.v7.widget.AppCompatImageView? = null
//        var desc: TextView? = null
//        var price: TextView? = null
//
//        init {
//            imageView = itemView.findViewById(R.id.imageView)
//            desc = itemView.findViewById(R.id.desc)
//            price = itemView.findViewById(R.id.price)
//        }
//
//        fun bind(model: ProductModel) {
//            Picasso.get().load("").into(imageView)
//            desc?.text = model.desc
//            price?.text = model.price.toString()
//        }
//    }
//}

class SearchableAdapter(context: Context, c: Cursor) : android.support.v4.widget.CursorAdapter(context, c, FLAG_REGISTER_CONTENT_OBSERVER) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(parent?.context).inflate(R.layout.searchview_item, null, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        var imageView: android.support.v7.widget.AppCompatImageView? = null
        var desc: TextView? = null
        var price: TextView? = null

        imageView = view?.findViewById(R.id.img)
        desc = view?.findViewById(R.id.desc)
        price = view?.findViewById(R.id.price)

        Picasso.get().load(cursor?.getString(cursor.getColumnIndexOrThrow("imgUrl"))).into(imageView)
        desc?.text = cursor?.getString(cursor.getColumnIndexOrThrow("desc"))
        price?.text = cursor?.getString(cursor.getColumnIndexOrThrow("price"))
    }

}