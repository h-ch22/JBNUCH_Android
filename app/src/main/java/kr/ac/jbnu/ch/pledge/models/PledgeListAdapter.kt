package kr.ac.jbnu.ch.pledge.models

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.pledge.helper.PledgeHelper
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.core.content.ContextCompat


class PledgeListAdapter(private val isFiltered : Boolean) : RecyclerView.Adapter<PledgeListAdapter.ViewHolder>(), Filterable {
    private lateinit var context : Context
    private var searchList : ArrayList<PledgeDataModel>? = null

    init{
        if(isFiltered){
            searchList = PledgeHelper.pledgeList_filtered
        }

        else{
            searchList = PledgeHelper.pledgeList
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PledgeListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_pledge, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PledgeListAdapter.ViewHolder, position: Int) {
        val data = searchList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return searchList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val pledgeCategory : TextView = view.findViewById(R.id.pledgeCategory)
        val pledgeStatus : TextView = view.findViewById(R.id.pledgeStatus)
        val pledgeTitle : TextView = view.findViewById(R.id.pledgeTitle)
        val img_pledgeStatus : ImageView = view.findViewById(R.id.img_pledgeStatus)

        fun bind(data : PledgeDataModel){
            pledgeCategory.text = data.category
            pledgeTitle.text = data.name

            when(data.status){
                "이행 완료" -> {
                    pledgeStatus.text = data.status
                    img_pledgeStatus.setImageDrawable(context.getDrawable(R.drawable.shape_circle))
                    img_pledgeStatus.setColorFilter(context.getColor(R.color.green))
                }

                "진행 중" -> {
                    pledgeStatus.text = data.status
                    img_pledgeStatus.setImageDrawable(context.getDrawable(R.drawable.shape_circle))
                    img_pledgeStatus.setColorFilter(context.getColor(R.color.orange))
                }

                "준비 중" -> {
                    pledgeStatus.text = data.status
                    img_pledgeStatus.setImageDrawable(context.getDrawable(R.drawable.shape_circle))
                    img_pledgeStatus.setColorFilter(context.getColor(R.color.red))
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()

                if(charString.isEmpty()){
                    if(isFiltered){
                        searchList = PledgeHelper.pledgeList_filtered
                    }

                    else{
                        searchList = PledgeHelper.pledgeList
                    }
                } else{
                    val filteredList = ArrayList<PledgeDataModel>()

                    if(isFiltered){
                        for(pledge in PledgeHelper.pledgeList_filtered){
                            if(pledge.name.contains(charString) == true){
                                filteredList.add(pledge)
                            }
                        }

                        searchList = filteredList
                    }

                    else{
                        for(pledge in PledgeHelper.pledgeList){
                            if(pledge.name.contains(charString) == true){
                                filteredList.add(pledge)
                            }
                        }

                        searchList = filteredList
                    }


                }

                val filterResults = FilterResults()
                filterResults.values = searchList

                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if(p1 != null){
                    searchList = p1.values as ArrayList<PledgeDataModel>
                }

                notifyDataSetChanged()
            }
        }
    }
}