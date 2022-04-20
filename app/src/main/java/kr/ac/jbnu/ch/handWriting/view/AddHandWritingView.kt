package kr.ac.jbnu.ch.handWriting.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.example.awesomedialog.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import gun0912.tedbottompicker.TedBottomPicker
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutAddhandwritingBinding
import kr.ac.jbnu.ch.databinding.LayoutAffiliatesListBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.handWriting.helper.HandWritingHelper
import kr.ac.jbnu.ch.handWriting.models.HandWritingDataModel
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.view.SignUpView
import java.text.SimpleDateFormat
import java.util.*

class AddHandWritingView : Fragment() {
    var title = ObservableField<String>("")
    var examName = ObservableField<String>("")
    var meter = ObservableField<String>("")
    var term = ObservableField<String>("")
    var review = ObservableField<String>("")
    var howTO = ObservableField<String>("")
    private lateinit var btn_selectDate : MaterialButton
    private lateinit var view : LinearLayout
    private lateinit var layout : LayoutAddhandwritingBinding

    private var date = ""
    private val calendar = Calendar.getInstance()
    private val helper = HandWritingHelper()
    private var uriList : MutableList<Uri> = ArrayList<Uri>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutAddhandwritingBinding = DataBindingUtil.inflate(inflater , R.layout.layout_addhandwriting , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        this.layout = layout
        this.view = layout.addHandWritingView

        btn_selectDate = layout.btnSelectDate

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

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
            R.id.btn_selectDate -> {
                val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    btn_selectDate.text = "${year}. ${month + 1}. ${dayOfMonth}"
                    date = "${year}. ${month + 1}. ${dayOfMonth}. "

                    TimePickerDialog(context, R.style.DialogTheme, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        btn_selectDate.text = "${date} ${hour} : ${minute}"
                        date += "${hour}:${minute}:00"
                    }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false).show()
                }

                context?.let {
                    DatePickerDialog(it, R.style.DialogTheme, datePicker, calendar.get(Calendar.YEAR), calendar.get(
                        Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                }
            }

            R.id.btn_selectImage -> {
                loadPicker()
            }

            R.id.btn_uploadHandWriting -> {
                AwesomeDialog.build(activity as MainActivity)
                    .title("수기 업로드 확인", null, resources.getColor(R.color.black))
                    .body("합격자 수기를 업로드하시겠습니까?", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_select)
                    .onPositive("예"){
                        if(title.get()!! == "" || examName.get()!! == "" || meter.get()!! == "" || term.get()!! == "" || review.get()!! == "" || howTO.get()!! == "" || date == ""){
                            AwesomeDialog.build(activity as MainActivity)
                                .title("공백 필드", null, resources.getColor(R.color.black))
                                .body("모든 필드를 채워주세요.", null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onPositive("확인")
                        }

                        else{
                            layout.btnUploadHandWriting.visibility = View.GONE
                            layout.progressView.visibility = View.VISIBLE

                            val dateFormatter = SimpleDateFormat("yyyy. MM. dd. HH:mm:ss")

                            helper.uploadHandWriting(HandWritingDataModel(
                                "",
                                UserManagement.userInfo?.college ?: "",
                                dateFormatter.format(Date()),
                                date,
                                examName.get()!!,
                                howTO.get()!!,
                                uriList.size,
                                meter.get()!!,
                                UserManagement.userInfo?.name ?: "",
                                0,
                                review.get()!!,
                                UserManagement.userInfo?.studentNo ?: "",
                                term.get()!!,
                                title.get()!!,
                                UserManagement.userInfo?.uid ?: ""
                            ),uriList){
                                if(it){
                                    AwesomeDialog.build(activity as MainActivity)
                                        .title("업로드 완료", null, resources.getColor(R.color.black))
                                        .body("수기가 업로드되었습니다.", null, resources.getColor(R.color.black))
                                        .icon(R.drawable.ic_select)
                                        .onPositive("확인")
                                }

                                else{
                                    AwesomeDialog.build(activity as MainActivity)
                                        .title("오류", null, resources.getColor(R.color.black))
                                        .body("요청한 작업을 처리하는 중 문제가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                        .icon(R.drawable.ic_warning)
                                        .onPositive("확인")
                                }

                                layout.btnUploadHandWriting.visibility = View.VISIBLE
                                layout.progressView.visibility = View.GONE
                            }
                        }
                    }
                    .onNegative("아니오"){

                    }
            }
        }
    }
}