package kr.ac.jbnu.ch.notice.view

import android.Manifest
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
                .setTitle(resources.getString(R.string.TXT_ALERT_TITLE_SELECT_IMAGE))
                .setPeekHeight(1600)
                .setCompleteButtonText(resources.getString(R.string.TXT_DONE))
                .setSelectedUriList(uriList)
                .setEmptySelectionText(resources.getString(R.string.TXT_ALERT_CONTENTS_NO_IMAGE_SELECTED))
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
                    Snackbar.make(view, resources.getString(R.string.TXT_ALERT_PERMISSION_NOT_GRANTED), Snackbar.LENGTH_LONG).show()
                }

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadPicker()
                }

                else{
                    Snackbar.make(view, resources.getString(R.string.TXT_ALERT_PERMISSION_NOT_GRANTED), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    fun requestPermission(){
        AwesomeDialog.build(activity as MainActivity)
            .title(resources.getString(R.string.TXT_ALERT_TITLE_REQUEST_PERMISSION), null, resources.getColor(R.color.black))
            .body(resources.getString(R.string.TXT_ALERT_CONTENTS_GALLERY_PERMISSION), null, resources.getColor(R.color.black))
            .icon(R.drawable.ic_warning)
            .onPositive(resources.getString(R.string.TXT_OK)){
                ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    SignUpView.PERMISSION_REQUEST_CODE
                )
            }
            .onNegative(resources.getString(R.string.TXT_CANCEL))
    }

    fun onButtonClick(v : View){
        when(v.id){
            R.id.btn_selectImage -> {
                loadPicker()
            }

            R.id.btn_uploadNotice -> {
                if(title.get()!! == "" || contents.get()!! == ""){
                    AwesomeDialog.build(activity as MainActivity)
                        .title(resources.getString(R.string.TXT_ALERT_TITLE_EMPTY_FIELD), null, resources.getColor(R.color.black))
                        .body(resources.getString(R.string.TXT_ALERT_CONTENTS_EMPTY_FIELD), null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive(resources.getString(R.string.TXT_OK)){
                        }
                }

                else{
                    AwesomeDialog.build(activity as MainActivity)
                        .title(resources.getString(R.string.TXT_ALERT_TITLE_CONFIRM_UPLOAD), null, resources.getColor(R.color.black))
                        .body(resources.getString(R.string.TXT_ALERT_CONTENTS_CONFIRM_UPLOAD), null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_select)
                        .onPositive(resources.getString(R.string.TXT_YES)){
                            layout.progressView.visibility = View.VISIBLE

                            helper.uploadNotice(title.get()!!, contents.get()!!, uriList){
                                if(it){
                                    layout.progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                .title(resources.getString(R.string.TXT_ALERT_TITLE_UPLOAD_SUCCESS), null, resources.getColor(R.color.black))
                                .body(resources.getString(R.string.TXT_ALERT_CONTENTS_UPLOAD_SUCCESS), null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_select)
                                .onPositive(resources.getString(R.string.TXT_OK)){
                                }
                                }

                                else{
                                    layout.progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                .title(resources.getString(R.string.TXT_ERROR), null, resources.getColor(R.color.black))
                                .body(resources.getString(R.string.TXT_ALERT_CONTENTS_ERROR), null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onPositive(resources.getString(R.string.TXT_OK)){
                                }
                                }
                            }
                        }
                        .onNegative(resources.getString(R.string.TXT_NO)){

                        }
                }

            }
        }
    }
}