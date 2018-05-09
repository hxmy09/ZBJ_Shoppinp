package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.shop.shopapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_adv.*


/**
 * Created by myron on 3/29/18.
 */

class ProductImagesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_top_imgs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var list = arguments.getStringArrayList("IMAGESURLS")
        val imageList = arrayListOf<ProductImagesFragment.ImageSlider>()
        list?.let {
            for (url in list) {
                imageList.add(ImageSlider(url, url))
            }
        }
        viewPager.adapter = ImageSliderAdapter(activity, imageList)

        indicator.setViewPager(viewPager)
    }

    class ImageSliderAdapter(private val mContext: Context, private val imageList: List<ImageSlider>) : PagerAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(mContext)

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val imageLayout = inflater.inflate(R.layout.slider_home, collection, false) as ViewGroup
            Picasso.get().load(imageList[position].url).into((imageLayout.findViewById<View>(R.id.imageView) as ImageView))
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


    }

    inner class ImageSlider(val name: String, //optional @DrawableRes
                            val url: String) {

        override fun toString(): String {
            return name
        }

    }

}

