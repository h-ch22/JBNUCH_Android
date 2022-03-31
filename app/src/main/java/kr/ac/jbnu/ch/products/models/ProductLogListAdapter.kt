package kr.ac.jbnu.ch.products.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.type.DateTime
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.products.helper.ProductsHelper
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ProductLogListAdapter :
    RecyclerView.Adapter<ProductLogListAdapter.ViewHolder>(){
        private lateinit var context : Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductLogListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_productlog, parent, false)

        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductLogListAdapter.ViewHolder, position: Int) {
        val data = ProductsHelper.productLogList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return ProductsHelper.productLogList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val ic_product : ImageView = view.findViewById(R.id.ic_product)
        val txt_productName : TextView = view.findViewById(R.id.txt_productName)
        val txt_time : TextView = view.findViewById(R.id.txt_time)
        val txt_isReturned : TextView = view.findViewById(R.id.txt_isReturned)
        val ic_isReturned : ImageView = view.findViewById(R.id.ic_isReturned)

        fun bind(data : ProductLogDataModel){
            txt_productName.text = "${data.productName} ${data.number}개"
            txt_time.text = data.dateTime

            when(data.productName){
                "농구공" -> {
                    ic_product.setImageResource(R.drawable.ic_basketball)
                    ic_product.setColorFilter(null)

                }

                "고데기" -> {
                    ic_product.setImageResource(R.drawable.ic_curlingiron)
                    ic_product.setColorFilter(null)

                }

                "부심기" -> {
                    ic_product.setImageResource(R.drawable.ic_flag)
                    ic_product.setColorFilter(null)

                }

                "족구공" -> {
                    ic_product.setImageResource(R.drawable.ic_football)
                    ic_product.setColorFilter(R.color.txtColor)
                }

                "풋살공" -> {
                    ic_product.setImageResource(R.drawable.ic_football)
                    ic_product.setColorFilter(R.color.txtColor)
                }

                "돗자리" -> {
                    ic_product.setImageResource(R.drawable.ic_mat)
                    ic_product.setColorFilter(null)

                }

                "족구네트" -> {
                    ic_product.setImageResource(R.drawable.ic_net)
                    ic_product.setColorFilter(null)

                }

                "축구공" -> {
                    ic_product.setImageResource(R.drawable.ic_soccerball)
                    ic_product.setColorFilter(R.color.txtColor)
                }

                "조끼 (파랑)" -> {
                    ic_product.setImageResource(R.drawable.ic_uniform_b)
                    ic_product.setColorFilter(null)

                }

                "조끼 (초록)" -> {
                    ic_product.setImageResource(R.drawable.ic_uniform_g)
                    ic_product.setColorFilter(null)

                }

                "조끼 (핑크)" -> {
                    ic_product.setImageResource(R.drawable.ic_unifrom_p)
                    ic_product.setColorFilter(null)

                }
            }

            val formatter = SimpleDateFormat("yyyy. MM. dd. HH:mm:ss")
            val calendar = Calendar.getInstance()
            calendar.time = formatter.parse(data.dateTime)

            calendar.add(Calendar.DAY_OF_MONTH, 3)

            when(data.isReturned){
                true -> {
                    ic_isReturned.setImageResource(R.drawable.ic_select)
                    ic_isReturned.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY)
                    txt_isReturned.text = "반납 완료"
                    txt_isReturned.setTextColor(context.resources.getColor(R.color.green))
                }

                false -> {
                    ic_isReturned.setImageResource(R.drawable.ic_closed)
                    ic_isReturned.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);

                    txt_isReturned.text = "미반납 (반납기일 : ${formatter.format(calendar.time)})"
                    txt_isReturned.setTextColor(context.resources.getColor(R.color.red))
                }
            }
        }
    }
}