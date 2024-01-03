package kr.ac.jbnu.ch.more.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.view.AffiliateListView
import kr.ac.jbnu.ch.calendar.view.CalendarView
import kr.ac.jbnu.ch.campusMap.view.CampusMapView
import kr.ac.jbnu.ch.databinding.LayoutHomeBinding
import kr.ac.jbnu.ch.databinding.LayoutMoreBinding
import kr.ac.jbnu.ch.feedbackhub.view.FeedbackHubView
import kr.ac.jbnu.ch.frameworks.models.GlideApp
import kr.ac.jbnu.ch.handWriting.view.HandWritingView
import kr.ac.jbnu.ch.meal.view.MealView
import kr.ac.jbnu.ch.petition.view.PetitionListView
import kr.ac.jbnu.ch.pledge.view.PledgeView
import kr.ac.jbnu.ch.products.view.ProductView
import kr.ac.jbnu.ch.sports.view.SportsView
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel
import kr.ac.jbnu.ch.userManagement.view.ProfileView

class MoreView : Fragment() {
    private val userManagement = UserManagement()
    private lateinit var img_Profile : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutMoreBinding = DataBindingUtil.inflate(inflater , R.layout.layout_more , container , false)
        layout.view = this
        layout.lifecycleOwner = this

        layout.txtName.text = UserManagement.userInfo?.name
        layout.txtDept.text = "${UserManagement.userInfo?.college} ${UserManagement.userInfo?.studentNo}"

        if(UserManagement.userInfo?.spot != null){
            layout.txtAdmin.text = UserManagement.userInfo?.spot
            layout.txtAdmin.visibility = View.VISIBLE
        }

//        if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COM || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH){
//            layout.btnStudyRoom.visibility = View.VISIBLE
//            layout.marginStudyRoom.visibility = View.VISIBLE
//        }
//
//        else{
//            layout.btnStudyRoom.visibility = View.GONE
//            layout.marginStudyRoom.visibility = View.GONE
//        }
//
//        if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH){
//            layout.btnLibrary.visibility = View.VISIBLE
//            layout.marginLibrary.visibility = View.VISIBLE
//        }
//
//        else{
//            layout.btnLibrary.visibility = View.GONE
//            layout.marginLibrary.visibility = View.GONE
//        }

        img_Profile = layout.profile

        loadProfile()

        return layout.root
    }

    fun loadProfile(){
        if(UserManagement.userInfo?.profile != null){
            context?.let {
                GlideApp.with(it)
                    .load(UserManagement.userInfo?.profile)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img_Profile)
            }
        }
    }

    fun changeView(v : View){
        when(v.id){
            R.id.btn_myPage -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, ProfileView())
                transaction.commit()
            }

            R.id.btn_introduceStudentCouncil -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, IntroduceView())
                transaction.commit()
            }

            R.id.btn_petition -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, PetitionListView())
                transaction.commit()
            }

            R.id.btn_campusMap -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, CampusMapView())
                transaction.commit()
            }

            R.id.btn_sports -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, SportsView())
                transaction.commit()
            }

            R.id.btn_calendar -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, CalendarView())
                transaction.commit()
            }

//            R.id.btn_meal -> {
//                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
//                transaction.addToBackStack(null)
//
//                transaction.replace(R.id.mainViewArea, MealView())
//                transaction.commit()
//            }

            R.id.btn_handWriting -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, HandWritingView())
                transaction.commit()
            }

            R.id.btn_pledge -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, PledgeView())
                transaction.commit()
            }

//            R.id.btn_studyRoom -> {
//
//            }
//
//            R.id.btn_library -> {
//
//            }
//
//            R.id.btn_store -> {
//
//            }

            R.id.btn_products -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, ProductView())
                transaction.commit()
            }

            R.id.btn_feedbackHub -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, FeedbackHubView())
                transaction.commit()
            }

            R.id.btn_info -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, InfoView())
                transaction.commit()
            }
        }
    }
}