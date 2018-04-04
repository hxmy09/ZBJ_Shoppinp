package com.android.shop.shopapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.DBUtil
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.dao.ShoppingModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_detal.*

/**
 * Created by myron on 3/31/18.
 */
class ProductDetailActivity : BaseActivity() {
    lateinit var model: ProductModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detal)


        var model = intent.getParcelableExtra<ProductModel>("Details")

        Picasso.get().load(model?.imageUrl).into(img)
        price.text = model?.price?.toString()
        desc.text = model?.desc
        total.text = price.text
        buyAmount.text = "1" //默认


        add.setOnClickListener {
            var amout = buyAmount.text.toString().toInt()
            buyAmount.text = (++amout).toString()
            reduce.isEnabled = amout > 0
            total.text = (amout * model!!.price!!).toString()
        }
        reduce.setOnClickListener {
            var amout = buyAmount.text.toString().toInt()
            amout--
            if (amout <= 0) {
                amout = 0
                reduce.isEnabled = false
            } else {
                reduce.isEnabled = true

            }
            buyAmount.text = amout.toString()
            total.text = (amout * model!!.price!!).toString()
        }

        result.setOnClickListener {
            if (buyAmount.text.toString().toInt() <= 0) {
                Toast.makeText(this@ProductDetailActivity, "请选择购买数量", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var shoppingModel = ShoppingModel()
            shoppingModel.desc = model.desc
            shoppingModel.groupName = model.groupName
            shoppingModel.imageUrl = model.imageUrl
            shoppingModel.price = model.price
            shoppingModel.amount = buyAmount.text.toString().toInt()
            shoppingModel.productId = model.productId


            var sM = DBUtil(this@ProductDetailActivity).mAppDatabase.shoppingDao().findByProductId(shoppingModel.productId!!)

            if (sM.productId?.isEmpty()!!) {
                DBUtil(this@ProductDetailActivity).mAppDatabase.shoppingDao().insert(shoppingModel)
            }else
            {
                sM.amount = sM.amount!! + shoppingModel.amount!!
                DBUtil(this@ProductDetailActivity).mAppDatabase.shoppingDao().insert(sM)
            }



            Toast.makeText(this@ProductDetailActivity, "加入购物车成功", Toast.LENGTH_SHORT).show()
        }

        mine.setOnClickListener {
            var intent = Intent(this@ProductDetailActivity, MainActivity::class.java).apply {

                putExtra("selectedItemId", R.id.navigation_shopping)
            }
            startActivity(intent)
            finish()
        }

    }
}