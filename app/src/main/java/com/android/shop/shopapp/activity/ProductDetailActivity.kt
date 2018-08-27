package com.android.shop.shopapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.xm.weidongjian.popuphelper.PopupWindowHelper
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.fragment.ProductImagesFragment
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.OrderModel
import com.android.shop.shopapp.network.services.ProductParameterRequest
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.product_detal.*
import java.util.*

/**
 * Created by myron on 3/31/18.
 */
class ProductDetailActivity : BaseActivity(), AttributeEvent {
    lateinit var model: ProductModel

    lateinit var defaultUrl: String
    lateinit var popupWindowHelper: PopupWindowHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detal)
        model = intent.getParcelableExtra<ProductModel>("Details")


//        model.imageUrls?.let {
//            defaultUrl = model.imageUrls!![0]
//        }

        price.text = model.price?.toString()
        desc.text = model.desc
        //抓取产品详细信息
        fetchData()
    }


    private fun generateData() {
        var details = model.details
        details?.let {
            for (detail in it) {

                var img = detail.img ?: ""
                var color = detail.color ?: "默认"
                mapColors[color] = img

                var store = detail.store
                store?.let {
                    for (s in it) {
                        var size = s.size ?: "默认"
                        var amount = s.amount ?: 0
                        //添加到集合里
                        mapSizes[size] = amount
                    }
                }
            }
        }
    }


    //尺寸跟库存一一对应
    var mapSizes: MutableMap<String, Int> = mutableMapOf()

    //颜色和图片一一对应
    var mapColors: MutableMap<String, String> = mutableMapOf()
    var sizeRecyclerView: RecyclerView? = null
    var colorRecyclerView: RecyclerView? = null
    var sizeList: MutableSet<String> = mapSizes.keys
    var colorList: MutableSet<String> = mapColors.keys


    var shppingImage: AppCompatImageView? = null
    private fun initPage() {

        //组合数据
        generateData()
        //加载layout
        var popView = LayoutInflater.from(this).inflate(R.layout.activity_prepare_shopping, null);
        popupWindowHelper = PopupWindowHelper(popView);

        var add = popView.findViewById<ImageButton>(R.id.add)
        var buyAmount = popView.findViewById<EditText>(R.id.buyAmount)
        var reduce = popView.findViewById<ImageButton>(R.id.reduce)
        var ok = popView.findViewById<Button>(R.id.ok)

        popView.findViewById<View>(R.id.closePopup).setOnClickListener { popupWindowHelper.dismiss() }
        //设置基本属性
        var imgView = popView.findViewById<AppCompatImageView>(R.id.img)
        var priceView = popView.findViewById<TextView>(R.id.price)
        var descView = popView.findViewById<TextView>(R.id.desc)
        shppingImage = imgView
//        model.details!![0].img

        Picasso.get().load(model.details!![0].img).into(imgView)

        var bundle = Bundle()
//        if (!TextUtils.isEmpty(model.imageUrl)) {
//            var imgurls = arrayListOf<String>()
//            imgurls.add(model.imageUrl!!)
//            bundle.putStringArrayList("IMAGESURLS", imgurls)
//            defaultUrl = model.imageUrl!!
//        } else {
//            bundle.putStringArrayList("IMAGESURLS", model.imageUrls as ArrayList<String>)
//            defaultUrl = model.imageUrls!![0]
//        }

        var tempList: ArrayList<String>? = arrayListOf()
        model.details!!.forEach {
            var detail = it
            tempList.also {
                it!!.add(detail.img!!)
            }
        }
        model.imageUrls = tempList
        bundle.putStringArrayList("IMAGESURLS", tempList)
        var imagesFragment = ProductImagesFragment()
        imagesFragment.arguments = bundle
        fragmentManager.beginTransaction().add(R.id.imgsContainer, imagesFragment, "IMAGES").commit()




        priceView.text = model.price?.toString()
        descView.text = model.desc
        minOrder.text = model.minOrder ?: "1"
        buyAmount?.setText(model.minOrder ?: "1")

        add.setOnClickListener {
            var amout = buyAmount.text.toString().toInt()
            buyAmount.setText((++amout).toString())
            reduce.isEnabled = amout > model.minOrder?.toInt() ?: 0
        }

        reduce.setOnClickListener {
            var amout = buyAmount.text.toString().toInt()
            amout--
            if (amout <= model.minOrder?.toInt() ?: 0) {
                amout = model.minOrder?.toInt() ?: 0
                reduce.isEnabled = false
            } else {
                reduce.isEnabled = true

            }
//            buyAmount.setText(amout.toString())
            buyAmount?.setText(if (TextUtils.isEmpty(amout.toString())) model.minOrder else amout.toString())
        }

        buyAmount?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length != 0) {
                    model.orderAmount = if (s.toString().toInt() <= model.minOrder?.toInt() ?: 0) {
                        model.minOrder?.toInt()
                    } else {
                        s.toString().toInt()
                    }

                } else {
                    buyAmount?.setText(model.minOrder?.toInt() ?: 0)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //fragment.countTotal(s.toString().toInt(), model)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //fragment.countTotal(s.toString().toInt(), model)
            }
        })
        //加入购物车
        ok.setOnClickListener {
            var seller = model.userName
            var buyer = (application as ShopApplication).sharedPreferences?.getString("userName", "")
            var productId = model.productId
            var productState = 0
            var color = colorList.elementAt(selectedColor)
            var size = sizeList.elementAt(selectedSize)
            var stock = mapSizes[size]
            var orderAmount = buyAmount.text.toString().toInt()
            var orderTime = ""
            var orderNumber = ""
            var imageUrl: String? = null
            model.imageUrls?.let {
                imageUrl = model.imageUrls!![selectedColor]
            }

            var orderModel = OrderModel(seller, buyer, productId, productState, color, size, orderAmount, orderTime, orderNumber, imageUrl, model.desc, model.price, model.minOrder
                    ?: "")

            val detailService = RetrofitHelper().getProductDetailService()
            mCompositeDisposable.add(detailService.addShoppingTrolley(orderModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        if (t.code == "100") {
                            Toast.makeText(this@ProductDetailActivity, "成功添加商品到购物车", Toast.LENGTH_LONG).show()
                            popupWindowHelper.dismiss()
                        } else {
                            Toast.makeText(this@ProductDetailActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                        }

                    }, { e ->
                        Toast.makeText(this@ProductDetailActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                    }))


        }
        result.setOnClickListener {
            popupWindowHelper.showFromBottom(group_item)
        }

        mine.setOnClickListener {
            //进入购物车页面
            var intent = Intent(this@ProductDetailActivity, MainActivity::class.java).apply {
                putExtra("selectedItemId", R.id.navigation_shopping)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            }
            startActivity(intent)
        }


        sizeRecyclerView = popView.findViewById<RecyclerView>(R.id.sizeRecyclerView)
        colorRecyclerView = popView.findViewById<RecyclerView>(R.id.colorRecyclerView)

        //加载页面数据
        sizeRecyclerView!!.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = AttributeAdapter(this@ProductDetailActivity, list = sizeList, events = this@ProductDetailActivity, type = AttributeType.SIZE, selectedColor = selectedColor, selectedSize = selectedSize)
        }

        //加载页面数据
        colorRecyclerView!!.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = AttributeAdapter(this@ProductDetailActivity, colorList, events = this@ProductDetailActivity, type = AttributeType.COLOR, selectedColor = selectedColor, selectedSize = selectedSize)
        }
    }

    private fun fetchData() {
        val detailService = RetrofitHelper().getProductDetailService()
        var request = ProductParameterRequest()
        request.productId = model.productId
        mCompositeDisposable.add(detailService.getProductDetail(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.code == "100") {
                        if (t.products!!.size > 0) {
                            val temp = model
                            model = t.products?.get(0)!!
//                            model.imageUrls = temp.imageUrls
                            initPage()
                        }

                    } else {
                        Toast.makeText(this@ProductDetailActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                    }

                }, { e ->
                    Toast.makeText(this@ProductDetailActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                }))
    }


    private var selectedColor = 0
    private var selectedSize = 0

    override fun onSizeClick(value: String, position: Int) {
        selectedSize = position
        sizeRecyclerView?.adapter = AttributeAdapter(this@ProductDetailActivity, list = sizeList, events = this@ProductDetailActivity, type = AttributeType.SIZE, selectedColor = selectedColor, selectedSize = selectedSize)
        sizeRecyclerView?.adapter?.notifyDataSetChanged()
    }

    override fun onColorClick(value: String, position: Int) {
        selectedColor = position
        Picasso.get().load(model.details!![selectedColor].img).into(shppingImage)
        colorRecyclerView?.adapter = AttributeAdapter(this@ProductDetailActivity, colorList, events = this@ProductDetailActivity, type = AttributeType.COLOR, selectedColor = selectedColor, selectedSize = selectedSize)
        colorRecyclerView?.adapter?.notifyDataSetChanged()

    }

}

