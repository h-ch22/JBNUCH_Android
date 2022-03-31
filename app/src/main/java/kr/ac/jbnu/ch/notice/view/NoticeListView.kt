package kr.ac.jbnu.ch.notice.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.awesomedialog.*
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.models.AffiliateDataModel
import kr.ac.jbnu.ch.affiliates.models.AffiliateListAdapter
import kr.ac.jbnu.ch.affiliates.view.AffiliateDetailView
import kr.ac.jbnu.ch.databinding.LayoutNoticelistBinding
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.notice.helper.NoticeHelper
import kr.ac.jbnu.ch.notice.models.NoticeDataModel
import kr.ac.jbnu.ch.notice.models.NoticeListAdapter
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel
import kr.ac.jbnu.ch.userManagement.view.SignUpView

class NoticeListView : Fragment(), onKeyBackPressedListener {
    private val helper = NoticeHelper()
    private val listAdapter = NoticeListAdapter()
    private lateinit var list : RecyclerView
    private lateinit var swipeContainer : SwipeRefreshLayout
    private lateinit var toggleGroup : SingleSelectToggleGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutNoticelistBinding = DataBindingUtil.inflate(inflater , R.layout.layout_noticelist , container , false)

        if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.COM || UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CHE || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CON){
            layout.toggleGroupNoticeCategory.visibility = View.VISIBLE
        }

        else{
            layout.toggleGroupNoticeCategory.visibility = View.GONE
        }

        list = layout.noticeList

        this.toggleGroup = layout.toggleGroupNoticeCategory

        swipeContainer = layout.swipeLayout

        swipeContainer.setColorSchemeColors(resources.getColor(R.color.accent))

        swipeContainer.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                when(toggleGroup.checkedId){
                    R.id.btn_noticeCategory_CH -> {
                        helper.getNotice("CH") {
                            if(!it){
                                swipeContainer.isRefreshing = false

                                layout.progressLL.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("공지사항을 불러올 수 없음", null, resources.getColor(R.color.black))
                                    .body("공지사항을 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인"){

                                    }
                            }

                            else{
                                swipeContainer.isRefreshing = false
                            }

                        }
                    }

                    R.id.btn_noticeCategory_college -> {
                        helper.getNotice("College") {
                            if(!it){
                                swipeContainer.isRefreshing = false

                                layout.progressLL.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("공지사항을 불러올 수 없음", null, resources.getColor(R.color.black))
                                    .body("공지사항을 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인"){

                                    }
                            }

                            else{
                                swipeContainer.isRefreshing = false
                            }

                        }
                    }
                }
            }

        })

        layout.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                listAdapter.filter.filter(query)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)

                return false
            }
        })

        toggleGroup.setOnCheckedChangeListener(object : SingleSelectToggleGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: SingleSelectToggleGroup?, checkedId: Int) {
                when(checkedId){
                    R.id.btn_noticeCategory_CH -> {
                        list.apply {
                            adapter = null
                        }

                        helper.getNotice("CH") {
                            if(!it){
                                layout.progressLL.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("공지사항을 불러올 수 없음", null, resources.getColor(R.color.black))
                                    .body("공지사항을 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인"){

                                    }
                            }

                            else{
                                layout.progressLL.visibility = View.GONE
                                layout.noticeList.visibility = View.VISIBLE
                            }
                        }

                        listAdapter.setOnItemClickListener(object : NoticeListAdapter.OnItemClickListener{
                            override fun onItemClick(v : View, data : NoticeDataModel, pos : Int){
                                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                                transaction.addToBackStack(null)

                                transaction.replace(R.id.mainViewArea, NoticeDetailView(data))
                                transaction.commit()
                            }
                        })

                        list.apply{
                            layoutManager = LinearLayoutManager(activity)
                            adapter = listAdapter
                        }
                    }

                    R.id.btn_noticeCategory_college -> {
                        list.apply{
                            adapter = null
                        }

                        helper.getNotice("College") {
                            if(!it){
                                layout.progressLL.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("공지사항을 불러올 수 없음", null, resources.getColor(R.color.black))
                                    .body("공지사항을 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인"){

                                    }
                            }

                            else{
                                layout.progressLL.visibility = View.GONE
                                layout.noticeList.visibility = View.VISIBLE
                            }
                        }

                        listAdapter.setOnItemClickListener(object : NoticeListAdapter.OnItemClickListener{
                            override fun onItemClick(v : View, data : NoticeDataModel, pos : Int){
                                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                                transaction.addToBackStack(null)

                                transaction.replace(R.id.mainViewArea, NoticeDetailView(data))
                                transaction.commit()
                            }
                        })

                        list.apply{
                            layoutManager = LinearLayoutManager(activity)
                            adapter = listAdapter
                        }
                    }
                }
            }

        })

        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onBackKeyPressed() {
    }
}