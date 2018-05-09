package com.android.shop.shopapp.model.response

import android.os.Parcel
import android.os.Parcelable
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.ShoppingModel
import com.google.gson.annotations.SerializedName

/**
 * @author a488606
 * @since 4/3/18
 */

open class ProductsResponse {
    var code: String? = null
    @SerializedName("data")
    var products: ArrayList<ProductModel>? = null
    @SerializedName("total_count")
    var totalCount: Int? = null
}

class ProductDetailResponse : ProductsResponse() {
//    var code: String? = null
//    @SerializedName("data")
//    var product: ProductModel? = null
}


data class ShoppingResponse(
        //这个是展示购物车时候，使用
        @SerializedName("data")
        var products: MutableList<ShoppingModel>? = null,
        var code: String? = null,
        //返回所有订单信息
//        var orders: MutableList<ProductOrder>,
        @SerializedName("total_count")
        var totalCount: Int? = null
)

data class OrderResponse(
//        //这个是展示购物车时候，使用
//        @SerializedName("data")
//        var products: MutableList<ShoppingModel>? = null,
        var code: String? = null,
        //返回所有订单信息
        @SerializedName("data")
        var orders: MutableList<ProductOrder>,
        @SerializedName("total_count")
        var totalCount: Int? = null
)


data class ProductOrder(
        @SerializedName("data")
        var products: List<ShoppingModel>? = null,
        @SerializedName("order_number")
        var orderNumber: String? = null,
        var total: Double? = null


) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(ShoppingModel),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(products)
        parcel.writeString(orderNumber)
        parcel.writeValue(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductOrder> {
        override fun createFromParcel(parcel: Parcel): ProductOrder {
            return ProductOrder(parcel)
        }

        override fun newArray(size: Int): Array<ProductOrder?> {
            return arrayOfNulls(size)
        }
    }

}