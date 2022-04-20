package kr.ac.jbnu.ch.more.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutOpensourcelicenseBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.more.models.OpenSourceLicenseListAdapter

class OpenSourceLicenseView : Fragment(){
    private lateinit var list : RecyclerView
    private val listAdapter = OpenSourceLicenseListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutOpensourcelicenseBinding = DataBindingUtil.inflate(inflater , R.layout.layout_opensourcelicense , container , false)
        layout.view = this
        layout.lifecycleOwner = this

        list = layout.licenseLL

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }
    }
}