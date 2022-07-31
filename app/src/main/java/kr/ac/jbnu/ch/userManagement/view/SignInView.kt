package kr.ac.jbnu.ch.userManagement.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutSigninBinding
import androidx.fragment.app.FragmentTransaction
import com.example.awesomedialog.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import kr.ac.jbnu.ch.frameworks.helper.PreferenceUtil
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.SignUpChangeViewModel
import kr.ac.jbnu.ch.userManagement.models.UserManagementResultModel
import kotlin.system.exitProcess


class SignInView : Fragment(), onKeyBackPressedListener {
    private lateinit var inflater : LayoutInflater
    private lateinit var progressView : CircularProgressIndicator
    private lateinit var btn_signIn : MaterialButton
    private var container : ViewGroup? = null
    private val helper = UserManagement()
    private val preferenceManager = context?.let { PreferenceUtil(it, "UserInfo") }

    var email = ObservableField<String>("")
    var password = ObservableField<String>("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutSigninBinding = DataBindingUtil.inflate(inflater , R.layout.layout_signin , container , false)
        layout.view = this

        if(preferenceManager?.getPreferences("SignIn_email", "") != null &&
                preferenceManager.getPreferences("SignIn_password", "") != ""){
            processSignIn()
        }

        this.inflater = inflater
        this.container = container
        this.progressView = layout.progressView
        this.btn_signIn = layout.btnSignIn

        return layout.root
    }

    private fun processSignIn(){
        if(email.get() == null || password.get() == null || email.get().equals("") || password.get().equals("")){
            AwesomeDialog.build(activity as StartActivity)
                .title("공백 필드", null, resources.getColor(R.color.black))
                .body("모든 필드를 입력해주세요.", null, resources.getColor(R.color.black))
                .icon(R.drawable.ic_warning)
                .onPositive("확인")
        }

        else{
            progressView.visibility = View.VISIBLE
            btn_signIn.visibility = View.GONE

            helper.signIn(email.get()!!, password.get()!!){
                when(it){
                    UserManagementResultModel.userNotFound -> {
                        progressView.visibility = View.GONE
                        btn_signIn.visibility = View.VISIBLE

                        AwesomeDialog.build(activity as StartActivity)
                            .title("사용자를 찾을 수 없음", null, resources.getColor(R.color.black))
                            .body("가입되지 않은 사용자입니다.", null, resources.getColor(R.color.black))
                            .icon(R.drawable.ic_warning)
                            .onPositive("확인")
                    }

                    UserManagementResultModel.success -> {
                        preferenceManager?.setString("SignIn_email", email.get()!!)
                        preferenceManager?.setString("SignIn_password", password.get()!!)

                        progressView.visibility = View.GONE
                        btn_signIn.visibility = View.VISIBLE

                        if(UserManagement.userInfo?.country == null){
                            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                            transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                            transaction.addToBackStack(null)

                            transaction.replace(R.id.viewArea, CountrySelectionView())
                            transaction.commit()
                        }

                        else{
                            val intent = Intent(activity as StartActivity, MainActivity :: class.java)
                            startActivity(intent)
                        }
                    }

                    UserManagementResultModel.legacyUser -> {
                        progressView.visibility = View.GONE
                        btn_signIn.visibility = View.VISIBLE

                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                        transaction.addToBackStack(null)

                        transaction.replace(R.id.viewArea, LegacyUserView(email.get()!!))
                        transaction.commit()
                    }

                    else -> {
                        progressView.visibility = View.GONE
                        btn_signIn.visibility = View.VISIBLE

                        AwesomeDialog.build(activity as StartActivity)
                            .title("오류", null, resources.getColor(R.color.black))
                            .body("알 수 없는 오류가 발생했습니다.\n나중에 다시 시도하세요.", null, resources.getColor(R.color.black))
                            .icon(R.drawable.ic_warning)
                            .onPositive("확인")
                    }
                }
            }
        }
    }

    fun onClick(view : View){
        when(view.id){
            R.id.btn_resetPassword -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.viewArea, ResetPasswordView())
                transaction.commit()
            }

            R.id.btn_signUp -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.viewArea, LicenseView(SignUpChangeViewModel.SIGNUP, null))
                transaction.commit()
            }

            R.id.btn_signIn -> {
                processSignIn()
            }
        }
    }


    override fun onBackKeyPressed() {
        exitProcess(0)
    }
}