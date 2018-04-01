package com.android.shop.shopapp.model.request

/**
 * state   0 - 超级管理员 1. 普通管理员 2 普通用户
 *
 * 0 - 可以进行所有商品的增删改查
 * 1 - 只可以对自己上传的商品进行增删改查
 * 3 - 只可以浏览。不能进行商品管理操作
 */
data class User(var userName: String?, var password: String?,var state: Int)