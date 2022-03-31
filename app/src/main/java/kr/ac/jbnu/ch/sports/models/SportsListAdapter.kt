package kr.ac.jbnu.ch.sports.models

import android.content.Context
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.models.AffiliateDataModel
import kr.ac.jbnu.ch.affiliates.models.AffiliateListAdapter
import kr.ac.jbnu.ch.sports.helper.SportsHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SportsListAdapter : RecyclerView.Adapter<SportsListAdapter.ViewHolder>(), Filterable {
    private lateinit var context : Context
    private var searchList : ArrayList<SportsDataModel>? = null

    init{
        searchList = SportsHelper.sportsList
    }

    interface OnItemClickListener{
        fun onItemClick(v : View, data : SportsDataModel, pos : Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SportsListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_sports, parent, false)

        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportsListAdapter.ViewHolder, position: Int) {
        val data = searchList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return searchList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val roomName : TextView = view.findViewById(R.id.txt_sportsRoomName)
        val people : TextView = view.findViewById(R.id.txt_sports_peopleCount)
        val location : TextView = view.findViewById(R.id.txt_position)
        val type : TextView = view.findViewById(R.id.txt_sportsType)
        val time : TextView = view.findViewById(R.id.txt_timeRemaining)

        fun bind(data : SportsDataModel){
            roomName.text = data.roomName
            people.text = "${data.currentPeople} / ${data.allPeople}"

            if(data.isOnline){
                val locationDescription = "<b>온라인 진행</b>"

                location.text = Html.fromHtml(locationDescription)
            }

            else{
                val locationDescription = "<b>${data.locationDescription}</b> <br>${data.address}"

                location.text = Html.fromHtml(locationDescription)
            }

            type.text = data.type

            val formatter = SimpleDateFormat("yyyy.MM.dd. HH:mm:ss")
            val parsedDate = formatter.format(Date())
            val currentDate = formatter.parse(parsedDate.toString())
            val date = formatter.parse(data.dateTime)
            val diff = date.time - currentDate.time

            val timer = object : CountDownTimer(diff, 1000){
                override fun onTick(p0: Long) {
                    val hour = (p0 / 3600000).toInt()
                    val minute = (p0 % 3600000 / 60000).toInt()
                    val sec = (p0 % 3600000 % 60000 / 1000).toInt()

                    time.text = "${hour}:${minute}:${sec}"
                }

                override fun onFinish() {

                }
            }.start()

            val pos = adapterPosition

            if(pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, data, pos)
                }
            }

        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()

                if(charString.isEmpty()){
                    searchList = SportsHelper.sportsList
                } else{
                    val filteredList = ArrayList<SportsDataModel>()

                    for(data in SportsHelper.sportsList){
                        if(data.roomName?.contains(charString) == true || data.type?.contains(charString) == true){
                            filteredList.add(data)
                        }
                    }

                    searchList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = searchList

                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (p1 != null) {
                    searchList = p1.values as ArrayList<SportsDataModel>
                }

                notifyDataSetChanged()
            }
        }
    }
}