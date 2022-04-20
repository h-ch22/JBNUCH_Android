package kr.ac.jbnu.ch.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.awesomedialog.*
import com.google.android.material.button.MaterialButton
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutProductBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.pledge.view.PledgeView
import kr.ac.jbnu.ch.products.helper.ProductsHelper
import kr.ac.jbnu.ch.products.models.CollegeProductListAdapter
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel
import java.util.*

class ProductView : Fragment() {
    private val helper = ProductsHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutProductBinding = DataBindingUtil.inflate(inflater , R.layout.layout_product , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC ||
                UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH ||
                UserManagement.userInfo?.collegeCode == CollegeCodeModel.CHE ||
                UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC ||
                UserManagement.userInfo?.collegeCode == CollegeCodeModel.CON){
            layout.toggleGroupProductCategory.visibility = View.VISIBLE
        }

        else{
            layout.toggleGroupProductCategory.visibility = View.GONE
        }

        val btn_logList : MaterialButton = layout.toolbar.findViewById(R.id.btn_showLog)

        btn_logList.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, ProductLogView())
                transaction.commit()
            }

        })

        val now = Calendar.getInstance()
        val currentHourIn24Format : Int = now.get(Calendar.HOUR_OF_DAY)

        if(currentHourIn24Format == 13){
            layout.imgStatus.setImageResource(R.drawable.ic_warning)
            layout.txtStatus.text = "점심시간입니다. (평일 13시 ~ 14시)"
            layout.txtStatus.setTextColor(resources.getColor(R.color.red))
        }

        else if(currentHourIn24Format >= 9 && currentHourIn24Format < 18){
            layout.imgStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_select))
            layout.txtStatus.text = "민원사업을 정상적으로 이용하실 수 있습니다."
            layout.txtStatus.setTextColor(resources.getColor(R.color.green))
        }

        else{
            layout.imgStatus.setImageResource(R.drawable.ic_warning)
            layout.txtStatus.text = "지금은 민원사업을 이용하실 수 없습니다.\n(평일 09시 ~ 18시, 점심시간 : 12시 ~ 13시)"
            layout.txtStatus.setTextColor(resources.getColor(R.color.red))
        }

        val dayNum = now.get(Calendar.DAY_OF_WEEK)

        when(dayNum){
            1, 7 -> {
                layout.imgStatus.setImageResource(R.drawable.ic_warning)
                layout.txtStatus.text = "지금은 민원사업을 이용하실 수 없습니다.\n(평일 09시 ~ 18시, 점심시간 : 12시 ~ 13시)"
                layout.txtStatus.setTextColor(resources.getColor(R.color.red))
            }

            else -> {

            }
        }

        layout.toggleGroupProductCategory.setOnCheckedChangeListener(object : SingleSelectToggleGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: SingleSelectToggleGroup?, checkedId: Int) {
                when(checkedId){
                    R.id.btn_collegeCategory_CH -> {
                        layout.CHProductListLL.visibility = View.VISIBLE
                        layout.CollegeProductListLL.visibility = View.GONE

                        helper.getProductList("CH"){
                            if(!it){
                                layout.swipeLayout.isRefreshing = false


                                AwesomeDialog.build(activity as MainActivity)
                                    .title("데이터를 불러올 수 없음", null, resources.getColor(R.color.black))
                                    .body("데이터를 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인"){

                                    }
                            }

                            else{
                                for(product in ProductsHelper.productList){
                                    when(product.productName){
                                        "CurlingIron" -> {
                                            layout.txtLateCurlingIron.text = "${product.late} / ${product.all}"
                                        }

                                        "basketBall" -> {
                                            layout.txtLateBasketball.text = "${product.late} / ${product.all}"
                                        }

                                        "flag" -> {
                                            layout.txtLateFlag.text = "${product.late} / ${product.all}"
                                        }

                                        "footBall" -> {
                                            layout.txtLateFootball.text = "${product.late} / ${product.all}"
                                        }

                                        "futsalBall" -> {
                                            layout.txtLateFutsal.text = "${product.late} / ${product.all}"
                                        }

                                        "mat" -> {
                                            layout.txtLateMat.text = "${product.late} / ${product.all}"
                                        }

                                        "net" -> {
                                            layout.txtLateNet.text = "${product.late} / ${product.all}"
                                        }

                                        "soccerBall" -> {
                                            layout.txtLateSoccerball.text = "${product.late} / ${product.all}"
                                        }

                                        "uniform_blue" -> {
                                            layout.txtLateUniformB.text = "${product.late} / ${product.all}"
                                        }

                                        "uniform_green" -> {
                                            layout.txtLateUniformG.text = "${product.late} / ${product.all}"
                                        }

                                        "uniform_pink" -> {
                                            layout.txtLateUniformP.text = "${product.late} / ${product.all}"
                                        }

                                        else -> {}
                                    }
                                }

                                layout.swipeLayout.isRefreshing = false
                            }
                        }
                    }

                    R.id.btn_collegeCategory_college -> {
                        layout.CHProductListLL.visibility = View.GONE
                        layout.CollegeProductListLL.visibility = View.VISIBLE

                        helper.getProductList("College"){
                            if(!it){
                                layout.swipeLayout.isRefreshing = false


                                AwesomeDialog.build(activity as MainActivity)
                                    .title("데이터를 불러올 수 없음", null, resources.getColor(R.color.black))
                                    .body("데이터를 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인"){

                                    }
                            }

                            else{
                                layout.CollegeProductListLL.apply{
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = CollegeProductListAdapter()
                                }

                                layout.swipeLayout.isRefreshing = false
                            }
                        }
                    }
                }
            }

        })



        helper.getProductList("CH"){
            if(!it){
                layout.progressView.visibility = View.GONE

                AwesomeDialog.build(activity as MainActivity)
                    .title("데이터를 불러올 수 없음", null, resources.getColor(R.color.black))
                    .body("데이터를 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onPositive("확인"){

                    }
            }

            else{
                for(product in ProductsHelper.productList){
                    when(product.productName){
                        "CurlingIron" -> {
                            layout.txtLateCurlingIron.text = "${product.late} / ${product.all}"
                        }

                        "basketBall" -> {
                            layout.txtLateBasketball.text = "${product.late} / ${product.all}"
                        }

                        "flag" -> {
                            layout.txtLateFlag.text = "${product.late} / ${product.all}"
                        }

                        "footBall" -> {
                            layout.txtLateFootball.text = "${product.late} / ${product.all}"
                        }

                        "futsalBall" -> {
                            layout.txtLateFutsal.text = "${product.late} / ${product.all}"
                        }

                        "mat" -> {
                            layout.txtLateMat.text = "${product.late} / ${product.all}"
                        }

                        "net" -> {
                            layout.txtLateNet.text = "${product.late} / ${product.all}"
                        }

                        "soccerBall" -> {
                            layout.txtLateSoccerball.text = "${product.late} / ${product.all}"
                        }

                        "uniform_blue" -> {
                            layout.txtLateUniformB.text = "${product.late} / ${product.all}"
                        }

                        "uniform_green" -> {
                            layout.txtLateUniformG.text = "${product.late} / ${product.all}"
                        }

                        "uniform_pink" -> {
                            layout.txtLateUniformP.text = "${product.late} / ${product.all}"
                        }

                        else -> {}
                    }
                }

                layout.progressView.visibility = View.GONE
                layout.swipeLayout.visibility = View.VISIBLE
            }

            layout.swipeLayout.setColorSchemeColors(resources.getColor(R.color.accent))

            layout.swipeLayout.setOnRefreshListener {
                helper.getProductList("CH"){
                    if(!it){
                        layout.swipeLayout.isRefreshing = false


                        AwesomeDialog.build(activity as MainActivity)
                            .title("데이터를 불러올 수 없음", null, resources.getColor(R.color.black))
                            .body("데이터를 불러오는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                            .icon(R.drawable.ic_warning)
                            .onPositive("확인"){

                            }
                    }

                    else{

                        for(product in ProductsHelper.productList){
                            when(product.productName){
                                "CurlingIron" -> {
                                    layout.txtLateCurlingIron.text = "${product.late} / ${product.all}"
                                }

                                "basketBall" -> {
                                    layout.txtLateBasketball.text = "${product.late} / ${product.all}"
                                }

                                "flag" -> {
                                    layout.txtLateFlag.text = "${product.late} / ${product.all}"
                                }

                                "footBall" -> {
                                    layout.txtLateFootball.text = "${product.late} / ${product.all}"
                                }

                                "futsalBall" -> {
                                    layout.txtLateFutsal.text = "${product.late} / ${product.all}"
                                }

                                "mat" -> {
                                    layout.txtLateMat.text = "${product.late} / ${product.all}"
                                }

                                "net" -> {
                                    layout.txtLateNet.text = "${product.late} / ${product.all}"
                                }

                                "soccerBall" -> {
                                    layout.txtLateSoccerball.text = "${product.late} / ${product.all}"
                                }

                                "uniform_blue" -> {
                                    layout.txtLateUniformB.text = "${product.late} / ${product.all}"
                                }

                                "uniform_green" -> {
                                    layout.txtLateUniformG.text = "${product.late} / ${product.all}"
                                }

                                "uniform_pink" -> {
                                    layout.txtLateUniformP.text = "${product.late} / ${product.all}"
                                }

                                else -> {}
                            }
                        }

                        layout.swipeLayout.isRefreshing = false
                    }
                }
            }

            layout.scrollView.viewTreeObserver.addOnScrollChangedListener {
                layout.swipeLayout.isEnabled = (layout.scrollView.scrollY == 0)
            }

        }

        return layout.root
    }
}