package kr.ac.jbnu.ch.petition.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutAddpetitionMainBinding
import kr.ac.jbnu.ch.databinding.LayoutNoticelistBinding
import kr.ac.jbnu.ch.feedbackhub.view.FeedbackHubView

class AddPetitionMainView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutAddpetitionMainBinding = DataBindingUtil.inflate(inflater , R.layout.layout_addpetition_main , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        return layout.root
    }

    fun changeView(){
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
        transaction.addToBackStack(null)

        transaction.replace(R.id.mainViewArea, WritePetitionView())
        transaction.commit()
    }
}