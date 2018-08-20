package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.util.FileUtil
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_adv.*


/**
 * Created by myron on 3/29/18.
 */

class AdvFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_adv, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewPager.adapter = ImageSliderAdapter(activity, getImageList())

        indicator.setViewPager(viewPager)
    }

    class ImageSliderAdapter(private val mContext: Context, private val imageList: List<ImageSlider>) : PagerAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(mContext)

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val imageLayout = inflater.inflate(R.layout.slider_home, collection, false) as ViewGroup
            Picasso.get().load(imageList[position].resId).into((imageLayout.findViewById<View>(R.id.imageView) as ImageView))

//            compressImage(imageLayout)
            collection.addView(imageLayout)
            return imageLayout
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return imageList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return imageList[position].name
        }

        fun compressImage(view: View) {

            // Compress image in main thread
            //compressedImage = news1 Compressor(this).compressToFile(actualImage);
            //setCompressedImage();

            // Compress image to bitmap in main thread
            //compressedImageView.setImageBitmap(news1 Compressor(this).compressToBitmap(actualImage));
            // Compress image using RxJava in background thread
            Compressor(mContext)
                    .compressToFileAsFlowable(FileUtil.from(mContext, Uri.parse("android.resource://com.android.shop.shopapp/drawable/adv0")))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //                            compressedImage = file
                        //                            setCompressedImage()
                        (view.findViewById<View>(R.id.imageView) as ImageView).setImageBitmap(BitmapFactory.decodeFile(it.getAbsolutePath()))
                    }, {
                        //                            throwable.printStackTrace()
                        //                            showError(throwable.message)
                    })
        }

        private fun setCompressedImage() {

        }


    }

    inner class ImageSlider(val name: String, //optional @DrawableRes
                            @field:DrawableRes
                            val resId: Int) {

        override fun toString(): String {
            return name
        }

    }

    fun getImageList(): List<ImageSlider> {
        val imageList = arrayListOf<ImageSlider>()
        if(pageIndicator == PageIndicator.HOME_PAGE) {
            imageList.add(ImageSlider("adv1", R.drawable.banner3))
            imageList.add(ImageSlider("adv2", R.drawable.banner2))
            imageList.add(ImageSlider("adv3", R.drawable.banner1))
        }else if(pageIndicator == PageIndicator.PRODUCT_SHOW_PAGE)
        {
            imageList.add(ImageSlider("adv4", R.drawable.banner4))
            imageList.add(ImageSlider("adv5", R.drawable.banner5))
            imageList.add(ImageSlider("adv6", R.drawable.banner6))
        }else if(pageIndicator == PageIndicator.SEARCH_PAGE)
        {
            imageList.add(ImageSlider("adv4", R.drawable.banner7))
            imageList.add(ImageSlider("adv5", R.drawable.banner8))
            imageList.add(ImageSlider("adv6", R.drawable.banner9))
        }
        return imageList
    }
}
enum class PageIndicator(indicator:Int){
    HOME_PAGE(1),
    PRODUCT_SHOW_PAGE(2),
    SEARCH_PAGE(3)

}

var pageIndicator:PageIndicator = PageIndicator.HOME_PAGE

