package com.android.shop.shopapp.activity

import android.os.Bundle
import com.android.shop.shopapp.model.request.RegisterRequest
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @author a488606
 * @since 3/22/18
 */
class UpdateInfoActivity : RegisterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnLogin.text = "更新"

    }
    //0 注册用户，1 更新用户
    override fun setSumitType(request: RegisterRequest) {
        request.submitType = 1
    }
}