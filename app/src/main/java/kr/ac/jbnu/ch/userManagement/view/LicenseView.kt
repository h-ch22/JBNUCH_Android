package kr.ac.jbnu.ch.userManagement.view

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutLicenseBinding
import kr.ac.jbnu.ch.frameworks.models.LicenseTypeModel
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.PDFViewer
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.userManagement.models.SignUpChangeViewModel

class LicenseView(val type : SignUpChangeViewModel, val email : String?) : Fragment(), onKeyBackPressedListener{
    private lateinit var pdfViewer : PDFViewer
    private lateinit var view : LinearLayout
    var acceptEULA = false
    var acceptPrivacyLicense = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutLicenseBinding = DataBindingUtil.inflate(inflater , R.layout.layout_license , container , false)

        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)
        title.text = "이용 약관"
        layout.view = this
        this.view = layout.licenseLayout

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return layout.root
    }

    fun loadLicense(view : View){
        when(view.id){
            R.id.btn_readEULA -> {
                this.pdfViewer = PDFViewer(LicenseTypeModel.EULA, null)
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
                transaction.addToBackStack(null)

                transaction.replace(R.id.viewArea, pdfViewer)
                transaction.commit()
            }

            R.id.btn_readPrivacy -> {
                this.pdfViewer = PDFViewer(LicenseTypeModel.PrivacyLicense, null)
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
                transaction.addToBackStack(null)

                transaction.replace(R.id.viewArea, pdfViewer)
                transaction.commit()
            }
        }
    }

    fun changeView(){
        if(!acceptEULA || !acceptPrivacyLicense){
            Snackbar.make(view, resources.getText(R.string.TXT_REQUEST_ACCEPT_LICENSE), Snackbar.LENGTH_LONG).show()
        }

        else{
            when(type){
                SignUpChangeViewModel.SIGNUP -> {
                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                    transaction.addToBackStack(null)
                    transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)

                    transaction.replace(R.id.viewArea, SignUpView())
                    transaction.commit()
                }

                SignUpChangeViewModel.TRANSFERDATA -> {
                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                    transaction.addToBackStack(null)
                    transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)

                    transaction.replace(R.id.viewArea, LegacyUserInfoView(email))
                    transaction.commit()
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