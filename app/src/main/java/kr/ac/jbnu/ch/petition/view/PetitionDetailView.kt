package kr.ac.jbnu.ch.petition.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomedialog.*
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutPetitiondetailBinding
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.frameworks.models.ListViewDecoration
import kr.ac.jbnu.ch.frameworks.view.FullScreenImageView
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.notice.models.NoticeImageAdapter
import kr.ac.jbnu.ch.petition.helper.PetitionHelper
import kr.ac.jbnu.ch.petition.models.*
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class PetitionDetailView(private val data : PetitionDataModel): Fragment() {
    private val helper = PetitionHelper()

    private lateinit var layout : LayoutPetitiondetailBinding
    private lateinit var participantsLL : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = DataBindingUtil.inflate(inflater , R.layout.layout_petitiondetail , container , false)
        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)

        layout.view = this
        layout.lifecycleOwner = this
        this.participantsLL = layout.participantsLL

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        helper.getRecommender(data){
            if(it){
                layout.progressViewRecommender.visibility = View.GONE
                layout.participantsLL.visibility = View.VISIBLE

                Log.d("PetitionDetailView", PetitionHelper.recommendList.toString())

                val decorationHeight = ListViewDecoration(20)
                val listAdapter = PetitionRecommendListAdapter()

                participantsLL.apply{
                    layoutManager = LinearLayoutManager(activity)
                    adapter = listAdapter
                    addItemDecoration(decorationHeight)
                }
            }

            else{
                layout.progressViewRecommender.visibility = View.GONE
                layout.participantsLL.visibility = View.VISIBLE
            }
        }

        if(AES256Util.decrypt(data.author) == UserManagement.userInfo?.uid){
            layout.btnRemove.visibility = View.VISIBLE
        }

        else{
            layout.btnRecommend.visibility = View.VISIBLE
        }

        when(data.status){
            PetitionStatusModel.INPROCESS -> {
                layout.progressInProgress.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.accent))
                layout.txtInProgress.setTextColor(resources.getColor(R.color.accent))

                layout.progressAnswered.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtAnswered.setTextColor(resources.getColor(R.color.gray))

                layout.progressDone.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtDone.setTextColor(resources.getColor(R.color.gray))
            }

            PetitionStatusModel.FINISH -> {
                layout.progressInProgress.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtInProgress.setTextColor(resources.getColor(R.color.gray))

                layout.progressAnswered.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.accent))
                layout.txtAnswered.setTextColor(resources.getColor(R.color.accent))

                layout.progressDone.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtDone.setTextColor(resources.getColor(R.color.gray))
            }

            PetitionStatusModel.ANSWERED -> {
                layout.progressInProgress.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtInProgress.setTextColor(resources.getColor(R.color.gray))

                layout.progressAnswered.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtAnswered.setTextColor(resources.getColor(R.color.gray))

                layout.progressDone.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.accent))
                layout.txtDone.setTextColor(resources.getColor(R.color.accent))
            }
        }

        if(data.imageIndex > 0){
            layout.imgList.visibility = View.VISIBLE
            layout.pageIndicatorView.visibility = View.VISIBLE

            helper.getImage(data.id, data.imageIndex){
                if(it){
                    val adapter = context?.let { PetitionImageAdapter(it, data) }
                    layout.imgList.adapter = adapter

                    adapter?.setOnItemClickListener(object : PetitionImageAdapter.OnItemClickListener{
                        override fun onItemClick(v: View, url: StorageReference) {
                            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                            transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                            transaction.addToBackStack(null)

                            transaction.replace(R.id.mainViewArea, FullScreenImageView(url))
                            transaction.commit()
                        }

                    })
                }
            }
        }

        else{
            layout.imgList.visibility = View.GONE
            layout.pageIndicatorView.visibility = View.GONE
        }

        layout.txtContents.text = AES256Util.decrypt(data.contents)
        title.text = AES256Util.decrypt(data.title)

        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun onClick(v:View){
        when(v.id){
            R.id.btn_recommend -> {
                val checkExists : PetitionRecommendModel? = PetitionHelper.recommendList.find{AES256Util.decrypt(it.uid) == UserManagement.userInfo?.uid ?: ""}

                if(checkExists != null){
                    AwesomeDialog.build(activity as MainActivity)
                        .title("이미 추천한 청원", null, resources.getColor(R.color.black))
                        .body("이미 추천한 청원입니다.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_select)
                        .onPositive("확인"){
                        }
                }

                else{
                    AwesomeDialog.build(activity as MainActivity)
                        .title("추천 확인", null, resources.getColor(R.color.black))
                        .body("추천하시겠습니까?", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_select)
                        .onPositive("예"){
                            layout.progressView.visibility = View.VISIBLE

                            helper.recommend(data){
                                if(it){
                                    layout.progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                        .title("추천 완료", null, resources.getColor(R.color.black))
                                        .body("정상 처리되었습니다.", null, resources.getColor(R.color.black))
                                        .icon(R.drawable.ic_select)
                                        .onPositive("확인"){
                                            layout.progressView.visibility = View.GONE
                                        }
                                }

                                else{
                                    layout.progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                        .title("오류", null, resources.getColor(R.color.black))
                                        .body("요청하신 작업을 처리하는 중 문제가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                        .icon(R.drawable.ic_warning)
                                        .onPositive("확인"){
                                        }
                                }
                            }
                        }
                        .onNegative("아니오"){

                        }
                }
            }

            R.id.btn_remove -> {
                AwesomeDialog.build(activity as MainActivity)
                    .title("삭제 확인", null, resources.getColor(R.color.black))
                    .body("삭제하시겠습니까?", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_select)
                    .onPositive("예"){
                        layout.progressView.visibility = View.VISIBLE

                        helper.remove(data){
                            if(it){
                                layout.progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("삭제 완료", null, resources.getColor(R.color.black))
                                    .body("정상 처리되었습니다.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_select)
                                    .onPositive("확인"){
                                        layout.progressView.visibility = View.GONE
                                    }
                            }

                            else{
                                layout.progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("오류", null, resources.getColor(R.color.black))
                                    .body("요청하신 작업을 처리하는 중 문제가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인"){
                                        fragmentManager?.popBackStack()
                                    }
                            }
                        }
                    }
                    .onNegative("아니오"){

                    }
            }
        }
    }
}