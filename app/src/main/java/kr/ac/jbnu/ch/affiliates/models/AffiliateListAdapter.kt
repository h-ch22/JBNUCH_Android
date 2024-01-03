package kr.ac.jbnu.ch.affiliates.models

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.helper.AffiliateHelper
import kr.ac.jbnu.ch.frameworks.models.GlideApp
import kr.ac.jbnu.ch.frameworks.models.MyAppGlideModule
import kr.ac.jbnu.ch.pledge.helper.PledgeHelper
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class AffiliateListAdapter(private val isFiltered : Boolean) :
    RecyclerView.Adapter<AffiliateListAdapter.ViewHolder>(), Filterable {
    private lateinit var context : Context
    private var searchList : ArrayList<AffiliateDataModel>? = null
    private val userManagement = UserManagement()

    init{
        if(isFiltered){
            searchList = AffiliateHelper.storeList_filtered
        }

        else{
            searchList = AffiliateHelper.storeList
        }
    }

    interface OnItemClickListener{
        fun onItemClick(v : View, data : AffiliateDataModel, pos : Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_affiliates, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AffiliateListAdapter.ViewHolder, position: Int) {
        val data = searchList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return searchList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var txt_storeName : TextView = view.findViewById(R.id.txt_storeName)
        var txt_benefits : TextView = view.findViewById(R.id.txt_benefits)
        var txt_closed : TextView = view.findViewById(R.id.txt_closed)
        var txt_time : TextView = view.findViewById(R.id.txt_time)
        var txt_breakTime : TextView = view.findViewById(R.id.txt_breakTime)
        var img_storeLogo : ImageView = view.findViewById(R.id.img_storeLogo)
        var breakTimeLL : LinearLayout = view.findViewById(R.id.breakTimeLL)
        var closedLL : LinearLayout = view.findViewById(R.id.closedLL)
        var timeLL : LinearLayout = view.findViewById(R.id.timeLL)
        var type : TextView = view.findViewById(R.id.affiliate_type)
        var isFavorite : ImageView = view.findViewById(R.id.img_favorite)

        fun bind(data : AffiliateDataModel){
            txt_storeName.text = data.storeName
            txt_benefits.text = data.benefits
            txt_breakTime.text = data.breakTime
            txt_closed.text = data.closed
            txt_time.text = "${data.openTime} ~ ${data.closeTime}"

            when(data.affiliateType){
                "CH" -> {
                    type.text = "총학"
                    type.background = ContextCompat.getDrawable(context, R.drawable.model_affiliate_ch)
                    type.visibility = View.VISIBLE
                }

                "College" -> {
                    type.text = userManagement.convertCollegeCodeAsShortString(UserManagement.userInfo?.collegeCode)
                    type.background = ContextCompat.getDrawable(context, R.drawable.model_affiliate_college)
                    type.visibility = View.VISIBLE
                }
            }

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms( CenterCrop(), RoundedCorners(16))

            GlideApp.with(itemView)
                .load(data.storeLogo)
                .placeholder(R.drawable.ic_logo_no_slogan)
                .apply(requestOptions)
                .into(img_storeLogo)

            if(data.breakTime == ""){
                breakTimeLL.visibility = View.GONE
            }

            if(data.openTime == "" || data.closeTime == ""){
                timeLL.visibility = View.GONE
            }

            if(data.closed == ""){
                closedLL.visibility = View.GONE

            }

            if(data.isFavorite){
                isFavorite.setColorFilter(ContextCompat.getColor(context, R.color.accent), android.graphics.PorterDuff.Mode.SRC_IN)
            }

            else{
                isFavorite.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN)
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
                    if(isFiltered){
                        searchList = AffiliateHelper.storeList_filtered
                    }

                    else{
                        searchList = AffiliateHelper.storeList
                    }
                } else{
                    if(isFiltered){
                        val filteredList = ArrayList<AffiliateDataModel>()

                        for(store in AffiliateHelper.storeList_filtered){
                            if(store.storeName?.contains(charString) == true || store.benefits?.contains(charString) == true){
                                filteredList.add(store)
                            }
                        }

                        searchList = filteredList
                    }

                    else{
                        val filteredList = ArrayList<AffiliateDataModel>()

                        for(store in AffiliateHelper.storeList){
                            if(store.storeName?.contains(charString) == true || store.benefits?.contains(charString) == true){
                                filteredList.add(store)
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
                if (p1 != null) {
                    searchList = p1.values as ArrayList<AffiliateDataModel>
                }

                notifyDataSetChanged()
            }

        }
    }


}