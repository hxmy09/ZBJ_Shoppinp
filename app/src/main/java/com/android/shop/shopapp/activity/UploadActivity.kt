package com.android.shop.shopapp.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.fragment.AddImgsFragment
import com.android.shop.shopapp.model.KindsModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.AddImgsRequest
import com.android.shop.shopapp.model.request.ProductReqeust
import com.android.shop.shopapp.upload.FileResolver
import com.android.shop.shopapp.upload.FileUploaderContract
import com.android.shop.shopapp.upload.FileUploaderModel
import com.android.shop.shopapp.upload.FileUploaderPresenter
import com.android.shop.shopapp.util.GrayscaleImageLoader
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_upload.*
import shopping.hxmy.com.shopping.util.compressImg
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URLEncoder
import java.util.*

/**
 * @author a488606
 * @since 3/22/18
 */

class UploadActivity : BaseActivity(), View.OnClickListener, FileUploaderContract.View {


    var product = ProductReqeust()
    var addImgsFragment = AddImgsFragment()
    private lateinit var presenter: FileUploaderPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initSpinner();
        //上传图片事件
        btnUploadImage.setOnClickListener {
            start();
        }
        //保存信息
        btnUpload.setOnClickListener(this)

        presenter = FileUploaderPresenter(
                this,
                FileResolver(contentResolver),
                FileUploaderModel(RetrofitHelper().getUploadsImService())
        )
        //隐藏loading
        stopAnim()
        fragmentManager.beginTransaction().add(R.id.imgsLayout, addImgsFragment, "ADDIMAGES").commit()


    }


    private fun initSpinner() {
        //初始化spinner
        var inputStream = assets.open("product_kinds.json") as InputStream
        var json = Gson()
        var kindsModel = json.fromJson(InputStreamReader(inputStream), KindsModel::class.java)
        spinner.setItems(kindsModel.kinds)

    }

    private var images = ArrayList<Image>()

    private fun start() {
        val returnAfterCapture = false
        val isSingleMode = false
        val useCustomImageLoader = false
        val folderMode = true
        val includeVideo = false
        val isExclude = false

        val imagePicker = ImagePicker.create(this)
//                .language("zh-rCN") // Set image picker language
                .theme(R.style.ImagePickerTheme)
                .returnMode(if (returnAfterCapture)
                    ReturnMode.ALL
                else
                    ReturnMode.NONE) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(folderMode) // set folder mode (false by default)
                .includeVideo(includeVideo) // include video (false by default)
                .toolbarArrowColor(Color.RED) // set toolbar arrow up color
                .toolbarFolderTitle("点击选择图片") // folder selection title
                .toolbarImageTitle("点击选择图片") // image selection title

        if (useCustomImageLoader) {
            imagePicker.imageLoader(GrayscaleImageLoader())
        }

        if (isSingleMode) {
            imagePicker.single()
        } else {
            imagePicker.multi() // multi mode (default mode)
        }

        if (isExclude) {
            imagePicker.exclude(images) // don't show anything on this selected images
        } else {
            imagePicker.origin(images) // original selected images, used in multi mode
        }

        imagePicker.limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .imageFullDirectory(Environment.getExternalStorageDirectory().path) // can be full path
                .start() // start image picker activity with request code
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images = ImagePicker.getImages(data) as ArrayList<Image>
            printImages(images)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun printImages(images: List<Image>?) {
        if (images == null || images.isEmpty()) return

        val stringBuffer = StringBuilder()
        var i = 0
        val l = images.size
        while (i < l) {
            stringBuffer.append(images[i].path).append("\n")
            i++
        }
        //  Toast.makeText(this@UploadActivity, stringBuffer.toString(), Toast.LENGTH_LONG).show()

        var paths = stringBuffer.toString().split("\n")


        Handler().post({
            var imgsList = arrayListOf<AddImgsRequest>()
            for (i in 0 until paths.size) {
                if (paths[i].isNotEmpty()) {
                    var air = AddImgsRequest()
                    air.path = paths[i]
                    imgsList.add(air)
                }

            }
            addImgsFragment.setData(imgsList)
        })


        //TODO  增加多张图片
//        Picasso.get().load(Uri.fromFile(File(stringBuffer.toString()))).into(img)
        product.img = stringBuffer.toString()
    }


    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        if (spinner.text.toString() == "选择商品种类") {
            Toast.makeText(this@UploadActivity, "请选择商品种类", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(edit_price.text)) {
            Toast.makeText(this@UploadActivity, "请输入价格", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(edit_desc.text)) {
            Toast.makeText(this@UploadActivity, "请输入商品描述", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(product.img)) {
            Toast.makeText(this@UploadActivity, "请选择商品图片", Toast.LENGTH_SHORT).show()
        } else {

            v?.isEnabled = false
            startAnim()
            product.groupName = URLEncoder.encode(spinner.text.toString(), "utf-8")
            product.price = edit_price.text.toString().toDouble()
            product.desc = URLEncoder.encode(edit_desc.text.toString(), "utf-8")
            product.productId = System.currentTimeMillis().toString()
            product.userName = URLEncoder.encode((application as ShopApplication).sharedPreferences?.getString("userName", ""), "utf-8")//(application as ShopApplication).sharedPreferences?.getString("userName","")
            //上传商品信息
            presenter.onImageSelected(product, this@UploadActivity)

        }
    }


    override fun showThumbnail(selectedImage: String?) {
    }

    override fun showErrorMessage(message: String?) {
        Toast.makeText(this@UploadActivity, "上传失败，请重新上传", Toast.LENGTH_LONG).show()
        stopAnim()
    }

    override fun uploadCompleted() {

        MaterialDialog.Builder(this)
                .content("上传成功,继续上传？")
                .positiveText("是的")
                .negativeText("关闭")
                .onPositive { dialog, which ->

                }
                .onNeutral { dialog, which ->
                    // TODO
                }
                .onNegative { dialog, which ->
                    finish()
                }
                .onAny { dialog, which ->
                    // TODO
                }.show()
    }

    override fun setUploadProgress(progress: Int) {
    }

    fun startAnim() {
        avi.show();
        // or avi.smoothToShow();
    }

    fun stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }


}