package com.android.shop.shopapp.fragment

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_full_imgs.*


/**
 * Created by myron on 3/29/18.
 */

class FullImageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_full_imgs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val list = activity.intent.extras.getParcelableArrayList<ImageSlider>(EXTRA_IMAGEURL)
        val selected = activity.intent.extras.getInt(EXTRA_SELECTED_IMAGE, 0)

//        val imageList = arrayListOf<ImageSlider>()
//        list?.let {
//            for (slide in list) {
//                imageList.add(ImageSlider(slide.url, slide.url))
//            }
//        }
        viewPager.adapter = ImageSliderAdapter(activity, list)

        indicator.setViewPager(viewPager)
        viewPager.currentItem = selected

    }

    class ImageSliderAdapter(private val mContext: Context, private val imageList: ArrayList<ImageSlider>) : PagerAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(mContext)

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val imageLayout = inflater.inflate(R.layout.slider_home, collection, false) as ViewGroup
            Picasso.get().load(imageList[position].url).into((imageLayout.findViewById<View>(R.id.imageView) as ImageView))
            collection.addView(imageLayout)
//            imageLayout.setOnClickListener {
//                val intent = Intent(mContext, ShowImageActivity::class.java).apply {
//                    this.putExtra(EXTRA_IMAGEURL, imageList)
//                    this.putExtra(EXTRA_SELECTED_IMAGE, imageList[position].url)
//                }
//                mContext.startActivity(intent)
//            }
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

