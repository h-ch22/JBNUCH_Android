package kr.ac.jbnu.ch.home.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.notice.helper.NoticeHelper
import kr.ac.jbnu.ch.notice.models.NoticeDataModel

class HomeNoticeAdapter :
    RecyclerView.Adapter<HomeNoticeAdapter.ViewHolder>(){
        private lateinit var context : Context

        interface OnItemClickListener{
            fun onItemClick(v : View, data : NoticeDataModel, pos : Int)
        }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeNoticeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_home, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeNoticeAdapter.ViewHolder, position: Int) {
        val data = NoticeHelper.noticeList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.title)
        val subTitle : TextView = view.findViewById(R.id.subTitle)

        fun bind(data : NoticeDataModel){
            title.text = data.noticeTitle
            subTitle.text = data.noticeDate

            val pos = adapterPosition

            if(pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, data, pos)
                }
            }
        }
    }
}