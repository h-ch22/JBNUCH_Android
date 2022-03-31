package kr.ac.jbnu.ch.notice.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.helper.AffiliateHelper
import kr.ac.jbnu.ch.databinding.LayoutNoticeBinding
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.AdminCodeModel
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel

class NoticeView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutNoticeBinding = DataBindingUtil.inflate(inflater , R.layout.layout_notice , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        if(UserManagement.userInfo?.admin == AdminCodeModel.CH_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_PRD_VicePresident
            || UserManagement.userInfo?.admin == AdminCodeModel.CHE_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CHE_PRD_VicePresident
            || UserManagement.userInfo?.admin == AdminCodeModel.COH_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.COH_PRD_VicePresident
            || UserManagement.userInfo?.admin == AdminCodeModel.COM_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.COM_PRD_VicePresident
            || UserManagement.userInfo?.admin == AdminCodeModel.CON_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CON_PRD_VicePresident
            || UserManagement.userInfo?.admin == AdminCodeModel.SOC_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.SOC_PRD_VicePresident){
            layout.btnAddNotice.visibility = View.VISIBLE
        }

        else{
            layout.btnAddNotice.visibility = View.GONE
        }

        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
        transaction.addToBackStack(null)

        transaction.replace(R.id.noticeViewArea, NoticeListView())
        transaction.commit()

        layout.noticeTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {
                        if(UserManagement.userInfo?.admin == AdminCodeModel.CH_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.CHE_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CHE_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.COH_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.COH_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.COM_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.COM_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.CON_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CON_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.SOC_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.SOC_PRD_VicePresident){
                            layout.btnAddNotice.visibility = View.VISIBLE
                        }

                        else{
                            layout.btnAddNotice.visibility = View.GONE
                        }

                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                        transaction.addToBackStack(null)

                        transaction.replace(R.id.noticeViewArea, NoticeListView())
                        transaction.commit()
                    }

                    1 -> {
                        layout.btnAddNotice.visibility = View.GONE

                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                        transaction.addToBackStack(null)

                        transaction.replace(R.id.noticeViewArea, InternalNoticeView())
                        transaction.commit()
                    }

                    else -> {
                        if(UserManagement.userInfo?.admin == AdminCodeModel.CH_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.CHE_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CHE_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.COH_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.COH_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.COM_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.COM_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.CON_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CON_PRD_VicePresident
                            || UserManagement.userInfo?.admin == AdminCodeModel.SOC_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.SOC_PRD_VicePresident){
                            layout.btnAddNotice.visibility = View.VISIBLE
                        }
                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                        transaction.addToBackStack(null)

                        transaction.replace(R.id.noticeViewArea, NoticeListView())
                        transaction.commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_addNotice -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, AddNoticeView())
                transaction.commit()
            }
        }
    }
}