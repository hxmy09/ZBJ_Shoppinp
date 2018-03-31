package com.android.shop.shopapp.activity

import android.os.Bundle
import com.android.shop.shopapp.R
import kotlinx.android.synthetic.main.activity_code_generate.*

/**
 * Created by myron on 3/25/18.
 */
class CodeGenerateActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_generate)

        var result = getRandomString(6)
        code.text = result
    }

    fun getRandomString(length: Int): String {
        //随机字符串的随机字符库
        val KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val sb = StringBuffer()
        val len = KeyString.length
        for (i in 0 until length) {
            sb.append(KeyString[Math.round(Math.random() * (len - 1)).toInt()])
        }
        return sb.toString()
    }

}