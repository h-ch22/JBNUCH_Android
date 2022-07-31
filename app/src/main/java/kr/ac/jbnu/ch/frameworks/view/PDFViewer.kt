package kr.ac.jbnu.ch.frameworks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.barteksc.pdfviewer.PDFView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutPdfViewBinding
import kr.ac.jbnu.ch.frameworks.models.LicenseTypeModel

class PDFViewer(val type : LicenseTypeModel) : Fragment() {
    private lateinit var pdfView : PDFView
    private var resID : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutPdfViewBinding = DataBindingUtil.inflate(inflater , R.layout.layout_pdf_view , container , false)

        layout.view = this
        this.pdfView = layout.pdfView

        loadLicense()

        return layout.root
    }

    private fun loadLicense(){
        when(type){
            LicenseTypeModel.EULA -> {
                val assetManager = resources.assets
                val inputStream = assetManager.open("license/EULA.pdf")
                pdfView.fromStream(inputStream).load()
            }

            LicenseTypeModel.PrivacyLicense -> {
                val assetManager = resources.assets
                val inputStream = assetManager.open("license/PrivacyLicense.pdf")
                pdfView.fromStream(inputStream).load()
            }

            LicenseTypeModel.PLEDGEBOOK -> {
                val assetManager = resources.assets
                val inputStream = assetManager.open("pledgeBook/PledgeBook.pdf")
                pdfView.fromStream(inputStream).load()
            }
        }
    }
}