package com.android.shop.shopapp.data

import android.content.Context
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
import com.android.shop.shopapp.dao.ShoppingModel
import com.android.shop.shopapp.fragment.ShoppingTrolleyFragment
import com.squareup.picasso.Picasso

/**
 * @author a488606
 * @since 3/20/18
 */

class ShoppingAdapter(var context: Context?, var fragment: ShoppingTrolleyFragment, list: List<ShoppingModel>) : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {

    var contents: List<ShoppingModel> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.shopping_trolley_card_item, parent, false)
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
        var reduce: ImageButton? = null
        var buyAmount: TextView? = null

        init {
            price = itemView.findViewById<TextView>(R.id.price)
            desc = itemView.findViewById<TextView>(R.id.title)
            imageView = itemView.findViewById<AppCompatImageView>(R.id.img)
            checkBox = itemView.findViewById<CheckBox>(R.id.ck)
            add = itemView.findViewById<ImageButton>(R.id.add)
            reduce = itemView.findViewById<ImageButton>(R.id.reduce)
            buyAmount = itemView.findViewById<TextView>(R.id.buyAmount)


        }

        fun bind(model: ShoppingModel?, fragment: ShoppingTrolleyFragment) {
            price?.text = model?.price.toString()
            desc?.text = model?.desc
            buyAmount?.text = model?.amount.toString()
            Picasso.get().load(model?.imageUrl).into(imageView)
            checkBox?.isChecked = model?.isSelected!!
            checkBox?.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->
                model.isSelected = b
                fragment.countTotal(buyAmount?.text.toString().toInt(), model)
            })

            add?.setOnClickListener {
                var amout = buyAmount?.text.toString().toInt()
                buyAmount?.text = (++amout).toString()
                //更改对象的amount 数量
                model.amount = amout
                reduce?.isEnabled = amout > 0
                fragment.countTotal(amout, model)
            }
            reduce?.setOnClickListener {
                var amout = buyAmount?.text.toString().toInt()
                amout--
                if (amout <= 0) {
                    amout = 0
                    reduce?.isEnabled = false
                } else {
                    reduce?.isEnabled = true

                }
                //更改对象的amount 数量
                model.amount = amout
                buyAmount?.text = amout.toString()
                fragment.countTotal(amout, model)
                // total.text = (amout * model!!.price!!).toString()
            }

        }
    }

}