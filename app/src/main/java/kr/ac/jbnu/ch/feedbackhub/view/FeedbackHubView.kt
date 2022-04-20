package kr.ac.jbnu.ch.feedbackhub.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.awesomedialog.*
import com.google.android.material.button.MaterialButton
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.view.AffiliateListView
import kr.ac.jbnu.ch.databinding.LayoutFeedbackhubBinding
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubItemModel
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.AdminCodeModel

class FeedbackHubView() : Fragment() {
    var selectedCategory : FeedbackHubItemModel? = null
    private lateinit var layout : LayoutFeedbackhubBinding
    private var buttons = ArrayList<MaterialButton>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutFeedbackhubBinding = DataBindingUtil.inflate(inflater , R.layout.layout_feedbackhub , container , false)

        this.layout = layout
        layout.view = this
        layout.lifecycleOwner = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        if(UserManagement.userInfo?.admin == AdminCodeModel.CH_PRD_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_PRD_VicePresident ||
            UserManagement.userInfo?.admin == AdminCodeModel.CH_Affairs_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_Affairs_VicePresident ||
                UserManagement.userInfo?.admin == AdminCodeModel.CH_Culture_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_Culture_VicePresident ||
            UserManagement.userInfo?.admin == AdminCodeModel.CH_Employment_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_Employment_VicePresident ||
                UserManagement.userInfo?.admin == AdminCodeModel.CH_Execution_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_Employment_VicePresident ||
                UserManagement.userInfo?.admin == AdminCodeModel.CH_ExternalCoop_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_ExternalCoop_VicePresident ||
            UserManagement.userInfo?.admin == AdminCodeModel.CH_Facility_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_Facility_VicePresident ||
            UserManagement.userInfo?.admin == AdminCodeModel.CH_Policy_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_Policy_VicePresident ||
            UserManagement.userInfo?.admin == AdminCodeModel.CH_Welfare_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_Welfare_VicePresident ||
            UserManagement.userInfo?.admin == AdminCodeModel.CH_President || UserManagement.userInfo?.admin == AdminCodeModel.CH_VicePresident ||
            UserManagement.userInfo?.admin == AdminCodeModel.CH_PRD_Member){
            layout.btnShowAllFeedbacks.visibility = View.VISIBLE
        }

        else{
            layout.btnShowAllFeedbacks.visibility = View.GONE
        }

        val btn_myFeedbacks : Button = layout.toolbar.findViewById(R.id.btn_showMyFeedbacks)

        btn_myFeedbacks.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
            transaction.addToBackStack(null)

            transaction.replace(R.id.mainViewArea, FeedbackHubListView("MyFeedback"))
            transaction.commit()
        }

        buttons.add(layout.btnFacility)
        buttons.add(layout.btnApp)
        buttons.add(layout.btnCommunication)
        buttons.add(layout.btnFestival)
        buttons.add(layout.btnOthers)
        buttons.add(layout.btnPledge)
        buttons.add(layout.btnWelfare)

        return layout.root
    }

    fun changeView(v : View){
        when(v.id){
            R.id.btn_next -> {
                if(selectedCategory == null){
                    AwesomeDialog.build(activity as MainActivity)
                        .title("카테고리 선택", null, resources.getColor(R.color.black))
                        .body("카테고리를 선택해주세요.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive("확인")
                }

                else{
                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                    transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                    transaction.addToBackStack(null)

                    transaction.replace(R.id.mainViewArea, FeedbackHubMainView(selectedCategory!!))
                    transaction.commit()
                }
            }

            R.id.btn_showAllFeedbacks -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, FeedbackHubListView("All"))
                transaction.commit()
            }
        }
    }

    fun onCategoryClick(v : View){
        layout.btnNext.visibility = View.VISIBLE

        when(v.id){
            R.id.btn_facility -> {
                selectedCategory = FeedbackHubItemModel.FACILITY

                for(i in 0..6){
                    if(i == 0){
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.accent))
                        buttons[i].setIconTintResource(R.color.accent)
                    }

                    else{
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                        buttons[i].setIconTintResource(R.color.gray)
                    }
                }
            }

            R.id.btn_welfare -> {
                selectedCategory = FeedbackHubItemModel.WELFARE

                for(i in 0..6){
                    if(i == 6){
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.accent))
                        buttons[i].setIconTintResource(R.color.accent)
                    }

                    else{
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                        buttons[i].setIconTintResource(R.color.gray)
                    }
                }
            }

            R.id.btn_communication -> {
                selectedCategory = FeedbackHubItemModel.COMMUNICATION

                for(i in 0..6){
                    if(i == 2){
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.accent))
                        buttons[i].setIconTintResource(R.color.accent)
                    }

                    else{
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                        buttons[i].setIconTintResource(R.color.gray)
                    }
                }
            }

            R.id.btn_pledge -> {
                selectedCategory = FeedbackHubItemModel.PLEDGE

                for(i in 0..6){
                    if(i == 5){
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.accent))
                        buttons[i].setIconTintResource(R.color.accent)
                    }

                    else{
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                        buttons[i].setIconTintResource(R.color.gray)
                    }
                }
            }

            R.id.btn_festival -> {
                selectedCategory = FeedbackHubItemModel.FESTIVAL

                for(i in 0..6){
                    if(i == 3){
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.accent))
                        buttons[i].setIconTintResource(R.color.accent)
                    }

                    else{
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                        buttons[i].setIconTintResource(R.color.gray)
                    }
                }
            }

            R.id.btn_app -> {
                selectedCategory = FeedbackHubItemModel.APP

                for(i in 0..6){
                    if(i == 1){
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.accent))
                        buttons[i].setIconTintResource(R.color.accent)
                    }

                    else{
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                        buttons[i].setIconTintResource(R.color.gray)
                    }
                }
            }

            R.id.btn_others -> {
                selectedCategory = FeedbackHubItemModel.OTHERS

                for(i in 0..6){
                    if(i == 4){
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.accent))
                        buttons[i].setIconTintResource(R.color.accent)
                    }

                    else{
                        buttons[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                        buttons[i].setIconTintResource(R.color.gray)
                    }
                }
            }


        }
    }
}