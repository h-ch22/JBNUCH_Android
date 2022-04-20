package kr.ac.jbnu.ch.userManagement.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.example.awesomedialog.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutLegacyuserUserinfoBinding
import kr.ac.jbnu.ch.frameworks.models.SignUpCallBackModel
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.SignUpChangeViewModel

class LegacyUserInfoView(val email : String?) : Fragment() {
    private lateinit var view : LinearLayout
    private lateinit var progressView : CircularProgressIndicator
    private lateinit var btn_next : MaterialButton
    private lateinit var binding : LayoutLegacyuserUserinfoBinding

    var name : MutableLiveData<String> = MutableLiveData<String>()
    var dept : MutableLiveData<String> = MutableLiveData<String>()
    var studentNo : MutableLiveData<String> = MutableLiveData<String>()
    var phone : MutableLiveData<String> = MutableLiveData<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.layout_legacyuser_userinfo, container, false)

        binding.lifecycleOwner = this
        binding.view = this

        view = binding.legacyUserInfoView
        progressView = binding.progressView
        btn_next = binding.btnNextStep

        getData()

        return binding.root
    }

    private fun getData(){
        progressView.visibility = View.VISIBLE

        UserManagement.getLegacyUserInfo(email ?: ""){
            if(!it){
                progressView.visibility = View.GONE

                AwesomeDialog.build(activity as StartActivity)
                    .title("오류", null, resources.getColor(R.color.black))
                    .body("사용자 정보를 로드하는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나, 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onNegative("확인"){
                        fragmentManager?.popBackStack()
                    }
            }

            else{
                progressView.visibility = View.GONE
                btn_next.visibility = View.VISIBLE

                binding.legacyUserData = UserManagement.legacyUserInfo
            }
        }
    }

    fun process(){
        progressView.visibility = View.VISIBLE

        val helper = UserManagement()

        helper.registerLegacyUser(email!!.replace(System.getProperty("line.separator"), ""), getRandomString(128), "공과대학", UserManagement.legacyUserInfo!!.name, UserManagement.legacyUserInfo!!.studentNo, UserManagement.legacyUserInfo!!.phone){
            if(it){
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.viewArea, CheckEmailView(email))
                transaction.commit()
            }

            else{
                AwesomeDialog.build(activity as StartActivity)
                    .title("오류", null, resources.getColor(R.color.black))
                    .body("요청하신 작업을 처리하는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나, 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onNegative("확인"){
                        fragmentManager?.popBackStack()
                    }
            }
        }
    }

    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }


}