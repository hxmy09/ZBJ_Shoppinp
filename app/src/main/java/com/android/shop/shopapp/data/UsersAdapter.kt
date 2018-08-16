package com.android.shop.shopapp.data

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.ManagerNewUserActivity
import com.android.shop.shopapp.model.request.RegisterRequest
import com.jaredrummler.materialspinner.MaterialSpinner


class UsersAdapter(var context: Context?, list: MutableList<RegisterRequest>) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    var contents: MutableList<RegisterRequest> = list
    private val usersList = arrayListOf<String>("普通会员", "普通管理员", "超级管理员","代理商") //    //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false)
        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contents[position], usersList, context)
    }

    override fun getItemCount(): Int {
        return contents.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var spinner: MaterialSpinner? = null
        var address: TextView? = null
        var phone: TextView? = null
        var userName: TextView? = null
        var btnSubmit: Button? = null

        init {
            spinner = itemView.findViewById<MaterialSpinner>(R.id.spinner)
            address = itemView.findViewById<TextView>(R.id.address)
            phone = itemView.findViewById<TextView>(R.id.phone)
            userName = itemView.findViewById<TextView>(R.id.userName)
            btnSubmit = itemView.findViewById<Button>(R.id.btnSubmit)
        }

        fun bind(model: RegisterRequest?, usersList: ArrayList<String>, context: Context?) {
            itemView.findViewById<MaterialSpinner>(R.id.spinner).setItems(usersList)
            address?.text = model?.address
            phone?.text = model?.phone
            userName?.text = model?.userName
            btnSubmit?.setOnClickListener {
                model?.userState = when (spinner?.text) {
                    "普通会员" -> 3
                    "普通管理员" -> 2
                    "超级管理员" -> 1
                    "代理商" -> 4
                    else -> {
                        0
                    }
                }
                (context as ManagerNewUserActivity).submit(model!!)
            }
        }
    }

}