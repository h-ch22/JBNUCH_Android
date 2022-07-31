package kr.ac.jbnu.ch.userManagement.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.example.awesomedialog.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutResetpasswordBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class ResetPasswordView : Fragment() {
    var email = ObservableField<String>("")

    private lateinit var progressView : CircularProgressIndicator

    private val helper = UserManagement()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutResetpasswordBinding = DataBindingUtil.inflate(inflater , R.layout.layout_resetpassword , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        progressView = layout.progressView

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_confirm -> {
                if(email.get()!! == ""){
                    AwesomeDialog.build(activity as StartActivity)
                        .title("공백 필드", null, resources.getColor(R.color.black))
                        .body("모든 필드를 입력해주세요.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive("확인"){

                        }
                }

                else{
                    progressView.visibility = View.VISIBLE
                    progressView.visibility = View.GONE

                    helper.sendResetPasswordMail(email.get()!!){
                        if(it){
                            AwesomeDialog.build(activity as StartActivity)
                                .title("발송 완료", null, resources.getColor(R.color.black))
                                .body("입력하신 E-Mail로 비밀번호 재설정 링크가 발송되었습니다.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_select)
                                .onPositive("확인"){
                                    fragmentManager?.popBackStack()
                                }
                        }

                        else{
                            AwesomeDialog.build(activity as StartActivity)
                                .title("오류", null, resources.getColor(R.color.black))
                                .body("요청하신 작업을 처리하는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나, 가입한 E-Mail이 맞는지 확인해주세요.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onPositive("확인"){
                                    progressView.visibility = View.GONE
                                    progressView.visibility = View.VISIBLE
                                }
                        }
                    }
                }
            }
        }
    }
}