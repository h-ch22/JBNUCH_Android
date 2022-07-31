package kr.ac.jbnu.ch.home.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.notice.helper.NoticeHelper
import kr.ac.jbnu.ch.notice.models.NoticeDataModel
import kr.ac.jbnu.ch.petition.helper.PetitionHelper
import kr.ac.jbnu.ch.petition.models.PetitionDataModel
import kr.ac.jbnu.ch.sports.helper.SportsHelper
import kr.ac.jbnu.ch.sports.models.SportsDataModel

class HomePetitionAdapter :
    RecyclerView.Adapter<HomePetitionAdapter.ViewHolder>(){
        private lateinit var context : Context

        interface OnItemClickListener{
            fun onItemClick(v : View, data : PetitionDataModel, pos : Int)
        }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePetitionAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_home, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomePetitionAdapter.ViewHolder, position: Int) {
        val data = PetitionHelper.petitionList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        if(PetitionHelper.petitionList.size > 5){
            return 5
        }

        else{
            return PetitionHelper.petitionList.size
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.title)
        val subTitle : TextView = view.findViewById(R.id.subTitle)

        fun bind(data : PetitionDataModel){
            title.text = AES256Util.decrypt(data.title)
            subTitle.text = data.timeStamp

            val pos = adapterPosition

            if(pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, data, pos)
                }
            }
        }
    }
}