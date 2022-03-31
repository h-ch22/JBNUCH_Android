package kr.ac.jbnu.ch.sports.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.sports.helper.SportsHelper

class ParticipantsListAdapter : RecyclerView.Adapter<ParticipantsListAdapter.ViewHolder>() {
    private lateinit var context : Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParticipantsListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_sports_participants, parent, false)

        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantsListAdapter.ViewHolder, position: Int) {
        val data = SportsHelper.particiapntsList[position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return SportsHelper.particiapntsList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val name : TextView = view.findViewById(R.id.sportsParticipants_name)
        val info : TextView = view.findViewById(R.id.sportsParticipants_info)

        fun bind(data : SportsParticipantsModel){
            name.text = AES256Util.decrypt(data.name)
            info.text = "${AES256Util.decrypt(data.college)} ${AES256Util.decrypt(data.studentNo)}"
        }
    }
}