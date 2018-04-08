package com.android.shop.shopapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.LoginRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*


/**
 * @author a488606
 * @since 3/22/18
 */
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var loggedIn = (application as ShopApplication).sharedPreferences?.getBoolean("loggedin", false) as Boolean
        if (loggedIn) {

            var intent = Intent(this@LoginActivity, MainActivity::class.java);
            startActivity(intent)
            finish()
            return;
        }
        stopAnim()
        forgetPwd.setOnClickListener {
            //            var intent = Intent(this@LoginActivity, UpdateInfoActivity::class.java);
//            startActivity(intent)
            Toast.makeText(this@LoginActivity, "请联系管理员", Toast.LENGTH_LONG).show()
        }
        register.setOnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java);
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            //login
            startAnim()
            if (edit_userName.text.isNotEmpty() && edit_pwd.text.isNotEmpty()) {
                val loginService = RetrofitHelper().getLoginService()
                var request = LoginRequest()
                request.userName = edit_userName.text.toString()
                request.password = edit_pwd.text.toString()
                mCompositeDisposable.add(loginService.login(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ t ->
                            //成功
                            if (t.code == "100") {//101失败

                                //保存登录状态
                                (application as ShopApplication).sharedPreferences?.edit()?.putBoolean("loggedin", true)?.apply()
                                (application as ShopApplication).sharedPreferences?.edit()?.putString("address", t.data?.address)?.apply()
                                (application as ShopApplication).sharedPreferences?.edit()?.putString("phone", t.data?.phone)?.apply()
                                (application as ShopApplication).sharedPreferences?.edit()?.putString("userName", t.data?.userName)?.apply()
                                (application as ShopApplication).sharedPreferences?.edit()?.putInt("userState", t.data?.userState!!)?.apply() //1管理员，0 普通客户
                                var intent = Intent(this@LoginActivity, MainActivity::class.java);
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "用户名，密码输入错误", Toast.LENGTH_LONG).show()
                            }
                            stopAnim()
                        }, {

                            //TODO  need to remove
//                            var intent = Intent(this@LoginActivity, MainActivity::class.java);
//                            startActivity(intent)
                            (application as ShopApplication).sharedPreferences?.edit()?.clear() //1管理员，0 普通客户
//                            finish()
                            Toast.makeText(this@LoginActivity, "用户名，密码输入错误", Toast.LENGTH_LONG).show()
                            stopAnim()

                        }))

            } else {
                Toast.makeText(this@LoginActivity, "用户名，密码输入错误", Toast.LENGTH_SHORT).show();
                stopAnim()
            }
        }
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