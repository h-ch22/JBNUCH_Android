package kr.ac.jbnu.ch.notice.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.example.awesomedialog.*
import com.google.android.material.snackbar.Snackbar
import gun0912.tedbottompicker.TedBottomPicker
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutAddnoticeBinding
import kr.ac.jbnu.ch.databinding.LayoutAffiliatesListBinding
import kr.ac.jbnu.ch.databinding.LayoutWritepetitionBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.notice.helper.NoticeHelper
import kr.ac.jbnu.ch.userManagement.view.SignUpView

class AddNoticeView : Fragment() {
    var title = ObservableField<String>("")
    var contents = ObservableField<String>("")

    private var uriList : MutableList<Uri> = ArrayList<Uri>()
    private val helper = NoticeHelper()
    private lateinit var view : ConstraintLayout
    private lateinit var layout : LayoutAddnoticeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutAddnoticeBinding = DataBindingUtil.inflate(inflater , R.layout.layout_addnotice , container , false)

        layout.view = this
        layout.lifecycleOwner = this
        this.view = layout.addNoticeView

        this.layout = layout

        return layout.root
    }

    fun loadPicker(){
        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED ||
            context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }

        else{
            TedBottomPicker.with(activity as MainActivity)
                .showCameraTile(true)
                .setTitle("이미지를 선택해주세요")
                .setPeekHeight(1600)
                .setCompleteButtonText("완료")
                .setSelectedUriList(uriList)
                .setEmptySelectionText("선택된 이미지가 없습니다.")
                .showMultiImage {
                    if(!it.isEmpty()){
                        for(image in it){
                            if(!uriList.contains(image)){
                                uriList.add(image)
                                val imageView = ImageView(context)
                                val params = LinearLayout.LayoutParams(layout.imageLL.height, layout.imageLL.height)
                                imageView.layoutParams = params
                                imageView.setPadding(0,0,20,0)
                                imageView.setImageURI(image)
                                layout.imageLL.addView(imageView)
                            }
                        }

                        //todo add remove
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

    fun requestPermission(){
        AwesomeDialog.build(activity as MainActivity)
            .title("권한 상승이 필요합니다.", null, resources.getColor(R.color.black))
            .body("갤러리 이미지 로드를 위해 권한을 허용해주세요!", null, resources.getColor(R.color.black))
            .icon(R.drawable.ic_warning)
            .onPositive("확인"){
                ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    SignUpView.PERMISSION_REQUEST_CODE
                )
            }
            .onNegative("취소")
    }

    fun onButtonClick(v : View){
        when(v.id){
            R.id.btn_selectImage -> {
                loadPicker()
            }

            R.id.btn_uploadNotice -> {
                if(title.get()!! == "" || contents.get()!! == ""){
                    AwesomeDialog.build(activity as MainActivity)
                        .title("공백 필드", null, resources.getColor(R.color.black))
                        .body("모든 필드를 채워주세요.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive("확인"){
                        }
                }

                else{
                    AwesomeDialog.build(activity as MainActivity)
                        .title("업로드 확인", null, resources.getColor(R.color.black))
                        .body("공지사항을 업로드하시겠습니까?.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_select)
                        .onPositive("예"){
                            layout.progressView.visibility = View.VISIBLE

                            helper.uploadNotice(title.get()!!, contents.get()!!, uriList){
                                if(it){
                                    layout.progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                .title("업로드 완료", null, resources.getColor(R.color.black))
                                .body("업로드가 완료되었습니다.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_select)
                                .onPositive("확인"){
                                }
                                }

                                else{
                                    layout.progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                .title("오류", null, resources.getColor(R.color.black))
                                .body("요청하신 작업을 처리하는 중 문제가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
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