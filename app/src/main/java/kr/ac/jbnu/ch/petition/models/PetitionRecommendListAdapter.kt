package kr.ac.jbnu.ch.petition.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.petition.helper.PetitionHelper

class PetitionRecommendListAdapter : RecyclerView.Adapter<PetitionRecommendListAdapter.ViewHolder>() {
    private lateinit var context : Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PetitionRecommendListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_petition_participants, parent, false)
        Log.d("PetitionHelper", "Creating...")

        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetitionRecommendListAdapter.ViewHolder, position: Int) {
        val data = PetitionHelper.recommendList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return PetitionHelper.recommendList.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val txt_author : TextView = view.findViewById(R.id.petitionParticipants_author)
        val txt_date : TextView = view.findViewById(R.id.petitionParticipants_date)

        fun bind(data : PetitionRecommendModel){
            Log.d("PetitionHelper", "Binding user data : ${data}")

            txt_date.text = data.date
            val college = AES256Util.decrypt(data.college)
            val studentNo = AES256Util.decrypt(data.studentNo)
            val name = AES256Util.decrypt(data.name)

            val studentNo_identifier = studentNo.substring(0,4)
            val firstName = name.substring(0,1)
            var lastName = ""

            for(i in 0..name.length-2){
                lastName += "*"
            }

            txt_author.text = "${college} ${studentNo_identifier}***** ${firstName}${lastName}"
        }
    }
}