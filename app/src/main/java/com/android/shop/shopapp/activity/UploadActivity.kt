package com.android.shop.shopapp.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.android.shop.shopapp.R
import com.android.shop.shopapp.ShopApplication
import com.android.shop.shopapp.data.UploadAdapter
import com.android.shop.shopapp.model.Detail
import com.android.shop.shopapp.model.KindsModel
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.Size
import com.android.shop.shopapp.model.network.RetrofitHelper
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
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

/**
 * @author a488606
 * @since 3/22/18
 */

class UploadActivity : BaseActivity(), View.OnClickListener, FileUploaderContract.View {


    private var product = ProductModel()
    //    var addImgsFragment = StaggeredGridLayoutFragment()
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
        add.setOnClickListener(this)

        presenter = FileUploaderPresenter(
                this,
                FileResolver(contentResolver),
                FileUploaderModel(RetrofitHelper().getUploadsImService())
        )
        //隐藏loading
        stopAnim()
        createList()
        uploadHint.setOnClickListener(this)


    }


    private fun initSpinner() {
        //初始化spinner
        val inputStream = assets.open("product_kinds.json") as InputStream
        val json = Gson()
        val kindsModel = json.fromJson(InputStreamReader(inputStream), KindsModel::class.java)
        spinner.setItems(kindsModel.kinds)

    }

    private var images = ArrayList<Image>()

    private fun start() {
        val returnAfterCapture = true
        val isSingleMode = true
        val useCustomImageLoader = false
        val folderMode = true
        val includeVideo = true
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
                .toolbarArrowColor(Color.WHITE) // set toolbar arrow up color
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
        Picasso.get().load(Uri.fromFile(File(stringBuffer.toString()))).into(imgView)
        //设置tag ，在上传数据集合列表里使用
        imgView.tag = stringBuffer.toString()
    }

    private var detailList = arrayListOf<Detail>()

    private fun createList() {

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@UploadActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@UploadActivity, DividerItemDecoration.HORIZONTAL))
            adapter = UploadAdapter(this@UploadActivity, detailList)
        }
    }


    override fun onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear()
        super.onDestroy()
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.btnUpload) {
            if (spinner.text.toString() == "选择商品种类") {
                Toast.makeText(this@UploadActivity, "请选择商品种类", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(edit_price.text)) {
                Toast.makeText(this@UploadActivity, "请输入价格", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(edit_desc.text)) {
                Toast.makeText(this@UploadActivity, "请输入商品描述", Toast.LENGTH_SHORT).show()
            } else if ((recyclerView?.adapter as UploadAdapter).contents.size <= 0) {
                Toast.makeText(this@UploadActivity, "请上传商品图片信息", Toast.LENGTH_SHORT).show()
            } else {

//                for(i in 0..3000) {
//                v?.isEnabled = false
                startAnim()
                product.groupName = spinner.text.toString()//URLEncoder.encode(spinner.text.toString(), "utf-8")
                product.price = edit_price.text.toString().toDouble()
                product.desc = edit_desc.text.toString()//URLEncoder.encode(edit_desc.text.toString(), "utf-8")
//                product.productId = System.currentTimeMillis().toString()
                product.userName = (application as ShopApplication).sharedPreferences?.getString("userName", "")// URLEncoder.encode((application as ShopApplication).sharedPreferences?.getString("userName", ""), "utf-8")//(application as ShopApplication).sharedPreferences?.getString("userName","")
                product.details = (recyclerView.adapter as UploadAdapter).contents
                product.keyWords = keyword.text.toString()
                //上传商品信息
                presenter.onImageSelected(product, this@UploadActivity)

//                }

            }
        } else if (v?.id == R.id.add) {

            if (imgView.tag == null) {
                Toast.makeText(this@UploadActivity, "请上传一张图片", Toast.LENGTH_SHORT).show()
                return
            }

            try {
                val detail = Detail()
                detail.color = if (colorView.text.toString().isEmpty()) {
                    "默认"
                } else {
                    colorView.text.toString()
                }
                detail.img = imgView.tag as String?

                val sizeList = arrayListOf<Size>()
                val sizeText = sizeView.text.toString()
                if (sizeText.isEmpty()) {
                    val size = Size()
                    size.size = "默认"
                    size.amount = 0
                    sizeList.add(size)
                } else {
                    var formatText = sizeText.replace("，", ",")
                    val sizeArr = formatText.split(",")
                    sizeArr.forEach {

                        if (!TextUtils.isEmpty(it)) {
                            val arr = it.split("/")
                            val size = Size()
                            size.amount = if (arr.size == 2) {
                                arr[1].toInt()
                            } else {
                                0
                            }
                            size.size = arr[0]
                            sizeList.add(size)
                        }

                    }
                }
                detail.store = sizeList
                (recyclerView.adapter as UploadAdapter).contents.add(detail)
                (recyclerView.adapter as UploadAdapter).notifyDataSetChanged()
            } catch (e: Exception) {

                Toast.makeText(this@UploadActivity, "数据输入有问题", Toast.LENGTH_LONG).show()
            }
        } else if (v?.id == R.id.uploadHint) {
            val intent = Intent(this@UploadActivity, UploadHintActivity::class.java)
            startActivity(intent)
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
//                .onPositive { dialog, which ->
//
//                }
//                .onNeutral { dialog, which ->
//                    // TODO
//                }
                .onNegative { _, _ ->
                    finish()
                }
//                .onAny { dialog, which ->
//                    // TODO
//                }
                .show()

        stopAnim()
    }

    override fun setUploadProgress(progress: Int) {
    }

    private fun startAnim() {
        avi.show();
    }

    private fun stopAnim() {
        avi.hide();
    }


}