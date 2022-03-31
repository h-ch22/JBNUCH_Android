package kr.ac.jbnu.ch.userManagement.view

import android.content.Intent
import android.os.Bundle
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.example.awesomedialog.*
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutChangephoneBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class ChangePhoneView : Fragment() {
    var phone = ObservableField<String>("")
    private val helper = UserManagement()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutChangephoneBinding = DataBindingUtil.inflate(inflater , R.layout.layout_changephone , container , false)

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_confirm -> {
                if(phone.get()!! == ""){
                    AwesomeDialog.build(activity as MainActivity)
                        .title("공백 필드", null, resources.getColor(R.color.black))
                        .body("모든 필드를 채워주세요.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive("확인"){

                        }
                }

                else{
                    AwesomeDialog.build(activity as MainActivity)
                        .title("연락처 변경", null, resources.getColor(R.color.black))
                        .body("입력하신 연락처로 연락처를 변경하시겠습니까?", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive("예"){
                            helper.changePhone(phone.get()!!){
                                if(it){
                                    AwesomeDialog.build(activity as MainActivity)
                                        .title("변경 완료", null, resources.getColor(R.color.black))
                                        .body("정상 처리되었습니다.", null, resources.getColor(R.color.black))
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
                        .onNegative("아니오"){

                        }
                }
            }
        }
    }
}