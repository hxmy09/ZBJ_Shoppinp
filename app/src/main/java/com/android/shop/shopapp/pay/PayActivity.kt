package com.android.shop.shopapp.pay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.network.services.PayParameter
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.pay_order_info.*

/**
 * Created by myron on 5/8/18.
 */

class PayActivity : AppCompatActivity() {
    lateinit var orderNumber: String
    lateinit var payAmount: String

    companion object {

        const val successed = 1
        const val failed = 2
        const val unknow = -1

        var isMainPay = unknow    //1付款成功,2失败 ,-1)
    }

    private var api: IWXAPI? = null
    val mCompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_order_info)
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID)

        useraddress.text = (application as ShopApplication).sharedPreferences?.getString("address", "")
        username.text = (application as ShopApplication).sharedPreferences?.getString("phone", "")
        userphone.text = (application as ShopApplication).sharedPreferences?.getString("userName", "")
        payAmount = intent.getStringExtra("payAmount")
        orderNumber = intent.getStringExtra("order_number")

        totalMoney.text = payAmount
        btnSubmit.setOnClickListener {
            if (isSupportPay()) {
                getPayInfo()
            } else {
                Toast.makeText(this@PayActivity, "您的设备不支持支付", Toast.LENGTH_SHORT).show()
                finish()
            }
            btnSubmit.isEnabled = false
            btnSubmit.setBackgroundResource(android.R.color.darker_gray)
        }
        api?.registerApp(Constants.APP_ID)
    }

    fun getPayInfo() {

        val payService = RetrofitHelper().getAppPay()

        var p = PayParameter(device_info = "", detail = "", out_trade_no = orderNumber,order_number = "")
        mCompositeDisposable.add(payService.getApppayInfo(p)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    val req = PayReq()
                    req.appId = t.data.appid
                    req.partnerId = t.data.partnerid
                    req.prepayId = t.data.prepayid
                    req.nonceStr = t.data.noncestr
                    req.timeStamp = t.data.timestamp
                    req.packageValue = t.data.pack
                    req.sign = t.data.sign
                    //  req.extData = "app data" // optional
                    api?.sendReq(req)
                }, { _ ->
                    MaterialDialog.Builder(this@PayActivity)
                            .content("支付失败")
                            .positiveText("确定")//.onPositive { _, _ -> finish() }
                            .show()
                    enableSubmitBtn()
                }))
    }

    fun queryPayResult() {
        val pay = RetrofitHelper().getAppPay()
        mCompositeDisposable.add(pay.queryPayResult(PayParameter(device_info = "", detail = "",out_trade_no = "", order_number = orderNumber
                ?: ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {
                        MaterialDialog.Builder(this@PayActivity)
                                .content("支付成功")
                                .positiveText("确定").onPositive { _, _ ->
                                    setResult(Activity.RESULT_OK)
                                    finish()
                                }.cancelable(false)
                                .show()
                    } else {
                        MaterialDialog.Builder(this@PayActivity)
                                .content("支付失败")
                                .positiveText("确定")//.onPositive { _, _ -> finish() }
                                .show()
                        enableSubmitBtn()
                    }

                }, { e ->
                    MaterialDialog.Builder(this@PayActivity)
                            .content("支付失败")
                            .positiveText("确定")//.onPositive { _, _ -> finish() }
                            .show()
                    enableSubmitBtn()
                }))
    }

    override fun onResume() {
        super.onResume()
        // 从结算界面过去的微信支付
        if (isMainPay == successed) {
            //付款成功
            queryPayResult()
        } else if (isMainPay == failed) {
            MaterialDialog.Builder(this@PayActivity)
                    .content("支付失败")
                    .positiveText("确定")//.onPositive { _, _ -> finish() }
                    .show()
            enableSubmitBtn()
        }
        isMainPay = unknow
    }


    fun enableSubmitBtn()
    {
        btnSubmit.isEnabled = true
        btnSubmit.setBackgroundResource(R.color.primaryColor)
    }
    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }

    private fun isSupportPay(): Boolean {
        return api!!.wxAppSupportAPI >= Build.PAY_SUPPORTED_SDK_INT
    }
}