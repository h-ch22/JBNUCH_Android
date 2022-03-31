package kr.ac.jbnu.ch.handWriting.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutHandwritingdetailsBinding
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.handWriting.helper.HandWritingHelper
import kr.ac.jbnu.ch.handWriting.models.HandWritingDataModel
import kr.ac.jbnu.ch.handWriting.models.HandWritingImageAdapter
import java.lang.StringBuilder

class HandWritingDetailView(val data : HandWritingDataModel) : Fragment() {
    private val helper = HandWritingHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutHandwritingdetailsBinding = DataBindingUtil.inflate(inflater , R.layout.layout_handwritingdetails , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        if(data.imageIndex > 0){
            val adapter = context?.let {HandWritingImageAdapter(it, data)}

            layout.imgList.visibility = View.VISIBLE
            layout.pageIndicatorView.visibility = View.VISIBLE

            helper.getImage(data.id, data.imageIndex){
                if(it){
                    layout.imgList.adapter = adapter
                }
            }
        }

        else{
            layout.imgList.visibility = View.GONE
            layout.pageIndicatorView.visibility = View.GONE
        }

        layout.txtTitle.text = data.title
        layout.txtExamDate.text = data.examDate
        layout.txtExamName.text = data.examName
        layout.txtHowTO.text = data.howTO
        layout.txtMeter.text = data.meter
        layout.txtReview.text = data.review
        layout.txtTerm.text = data.term

        val listModel_title : TextView = layout.listModel.findViewById(R.id.title)
        val listModel_date : TextView = layout.listModel.findViewById(R.id.date)
        val listModel_recommend : TextView = layout.listModel.findViewById(R.id.recommend)
        val listModel_author : TextView = layout.listModel.findViewById(R.id.author)
        val listModel_type : TextView = layout.listModel.findViewById(R.id.type)

        listModel_title.text = data.title
        listModel_date.text = data.date
        listModel_recommend.text = data.recommend.toString()

        val studentNo_decrypted = AES256Util.decrypt(data.studentNo)
        val name_decrypted = AES256Util.decrypt(data.name)

        val studentNoAsBuilder = StringBuilder(studentNo_decrypted)
        val nameAsBuilder = StringBuilder(name_decrypted)

        for(i in 0..studentNoAsBuilder.count() - 1){
            if(i >= 4){
                studentNoAsBuilder.setCharAt(i, '*')
            }
        }

        for(i in 0..nameAsBuilder.count() - 1){
            if(i > 0){
                nameAsBuilder.setCharAt(i, '*')
            }
        }

        listModel_author.text = data.college + " " + studentNoAsBuilder + " " + nameAsBuilder
        listModel_type.text = data.examName

        return layout.root
    }
}