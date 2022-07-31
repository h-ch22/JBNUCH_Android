package kr.ac.jbnu.ch.calendar.models

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import kr.ac.jbnu.ch.R

class DayViewContainer(view : View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)
}