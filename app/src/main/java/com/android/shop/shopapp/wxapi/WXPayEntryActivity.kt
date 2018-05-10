package com.android.shop.shopapp.wxapi


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import com.android.shop.shopapp.R
import com.android.shop.shopapp.pay.Constants
import com.android.shop.shopapp.pay.PayActivity
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    private var api: IWXAPI? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_result)

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID)
        api!!.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {}

    override fun onResp(resp: BaseResp) {

        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                //付款成功
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show()
                if (PayActivity.ordertype != null && PayActivity.ordertype == "1") {
                    // 从结算界面过来的主订单
                    PayActivity.isMainPay = 0
                    finish()
                } else {
                    //从订单界面过来的
                    PayActivity.isMainPay = 1
//                    OrderStateFragment.isPay = true
                    finish()
                }
            } else {
                //付款失败
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show()
                if (PayActivity.ordertype != null && PayActivity.ordertype == "1") {
                    // 从结算界面过来的主订单
                    PayActivity.isMainPay = 3
                } else {
                    PayActivity.isMainPay = 2
                }
                finish()
            }
        }
    }

    companion object {

        private val TAG = "MicroMsg.SDKSample.WXPayEntryActivity"
    }
}