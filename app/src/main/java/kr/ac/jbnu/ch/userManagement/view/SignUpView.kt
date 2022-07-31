package kr.ac.jbnu.ch.userManagement.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.example.awesomedialog.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import gun0912.tedbottompicker.TedBottomPicker
import gun0912.tedbottompicker.TedBottomSheetDialogFragment
import gun0912.tedbottompicker.TedRxBottomPicker
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutSignupBinding
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.frameworks.helper.PreferenceUtil
import kr.ac.jbnu.ch.frameworks.models.SignUpCallBackModel
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.more.view.GreetView
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.SignUpChangeViewModel
import java.net.URI

class SignUpView : Fragment(), onKeyBackPressedListener {
    var selectedCollege = MutableLiveData<String>()
    private val helper = UserManagement()
    private val collegeList = arrayOf("간호대학", "공과대학", "글로벌융합대학", "농업생명과학대학", "사범대학", "사회과학대학", "상과대학", "생활과학대학", "수의과대학", "스마트팜학과", "약학대학", "예술대학", "의과대학", "인문대학", "자연과학대학", "치과대학", "환경생명자원대학")
    private val preferenceManager = context?.let { PreferenceUtil(it, "UserInfo") }
    private var photoURi : Uri? = null
    private lateinit var dialog : AlertDialog
    private lateinit var view : ScrollView
    private lateinit var progressView : LinearProgressIndicator
    private lateinit var btn_confirm : MaterialButton

    var email = ObservableField<String>("")
    var password = ObservableField<String>("")
    var checkPassword = ObservableField<String>("")
    var name = ObservableField<String>("")
    var phone = ObservableField<String>("")
    var studentNo = ObservableField<String>("")

    init{
        selectedCollege.value = "단과대학 선택"
    }

    companion object{
        const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutSignupBinding = DataBindingUtil.inflate(inflater , R.layout.layout_signup , container , false)
        layout.view = this

        layout.lifecycleOwner = this

        progressView = layout.progressView
        btn_confirm = layout.btnSignUp
        view = layout.signUpView

        return layout.root
    }

    fun showDialog(){
        dialog = AlertDialog.Builder(context)
            .setItems(collegeList,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    selectedCollege.value = collegeList[i]
                })
            .setTitle("단과대학 선택")
            .create()

