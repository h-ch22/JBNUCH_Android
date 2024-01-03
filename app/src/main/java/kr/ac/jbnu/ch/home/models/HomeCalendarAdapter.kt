package kr.ac.jbnu.ch.home.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.calendar.helper.CalendarDataHelper
import kr.ac.jbnu.ch.calendar.models.CalendarDataModel
import kr.ac.jbnu.ch.sports.models.SportsDataModel
import java.text.SimpleDateFormat
import java.util.*

class HomeCalendarAdapter :
    RecyclerView.Adapter<HomeCalendarAdapter.ViewHolder>(){
        private lateinit var context : Context
        private val calendarList = arrayListOf<CalendarDataModel>()

    init{
        val parser = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")

        for(i in 0 until CalendarDataHelper.calendarList.size){
            if(CalendarDataHelper.calendarList[i].startDate >= parser.parse(parser.format(Date()))){
                calendarList.add(CalendarDataHelper.calendarList[i])
            }
        }
    }

    interface OnItemClickListener{
            fun onItemClick(v : View, data : CalendarDataModel, pos : Int)
        }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCalendarAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_home, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeCalendarAdapter.ViewHolder, position: Int) {
        val data = calendarList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        if(calendarList.size > 5){
            return 5
        }

        else{
            return calendarList.size
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.title)
        val subTitle : TextView = view.findViewById(R.id.subTitle)

        fun bind(data : CalendarDataModel){
            val formatter = SimpleDateFormat("yyyy. MM. dd.")
            val parser = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")

            title.text = data.title
            subTitle.text = "${formatter.format(data.startDate)}"

            val pos = adapterPosition

            if(pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, data, pos)
                }
            }
        }
    }
}