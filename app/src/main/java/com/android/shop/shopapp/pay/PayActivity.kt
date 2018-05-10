package com.android.shop.shopapp.pay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.network.services.PayParameter
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by myron on 5/8/18.
 */

class PayActivity : AppCompatActivity() {

    companion object {
        var ordertype: String? = "" // 从结算界面过来的(1是主订单)
        var isMainPay = -1    //从结算界面过来的(0从结算付款成功,3从结算过来的失败,2从订单付款失败，1从订单过来的成功)
    }

    private var api: IWXAPI? = null
    val mCompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay)
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID)
        if (isSupportPay()) {
            getPayInfo()
        } else {
            Toast.makeText(this@PayActivity, "您的设备不支持支付", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun getPayInfo() {

        val payAmount = intent.getStringExtra("payAmount")
        val orderNumber = intent.getStringExtra("order_number")
        val payService = RetrofitHelper().getAppPay()

        var p = PayParameter(device_info = "", detail = "", order_number = orderNumber)
        mCompositeDisposable.add(payService.getApppayInfo(p)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    val req = PayReq()
                    //req.appId = Constants.APP_ID; // ������appId
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
                    Toast.makeText(this@PayActivity, "支付失败", Toast.LENGTH_LONG).show()
                }))
    }

    override fun onResume() {
        super.onResume()
        // 从结算界面过去的微信支付
        if (ordertype != null && ordertype == "1") {
            if (isMainPay == 0) {
                //付款成功
                finish()
            } else if (isMainPay == 3) {
                isMainPay = -1
            }
        } else if (isMainPay == 1 || isMainPay == 2) {
            isMainPay = -1
            finish()
        }
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