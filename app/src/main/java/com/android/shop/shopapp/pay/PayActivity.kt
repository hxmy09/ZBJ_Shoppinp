package com.android.shop.shopapp.pay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.model.network.RetrofitHelper
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
        val payService = RetrofitHelper().getAppPay()

        mCompositeDisposable.add(payService.getApppayInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    val req = PayReq()
                    //req.appId = Constants.APP_ID; // ������appId
                    req.appId = t.appid
                    req.partnerId = t.partnerid
                    req.prepayId = t.prepayid
                    req.nonceStr = t.noncestr
                    req.timeStamp = t.timestamp
                    req.packageValue = t.pack
                    req.sign = t.sign
                    req.extData = "app data" // optional
                    api?.sendReq(req)
                }, { _ ->
                    Toast.makeText(this@PayActivity, "请求支付信息失败", Toast.LENGTH_LONG).show()
                }))
    }

    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }

    fun isSupportPay(): Boolean {
        return api!!.wxAppSupportAPI >= Build.PAY_SUPPORTED_SDK_INT
    }
}