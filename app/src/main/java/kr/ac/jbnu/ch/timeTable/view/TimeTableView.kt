package kr.ac.jbnu.ch.timeTable.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutTimetableBinding

class TimeTableView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutTimetableBinding = DataBindingUtil.inflate(inflater , R.layout.layout_timetable , container , false)

        return layout.root
    }
}