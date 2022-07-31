package kr.ac.jbnu.ch.affiliates.view

import android.os.Bundle
import android.util.Log
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
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.helper.AffiliateHelper
import kr.ac.jbnu.ch.affiliates.models.AffiliateDataModel
import kr.ac.jbnu.ch.affiliates.models.AffiliateListAdapter
import kr.ac.jbnu.ch.databinding.LayoutAffiliatesListBinding
import kr.ac.jbnu.ch.databinding.LayoutLicenseBinding
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.frameworks.view.MainActivity

class AffiliateListView(private val category : String, private val categoryAsKorean : String) : Fragment(), onKeyBackPressedListener {
    private val helper = AffiliateHelper()
    private val listAdapter = AffiliateListAdapter()
    private lateinit var list : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutAffiliatesListBinding = DataBindingUtil.inflate(inflater , R.layout.layout_affiliates_list , container , false)
        list = layout.affiliateListLL
        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)
        title.text = categoryAsKorean

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

        val btn_Map : ImageButton = layout.toolbar.findViewById(R.id.btn_showMap)

        btn_Map.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
            transaction.addToBackStack(null)

            transaction.replace(R.id.mainViewArea, AffiliateMapView())
            transaction.commit()
        }

        helper.getStoreList(category){
            if(it){
                layout.progressLL.visibility = View.GONE
                layout.affiliateListLL.visibility = View.VISIBLE
            }
        }

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter.setOnItemClickListener(object : AffiliateListAdapter.OnItemClickListener{
            override fun onItemClick(v : View, data : AffiliateDataModel, pos : Int){
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, AffiliateDetailView(data))
                transaction.commit()
            }
        })

        list.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }
    }

    override fun onBackKeyPressed() {
    }
}