package kr.ac.jbnu.ch.affiliates.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.helper.AffiliateHelper
import kr.ac.jbnu.ch.frameworks.models.GlideApp

class AdPagerAdapter(val context : Context, val viewList : ArrayList<View>) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imgView : ImageView = viewList.get(position).findViewById(R.id.ad_img)

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms( CenterCrop(), RoundedCorners(16))

        GlideApp.with(context)
            .load(AffiliateHelper.adList[position])
            .placeholder(R.drawable.ic_logo_no_slogan)
            .apply(requestOptions)
            .into(imgView)

        container.addView(viewList[position])

        return viewList[position]
    }



    override fun getCount(): Int {
        return viewList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == `object` as View)
    }

}