package com.android.shop.shopapp.model.response

import com.android.shop.shopapp.dao.ProductModel
import com.google.gson.annotations.SerializedName

/**
 * @author a488606
 * @since 4/3/18
 */

class ProductsResponse {
    var code: String? = null
    @SerializedName("data")
    var products: ArrayList<ProductModel>? = null
}