package kr.ac.jbnu.ch.affiliates.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.rd.PageIndicatorView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.helper.AffiliateHelper
import kr.ac.jbnu.ch.affiliates.models.AdPagerAdapter
import kr.ac.jbnu.ch.databinding.LayoutAffiliatesBinding
import kr.ac.jbnu.ch.userManagement.view.LegacyUserView

class AffiliateView : Fragment(){
    private lateinit var pager : ViewPager
    private var viewList = ArrayList<View>()

    private val helper = AffiliateHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutAffiliatesBinding = DataBindingUtil.inflate(inflater , R.layout.layout_affiliates , container , false)

        layout.lifecycleOwner = this
        layout.view = this

        pager = layout.adView

        viewList.add(inflater.inflate(R.layout.ad_view_model_page1, null))
        viewList.add(inflater.inflate(R.layout.ad_view_model_page2, null))
        viewList.add(inflater.inflate(R.layout.ad_view_model_page3, null))

        helper.getAdList(){
            if(it){
                pager.clipToPadding = false
                pager.setPadding(100, 0, 100, 0)
                pager.pageMargin = 50
                pager.adapter = context?.let { it1 -> AdPagerAdapter(it1, viewList) }
            }
        }

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_all -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, AffiliateListView("All", "전체"))
                transaction.commit()
            }

            R.id.btn_meal -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, AffiliateListView("Meal", "식사"))
                transaction.commit()
            }

            R.id.btn_cafe -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, AffiliateListView("Cafe", "카페"))
                transaction.commit()
            }

            R.id.btn_liquor -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, AffiliateListView("Alcohol","술"))
                transaction.commit()
            }

            R.id.btn_convenience -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, AffiliateListView("Convenience", "편의"))
                transaction.commit()
            }
        }
    }
}