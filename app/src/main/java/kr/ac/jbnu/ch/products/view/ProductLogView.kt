package kr.ac.jbnu.ch.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutProductBinding
import kr.ac.jbnu.ch.databinding.LayoutProductlogBinding
import kr.ac.jbnu.ch.notice.models.NoticeListAdapter
import kr.ac.jbnu.ch.products.helper.ProductsHelper
import kr.ac.jbnu.ch.products.models.ProductLogListAdapter

class ProductLogView : Fragment() {
    private val helper = ProductsHelper()
    private val listAdapter = ProductLogListAdapter()
    private lateinit var list : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutProductlogBinding = DataBindingUtil.inflate(inflater, R.layout.layout_productlog, container, false)

        layout.view = this
        layout.lifecycleOwner = this

        helper.getLog {
            if(it){
                layout.logList.visibility = View.VISIBLE
                layout.progressView.visibility = View.GONE
            }

            else{
                layout.logList.visibility = View.VISIBLE
                layout.progressView.visibility = View.GONE
            }
        }

        list = layout.logList
        layout.swipeLayout.setColorSchemeColors(resources.getColor(R.color.accent))

        layout.swipeLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                helper.getLog {
                    layout.swipeLayout.isRefreshing = false
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