        dialog.show()
    }

    fun loadPicker(){
        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED ||
            context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }

        else{
            TedBottomPicker.with(activity as StartActivity)
                .showCameraTile(false)
                .setTitle("학생증 이미지를 선택해주세요")
                .show(TedBottomSheetDialogFragment.OnImageSelectedListener { uri ->
                    if(uri != null){
                        photoURi = uri

                        openCropActivity()
                    }
                })
        }
    }

    fun loadJBNUApp(){
        try{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.jbnu.jbnuapp")))
        }catch(e : ActivityNotFoundException){
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.jbnu.jbnuapp&hl=ko&gl=US")))
        }
    }

    fun onLoadBtnClick(){
        AwesomeDialog.build(activity as StartActivity)
            .title("원하시는 옵션을 선택하세요.", null, resources.getColor(R.color.black))
            .body("캡처한 이미지가 있으면 이미지 선택, 없는 경우 전북대앱 열기를 눌러주세요.", null, resources.getColor(R.color.black))
            .icon(R.drawable.ic_select)
            .onPositive("이미지 선택"){
                loadPicker()
            }
            .onNegative("전북대앱 열기"){
                loadJBNUApp()
            }
    }

    fun openCropActivity(){
        CropImage.activity(photoURi).setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(activity as StartActivity)
    }

    fun requestPermission(){
        AwesomeDialog.build(activity as StartActivity)
            .title("권한 상승이 필요합니다.", null, resources.getColor(R.color.black))
            .body("갤러리 이미지 로드를 위해 권한을 허용해주세요!", null, resources.getColor(R.color.black))
            .icon(R.drawable.ic_warning)
            .onPositive("확인"){
                ActivityCompat.requestPermissions(context as StartActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            }
            .onNegative("취소")
    }

    fun onTutorialButtonClick(){
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
        transaction.addToBackStack(null)

        transaction.replace(R.id.viewArea, TutorialView())
        transaction.commit()
    }

    fun onSignUpButtonClick(){
        progressView.visibility = View.VISIBLE

        if(email.get().equals("") || password.get().equals("") || checkPassword.get().equals("") || name.get().equals("") || phone.get().equals("") || studentNo.get().equals("")){
            AwesomeDialog.build(activity as StartActivity)
                .title("공백 필드", null, resources.getColor(R.color.black))
                .body("모든 필드를 입력해주세요.", null, resources.getColor(R.color.black))
                .icon(R.drawable.ic_warning)
                .onNegative("확인")
        }

        else if(!password.get().equals(checkPassword.get())){
            AwesomeDialog.build(activity as StartActivity)
                .title("비밀번호 불일치", null, resources.getColor(R.color.black))
                .body("비밀번호가 일치하지 않습니다.", null, resources.getColor(R.color.black))
                .icon(R.drawable.ic_warning)
                .onNegative("확인")
        }

        else if(password.get()?.length ?: 0 < 6){
            AwesomeDialog.build(activity as StartActivity)
                .title("취약한 비밀번호", null, resources.getColor(R.color.black))
                .body("안전을 위해 6자리 이상의 비밀번호를 입력해주세요.", null, resources.getColor(R.color.black))
                .icon(R.drawable.ic_warning)
                .onNegative("확인")
        }

        else if(selectedCollege.value.equals("단과대학 선택") || selectedCollege.value.equals("")){
            AwesomeDialog.build(activity as StartActivity)
                .title("단과대학 선택", null, resources.getColor(R.color.black))
                .body("소속 단과대학을 선택하세요.", null, resources.getColor(R.color.black))
                .icon(R.drawable.ic_warning)
                .onNegative("확인")
        }

        else if(!email.get()?.contains("@")!!){
            AwesomeDialog.build(activity as StartActivity)
                .title("잘못된 E-Mail 형식", null, resources.getColor(R.color.black))
                .body("올바른 형식의 E-Mail을 입력해주세요.", null, resources.getColor(R.color.black))
                .icon(R.drawable.ic_warning)
                .onNegative("확인")
        }

        else if(photoURi == null){
            AwesomeDialog.build(activity as StartActivity)
                .title("학생증 이미지 선택", null, resources.getColor(R.color.black))
                .body("모바일 학생증 이미지를 선택하세요.", null, resources.getColor(R.color.black))
                .icon(R.drawable.ic_warning)
                .onNegative("확인")
        }

        else{
            btn_confirm.visibility = View.GONE
            progressView.visibility = View.VISIBLE

            context?.let {
                helper.signUp(email.get()!!.lowercase(), password.get()!!, name.get()!!, studentNo.get()!!, phone.get()!!, selectedCollege.value!!,
                    photoURi!!,
                    it, SignUpChangeViewModel.SIGNUP
                ){
                    when(it){
                        SignUpCallBackModel.SUCCESS -> {
                            preferenceManager?.setString("SignIn_email", email.get()!!)
                            preferenceManager?.setString("SignIn_password", password.get()!!)

                            progressView.visibility = View.GONE
                            btn_confirm.visibility = View.VISIBLE

                            val intent = Intent(activity as StartActivity, MainActivity :: class.java)
                            startActivity(intent)
                        }

                        SignUpCallBackModel.STUDENT_NO_ALREADY_EXISTS -> {
                            progressView.visibility = View.GONE
                            btn_confirm.visibility = View.VISIBLE

                            AwesomeDialog.build(activity as StartActivity)
                                .title("이미 존재하는 학번", null, resources.getColor(R.color.black))
                                .body("이미 가입된 학번입니다.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onNegative("확인")
                        }

                        SignUpCallBackModel.LEGACY_USER -> {
                            progressView.visibility = View.GONE
                            btn_confirm.visibility = View.VISIBLE

                            AwesomeDialog.build(activity as StartActivity)
                                .title("데이터 변환 필요", null, resources.getColor(R.color.black))
                                .body("데이터 변환이 필요한 사용자입니다. (공대앱 사용자)", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onPositive("데이터 변환"){
                                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                                    transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                                    transaction.addToBackStack(null)

                                    transaction.replace(R.id.viewArea, LegacyUserView(email.get()))
                                    transaction.commit()
                                }
                                .onNegative("취소")
                        }

                        SignUpCallBackModel.EMAIL_EXISTS -> {
                            progressView.visibility = View.GONE
                            btn_confirm.visibility = View.VISIBLE

                            AwesomeDialog.build(activity as StartActivity)
                                .title("E-Mail 중복", null, resources.getColor(R.color.black))
                                .body("이미 가입된 E-Mail 입니다.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onPositive("확인")
                                .onNegative("취소")
                        }

                        SignUpCallBackModel.FAIL_TO_CHECK_ID_CARD -> {
                            progressView.visibility = View.GONE
                            btn_confirm.visibility = View.VISIBLE

                            AwesomeDialog.build(activity as StartActivity)
                                .title("학생증 유효성 검사 실패", null, resources.getColor(R.color.black))
                                .body("학생증 유효성 검사를 진행할 수 없습니다.\n인적사항이 모두 나오게 자르신 후 다시 시도하세요.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onPositive("확인")
                                .onNegative("취소")
                        }

                        SignUpCallBackModel.FAIL -> {
                            progressView.visibility = View.GONE
                            btn_confirm.visibility = View.VISIBLE

                            AwesomeDialog.build(activity as StartActivity)
                                .title("오류", null, resources.getColor(R.color.black))
                                .body("내부 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하세요.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onPositive("확인")
                                .onNegative("취소")
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_REQUEST_CODE -> {
                if(grantResults.isEmpty()){
                    Snackbar.make(view, "권한이 허용되지 않았습니다.", Snackbar.LENGTH_LONG).show()
                }

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadPicker()
                }

                else{
                    Snackbar.make(view, "권한이 허용되지 않았습니다.", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)

                if(resultCode == Activity.RESULT_OK){
                    result.uri?.let {
                        photoURi = result.uri
                    }
                }

                else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Snackbar.make(view, "이미지를 자르는 중 오류가 발생했습니다.", Snackbar.LENGTH_LONG).show()
                }

            }

        }
    }

    override fun onBackKeyPressed() {

    }
}