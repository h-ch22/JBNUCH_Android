package kr.ac.jbnu.ch.feedbackhub.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.awesomedialog.*
import com.google.android.material.button.MaterialButton
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutFeedbackhubBinding
import kr.ac.jbnu.ch.databinding.LayoutFeedbackhubWriteBinding
import kr.ac.jbnu.ch.feedbackhub.helper.FeedbackHubHelper
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubDataModel
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubItemModel
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubTypeModel
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class FeedbackHubMainView(val category : FeedbackHubItemModel) : Fragment() {
    var title = ObservableField<String>("")
    var contents = ObservableField<String>("")
    lateinit var layout : LayoutFeedbackhubWriteBinding

    private var btnList = ArrayList<MaterialButton>()
    private var selectedType : FeedbackHubTypeModel? = null

    private val helper = FeedbackHubHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutFeedbackhubWriteBinding = DataBindingUtil.inflate(inflater , R.layout.layout_feedbackhub_write , container , false)
        this.layout = layout

        layout.view = this
        layout.lifecycleOwner = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        val btn_send : Button = layout.toolbar.findViewById(R.id.btn_sendFeedback)

        btn_send.setOnClickListener {
            AwesomeDialog.build(activity as MainActivity)
                .title(resources.getString(R.string.TXT_ALERT_TITLE_CONFIRM_UPLOAD), null, resources.getColor(R.color.black))
                .body(resources.getString(R.string.TXT_ALERT_CONTENTS_CONFIRM_UPLOAD), null, resources.getColor(R.color.black))
                .icon(R.drawable.ic_select)
                .onPositive(resources.getString(R.string.TXT_YES)){
                    if(title.get()!! == "" || contents.get()!! == ""){
                        AwesomeDialog.build(activity as MainActivity)
                            .title(resources.getString(R.string.TXT_ALERT_TITLE_EMPTY_FIELD), null, resources.getColor(R.color.black))
                            .body(resources.getString(R.string.TXT_ALERT_CONTENTS_EMPTY_FIELD), null, resources.getColor(R.color.black))
                            .icon(R.drawable.ic_warning)
                            .onPositive(resources.getString(R.string.TXT_OK))
                    }

                    else if(selectedType == null){
                        AwesomeDialog.build(activity as MainActivity)
                            .title(resources.getString(R.string.TXT_ALERT_TITLE_SELECT_FEEDBACK_TYPE), null, resources.getColor(R.color.black))
                            .body(resources.getString(R.string.TXT_ALERT_CONTENTS_SELECT_FEEDBACK_TYPE), null, resources.getColor(R.color.black))
                            .icon(R.drawable.ic_warning)
                            .onPositive("확인")
                    }

                    else{
                        layout.progressView.visibility = View.VISIBLE

                        helper.uploadFeedback(FeedbackHubDataModel(title.get()!!, contents.get()!!, FeedbackHubHelper.convertCategoryAsString(category), FeedbackHubHelper.convertTypeAsString(selectedType!!), "${UserManagement.userInfo?.college} ${UserManagement.userInfo?.studentNo} ${UserManagement.userInfo?.name}", "", "", "", "", UserManagement.userInfo?.uid ?: "", "")){
                            if(it){
                                layout.progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title(resources.getString(R.string.TXT_ALERT_TITLE_UPLOAD_SUCCESS), null, resources.getColor(R.color.black))
                                    .body(resources.getString(R.string.TXT_ALERT_CONTENTS_UPLOAD_SUCCESS), null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_select)
                                    .onPositive(resources.getString(R.string.TXT_OK))
                            }

                            else{
                                layout.progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title(resources.getString(R.string.TXT_ERROR), null, resources.getColor(R.color.black))
                                    .body(resources.getString(R.string.TXT_ALERT_CONTENTS_ERROR), null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive(resources.getString(R.string.TXT_OK))
                            }
                        }
                    }
                }
                .onNegative(resources.getString(R.string.TXT_NO))
        }

        btnList.add(layout.btnHeart)
        btnList.add(layout.btnImprove)
        btnList.add(layout.btnQuestion)

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_heart -> {
                selectedType = FeedbackHubTypeModel.HEART

                for(i in 0..2){
                    when(i){
                        0 -> {
                            btnList[i].setTextColor(context?.resources!!.getColor(R.color.red))
                            btnList[i].setIconTintResource(R.color.red)
                        }

                        1 -> {
                            btnList[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                            btnList[i].setIconTintResource(R.color.gray)
                        }

                        2 -> {
                            btnList[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                            btnList[i].setIconTintResource(R.color.gray)
                        }
                    }
                }
            }

            R.id.btn_improve -> {
                selectedType = FeedbackHubTypeModel.IMPROVE

                for(i in 0..2){
                    when(i){
                        0 -> {
                            btnList[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                            btnList[i].setIconTintResource(R.color.gray)
                        }

                        1 -> {
                            btnList[i].setTextColor(context?.resources!!.getColor(R.color.blue))
                            btnList[i].setIconTintResource(R.color.blue)
                        }

                        2 -> {
                            btnList[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                            btnList[i].setIconTintResource(R.color.gray)
                        }
                    }
                }
            }

            R.id.btn_question -> {
                selectedType = FeedbackHubTypeModel.QUESTION

                for(i in 0..2){
                    when(i){
                        0 -> {
                            btnList[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                            btnList[i].setIconTintResource(R.color.gray)
                        }

                        1 -> {
                            btnList[i].setTextColor(context?.resources!!.getColor(R.color.gray))
                            btnList[i].setIconTintResource(R.color.gray)
                        }

                        2 -> {
                            btnList[i].setTextColor(context?.resources!!.getColor(R.color.orange))
                            btnList[i].setIconTintResource(R.color.orange)
                        }
                    }
                }
            }


        }
    }
}