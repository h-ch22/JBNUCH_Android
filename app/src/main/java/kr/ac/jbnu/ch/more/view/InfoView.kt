package kr.ac.jbnu.ch.more.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kr.ac.jbnu.ch.BuildConfig
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutInfoBinding
import kr.ac.jbnu.ch.databinding.LayoutMoreBinding
import kr.ac.jbnu.ch.frameworks.models.LicenseTypeModel
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.PDFViewer
import kr.ac.jbnu.ch.more.helper.VersionManagement

class InfoView : Fragment() {
    private val helper = VersionManagement()
    private lateinit var pdfViewer : PDFViewer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutInfoBinding = DataBindingUtil.inflate(inflater , R.layout.layout_info , container , false)

        layout.view = this
        layout.lifecycleOwner = this
        layout.txtVersion.text = "버전 : ${BuildConfig.VERSION_NAME.toString()}"
        layout.txtBuild.text = "빌드 번호 : ${BuildConfig.VERSION_CODE.toString()}"

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        helper.getLatestVersion(){
            if(it){
                if(VersionManagement.version != BuildConfig.VERSION_NAME.toString() ||
                    VersionManagement.build != BuildConfig.VERSION_CODE.toString()){
                    layout.txtStatus.text = "업데이트가 필요합니다."
                    context?.resources?.let { layout.txtStatus.setTextColor(it.getColor(R.color.orange)) }
                    layout.btnUpdate.visibility = View.VISIBLE
                }

                else{
                    layout.txtStatus.text = "최신버전 입니다."
                    context?.resources?.let { layout.txtStatus.setTextColor(it.getColor(R.color.green)) }
                    layout.btnUpdate.visibility = View.GONE
                }
            }

            else{
                layout.txtStatus.text = "버전 정보를 확인할 수 없습니다."
                context?.resources?.let { layout.txtStatus.setTextColor(it.getColor(R.color.red)) }
            }
        }

        return layout.root
    }

    fun changeView(v : View){
        when(v.id){
            R.id.btn_EULA -> {
                this.pdfViewer = PDFViewer(LicenseTypeModel.EULA)
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, pdfViewer)
                transaction.commit()
            }

            R.id.btn_privacy -> {
                this.pdfViewer = PDFViewer(LicenseTypeModel.PrivacyLicense)
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, pdfViewer)
                transaction.commit()
            }

            R.id.btn_openSource -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, OpenSourceLicenseView())
                transaction.commit()
            }
        }
    }
}