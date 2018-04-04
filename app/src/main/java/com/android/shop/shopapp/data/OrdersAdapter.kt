package com.android.shop.shopapp.data

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.OrderDetailActivity
import com.android.shop.shopapp.model.response.Order


class OrdersAdapter(var context: Context?, list: ArrayList<Order>) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    var contents: ArrayList<Order> = list
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_item, parent, false)
        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contents[position], context)
    }

    override fun getItemCount(): Int {
        return contents.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var orderNum: TextView? = null
        var orderTime: TextView? = null
        var orderAmount: TextView? = null
        var btnSubmit: Button? = null

        init {
            orderNum = itemView.findViewById<TextView>(R.id.orderNum)
            orderAmount = itemView.findViewById<TextView>(R.id.orderAmount)
            orderTime = itemView.findViewById<TextView>(R.id.orderTime)
            btnSubmit = itemView.findViewById<Button>(R.id.btnSubmit)
        }

        fun bind(model: Order?, context: Context?) {
//            itemView.findViewById<MaterialSpinner>(R.id.spinner).setItems(usersList)
            orderNum?.text = model?.orderNum
            orderAmount?.text = model?.orderAmount
            orderTime?.text = model?.orderTime
            btnSubmit?.setOnClickListener {
                var intent = Intent(context, OrderDetailActivity::class.java).apply {
                    putExtra("OrderNum", model?.orderNum)
                }
                context?.startActivity(intent)
            }
        }
    }

}