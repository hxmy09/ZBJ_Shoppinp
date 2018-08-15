package com.android.shop.shopapp.data

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.OrderDetailsActivity
import com.android.shop.shopapp.model.response.ProductOrder


class OrdersAdapter(var context: Context?, list: MutableList<ProductOrder>, var userState: Int, var productState: Int) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    var contents: MutableList<ProductOrder> = list
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_item, parent, false)
        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contents[position], context, userState, productState)
    }

    override fun getItemCount(): Int {
        return contents.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var orderNum: TextView? = null
        var orderTime: TextView? = null
        var orderAmount: TextView? = null
        var phone: TextView? = null
        var address: TextView? = null
        var total: TextView? = null
        var addressLayout: LinearLayout? = null
        var phoneLayout: LinearLayout? = null
        var btnSubmit: Button? = null

        init {
            orderNum = itemView.findViewById<TextView>(R.id.orderNum)
//            orderAmount = itemView.findViewById<TextView>(R.id.orderAmount)
            orderTime = itemView.findViewById<TextView>(R.id.orderTime)
            address = itemView.findViewById<TextView>(R.id.address)
            phone = itemView.findViewById<TextView>(R.id.phone)
            addressLayout = itemView.findViewById<LinearLayout>(R.id.addressLayout)
            phoneLayout = itemView.findViewById<LinearLayout>(R.id.phoneLayout)
            btnSubmit = itemView.findViewById<Button>(R.id.btnSubmit)
            total = itemView.findViewById<TextView>(R.id.total)
        }

        fun bind(model: ProductOrder?, context: Context?, userState: Int, productState: Int) {

            orderNum?.text = model?.orderNumber
            orderTime?.text = model?.products?.get(0)?.orderTime
            address?.text = model?.products?.get(0)?.buyerAddress
            phone?.text = model?.products?.get(0)?.buyerPhone
            total?.text = model?.total?.toString()
//            orderAmount?.text = model?.products?.get(0)
            btnSubmit?.setOnClickListener {
                val intent = Intent(context, OrderDetailsActivity::class.java).apply {
                    putExtra("OrderNum", model?.orderNumber)
                    putExtra("total", model?.total)
                    putExtra("orders", model?.products as ArrayList)
                    putExtra("ProductState", productState)
                }

                (context as AppCompatActivity).startActivityForResult(intent, 0x11)
            }

//            if (userState == 3) {
//                addressLayout?.visibility = View.GONE
//                phoneLayout?.visibility = View.GONE
//            } else {
//                addressLayout?.visibility = View.VISIBLE
//                phoneLayout?.visibility = View.VISIBLE
//            }
        }
    }

}