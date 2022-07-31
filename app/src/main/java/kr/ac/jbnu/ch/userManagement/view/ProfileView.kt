package kr.ac.jbnu.ch.userManagement.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.awesomedialog.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.auth.User
import com.theartofdev.edmodo.cropper.CropImage
import gun0912.tedbottompicker.TedBottomPicker
import gun0912.tedbottompicker.TedBottomSheetDialogFragment
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutProfileBinding
import kr.ac.jbnu.ch.frameworks.helper.PreferenceUtil
import kr.ac.jbnu.ch.frameworks.models.GlideApp
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.SignUpChangeViewModel

class ProfileView : Fragment() {
    val helper = UserManagement()
    private lateinit var view : ConstraintLayout
    private lateinit var progressView : CircularProgressIndicator
    private lateinit var img_Profile : ImageView
    private var photoURi : Uri? = null

    private val preferenceManager = context?.let { PreferenceUtil(it, "UserInfo") }

    companion object{
        const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutProfileBinding = DataBindingUtil.inflate(inflater, R.layout.layout_profile, container, false)
        layout.view = this
        layout.lifecycleOwner = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        this.view = layout.profileView
        this.progressView = layout.progressView
        img_Profile = layout.imgProfile

        layout.txtName.text = UserManagement.userInfo?.name
        layout.txtCollege.text = UserManagement.userInfo?.college + " " + UserManagement.userInfo?.studentNo

        loadProfile()

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_changeProfile -> {
                loadPicker()
            }

            R.id.btn_changePhone -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, ChangePhoneView())
                transaction.commit()
            }

            R.id.btn_changePassword -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, ChangePasswordView())
                transaction.commit()
            }

            R.id.btn_signOut -> {
                AwesomeDialog.build(activity as MainActivity)
                    .title("로그아웃 확인", null, resources.getColor(R.color.black))
                    .body("로그아웃 시 자동 로그인이 해제되며 다시 로그인하셔야합니다.\n계속 하시겠습니까?", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onPositive("예"){
                        progressView.visibility = View.VISIBLE

                        helper.signOut {
                            progressView.visibility = View.GONE

                            if(it){
                                preferenceManager?.removeString("SignIn_email")
                                preferenceManager?.removeString("SignIn_password")

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("로그아웃 완료", null, resources.getColor(R.color.black))
                                    .body("정상 처리되었습니다.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_select)
                                    .onPositive("확인"){
                                        val intent = Intent(context, StartActivity::class.java)
                                        startActivity(intent)
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

            R.id.btn_secession -> {
                AwesomeDialog.build(activity as MainActivity)
                    .title("회원탈퇴 확인", null, resources.getColor(R.color.black))
                    .body("회원탈퇴 하시겠습니까?", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onPositive("예"){
                        progressView.visibility = View.VISIBLE

                        helper.secession {
                            progressView.visibility = View.GONE

                            if(it){
                                preferenceManager?.removeString("SignIn_email")
                                preferenceManager?.removeString("SignIn_password")

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("탈퇴 완료", null, resources.getColor(R.color.black))
                                    .body("정상 처리되었습니다.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_select)
                                    .onPositive("확인"){
                                        val intent = Intent(context, StartActivity::class.java)
                                        startActivity(intent)
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

    fun loadProfile(){
        if(UserManagement.userInfo?.profile != null){
            context?.let {
                GlideApp.with(it)
                    .load(UserManagement.userInfo?.profile)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img_Profile)
            }
        }
    }

    fun loadPicker(){
        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED ||
            context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }

        else{
            TedBottomPicker.with(activity as MainActivity)
                .showCameraTile(false)
                .setTitle("변경할 프로필 이미지를 선택해주세요")
                .showCameraTile(true)
                .show(TedBottomSheetDialogFragment.OnImageSelectedListener { uri ->
                    if(uri != null){
                        photoURi = uri

                        progressView.visibility = View.VISIBLE

                        helper.changeProfile(uri){
                            if(it){
                                progressView.visibility = View.GONE

                                loadProfile()
                            }

                            else{
                                progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("오류", null, resources.getColor(R.color.black))
                                    .body("요청하신 작업을 처리하는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인"){

                                    }
                            }
                        }
                    }
                })
        }
    }

    fun requestPermission(){
        AwesomeDialog.build(activity as MainActivity)
            .title("권한 상승이 필요합니다.", null, resources.getColor(R.color.black))
            .body("갤러리 이미지 로드를 위해 권한을 허용해주세요!", null, resources.getColor(R.color.black))
            .icon(R.drawable.ic_warning)
            .onPositive("확인"){
                ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .onNegative("취소")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            SignUpView.PERMISSION_REQUEST_CODE -> {
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
}