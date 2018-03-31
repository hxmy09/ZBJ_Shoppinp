package com.android.shop.shopapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.RegisterRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @author a488606
 * @since 3/22/18
 */
open class RegisterActivity : BaseActivity() {

    enum class SubmitType {
        REGISTER, UPDATE
    }

    private val mCompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        stopAnim()
        btnLogin.setOnClickListener {
            //login
            startAnim()
            if (edit_pwd.text != edit_repwd.text) {
                Toast.makeText(this@RegisterActivity, "密码不匹配", Toast.LENGTH_LONG).show()
            }
            if (edit_userName.text.isNotEmpty() && edit_pwd.text.isNotEmpty() && code.text.isNotEmpty() && edit_pwd.text.isNotEmpty() && edit_address.text.isNotEmpty() && edit_phone.text.isNotEmpty()) {

                var request = RegisterRequest()
                request.userName = edit_userName.text.toString()
                request.password = edit_pwd.text.toString()
                request.code = code.text.toString()
                request.address = edit_address.text.toString()
                request.repwd = edit_repwd.text.toString()

                submit(request)

            } else {
                Toast.makeText(this@RegisterActivity, "输入信息不完整", Toast.LENGTH_SHORT).show();
                stopAnim()
            }
        }
    }

    private fun submit(request: RegisterRequest) {
        //设置提交类型
        setSumitType(request)
        val registerService = RetrofitHelper().getRegisterService()
        mCompositeDisposable.add(registerService.register(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    //成功
                    if (t.code == "100") {//101失败
                        var intent = Intent(this@RegisterActivity, LoginActivity::class.java);
                        startActivity(intent)
                        Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "邀请码输入错误", Toast.LENGTH_LONG).show()
                    }
                    stopAnim()
                }, { e ->

                    //TODO  need to remove
                    var intent = Intent(this@RegisterActivity, MainActivity::class.java);
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity, "邀请码输入错误", Toast.LENGTH_LONG).show()
                    stopAnim()

                }))

    }

    //0 注册用户，1 更新用户
    open fun setSumitType(request: RegisterRequest) {
        request.submitType = 0
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