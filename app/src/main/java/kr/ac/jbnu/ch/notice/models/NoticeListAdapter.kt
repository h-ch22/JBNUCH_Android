package kr.ac.jbnu.ch.notice.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.notice.helper.NoticeHelper

class NoticeListAdapter() :
    RecyclerView.Adapter<NoticeListAdapter.ViewHolder>(), Filterable {
    private lateinit var context : Context
    private var searchList : ArrayList<NoticeDataModel>? = null

    init{
        searchList = NoticeHelper.noticeList
    }

    interface OnItemClickListener{
        fun onItemClick(v : View, data : NoticeDataModel, pos : Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_notice, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeListAdapter.ViewHolder, position: Int) {
        val data = searchList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return searchList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val title : TextView = view.findViewById(R.id.noticeTitle)
        val date : TextView = view.findViewById(R.id.noticeDate)
        val type : TextView = view.findViewById(R.id.notice_type)

        fun bind(data : NoticeDataModel){
            title.text = data.noticeTitle
            date.text = data.noticeDate

            when(data.noticeType){
                "CH" -> {
                    type.text = "총학"
                    type.background = ContextCompat.getDrawable(context, R.drawable.model_affiliate_ch)
                    type.visibility = View.VISIBLE
                }

                "College" -> {
                    type.text = "단대"
                    type.background = ContextCompat.getDrawable(context, R.drawable.model_affiliate_college)
                    type.visibility = View.VISIBLE
                }
            }

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
                    searchList = NoticeHelper.noticeList
                } else{
                    val filteredList = ArrayList<NoticeDataModel>()

                    for(notice in NoticeHelper.noticeList){
                        if(notice.noticeTitle.contains(charString)){
                            filteredList.add(notice)
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
                    searchList = p1.values as ArrayList<NoticeDataModel>
                }

                notifyDataSetChanged()
            }

        }
    }
}