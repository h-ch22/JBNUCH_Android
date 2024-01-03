package kr.ac.jbnu.ch.calendar.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.applikeysolutions.cosmocalendar.dialog.CalendarDialog
import com.applikeysolutions.cosmocalendar.dialog.OnDaysSelectionListener
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager
import com.applikeysolutions.cosmocalendar.settings.appearance.ConnectedDayIconPosition
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays
import com.example.awesomedialog.*
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.calendar.helper.CalendarDataHelper
import kr.ac.jbnu.ch.databinding.LayoutCalendarBinding
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import java.text.SimpleDateFormat
import java.util.*

class CalendarView : Fragment(), onKeyBackPressedListener {
    private lateinit var calendarView : com.applikeysolutions.cosmocalendar.view.CalendarView
    private val helper = CalendarDataHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutCalendarBinding = DataBindingUtil.inflate(inflater, R.layout.layout_calendar, container, false)

        CalendarDataHelper.calendarList.clear()

        layout.lifecycleOwner = this
        layout.view = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        this.calendarView = layout.calendarView

        initView()

        return layout.root
    }

    private fun initView(){
        calendarView.weekendDays = hashSetOf(Calendar.SUNDAY, Calendar.SATURDAY) as MutableSet<Long>

        calendarView.firstDayOfWeek = Calendar.SUNDAY

        calendarView.selectionManager = SingleSelectionManager(object : OnDaySelectedListener{
            override fun onDaySelected() {
                if(calendarView.selectedDates.size <= 0){
                    return
                }

                val selectedDay = calendarView.selectedDays

                val formatter = SimpleDateFormat("yyyy. MM. dd.")
                val formatter_dialog = SimpleDateFormat("MM. dd.")

                val date = formatter.format(selectedDay.get(0).calendar.time)

                var eventsAsString = ""

                for(i in 0 until CalendarDataHelper.calendarList.size){
                    val startDate = formatter.format(CalendarDataHelper.calendarList[i].startDate)

                    if (startDate == date){
                        eventsAsString += CalendarDataHelper.calendarList[i].title + "(~${formatter_dialog.format(CalendarDataHelper.calendarList[i].endDate)})\n"
                    }
                }

                if(eventsAsString.length > 1){
                    AwesomeDialog.build(activity as MainActivity)
                        .title(date.toString(), null, resources.getColor(R.color.black))
                        .body(eventsAsString, null, resources.getColor(R.color.black))
                        .onPositive(resources.getString(R.string.TXT_OK))
                }
            }

        })

        helper.getResource(){
            if(it){
                addEvents()
            }

            else{
                Log.d("CalendarView", "getResource() failed.")
            }
        }
    }

    private fun addEvents(){
        val parser = SimpleDateFormat("yyyy. MM. dd. HH:mm")
        val formatter = SimpleDateFormat("yyyy. MM. dd.")
        val days = TreeSet<Long>()

        for(i in 0 until CalendarDataHelper.calendarList.size){
            val startDate = formatter.parse(parser.format(CalendarDataHelper.calendarList[i].startDate))

            days.add(startDate.time)
        }

        val connectedDays = ConnectedDays(days, Color.parseColor("#0063B0"))
        calendarView.addConnectedDays(connectedDays)

        calendarView.connectedDayIconRes = R.drawable.ic_baseline_circle_6
        calendarView.connectedDayIconPosition = ConnectedDayIconPosition.BOTTOM
        calendarView.update()
    }

    override fun onBackKeyPressed() {

    }
}