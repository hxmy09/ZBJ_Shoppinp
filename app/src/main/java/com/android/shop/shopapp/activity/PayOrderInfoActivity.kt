package com.android.shop.shopapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.model.ShoppingModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.response.ProductOrder
import com.android.shop.shopapp.network.services.PayParameter
import com.android.shop.shopapp.pay.PayActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.pay_order_info.*
import shopping.hxmy.com.shopping.util.MSG_CODE_REFRESH

/**
 * Created by myron on 5/10/18.
 */
class PayOrderInfoActivity : BaseActivity() {

    var orderNumber: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_order_info)

        useraddress.text = (application as ShopApplication).sharedPreferences?.getString("address", "")
        username.text = (application as ShopApplication).sharedPreferences?.getString("phone", "")
        userphone.text = (application as ShopApplication).sharedPreferences?.getString("userName", "")
        val payAmount = intent.getStringExtra("payAmount")
        orderNumber = intent.getStringExtra("order_number")

        totalMoney.text = payAmount
        btnSubmit.setOnClickListener {
            val intent = Intent(this@PayOrderInfoActivity, PayActivity::class.java).apply {
                putExtra("payAmount", payAmount)
                putExtra("order_number", orderNumber)
            }
            startActivityForResult(intent, 0x11)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 0x11) {
            queryPayResult()
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    fun queryPayResult() {
        val pay = RetrofitHelper().getAppPay()
        mCompositeDisposable.add(pay.queryPayResult(PayParameter(device_info = "", detail = "", order_number = orderNumber
                ?: ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {
                        MaterialDialog.Builder(this@PayOrderInfoActivity)
                                .content("支付成功")
                                .positiveText("确定").onPositive { _, _ -> finish() }
                                .show()
                    } else {
                        MaterialDialog.Builder(this@PayOrderInfoActivity)
                                .content("支付失败")
                                .positiveText("确定").onPositive { _, _ -> finish() }
                                .show()
                    }

                }, { e ->
                    MaterialDialog.Builder(this@PayOrderInfoActivity)
                            .content("支付失败")
                            .positiveText("确定").onPositive { _, _ -> finish() }
                            .show()
                }))
    }
}