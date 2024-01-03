package kr.ac.jbnu.ch.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.view.AffiliateListView
import kr.ac.jbnu.ch.calendar.helper.CalendarDataHelper
import kr.ac.jbnu.ch.calendar.models.CalendarDataModel
import kr.ac.jbnu.ch.calendar.view.CalendarView
import kr.ac.jbnu.ch.databinding.LayoutHomeBinding
import kr.ac.jbnu.ch.feedbackhub.view.FeedbackHubView
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.home.models.HomeNoticeAdapter
import kr.ac.jbnu.ch.home.models.HomePetitionAdapter
import kr.ac.jbnu.ch.home.models.HomeCalendarAdapter
import kr.ac.jbnu.ch.notice.helper.NoticeHelper
import kr.ac.jbnu.ch.notice.models.NoticeDataModel
import kr.ac.jbnu.ch.notice.view.NoticeDetailView
import kr.ac.jbnu.ch.notice.view.NoticeListView
import kr.ac.jbnu.ch.petition.helper.PetitionHelper
import kr.ac.jbnu.ch.petition.models.PetitionDataModel
import kr.ac.jbnu.ch.petition.view.PetitionDetailView
import kr.ac.jbnu.ch.petition.view.PetitionListView
import kr.ac.jbnu.ch.products.view.ProductView
import kr.ac.jbnu.ch.sports.helper.SportsHelper
import kr.ac.jbnu.ch.sports.models.SportsDataModel
import kr.ac.jbnu.ch.sports.view.SportsDetailView
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import java.text.SimpleDateFormat
import java.util.*

class HomeView : Fragment(){
    private val noticeHelper = NoticeHelper()
    private val petitionHelper = PetitionHelper()
    private val calendarHelper = CalendarDataHelper()

    private lateinit var noticeList : RecyclerView
    private lateinit var petitionList : RecyclerView
    private lateinit var calendarList : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutHomeBinding = DataBindingUtil.inflate(inflater , R.layout.layout_home , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        layout.txtGreet.text = "${resources.getString(R.string.TXT_HOME_GREET)}\n${UserManagement.userInfo?.name ?: ""}${resources.getString(R.string.TXT_HOME_GREET_FIN)}"

        val formatter_day = SimpleDateFormat("MM/dd")
        val day = formatter_day.format(Date())

        val formatter_dayOfWeek = SimpleDateFormat("E")
        val dayOfWeek = formatter_dayOfWeek.format(Date())

        layout.txtDayOfWeek.text = dayOfWeek
        layout.txtDate.text = day

        noticeList = layout.latestNoticeLL
        petitionList = layout.petitionLL
        calendarList = layout.calendarLL

        val noticeLayoutManager = LinearLayoutManager(activity)
        noticeLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val petitionLayoutManager = LinearLayoutManager(activity)
        petitionLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val calendarLayoutManager = LinearLayoutManager(activity)
        calendarLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val noticeAdapter = HomeNoticeAdapter()
        val petitionAdapter = HomePetitionAdapter()


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

        calendarHelper.getResource(){
            if(it){
                val calendarAdapter = HomeCalendarAdapter()

                calendarList.apply{
                    layoutManager = calendarLayoutManager
                    adapter = calendarAdapter
                }

                calendarAdapter.setOnItemClickListener(object : HomeCalendarAdapter.OnItemClickListener{
                    override fun onItemClick(v : View, data : CalendarDataModel, pos : Int){
                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                        transaction.addToBackStack(null)

                        transaction.replace(R.id.mainViewArea, CalendarView())
                        transaction.commit()
                    }
                })
            }

        }

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

                transaction.replace(R.id.mainViewArea, CalendarView())
                transaction.commit()
            }
        }
    }
}