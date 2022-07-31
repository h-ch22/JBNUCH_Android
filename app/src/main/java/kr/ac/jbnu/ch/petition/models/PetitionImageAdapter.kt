package kr.ac.jbnu.ch.petition.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.frameworks.models.GlideApp
import kr.ac.jbnu.ch.notice.helper.NoticeHelper
import kr.ac.jbnu.ch.petition.helper.PetitionHelper

class PetitionImageAdapter(private val context : Context, private val data : PetitionDataModel) : PagerAdapter() {
    private lateinit var inflater : LayoutInflater

    interface OnItemClickListener{
        fun onItemClick(v : View, url : StorageReference)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener){
        this.listener = listener
    }

    override fun getCount(): Int {
        return data.imageIndex
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == `object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.imgview_pager, container, false)
        val imgView : ImageView = view.findViewById(R.id.imgView)

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms( CenterCrop(), RoundedCorners(16))

        GlideApp.with(context)
            .load(PetitionHelper.imgList[position])
            .placeholder(R.drawable.ic_logo_no_slogan)
            .apply(requestOptions)
            .into(imgView)

        val pager = container as ViewPager

        view.setOnClickListener {
            listener?.onItemClick(view, PetitionHelper.imgList[position])
        }

        pager.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View

        vp.removeView(v)
    }
}