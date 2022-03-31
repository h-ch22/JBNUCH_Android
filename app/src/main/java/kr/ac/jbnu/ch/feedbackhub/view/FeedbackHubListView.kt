package kr.ac.jbnu.ch.feedbackhub.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.awesomedialog.*
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutFeedbacklistBinding
import kr.ac.jbnu.ch.feedbackhub.helper.FeedbackHubHelper
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubDataModel
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubListAdapter
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.notice.view.NoticeDetailView

class FeedbackHubListView(private val type : String) : Fragment() {
    private val helper = FeedbackHubHelper()
    private val listAdapter = FeedbackHubListAdapter(type)
    private lateinit var list : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutFeedbacklistBinding = DataBindingUtil.inflate(inflater , R.layout.layout_feedbacklist , container , false)

        layout.view = this
        layout.lifecycleOwner = this
        list = layout.feedbackList

        layout.progressLL.visibility = View.VISIBLE

                layout.swipeLayout.setColorSchemeColors(resources.getColor(R.color.accent))

        layout.swipeLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                when(type){
                    "MyFeedback" -> {
                        helper.getMyFeedback(){
                            if(!it){
                                layout.swipeLayout.isRefreshing = false

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("오류", null, resources.getColor(R.color.black))
                                    .body("피드백 목록을 받아오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인")
                            }

                            else{
                                layout.swipeLayout.isRefreshing = false
                            }

                        }
                    }

                    "All" -> {
                        helper.getFeedbackList(){
                            if(!it){
                                layout.swipeLayout.isRefreshing = false

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("오류", null, resources.getColor(R.color.black))
                                    .body("피드백 목록을 받아오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인")
                            }

                            else{
                                layout.swipeLayout.isRefreshing = false
                            }

                        }
                    }
                }
            }

        })

        when(type){
            "MyFeedback" -> {
                helper.getMyFeedback(){
                    if(!it){
                        layout.progressLL.visibility = View.GONE

                        AwesomeDialog.build(activity as MainActivity)
                            .title("오류", null, resources.getColor(R.color.black))
                            .body("피드백 목록을 받아오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                            .icon(R.drawable.ic_warning)
                            .onPositive("확인")
                    }

                    else{
                        layout.progressLL.visibility = View.GONE

                        list.apply{
                            layoutManager = LinearLayoutManager(activity)
                            adapter = listAdapter
                        }
                    }

                }
            }

            "All" -> {
                helper.getFeedbackList(){
                    if(!it){
                        layout.progressLL.visibility = View.GONE

                        AwesomeDialog.build(activity as MainActivity)
                            .title("오류", null, resources.getColor(R.color.black))
                            .body("피드백 목록을 받아오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                            .icon(R.drawable.ic_warning)
                            .onPositive("확인")
                    }

                    else{
                        layout.progressLL.visibility = View.GONE

                        list.apply{
                            layoutManager = LinearLayoutManager(activity)
                            adapter = listAdapter
                        }
                    }

                }
            }
        }

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

        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter.setOnItemClickListener(object : FeedbackHubListAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: FeedbackHubDataModel, pos: Int) {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, FeedbackHubDetailView(data, type))
                transaction.commit()
            }
        })
    }
}