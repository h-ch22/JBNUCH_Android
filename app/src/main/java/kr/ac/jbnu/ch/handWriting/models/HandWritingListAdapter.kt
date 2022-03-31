package kr.ac.jbnu.ch.handWriting.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.handWriting.helper.HandWritingHelper
import java.lang.StringBuilder

class HandWritingListAdapter :
    RecyclerView.Adapter<HandWritingListAdapter.ViewHolder>(), Filterable {
        private lateinit var context : Context
        private var searchList : ArrayList<HandWritingDataModel>? = null

    init{
        searchList = HandWritingHelper.handWritingList
    }

    interface OnItemClickListener{
        fun onItemClick(v : View, data : HandWritingDataModel, pos : Int)
    }

    private var listener : HandWritingListAdapter.OnItemClickListener? = null

    fun setOnItemClickListener(listener : HandWritingListAdapter.OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HandWritingListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_handwriting, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HandWritingListAdapter.ViewHolder, position: Int) {
        val data = searchList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return searchList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.title)
        val date : TextView = view.findViewById(R.id.date)
        val recommend : TextView = view.findViewById(R.id.recommend)
        val author : TextView = view.findViewById(R.id.author)
        val type : TextView = view.findViewById(R.id.type)

        fun bind(data : HandWritingDataModel){
            title.text = data.title
            date.text = data.date
            recommend.text = data.recommend.toString()
            val studentNo_decrypted = AES256Util.decrypt(data.studentNo)
            val name_decrypted = AES256Util.decrypt(data.name)

            val studentNoAsBuilder = StringBuilder(studentNo_decrypted)
            val nameAsBuilder = StringBuilder(name_decrypted)

            for(i in 0..studentNoAsBuilder.count() - 1){
                if(i >= 4){
                    studentNoAsBuilder.setCharAt(i, '*')
                }
            }

            for(i in 0..nameAsBuilder.count() - 1){
                if(i > 0){
                    nameAsBuilder.setCharAt(i, '*')
                }
            }


            author.text = data.college + " " + studentNoAsBuilder + " " + nameAsBuilder
            type.text = data.examName

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
                    searchList = HandWritingHelper.handWritingList
                } else{
                    val filteredList = ArrayList<HandWritingDataModel>()

                    for(handWriting in HandWritingHelper.handWritingList){
                        if(handWriting.title.contains(charString) ||
                                handWriting.examName.contains(charString)){
                            filteredList.add(handWriting)
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
                    searchList = p1.values as ArrayList<HandWritingDataModel>
                }

                notifyDataSetChanged()
            }

        }
    }
}