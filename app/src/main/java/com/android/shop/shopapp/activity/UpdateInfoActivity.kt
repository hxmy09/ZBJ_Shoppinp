package com.android.shop.shopapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.RegisterRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @author a488606
 * @since 3/22/18
 */
class UpdateInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_pwd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        stopAnim()
        btnLogin.setOnClickListener {
            //login
            startAnim()
            if (edit_pwd.text != edit_repwd.text) {
                Toast.makeText(this@UpdateInfoActivity, "密码不匹配", Toast.LENGTH_LONG).show()
            }
            if (edit_userName.text.isNotEmpty() && edit_pwd.text.isNotEmpty() && edit_pwd.text.isNotEmpty() && edit_address.text.isNotEmpty() && edit_phone.text.isNotEmpty()) {

                var request = RegisterRequest()
                request.userName = edit_userName.text.toString()
                request.password = edit_pwd.text.toString()
                request.address = edit_address.text.toString()

                submit(request)

            } else {
                Toast.makeText(this@UpdateInfoActivity, "输入信息不完整", Toast.LENGTH_SHORT).show();
                stopAnim()
            }
        }
    }

    private fun submit(request: RegisterRequest) {
        val registerService = RetrofitHelper().getRegisterService()
        mCompositeDisposable.add(registerService.register(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    //成功
                    if (t.code == "100") {//101失败
                        var intent = Intent(this@UpdateInfoActivity, LoginActivity::class.java);
                        startActivity(intent)
                        Toast.makeText(this@UpdateInfoActivity, "更改密码成功", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@UpdateInfoActivity, "更改密码失败", Toast.LENGTH_LONG).show()
                    }
                    stopAnim()
                }, {
                    stopAnim()

                }))

    }

    private fun startAnim() {
        avi.show()
        // or avi.smoothToShow();
    }

    private fun stopAnim() {
        avi.hide()
        // or avi.smoothToHide();
    }
}