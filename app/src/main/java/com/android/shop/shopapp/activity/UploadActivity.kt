package com.android.shop.shopapp.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.DBUtil
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.model.KindsModel
import com.android.shop.shopapp.model.network.RetrofitHelper
import com.android.shop.shopapp.model.request.ProductReqeust
import com.android.shop.shopapp.upload.FileResolver
import com.android.shop.shopapp.upload.FileUploaderContract
import com.android.shop.shopapp.upload.FileUploaderModel
import com.android.shop.shopapp.upload.FileUploaderPresenter
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * @author a488606
 * @since 3/22/18
 */

class UploadActivity : BaseActivity(), View.OnClickListener, FileUploaderContract.View {


    var product = ProductReqeust()

    private val mCompositeDisposable = CompositeDisposable()
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
        );

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
        val returnAfterCapture = true
        val isSingleMode = true
        val useCustomImageLoader = false
        val folderMode = true
        val includeVideo = false
        val isExclude = true

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
            stringBuffer.append(images[i].path)//.append("\n")
            i++
        }
      //  Toast.makeText(this@UploadActivity, stringBuffer.toString(), Toast.LENGTH_LONG).show()

        Picasso.with(this@UploadActivity).load(Uri.fromFile(File(stringBuffer.toString()))).into(img)
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
            product.groupName = spinner.text.toString()
            product.price = edit_price.text.toString().toDouble()
            product.desc = edit_desc.text.toString()
            product.groupId = System.currentTimeMillis().toString() //唯一性
            //上传商品信息
            // presenter.onImageSelected(product);
            //将数据保存在本地数据库
            DBUtil(this@UploadActivity).mAppDatabase.productDao().insert(convertFromProductReqeust2ProductModel(product))
            Toast.makeText(this@UploadActivity, "上传成功", Toast.LENGTH_LONG).show()
            finish()
//            var intent = Intent(this@UploadActivity, MineActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun convertFromProductReqeust2ProductModel(request: ProductReqeust): ProductModel {

        var model = ProductModel()
        model.desc = request.desc
        model.groupName = request.groupName
        model.imageUrl = request.img
        model.price = request.price
        model.groupId = request.groupId
        return model
    }

    override fun showThumbnail(selectedImage: String?) {
    }

    override fun showErrorMessage(message: String?) {
        Toast.makeText(this@UploadActivity, "上传失败，请重新上传", Toast.LENGTH_LONG).show()
    }

    override fun uploadCompleted() {
        Toast.makeText(this@UploadActivity, "上传成功", Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun setUploadProgress(progress: Int) {
    }

}