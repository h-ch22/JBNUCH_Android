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
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
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
    private lateinit var listAdapter : AffiliateListAdapter
    private lateinit var list : RecyclerView
    private lateinit var toggleGroup : SingleSelectToggleGroup

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

            transaction.replace(R.id.mainViewArea, AffiliateMapView(helper))
            transaction.commit()
        }

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        toggleGroup = layout.toggleGroupAffiliatePosition

        toggleGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_affiliatePos_All -> {
                    layout.progressLL.visibility = View.VISIBLE
                    layout.affiliateListLL.visibility = View.GONE

                    helper.getStoreList(category) {
                        if(it){
                            println(AffiliateHelper.storeList)
                            layout.progressLL.visibility = View.GONE
                            layout.affiliateListLL.visibility = View.VISIBLE

                            listAdapter = AffiliateListAdapter(false)

                            list.apply {
                                layoutManager = LinearLayoutManager(activity)
                                adapter = listAdapter
                            }

                            listAdapter.setOnItemClickListener(object :
                                AffiliateListAdapter.OnItemClickListener {
                                override fun onItemClick(v: View, data: AffiliateDataModel, pos: Int) {
                                    val transaction: FragmentTransaction =
                                        requireFragmentManager().beginTransaction()
                                    transaction.setCustomAnimations(
                                        R.anim.anim_slide_in_bottom,
                                        R.anim.anim_slide_out_top
                                    )
                                    transaction.addToBackStack(null)

                                    transaction.replace(R.id.mainViewArea, AffiliateDetailView(data, helper))
                                    transaction.commit()
                                }
                            })
                        }
                    }
                }

                R.id.btn_affiliatePos_JBNU -> {
                    sortStore("deokjin")


                }

                R.id.btn_affiliatePos_NewTown -> {
                    sortStore("hyoja")

                }

                R.id.btn_affiliatePos_OldTown -> {
                    sortStore("gosa")

                }

                R.id.btn_affiliatePos_Others -> {
                    sortStore("others")
                }

                R.id.btn_affiliatePos_Favorite -> {
                    sortStore("favorite")
                }
            }
        }

        return layout.root
    }

    private fun sortStore(pos : String){
        helper.getStoreByPos(pos) {
            if (it) {
                Log.d("AffiliateListView", "Sort : Successful")

                listAdapter = AffiliateListAdapter(true)

                list.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = listAdapter
                }

                listAdapter.setOnItemClickListener(object :
                    AffiliateListAdapter.OnItemClickListener {
                    override fun onItemClick(
                        v: View,
                        data: AffiliateDataModel,
                        pos: Int
                    ) {
                        val transaction: FragmentTransaction =
                            requireFragmentManager().beginTransaction()
                        transaction.setCustomAnimations(
                            R.anim.anim_slide_in_bottom,
                            R.anim.anim_slide_out_top
                        )
                        transaction.addToBackStack(null)

                        transaction.replace(
                            R.id.mainViewArea,
                            AffiliateDetailView(data, helper)
                        )
                        transaction.commit()
                    }
                })
            }

            else{
                Log.d("AffiliateListView", "Sort : Fail")
            }
        }
    }

    override fun onBackKeyPressed() {
    }
}