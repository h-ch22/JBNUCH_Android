package kr.ac.jbnu.ch.petition.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutPetitiondetailBinding
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.petition.helper.PetitionHelper
import kr.ac.jbnu.ch.petition.models.PetitionDataModel
import kr.ac.jbnu.ch.petition.models.PetitionImageAdapter
import kr.ac.jbnu.ch.petition.models.PetitionStatusModel

class PetitionDetailView(private val data : PetitionDataModel): Fragment() {
    private val helper = PetitionHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutPetitiondetailBinding = DataBindingUtil.inflate(inflater , R.layout.layout_petitiondetail , container , false)
        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)
        layout.view = this
        layout.lifecycleOwner = this

        when(data.status){
            PetitionStatusModel.INPROCESS -> {
                layout.progressInProgress.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.accent))
                layout.txtInProgress.setTextColor(resources.getColor(R.color.accent))

                layout.progressAnswered.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtAnswered.setTextColor(resources.getColor(R.color.gray))

                layout.progressDone.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtDone.setTextColor(resources.getColor(R.color.gray))
            }

            PetitionStatusModel.FINISH -> {
                layout.progressInProgress.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtInProgress.setTextColor(resources.getColor(R.color.gray))

                layout.progressAnswered.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.accent))
                layout.txtAnswered.setTextColor(resources.getColor(R.color.accent))

                layout.progressDone.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtDone.setTextColor(resources.getColor(R.color.gray))
            }

            PetitionStatusModel.ANSWERED -> {
                layout.progressInProgress.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtInProgress.setTextColor(resources.getColor(R.color.gray))

                layout.progressAnswered.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
                layout.txtAnswered.setTextColor(resources.getColor(R.color.gray))

                layout.progressDone.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.accent))
                layout.txtDone.setTextColor(resources.getColor(R.color.accent))
            }
        }

        if(data.imageIndex > 0){
            layout.imgList.visibility = View.VISIBLE
            layout.pageIndicatorView.visibility = View.VISIBLE

            helper.getImage(data.id, data.imageIndex){
                if(it){
                    val adapter = context?.let { PetitionImageAdapter(it, data) }
                    layout.imgList.adapter = adapter
                }
            }
        }

        else{
            layout.imgList.visibility = View.GONE
            layout.pageIndicatorView.visibility = View.GONE
        }

        layout.txtContents.text = AES256Util.decrypt(data.contents)
        title.text = AES256Util.decrypt(data.title)

        return layout.root
    }
}