package kr.ac.jbnu.ch.userManagement.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.example.awesomedialog.*
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutAffiliatesListBinding
import kr.ac.jbnu.ch.databinding.LayoutChangepasswordBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class ChangePasswordView : Fragment() {
    var password = ObservableField<String>("")
    var checkPassword = ObservableField<String>("")

    private val helper = UserManagement()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutChangepasswordBinding = DataBindingUtil.inflate(inflater , R.layout.layout_changepassword , container , false)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        layout.view = this
        layout.lifecycleOwner = this

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_resetPassword -> {
                if(password != checkPassword){
                    AwesomeDialog.build(activity as MainActivity)
                        .title("비밀번호 불일치", null, resources.getColor(R.color.black))
                        .body("입력한 비밀번호와 비밀번호 확인이 일치하지 않습니다.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive("확인"){

                        }
                }

                else if(password.get()!! == ""){
                    AwesomeDialog.build(activity as MainActivity)
                        .title("공백 필드", null, resources.getColor(R.color.black))
                        .body("모든 필드를 채워주세요.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive("확인"){

                        }
                }

                else if(password.get()!!.length < 6){
                    AwesomeDialog.build(activity as MainActivity)
                        .title("취약한 비밀번호", null, resources.getColor(R.color.black))
                        .body("보안을 위해 6자리 이상의 비밀번호를 입력해주세요.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive("확인"){

                        }
                }

                else{
                    helper.changePassword(password.get()!!){
                        if(it){
                            AwesomeDialog.build(activity as MainActivity)
                                .title("비밀번호 변경 완료", null, resources.getColor(R.color.black))
                                .body("비밀번호가 변경되었습니다.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_select)
                                .onPositive("확인"){
                                    fragmentManager?.popBackStack()
                                }
                        }

                        else{
                            AwesomeDialog.build(activity as MainActivity)
                                .title("오류", null, resources.getColor(R.color.black))
                                .body("요청하신 작업을 처리하는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onPositive("확인"){

                                }
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId){
            R.id.btn_toolbarBack -> {
                (activity as MainActivity).onBackPressed()
            }
        }

        return true
    }
}