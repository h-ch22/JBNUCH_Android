package kr.ac.jbnu.ch.notice.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.frameworks.helper.TranslationManager
import kr.ac.jbnu.ch.frameworks.models.LanguageModel

class TranslateLanguageSelectionViewAdapter :
    RecyclerView.Adapter<TranslateLanguageSelectionViewAdapter.ViewHolder>(){
        private lateinit var context : Context
        private var list : ArrayList<LanguageModel>? = null

    init{
        list = TranslationManager.languageList
    }

        interface OnItemClickListener{
            fun onItemClick(v : View, data : LanguageModel, pos : Int)
        }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TranslateLanguageSelectionViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_translate_language_selection, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TranslateLanguageSelectionViewAdapter.ViewHolder,
        position: Int
    ) {
        val data = list!![position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val language : TextView = view.findViewById(R.id.txt_language)
        val languageCode : TextView = view.findViewById(R.id.txt_language_code)

        fun bind(data : LanguageModel){
            language.text = data.languageName
            languageCode.text = data.languageCode

            val pos = adapterPosition

            if(pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, data, pos)
                }
            }
        }
    }
}