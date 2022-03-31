package kr.ac.jbnu.ch.handWriting.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.awesomedialog.*
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutHandwritingBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.handWriting.helper.HandWritingHelper
import kr.ac.jbnu.ch.handWriting.models.HandWritingDataModel
import kr.ac.jbnu.ch.handWriting.models.HandWritingListAdapter
import kr.ac.jbnu.ch.notice.view.NoticeDetailView
import kr.ac.jbnu.ch.sports.view.SportsView

class HandWritingView : Fragment() {
    private val helper = HandWritingHelper()
    private val listAdapter = HandWritingListAdapter()
    private lateinit var list : RecyclerView
    private lateinit var btn_add : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutHandwritingBinding = DataBindingUtil.inflate(inflater, R.layout.layout_handwriting, container, false)
        layout.view = this
        layout.lifecycleOwner = this

        list = layout.handWritingList
        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)
        btn_add = layout.toolbar.findViewById(R.id.btn_add)
        title.text = "합격자 수기 공유"

                layout.swipeLayout.setColorSchemeColors(resources.getColor(R.color.accent))

        layout.swipeLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                helper.getHandWritingList {
                    if(!it){
                        layout.swipeLayout.isRefreshing = false

                        AwesomeDialog.build(activity as MainActivity)
                            .title("수기 목록을 불러올 수 없음", null, resources.getColor(R.color.black))
                            .body("수기 목록을 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                            .icon(R.drawable.ic_warning)
                            .onPositive("확인"){

                            }
                    }

                    else{
                        layout.swipeLayout.isRefreshing = false
                    }

                }
            }

        })

        btn_add.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
            transaction.addToBackStack(null)

            transaction.replace(R.id.mainViewArea, AddHandWritingView())
            transaction.commit()
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

        helper.getHandWritingList {
            if(!it){
                layout.progressLL.visibility = View.GONE

                AwesomeDialog.build(activity as MainActivity)
                    .title("수기 목록을 불러올 수 없음", null, resources.getColor(R.color.black))
                    .body("수기 목록을 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onPositive("확인"){

                    }
            }

            else{
                layout.progressLL.visibility = View.GONE
                layout.handWritingList.visibility = View.VISIBLE
            }
        }

        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter.setOnItemClickListener(object : HandWritingListAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: HandWritingDataModel, pos: Int) {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, HandWritingDetailView(data))
                transaction.commit()
            }

        })

        list.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }
    }
}