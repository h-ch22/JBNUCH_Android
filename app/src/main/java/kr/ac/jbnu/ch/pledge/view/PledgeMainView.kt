package kr.ac.jbnu.ch.pledge.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutPledgeMainBinding
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel

class PledgeMainView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutPledgeMainBinding = DataBindingUtil.inflate(inflater , R.layout.layout_pledge_main , container , false)

        when(UserManagement.userInfo?.collegeCode){
            CollegeCodeModel.SOC, CollegeCodeModel.COH, CollegeCodeModel.CON, CollegeCodeModel.COM, CollegeCodeModel.CHE -> {
                layout.toggleGroupPledgeCategory.visibility = View.VISIBLE

                layout.framePledge.addView(PledgeView())
            }

            else -> {
                layout.toggleGroupPledgeCategory.visibility = View.GONE
            }
        }

        return layout.root
    }
}