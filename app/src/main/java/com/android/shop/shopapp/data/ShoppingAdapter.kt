package com.android.shop.shopapp.data

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.shop.shopapp.R
import com.android.shop.shopapp.fragment.ShoppingTrolleyFragment
import com.android.shop.shopapp.model.ShoppingModel
import com.squareup.picasso.Picasso

/**
 * @author a488606
 * @since 3/20/18
 */

class ShoppingAdapter(var context: Context?, var fragment: ShoppingTrolleyFragment, list: MutableList<ShoppingModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contents: MutableList<ShoppingModel> = list


    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.shopping_trolley_card_item, parent, false)
        return ViewHolder(view!!)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ShoppingAdapter.ViewHolder).bind(contents.get(position), fragment)

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
        var buyAmount: EditText? = null
        var sizeView: TextView? = null
        var colorView: TextView? = null

        init {
            price = itemView.findViewById<TextView>(R.id.price)
            desc = itemView.findViewById<TextView>(R.id.desc)
            imageView = itemView.findViewById<AppCompatImageView>(R.id.img)
            checkBox = itemView.findViewById<CheckBox>(R.id.ck)
            add = itemView.findViewById<ImageButton>(R.id.add)
            reduce = itemView.findViewById<ImageButton>(R.id.reduce)
            buyAmount = itemView.findViewById<EditText>(R.id.buyAmount)
            sizeView = itemView.findViewById<TextView>(R.id.size)
            colorView = itemView.findViewById<TextView>(R.id.color)


        }

        fun bind(model: ShoppingModel?, fragment: ShoppingTrolleyFragment) {
            price?.text = model?.price.toString()
            desc?.text = model?.desc
            buyAmount?.setText(model?.orderAmount.toString())
            Picasso.get().load(model?.imageUrl).into(imageView)
            sizeView?.text = model?.size
            colorView?.text = model?.color
            checkBox?.isChecked = model?.isSelected!!
            checkBox?.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->
                model.isSelected = b
                fragment.countTotal(buyAmount?.text.toString().toInt(), model)
            })

            add?.setOnClickListener {
                var amout = buyAmount?.text.toString().toInt()
                buyAmount?.setText(if (TextUtils.isEmpty((++amout).toString())) "0" else (++amout).toString())
                //更改对象的amount 数量
                model.orderAmount = amout
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
                model.orderAmount = amout
                buyAmount?.setText(if (TextUtils.isEmpty(amout.toString())) "0" else amout.toString())
                fragment.countTotal(amout, model)
                // total.text = (amout * model!!.price!!).toString()
            }

//            buyAmount.setOnEditorActionListener(object: TextView.OnEditorActionListener {
//                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
//                }
//
//            })
            buyAmount?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length != 0) {
                        model.orderAmount = s.toString().toInt()
                        fragment.countTotal(s.toString().toInt(), model)
                    } else {
                        buyAmount?.setText("0")
                        fragment.countTotal(0, model)
                    }

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //fragment.countTotal(s.toString().toInt(), model)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    //fragment.countTotal(s.toString().toInt(), model)
                }
            })

        }
    }
}