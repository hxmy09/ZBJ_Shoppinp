package com.android.shop.shopapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.shop.shopapp.R
import com.android.shop.shopapp.activity.EXTRA_IMAGEURL
import com.android.shop.shopapp.activity.EXTRA_SELECTED_IMAGE
import com.android.shop.shopapp.activity.ShowImageActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_adv.*


/**
 * Created by myron on 3/29/18.
 */

class ProductImagesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_top_imgs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var list = arguments?.getStringArrayList("IMAGESURLS") ?: arrayListOf()
        val imageList = arrayListOf<ImageSlider>()
        list?.let {
            for (url in list) {
                imageList.add(ImageSlider(url, url))
            }
        }
        viewPager.adapter = ImageSliderAdapter(activity!!, imageList)

        indicator.setViewPager(viewPager)

    }

    class ImageSliderAdapter(private val mContext: Context, private val imageList: ArrayList<ImageSlider>) : PagerAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(mContext)

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val imageLayout = inflater.inflate(R.layout.slider_home, collection, false) as ViewGroup
            Picasso.get().load(imageList[position].url).into((imageLayout.findViewById<View>(R.id.imageView) as ImageView))
            collection.addView(imageLayout)
            imageLayout.setOnClickListener {
                val intent = Intent(mContext, ShowImageActivity::class.java).apply {
                    this.putExtra(EXTRA_IMAGEURL, imageList)
                    this.putExtra(EXTRA_SELECTED_IMAGE, position)
                }
                mContext.startActivity(intent)
            }
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


    }


}

class ImageSlider(val name: String, //optional @DrawableRes
                  val url: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun toString(): String {
        return name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageSlider> {
        override fun createFromParcel(parcel: Parcel): ImageSlider {
            return ImageSlider(parcel)
        }

        override fun newArray(size: Int): Array<ImageSlider?> {
            return arrayOfNulls(size)
        }
    }

}