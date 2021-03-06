package com.android.shop.shopapp.data

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.UserManagementFragment
import com.android.shop.shopapp.model.UserModel


class UsersManagementAdapter(var context: Context?, var fragment: UserManagementFragment, list: List<UserModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contents: List<UserModel> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item_all, parent, false)
        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as UsersManagementAdapter.ViewHolder).bind(contents.get(position))
    }

    override fun getItemCount(): Int {
        return contents.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var checkBox: CheckBox? = null
        var address: TextView? = null
        var phone: TextView? = null
        var userName: TextView? = null
        var level: TextView? = null

        init {
            address = itemView.findViewById<TextView>(R.id.address)
            phone = itemView.findViewById<TextView>(R.id.phone)
            userName = itemView.findViewById<TextView>(R.id.userName)
            level = itemView.findViewById<TextView>(R.id.level)
            checkBox = itemView.findViewById<CheckBox>(R.id.ck)

        }

        fun bind(model: UserModel?) {
            address?.text = model?.address
            phone?.text = model?.phone
            userName?.text = model?.userName
            if (model?.userState == 1) {
                level?.text = "超级管理员"
            } else if (model?.userState == 2) {
                level?.text = "普通管理员"
            } else if (model?.userState == 3) {
                level?.text = "普通会员"
            } else {
                level?.text = "未知用户"
            }

            checkBox?.isChecked = model?.isSelected!!
            checkBox?.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->
                model.isSelected = b
            })

        }
    }
}