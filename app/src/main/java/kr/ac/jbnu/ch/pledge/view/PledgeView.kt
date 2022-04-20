package kr.ac.jbnu.ch.pledge.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import com.nex3z.togglebuttongroup.ToggleButtonGroup
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutPledgeBinding
import kr.ac.jbnu.ch.frameworks.models.LicenseTypeModel
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.PDFViewer
import kr.ac.jbnu.ch.pledge.helper.PledgeHelper
import kr.ac.jbnu.ch.pledge.models.PledgeDataModel
import kr.ac.jbnu.ch.pledge.models.PledgeListAdapter

class PledgeView : Fragment() {
    private val helper = PledgeHelper()
    private lateinit var listAdapter : PledgeListAdapter
    private lateinit var pledgeList : RecyclerView
    private lateinit var toggleGroup : SingleSelectToggleGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutPledgeBinding = DataBindingUtil.inflate(inflater , R.layout.layout_pledge , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        listAdapter = PledgeListAdapter(false)

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        layout.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                listAdapter.filter.filter(p0)

                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                listAdapter.filter.filter(p0)

                return false
            }
        })

        pledgeList = layout.pledgeLL
        toggleGroup = layout.toggleGroupPledgeCategory

        toggleGroup.setOnCheckedChangeListener(object : SingleSelectToggleGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: SingleSelectToggleGroup?, checkedId: Int) {
                when(checkedId){
                    R.id.btn_pledgeCategory_All -> {
                        listAdapter = PledgeListAdapter(false)

                        layout.txtPercentage.text = "전체 공약 이행률"

                        helper.getPledgeList("CH"){
                            if(it){
                                pledgeList.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }

                                var completeCount = 0
                                var inProgressCount = 0
                                var preparingCount = 0

                                PledgeHelper.pledgeList.forEach{
                                    if(it.status == "이행 완료"){
                                        completeCount += 1
                                    }

                                    else if(it.status == "진행 중"){
                                        inProgressCount += 1
                                    }

                                    else if(it.status == "준비 중"){
                                        preparingCount += 1
                                    }
                                }

                                layout.txtPledgePercentage.text = "${(completeCount * 100 / PledgeHelper.pledgeList.size)} %"
                                layout.txtPrepareCount.text = "준비 중 : ${preparingCount}"
                                layout.txtInProcessCount.text = "진행 중 : ${inProgressCount}"
                                layout.txtDoneCount.text = "이행 완료 : ${completeCount}"
                            }
                        }
                    }

                    R.id.btn_pledgeCategory_App -> {
                        helper.getPledgeListByCategory("CH", "앱"){
                            if(it){
                                listAdapter = PledgeListAdapter(true)

                                pledgeList.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }

                                var completeCount = 0
                                var inProgressCount = 0
                                var preparingCount = 0

                                PledgeHelper.pledgeList_filtered.forEach{
                                    if(it.status == "이행 완료"){
                                        completeCount += 1
                                    }

                                    else if(it.status == "진행 중"){
                                        inProgressCount += 1
                                    }

                                    else if(it.status == "준비 중"){
                                        preparingCount += 1
                                    }
                                }

                                layout.txtPercentage.text = "앱 공약 이행률"
                                layout.txtPledgePercentage.text = "${(completeCount * 100 / PledgeHelper.pledgeList_filtered.size)} %"
                                layout.txtPrepareCount.text = "준비 중 : ${preparingCount}"
                                layout.txtInProcessCount.text = "진행 중 : ${inProgressCount}"
                                layout.txtDoneCount.text = "이행 완료 : ${completeCount}"
                            }
                        }
                    }

                    R.id.btn_pledgeCategory_Bachelor -> {
                        helper.getPledgeListByCategory("CH", "취/창업 및 학사"){
                            if(it){
                                listAdapter = PledgeListAdapter(true)

                                pledgeList.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }

                                var completeCount = 0
                                var inProgressCount = 0
                                var preparingCount = 0

                                PledgeHelper.pledgeList_filtered.forEach{
                                    if(it.status == "이행 완료"){
                                        completeCount += 1
                                    }

                                    else if(it.status == "진행 중"){
                                        inProgressCount += 1
                                    }

                                    else if(it.status == "준비 중"){
                                        preparingCount += 1
                                    }
                                }

                                layout.txtPercentage.text = "취/창업 및 학사 공약 이행률"
                                layout.txtPledgePercentage.text = "${(completeCount * 100 / PledgeHelper.pledgeList_filtered.size)} %"
                                layout.txtPrepareCount.text = "준비 중 : ${preparingCount}"
                                layout.txtInProcessCount.text = "진행 중 : ${inProgressCount}"
                                layout.txtDoneCount.text = "이행 완료 : ${completeCount}"
                            }
                        }
                    }

                    R.id.btn_pledgeCategory_Culture -> {
                        helper.getPledgeListByCategory("CH", "문화 및 예술"){
                            if(it){
                                listAdapter = PledgeListAdapter(true)

                                pledgeList.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }

                                var completeCount = 0
                                var inProgressCount = 0
                                var preparingCount = 0

                                PledgeHelper.pledgeList_filtered.forEach{
                                    if(it.status == "이행 완료"){
                                        completeCount += 1
                                    }

                                    else if(it.status == "진행 중"){
                                        inProgressCount += 1
                                    }

                                    else if(it.status == "준비 중"){
                                        preparingCount += 1
                                    }
                                }

                                layout.txtPercentage.text = "문화 및 예술 공약 이행률"
                                layout.txtPledgePercentage.text = "${(completeCount * 100 / PledgeHelper.pledgeList_filtered.size)} %"
                                layout.txtPrepareCount.text = "준비 중 : ${preparingCount}"
                                layout.txtInProcessCount.text = "진행 중 : ${inProgressCount}"
                                layout.txtDoneCount.text = "이행 완료 : ${completeCount}"
                            }
                        }
                    }

                    R.id.btn_pledgeCategory_Dorm -> {
                        helper.getPledgeListByCategory("CH", "생활관"){
                            if(it){
                                listAdapter = PledgeListAdapter(true)

                                pledgeList.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }

                                var completeCount = 0
                                var inProgressCount = 0
                                var preparingCount = 0

                                PledgeHelper.pledgeList_filtered.forEach{
                                    if(it.status == "이행 완료"){
                                        completeCount += 1
                                    }

                                    else if(it.status == "진행 중"){
                                        inProgressCount += 1
                                    }

                                    else if(it.status == "준비 중"){
                                        preparingCount += 1
                                    }
                                }

                                layout.txtPercentage.text = "생활관 공약 이행률"
                                layout.txtPledgePercentage.text = "${(completeCount * 100 / PledgeHelper.pledgeList_filtered.size)} %"
                                layout.txtPrepareCount.text = "준비 중 : ${preparingCount}"
                                layout.txtInProcessCount.text = "진행 중 : ${inProgressCount}"
                                layout.txtDoneCount.text = "이행 완료 : ${completeCount}"
                            }
                        }
                    }

                    R.id.btn_pledgeCategory_Facility -> {
                        helper.getPledgeListByCategory("CH", "시설 및 안전"){
                            if(it){
                                listAdapter = PledgeListAdapter(true)

                                pledgeList.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }

                                var completeCount = 0
                                var inProgressCount = 0
                                var preparingCount = 0

                                PledgeHelper.pledgeList_filtered.forEach{
                                    if(it.status == "이행 완료"){
                                        completeCount += 1
                                    }

                                    else if(it.status == "진행 중"){
                                        inProgressCount += 1
                                    }

                                    else if(it.status == "준비 중"){
                                        preparingCount += 1
                                    }
                                }

                                layout.txtPercentage.text = "시설 및 안전 공약 이행률"
                                layout.txtPledgePercentage.text = "${(completeCount * 100 / PledgeHelper.pledgeList_filtered.size)} %"
                                layout.txtPrepareCount.text = "준비 중 : ${preparingCount}"
                                layout.txtInProcessCount.text = "진행 중 : ${inProgressCount}"
                                layout.txtDoneCount.text = "이행 완료 : ${completeCount}"
                            }
                        }
                    }

                    R.id.btn_pledgeCategory_HumanRights -> {
                        helper.getPledgeListByCategory("CH", "인권"){
                            if(it){
                                listAdapter = PledgeListAdapter(true)

                                pledgeList.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }

                                var completeCount = 0
                                var inProgressCount = 0
                                var preparingCount = 0

                                PledgeHelper.pledgeList_filtered.forEach{
                                    if(it.status == "이행 완료"){
                                        completeCount += 1
                                    }

                                    else if(it.status == "진행 중"){
                                        inProgressCount += 1
                                    }

                                    else if(it.status == "준비 중"){
                                        preparingCount += 1
                                    }
                                }

                                layout.txtPercentage.text = "인권 공약 이행률"
                                layout.txtPledgePercentage.text = "${(completeCount * 100 / PledgeHelper.pledgeList_filtered.size)} %"
                                layout.txtPrepareCount.text = "준비 중 : ${preparingCount}"
                                layout.txtInProcessCount.text = "진행 중 : ${inProgressCount}"
                                layout.txtDoneCount.text = "이행 완료 : ${completeCount}"
                            }
                        }
                    }

                    R.id.btn_pledgeCategory_Welfare -> {
                        helper.getPledgeListByCategory("CH", "복지"){
                            if(it){
                                listAdapter = PledgeListAdapter(true)

                                pledgeList.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = listAdapter
                                }

                                var completeCount = 0
                                var inProgressCount = 0
                                var preparingCount = 0

                                PledgeHelper.pledgeList_filtered.forEach{
                                    if(it.status == "이행 완료"){
                                        completeCount += 1
                                    }

                                    else if(it.status == "진행 중"){
                                        inProgressCount += 1
                                    }

                                    else if(it.status == "준비 중"){
                                        preparingCount += 1
                                    }
                                }

                                layout.txtPercentage.text = "복지 공약 이행률"
                                layout.txtPledgePercentage.text = "${(completeCount * 100 / PledgeHelper.pledgeList_filtered.size)} %"
                                layout.txtPrepareCount.text = "준비 중 : ${preparingCount}"
                                layout.txtInProcessCount.text = "진행 중 : ${inProgressCount}"
                                layout.txtDoneCount.text = "이행 완료 : ${completeCount}"
                            }
                        }
                    }
                }
            }

        })

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_openPledgeBook -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, PDFViewer(LicenseTypeModel.PLEDGEBOOK))
                transaction.commit()
            }
        }
    }
}