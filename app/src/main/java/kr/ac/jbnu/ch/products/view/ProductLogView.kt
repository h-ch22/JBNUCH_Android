package kr.ac.jbnu.ch.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.products.helper.ProductsHelper
import kr.ac.jbnu.ch.products.models.ProductLogListAdapter
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel

class ProductLogView : Fragment() {
    private val helper = ProductsHelper()
    private val listAdapter = ProductLogListAdapter()
    private lateinit var list : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : kr.ac.jbnu.ch.databinding.LayoutProductlogBinding = DataBindingUtil.inflate(inflater, R.layout.layout_productlog, container, false)

        layout.view = this
        layout.lifecycleOwner = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC ||
                UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH ||
                UserManagement.userInfo?.collegeCode == CollegeCodeModel.CHE ||
                UserManagement.userInfo?.collegeCode == CollegeCodeModel.CON ||
                UserManagement.userInfo?.collegeCode == CollegeCodeModel.COM){
            layout.toggleGroupProductCategory.visibility = View.VISIBLE
        }

        else{
            layout.toggleGroupProductCategory.visibility = View.GONE
        }

        helper.getLog("CH") {
            if(it){
                layout.logList.visibility = View.VISIBLE
                layout.progressView.visibility = View.GONE
            }

            else{
                layout.logList.visibility = View.VISIBLE
                layout.progressView.visibility = View.GONE
            }
        }

        layout.toggleGroupProductCategory.setOnCheckedChangeListener(object : SingleSelectToggleGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: SingleSelectToggleGroup?, checkedId: Int) {
                when(checkedId){
                    R.id.btn_collegeCategory_CH -> {
                        helper.getLog("CH") {
                            if(it){
                                layout.logList.visibility = View.VISIBLE
                                layout.progressView.visibility = View.GONE

                                list.apply {
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }
                            }

                            else{
                                layout.logList.visibility = View.VISIBLE
                                layout.progressView.visibility = View.GONE
                            }
                        }
                    }

                    R.id.btn_collegeCategory_college -> {
                        helper.getLog("College") {
                            if(it){
                                layout.logList.visibility = View.VISIBLE
                                layout.progressView.visibility = View.GONE

                                list.apply {
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }
                            }

                            else{
                                layout.logList.visibility = View.VISIBLE
                                layout.progressView.visibility = View.GONE
                            }
                        }
                    }
                }
            }

        })

        list = layout.logList
        layout.swipeLayout.setColorSchemeColors(resources.getColor(R.color.accent))

        layout.swipeLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                when(layout.toggleGroupProductCategory.checkedId){
                    R.id.btn_collegeCategory_college -> {
                        helper.getLog("College") {
                            layout.swipeLayout.isRefreshing = false
                        }
                    }

                    R.id.btn_collegeCategory_CH -> {
                        helper.getLog("CH") {
                            layout.swipeLayout.isRefreshing = false
                        }
                    }
                }
            }
        })

        list.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }

        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}