interface AttributeEvent {
    fun onSizeClick(value: String, position: Int)
    fun onColorClick(value: String, position: Int)
}

enum class AttributeType {
    COLOR, SIZE
}

class AttributeAdapter(var context: Context?, list: MutableSet<String>, var events: AttributeEvent, var type: AttributeType, var selectedColor: Int, var selectedSize: Int) : RecyclerView.Adapter<AttributeAdapter.ViewHolder>() {

    var contents: MutableSet<String> = list

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        @LayoutRes var resId = when (type) {
            AttributeType.COLOR -> {
                R.layout.attribute_item
            }
            AttributeType.SIZE -> {

                R.layout.attribute_item
            }
        }
        var view = LayoutInflater.from(parent.context)
                .inflate(resId, parent, false)
        return ViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(contents.elementAt(position), events, type, selectedColor, selectedSize)
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var attribute: TextView? = null

        init {
            attribute = itemView.findViewById<TextView>(R.id.attribute)
        }

        fun bind(model: String?, events: AttributeEvent, type: AttributeType, selectedColor: Int, selectedSize: Int) {
            itemView.setOnClickListener {
                when (type) {
                    AttributeType.COLOR -> {
                        events.onColorClick(model!!, position = adapterPosition)
                    }
                    AttributeType.SIZE -> {
                        events.onSizeClick(model!!, position = adapterPosition)
                    }
                }
            }
            attribute?.text = model
            when (type) {
                AttributeType.COLOR -> {
                    if (adapterPosition == selectedColor) {
                        attribute?.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primaryColor))
                    } else {
                        attribute?.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.secondaryLightColor))
                    }
                }
                AttributeType.SIZE -> {
                    if (adapterPosition == selectedSize) {
                        attribute?.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primaryColor))
                    } else {
                        attribute?.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.secondaryLightColor))
                    }
                }
            }
        }
    }

}