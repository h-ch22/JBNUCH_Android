package kr.ac.jbnu.ch.calendar.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.calendar.models.DayViewContainer
import kr.ac.jbnu.ch.databinding.LayoutCalendarBinding
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class CalendarView : Fragment() {
    private lateinit var calendarView : CalendarView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutCalendarBinding = DataBindingUtil.inflate(inflater , R.layout.layout_calendar , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        this.calendarView = layout.calendarView

        initializeCalendar()

        return layout.root
    }

    private fun initializeCalendar(){
        calendarView.dayBinder = object : DayBinder<DayViewContainer>{
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()

                if(day.owner == DayOwner.THIS_MONTH){
                    container.textView.setTextColor(resources.getColor(R.color.txtColor))
                }

                else{
                    container.textView.setTextColor(resources.getColor(R.color.gray))
                }
            }

            override fun create(view: View) = DayViewContainer(view)
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
    }
}