package kr.ac.jbnu.ch.home.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomedialog.*
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.view.AffiliateListView
import kr.ac.jbnu.ch.databinding.LayoutHomeBinding
import kr.ac.jbnu.ch.feedbackhub.view.FeedbackHubMainView
import kr.ac.jbnu.ch.feedbackhub.view.FeedbackHubView
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.home.models.HomeNoticeAdapter
import kr.ac.jbnu.ch.home.models.HomePetitionAdapter
import kr.ac.jbnu.ch.home.models.HomeSportsAdapter
import kr.ac.jbnu.ch.notice.helper.NoticeHelper
import kr.ac.jbnu.ch.notice.models.NoticeDataModel
import kr.ac.jbnu.ch.notice.models.NoticeListAdapter
import kr.ac.jbnu.ch.notice.view.NoticeDetailView
import kr.ac.jbnu.ch.notice.view.NoticeListView
import kr.ac.jbnu.ch.petition.helper.PetitionHelper
import kr.ac.jbnu.ch.petition.models.PetitionDataModel
import kr.ac.jbnu.ch.petition.view.PetitionDetailView
import kr.ac.jbnu.ch.petition.view.PetitionListView
import kr.ac.jbnu.ch.pledge.view.PledgeView
import kr.ac.jbnu.ch.products.view.ProductView
import kr.ac.jbnu.ch.sports.helper.SportsHelper
import kr.ac.jbnu.ch.sports.models.SportsDataModel
import kr.ac.jbnu.ch.sports.view.SportsDetailView
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class HomeView : Fragment(){
    private val noticeHelper = NoticeHelper()
    private val petitionHelper = PetitionHelper()
    private val sportsHelper = SportsHelper()

    private lateinit var noticeList : RecyclerView
    private lateinit var petitionList : RecyclerView
    private lateinit var sportsList : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutHomeBinding = DataBindingUtil.inflate(inflater , R.layout.layout_home , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        layout.txtGreet.text = "${resources.getString(R.string.TXT_HOME_GREET)}\n${UserManagement.userInfo?.name ?: ""}${resources.getString(R.string.TXT_HOME_GREET_FIN)}"

        noticeList = layout.latestNoticeLL
        petitionList = layout.petitionLL
        sportsList = layout.sportsLL

        val noticeLayoutManager = LinearLayoutManager(activity)
        noticeLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val petitionLayoutManager = LinearLayoutManager(activity)
        petitionLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val sportsLayoutManager = LinearLayoutManager(activity)
        sportsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val noticeAdapter = HomeNoticeAdapter()
        val petitionAdapter = HomePetitionAdapter()
        val sportsAdapter = HomeSportsAdapter()

        noticeHelper.getNotice("CH"){
            if(it){
                noticeList.apply{
                    layoutManager = noticeLayoutManager
                    adapter = noticeAdapter
                }
            }
        }

        noticeAdapter.setOnItemClickListener(object : HomeNoticeAdapter.OnItemClickListener{
            override fun onItemClick(v : View, data : NoticeDataModel, pos : Int){
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, NoticeDetailView(data))
                transaction.commit()
            }
        })

        petitionHelper.getPetitionList {
            if(it){
                petitionList.apply{
                    layoutManager = petitionLayoutManager
                    adapter = petitionAdapter
                }
            }
        }

        petitionAdapter.setOnItemClickListener(object : HomePetitionAdapter.OnItemClickListener{
            override fun onItemClick(v : View, data : PetitionDataModel, pos : Int){
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, PetitionDetailView(data))
                transaction.commit()
            }
        })

        sportsHelper.getSportsList("All"){
            if(it){
                sportsList.apply{
                    layoutManager = sportsLayoutManager
                    adapter = sportsAdapter
                }
            }

        }

        sportsAdapter.setOnItemClickListener(object : HomeSportsAdapter.OnItemClickListener{
            override fun onItemClick(v : View, data : SportsDataModel, pos : Int){
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, SportsDetailView(data))
                transaction.commit()
            }
        })

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_affiliates -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, AffiliateListView("All", "전체"))
                transaction.commit()
            }

            R.id.btn_notice -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, NoticeListView())
                transaction.commit()
            }

            R.id.btn_petition -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, PetitionListView())
                transaction.commit()
            }

            R.id.btn_feedback -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, FeedbackHubView())
                transaction.commit()
            }

            R.id.btn_products -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, ProductView())
                transaction.commit()
            }

            R.id.btn_pledge -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, PledgeView())
                transaction.commit()
            }
        }
    }
}