package kr.ac.jbnu.ch.campusMap.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutCampusmapInsideBinding

class CampusMapInsideView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutCampusmapInsideBinding = DataBindingUtil.inflate(inflater , R.layout.layout_campusmap_inside , container , false)

        layout.view = this
        layout.lifecycleOwner = this



        return layout.root
    }
}