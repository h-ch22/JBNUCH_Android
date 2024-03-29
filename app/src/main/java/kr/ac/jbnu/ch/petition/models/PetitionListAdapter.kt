package kr.ac.jbnu.ch.petition.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.petition.helper.PetitionHelper

class PetitionListAdapter :
    RecyclerView.Adapter<PetitionListAdapter.ViewHolder>(), Filterable {
    private lateinit var context : Context
    private var searchList : ArrayList<PetitionDataModel>? = null

    init{
        searchList = PetitionHelper.filteredList
    }

    interface OnItemClickListener{
        fun onItemClick(v : View, data : PetitionDataModel, pos : Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_petition, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetitionListAdapter.ViewHolder, position: Int) {
        val data = searchList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return searchList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val title : TextView = view.findViewById(R.id.title)
        val date : TextView = view.findViewById(R.id.date)
        val recommend : TextView = view.findViewById(R.id.recommend)
        val category : TextView = view.findViewById(R.id.petitionCategory)

        fun bind(data : PetitionDataModel){
            category.text = data.category
            title.text = AES256Util.decrypt(data.title)
            date.text = data.timeStamp
            recommend.text = data.recommend.toString()

            val pos = adapterPosition

            if(pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, data, pos)
                }
            }

        }

    }

    fun sortByCategory(category : String){
        PetitionHelper.filteredList.clear()

        if(category != "전체"){
            Log.d("PetitionListAdapter", "original List : ${PetitionHelper.petitionList}")

            for(data in PetitionHelper.petitionList){
                Log.d("PetitionListAdapter", "category : ${category}, data.category : ${data.category}")

                if(data.category == category){
                    PetitionHelper.filteredList.add(data)
                    Log.d("PetitionListAdapter", "Added : ${data}")
                }
            }

            notifyDataSetChanged()

        }

        else{
            PetitionHelper.filteredList.addAll(PetitionHelper.petitionList)
            Log.d("PetitionListAdapter", "Added : ${PetitionHelper.filteredList}")

            notifyDataSetChanged()
        }
    }

    private fun updateList(newList : ArrayList<PetitionDataModel>){
        searchList?.clear()
        searchList?.addAll(newList)
        Log.d("PetitionHelper", "newList : ${newList}")
        Log.d("PetitionHelper", "searchList : ${searchList}")
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()

                if(charString.isEmpty()){
                    searchList = PetitionHelper.filteredList
                } else{
                    val filteredList = ArrayList<PetitionDataModel>()

                    for(petition in PetitionHelper.filteredList){
                        if(petition.title.contains(charString)){
                            filteredList.add(petition)
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
                    searchList = p1.values as ArrayList<PetitionDataModel>
                }

                notifyDataSetChanged()
            }

        }
    }
}