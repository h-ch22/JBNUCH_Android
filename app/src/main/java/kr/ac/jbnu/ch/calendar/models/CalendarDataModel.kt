package kr.ac.jbnu.ch.calendar.models

import java.util.Date

data class CalendarDataModel(
    val title : String,
    val startDate : Date,
    val endDate : Date,
    val type : CalendarTypeModel,
    val isAllDay : Boolean
)
