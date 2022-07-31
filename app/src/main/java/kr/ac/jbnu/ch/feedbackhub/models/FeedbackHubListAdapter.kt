package kr.ac.jbnu.ch.feedbackhub.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.feedbackhub.helper.FeedbackHubHelper
import kr.ac.jbnu.ch.frameworks.helper.AES256Util

class FeedbackHubListAdapter(val type : String) : RecyclerView.Adapter<FeedbackHubListAdapter.ViewHolder>(),
    Filterable {
        private lateinit var context : Context
        private var searchList : ArrayList<FeedbackHubDataModel>? = null

    init{
        when(type){
            "MyFeedback" -> {
                searchList = FeedbackHubHelper.myFeedbackList
            }

            "All" -> {
                searchList = FeedbackHubHelper.feedbackList
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(v : View, data : FeedbackHubDataModel, pos : Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedbackHubListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_feedback, parent, false)

        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackHubListAdapter.ViewHolder, position: Int) {
        val data = searchList!![position]

        holder.bind(data, type)
    }

    override fun getItemCount(): Int {
        return searchList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.txt_feedbackTitle)
        val author : TextView = view.findViewById(R.id.txt_feedbackAuthor)
        val category : TextView = view.findViewById(R.id.txt_feedbackCategory)
        val date : TextView = view.findViewById(R.id.txt_feedbackDate)
        val img : ImageView = view.findViewById(R.id.img_feedbackCategory)
        val status : TextView = view.findViewById(R.id.txt_feedbackStatus)

        fun bind(data : FeedbackHubDataModel, type : String){
            when(AES256Util.decrypt(data.type)){
                "칭찬해요" -> img.setImageDrawable(context.getDrawable(R.drawable.ic_heart))
                "개선해주세요" -> img.setImageDrawable(context.getDrawable(R.drawable.ic_improve))
                "궁금해요" -> img.setImageDrawable(context.getDrawable(R.drawable.ic_question))
            }

            when(type){
                "MyFeedback" -> author.visibility = View.GONE
                "All" -> author.visibility = View.VISIBLE
            }

            title.text = AES256Util.decrypt(data.title)
            author.text = AES256Util.decrypt(data.author)
            category.text = AES256Util.decrypt(data.type)
            date.text = AES256Util.decrypt(data.date)

            if(AES256Util.decrypt(data.answer) == ""){
                status.text = "아직 답변이 등록되지 않은 피드백입니다."
            }

            else{
                status.text = "피드백에 대한 답변이 등록되었습니다."
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
                    when(type){
                        "MyFeedback" -> searchList = FeedbackHubHelper.myFeedbackList
                        "All" -> searchList = FeedbackHubHelper.feedbackList
                    }


                }

                else{
                    val filteredList = ArrayList<FeedbackHubDataModel>()

                    when(type){
                        "MyFeedback" -> {
                            for(feedback in FeedbackHubHelper.myFeedbackList){
                                if(AES256Util.decrypt(feedback.title).contains(charString)){
                                    filteredList.add(feedback)
                                }
                            }
                        }

                        "All" -> {
                            for(feedback in FeedbackHubHelper.feedbackList){
                                if(AES256Util.decrypt(feedback.title).contains(charString)){
                                    filteredList.add(feedback)
                                }
                            }
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = searchList

                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if(p1 != null){
                    searchList = p1.values as ArrayList<FeedbackHubDataModel>
                }

                notifyDataSetChanged()
            }
        }
    }
}