package kr.ac.jbnu.ch.userManagement.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.ac.jbnu.ch.R

class TutorialImageAdapter(private val context : Context) : PagerAdapter() {
    private lateinit var inflater : LayoutInflater

    override fun getCount(): Int {
        return 5
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

        when(position){
            0 -> {
                imgView.setImageResource(R.drawable.tutorialimg_0)
            }

            1 -> {
                imgView.setImageResource(R.drawable.tutorialimg_1)
            }

            2 -> {
                imgView.setImageResource(R.drawable.tutorialimg_2)

            }

            3 -> {
                imgView.setImageResource(R.drawable.tutorialimg_3)

            }

            4 -> {
                imgView.setImageResource(R.drawable.tutorialimg_4)

            }
        }

        val pager = container as ViewPager

        pager.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View

        vp.removeView(v)
    }
}