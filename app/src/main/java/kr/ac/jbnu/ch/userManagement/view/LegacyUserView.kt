package kr.ac.jbnu.ch.userManagement.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.awesomedialog.*
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutLegacyuserBinding
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.SignUpChangeViewModel

class LegacyUserView(val email : String?) : Fragment(), onKeyBackPressedListener {
    private lateinit var view : LinearLayout
    private lateinit var inflater : LayoutInflater
    private lateinit var progressView : com.google.android.material.progressindicator.LinearProgressIndicator
    private var container : ViewGroup? = null
    private val helper = UserManagement()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutLegacyuserBinding = DataBindingUtil.inflate(inflater, R.layout.layout_legacyuser, container, false)
        layout.view = this

        this.inflater = inflater
        this.container = container
        this.view = layout.legacyUserView
        this.progressView = layout.progressView

        return layout.root
    }

    fun onButtonClick(v : View){
        when(v.id){
            R.id.btn_transferData -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.viewArea, LicenseView(SignUpChangeViewModel.TRANSFERDATA, email))
                transaction.commit()
            }

            R.id.btn_removeData -> {
                AwesomeDialog.build(activity as StartActivity)
                    .title("데이터 삭제", null, resources.getColor(R.color.black))
                    .body("데이터를 삭제하시겠습니까?\n데이터를 제거하면 복구할 수 없으며, 다시 가입하셔야합니다.", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onPositive("예"){
                        progressView.visibility = View.VISIBLE

                        helper.removeLegacyUserData(email!!){
                            if(it){
                                progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as StartActivity)
                                    .title("제거 완료", null, resources.getColor(R.color.black))
                                    .body("정상 처리되었습니다.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인"){
                                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                                        transaction.addToBackStack(null)

                                        transaction.replace(R.id.viewArea, SignInView())
                                        transaction.commit()
                                    }
                            }

                            else{
                                progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as StartActivity)
                                    .title("오류", null, resources.getColor(R.color.black))
                                    .body("요청하신 작업을 처리하는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인")
                            }
                        }
                    }
                    .onNegative("아니오"){

                    }
            }
        }
    }

    override fun onBackKeyPressed() {
        val activity : StartActivity = getActivity() as StartActivity
        activity.setOnKeyBackPreseedListener(null)
        activity.onBackPressed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as StartActivity).setOnKeyBackPreseedListener(this)
    }
}