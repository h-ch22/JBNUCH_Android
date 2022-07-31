package kr.ac.jbnu.ch.sports.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutSportsBinding
import kr.ac.jbnu.ch.frameworks.models.ListViewDecoration
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.sports.helper.SportsHelper
import kr.ac.jbnu.ch.sports.models.SportsDataModel
import kr.ac.jbnu.ch.sports.models.SportsListAdapter

class SportsView : Fragment(), View.OnClickListener {
    private val helper = SportsHelper()
    private val listAdapter = SportsListAdapter()
    private lateinit var list : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutSportsBinding = DataBindingUtil.inflate(inflater , R.layout.layout_sports , container , false)

        list = layout.sportsListLL
        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)
        title.text = "스포츠 용병 제도"

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        helper.getSportsList("All") {
            if(it){
                layout.progressView.visibility = View.GONE
                layout.sportsListLL.visibility = View.VISIBLE

                val decorationHeight = ListViewDecoration(10)

                list.apply{
                    layoutManager = LinearLayoutManager(activity)
                    adapter = listAdapter
                    addItemDecoration(decorationHeight)
                }

                listAdapter.setOnItemClickListener(object : SportsListAdapter.OnItemClickListener{
                    override fun onItemClick(v: View, data: SportsDataModel, pos: Int) {
                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                        transaction.addToBackStack(null)

                        transaction.replace(R.id.mainViewArea, SportsDetailView(data))
                        transaction.commit()
                    }
                })
            }
        }

        layout.swipeLayout.setOnRefreshListener {
            when(layout.tabView.selectedTabPosition){
                0 -> {
                    helper.getSportsList("All") {
                        if(it){
                            val decorationHeight = ListViewDecoration(10)

                            list.apply{
                                layoutManager = LinearLayoutManager(activity)
                                adapter = listAdapter
                                addItemDecoration(decorationHeight)
                            }
                        }
                    }
                }

                1 -> {
                    helper.getSportsList("My") {
                        if(it){
                            val decorationHeight = ListViewDecoration(10)

                            list.apply{
                                layoutManager = LinearLayoutManager(activity)
                                adapter = listAdapter
                                addItemDecoration(decorationHeight)
                            }
                        }
                    }
                }

                else -> {
                    helper.getSportsList("All") {
                        if(it){
                            val decorationHeight = ListViewDecoration(10)

                            list.apply{
                                layoutManager = LinearLayoutManager(activity)
                                adapter = listAdapter
                                addItemDecoration(decorationHeight)
                            }
                        }
                    }
                }
            }

            layout.swipeLayout.isRefreshing = false
        }

        layout.tabView.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                list.apply{
                    adapter = null
                }

                when(tab?.position){
                    0 -> {
                        helper.getSportsList("All") {
                            if(it){
                                layout.progressView.visibility = View.GONE
                                layout.sportsListLL.visibility = View.VISIBLE

                                val decorationHeight = ListViewDecoration(10)

                                list.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                    addItemDecoration(decorationHeight)
                                }

                                listAdapter.setOnItemClickListener(object : SportsListAdapter.OnItemClickListener{
                                    override fun onItemClick(v: View, data: SportsDataModel, pos: Int) {
                                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                                        transaction.addToBackStack(null)

                                        transaction.replace(R.id.mainViewArea, SportsDetailView(data))
                                        transaction.commit()
                                    }
                                })
                            }
                        }
                    }

                    1 -> {
                        layout.progressView.visibility = View.VISIBLE

                        helper.getSportsList("My") {
                            if(it){
                                layout.progressView.visibility = View.GONE
                                layout.sportsListLL.visibility = View.VISIBLE

                                val decorationHeight = ListViewDecoration(10)

                                list.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                    addItemDecoration(decorationHeight)
                                }

                                listAdapter.setOnItemClickListener(object : SportsListAdapter.OnItemClickListener{
                                    override fun onItemClick(v: View, data: SportsDataModel, pos: Int) {
                                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                                        transaction.addToBackStack(null)

                                        transaction.replace(R.id.mainViewArea, SportsDetailView(data))
                                        transaction.commit()
                                    }
                                })
                            }
                        }
                    }

                    else -> {
                        helper.getSportsList("All") {
                            if(it){
                                layout.progressView.visibility = View.GONE
                                layout.sportsListLL.visibility = View.VISIBLE

                                val decorationHeight = ListViewDecoration(10)

                                list.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                    addItemDecoration(decorationHeight)
                                }

                                listAdapter.setOnItemClickListener(object : SportsListAdapter.OnItemClickListener{
                                    override fun onItemClick(v: View, data: SportsDataModel, pos: Int) {
                                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                                        transaction.addToBackStack(null)

                                        transaction.replace(R.id.mainViewArea, SportsDetailView(data))
                                        transaction.commit()
                                    }
                                })
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        val btn_Add : ImageButton = layout.toolbar.findViewById(R.id.btn_add)

        btn_Add.setOnClickListener(this)

        return layout.root
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, AddSportsView())
                transaction.commit()
            }
        }
    }
}