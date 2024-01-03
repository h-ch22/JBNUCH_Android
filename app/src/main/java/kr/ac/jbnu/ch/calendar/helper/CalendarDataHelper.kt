package kr.ac.jbnu.ch.calendar.helper

import android.app.Activity
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kr.ac.jbnu.ch.calendar.models.CalendarDataModel
import kr.ac.jbnu.ch.calendar.models.CalendarTypeModel
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.text.SimpleDateFormat

class CalendarDataHelper {
    private val storage = Firebase.storage

    companion object{
        var calendarList = arrayListOf<CalendarDataModel>()
    }

    fun getResource(completion : (Boolean) -> Unit){
        val storageRef = storage.reference
        val resourceRef = storageRef.child("calendar/events.json")
        val file = File.createTempFile("resources", ".json")

        resourceRef.getFile(file).addOnSuccessListener {
            Log.d("CalendarDataHelper", "File downloaded successfully : ${file.absolutePath}")

            parseJSON(file){
                if(it){
                    completion(true)
                } else{
                    completion(false)
                }
            }
        }.addOnFailureListener {
            Log.d("CalendarDataHelper", "File download fail : ${it.printStackTrace()}")
        }
    }

    fun parseJSON(file : File, completion: (Boolean) -> Unit){
        try{
            val fileInputStream = FileInputStream(file)
            val fileChannel = fileInputStream.channel
            val byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size())

            val jsonStr = Charset.defaultCharset().decode(byteBuffer).toString()

            val jsonObj = JSONObject(jsonStr)
            val jsonArr = jsonObj.getJSONArray("data")

            for(i in 0 until jsonArr.length()){
                val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                val formatter = SimpleDateFormat("yyyy. MM. dd. HH:mm")

                val char = jsonArr.getJSONObject(i)
                val title = char.getString("title")
                val start = char.getString("start")
                val end = char.getString("end")
                var isAllDay = false


                if(char.getString("all_day") == "1"){
                    isAllDay = true
                }

                var type = CalendarTypeModel.ACADEMIC_CALENDAR

                if(char.getString("color") == "#99275F"){
                    type = CalendarTypeModel.ACADEMIC_CALENDAR
                }

                else if(char.getString("color") == "#F54E5F"){
                    type = CalendarTypeModel.EMPLOYMENT
                }

                else if(char.getString("color") == "#57B4FA"){
                    type = CalendarTypeModel.OUTDOOR_ACTIVITY
                }

                else{
                    type = CalendarTypeModel.COMPETITION
                }

                calendarList.add(
                    CalendarDataModel(
                        title = title,
                        startDate = formatter.parse(formatter.format(dateParser.parse(start))),
                        endDate = formatter.parse(formatter.format(dateParser.parse(end))),
                        isAllDay = isAllDay,
                        type = type
                    )
                )
            }

            completion(true)

        } catch(e : Exception){
            e.printStackTrace()
            completion(false)
        }
    }
}