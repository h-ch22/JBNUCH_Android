package kr.ac.jbnu.ch.petition.view

import android.app.AlertDialog
import android.content.DialogInterface
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
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.awesomedialog.*
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutPetitionListBinding
import kr.ac.jbnu.ch.feedbackhub.view.FeedbackHubView
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.petition.helper.PetitionHelper
import kr.ac.jbnu.ch.petition.models.PetitionDataModel
import kr.ac.jbnu.ch.petition.models.PetitionListAdapter
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class PetitionListView : Fragment(), onKeyBackPressedListener {
    private val helper = PetitionHelper()
    private val listAdapter = PetitionListAdapter()
    private lateinit var list : RecyclerView
    private lateinit var toggleGroup : SingleSelectToggleGroup
    private lateinit var layout : LayoutPetitionListBinding

    var selectedCategory = MutableLiveData<String>()

    init{
        selectedCategory.value = "전체"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.layout = DataBindingUtil.inflate(inflater , R.layout.layout_petition_list , container , false)
        layout.view = this
        layout.lifecycleOwner = this

        list = layout.petitionList
        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        title.text = "전대 청원제도"

        layout.swipeLayout.setColorSchemeColors(resources.getColor(R.color.accent))

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

        val btn_addPetition : ImageButton = layout.toolbar.findViewById(R.id.btn_add)

        btn_addPetition.setOnClickListener {
            changeView()
        }

        helper.getPetitionList{
            if(!it){
                layout.progressLL.visibility = View.GONE

                AwesomeDialog.build(activity as MainActivity)
                    .title("청원 목록을 불러올 수 없음", null, resources.getColor(R.color.black))
                    .body("청원 목록을 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onPositive("확인"){

                    }
            }

            else{
                layout.progressLL.visibility = View.GONE
                layout.petitionList.visibility = View.VISIBLE

                toggleGroup = layout.toggleGroupPetitionCategory

                toggleGroup.setOnCheckedChangeListener(object : SingleSelectToggleGroup.OnCheckedChangeListener{
                    override fun onCheckedChanged(group: SingleSelectToggleGroup?, checkedId: Int) {
                        when(checkedId){
                            R.id.btn_petitionCategory_All -> selectedCategory.value = "전체"
                            R.id.btn_petitionCategory_Bachelor -> selectedCategory.value = "학사"
                            R.id.btn_petitionCategory_Culture -> selectedCategory.value = "문화 및 예술"
                            R.id.btn_petitionCategory_Facility -> selectedCategory.value = "시설"
                            R.id.btn_petitionCategory_Others -> selectedCategory.value = "기타"
                            R.id.btn_petitionCategory_Welfare -> selectedCategory.value = "복지"
                        }

                        updateList()
                    }

                })

                list.apply{
                    layoutManager = LinearLayoutManager(activity)
                    adapter = listAdapter
                }
            }
        }

        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter.setOnItemClickListener(object : PetitionListAdapter.OnItemClickListener{
            override fun onItemClick(v : View, data : PetitionDataModel, pos : Int){
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, PetitionDetailView(data))
                transaction.commit()
            }
        })

        layout.swipeLayout.setOnRefreshListener {
            helper.getPetitionList {
                if (!it) {
                    layout.swipeLayout.isRefreshing = false

                    layout.progressLL.visibility = View.GONE

                    AwesomeDialog.build(activity as MainActivity)
                        .title("청원 목록을 불러올 수 없음", null, resources.getColor(R.color.black))
                        .body(
                            "청원 목록을 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.",
                            null,
                            resources.getColor(R.color.black)
                        )
                        .icon(R.drawable.ic_warning)
                        .onPositive("확인") {

                        }
                } else {
                    listAdapter.sortByCategory(selectedCategory.value!!)

                    layout.swipeLayout.isRefreshing = false
                }

            }
        }
    }

    fun changeView(){
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
        transaction.addToBackStack(null)

        transaction.replace(R.id.mainViewArea, AddPetitionMainView())
        transaction.commit()
    }

    private fun updateList(){
        listAdapter.sortByCategory(selectedCategory.value!!)
    }

    override fun onBackKeyPressed() {
    }
}