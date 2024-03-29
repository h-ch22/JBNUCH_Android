package kr.ac.jbnu.ch.feedbackhub.view

import android.os.Bundle
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.example.awesomedialog.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutFeedbackDetailBinding
import kr.ac.jbnu.ch.feedbackhub.helper.FeedbackHubHelper
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubDataModel
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import java.text.SimpleDateFormat
import java.util.*

class FeedbackHubDetailView(val data : FeedbackHubDataModel, val type : String) : Fragment() {
    var answer = ObservableField<String>("")
    private lateinit var progressView : LinearProgressIndicator
    private val helper = FeedbackHubHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutFeedbackDetailBinding = DataBindingUtil.inflate(inflater , R.layout.layout_feedback_detail , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        when(type){
            "All" -> {
                if(UserManagement.userInfo?.admin != null){
                    layout.fieldAnswer.visibility = View.VISIBLE
                    layout.txtAuthor.visibility = View.VISIBLE
                    layout.btnWriteAnswer.visibility = View.VISIBLE
                }

                else{
                    fragmentManager?.popBackStack()
                }
            }

            "MyFeedback" -> {
                layout.fieldAnswer.visibility = View.GONE
                layout.txtAuthor.visibility = View.GONE
                layout.btnWriteAnswer.visibility = View.GONE
            }
        }

        if(AES256Util.decrypt(data.answer) == ""){
            layout.answerLL.visibility = View.GONE
        }

        else{
            layout.answerLL.visibility = View.VISIBLE
            layout.txtAnswer.text = AES256Util.decrypt(data.answer)
            layout.txtAnswerDate.text = AES256Util.decrypt(data.answerDate)
            layout.txtAdmin.text = "답변 작성자 : ${AES256Util.decrypt(data.answerAuthor)}"
        }

        when(AES256Util.decrypt(data.type)){
            "칭찬해요" -> layout.imgType.setImageDrawable(context?.getDrawable(R.drawable.ic_heart))
            "개선해주세요" -> layout.imgType.setImageDrawable(context?.getDrawable(R.drawable.ic_improve))
            "궁금해요" -> layout.imgType.setImageDrawable(context?.getDrawable(R.drawable.ic_question))
        }

        if(AES256Util.decrypt(data.answer) == ""){
            layout.txtStatus.text = "아직 답변이 등록되지 않은 피드백입니다."
        }

        else{
            layout.txtStatus.text = "피드백에 대한 답변이 등록되었습니다."
        }

        layout.txtTitle.text = AES256Util.decrypt(data.title)
        layout.txtAuthor.text = AES256Util.decrypt(data.author)
        layout.txtType.text = AES256Util.decrypt(data.type)
        layout.txtDate.text = AES256Util.decrypt(data.date)
        layout.txtContents.text = AES256Util.decrypt(data.contents)

        this.progressView = layout.progressView

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_writeAnswer -> {
                if(answer.get()!! == ""){
                    AwesomeDialog.build(activity as MainActivity)
                        .title(resources.getString(R.string.TXT_ALERT_TITLE_EMPTY_FIELD), null, resources.getColor(R.color.black))
                        .body(resources.getString(R.string.TXT_ALERT_CONTENTS_EMPTY_FIELD), null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive(resources.getString(R.string.TXT_OK))
                }

                else{
                    if(UserManagement.userInfo?.admin == null){
                        fragmentManager?.popBackStack()
                    }

                    else{
                        progressView.visibility = View.VISIBLE

                        val dateFormatter = SimpleDateFormat("yyyy. MM. dd. kk:mm:ss")
                        val dateAsString = dateFormatter.format(Date())

                        val userManagement = UserManagement()
                        userManagement.convertAdminCodeAsString(UserManagement.userInfo?.admin){
                            if(it != "" && it != null){
                                helper.uploadAnswer(data.id, answer.get()!!, dateAsString, it){
                                    if(it){
                                        progressView.visibility = View.GONE

                                        AwesomeDialog.build(activity as MainActivity)
                                            .title(resources.getString(R.string.TXT_ALERT_TITLE_UPLOAD_SUCCESS), null, resources.getColor(R.color.black))
                                            .body(resources.getString(R.string.TXT_ALERT_CONTENTS_UPLOAD_SUCCESS), null, resources.getColor(R.color.black))
                                            .icon(R.drawable.ic_select)
                                            .onPositive(resources.getString(R.string.TXT_OK))
                                    }

                                    else{
                                        progressView.visibility = View.GONE

                                        AwesomeDialog.build(activity as MainActivity)
                                            .title(resources.getString(R.string.TXT_ERROR), null, resources.getColor(R.color.black))
                                            .body(resources.getString(R.string.TXT_ALERT_CONTENTS_ERROR), null, resources.getColor(R.color.black))
                                            .icon(R.drawable.ic_warning)
                                            .onPositive(resources.getString(R.string.TXT_OK))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}