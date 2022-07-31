package kr.ac.jbnu.ch.products.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.products.helper.ProductsHelper

class CollegeProductListAdapter :
    RecyclerView.Adapter<CollegeProductListAdapter.ViewHolder>(){
        private lateinit var context : Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollegeProductListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_product, parent, false)

        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollegeProductListAdapter.ViewHolder, position: Int) {
        val data = ProductsHelper.collegeProductList!![position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return ProductsHelper.collegeProductList!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val ic_product : ImageView = view.findViewById(R.id.ic_product)
        val txt_productName : TextView = view.findViewById(R.id.txt_productName)
        val txt_late : TextView = view.findViewById(R.id.txt_late)

        fun bind(data : ProductDataModel){
            txt_productName.text = "${data.productName}"
            txt_late.text = "${data.late} / ${data.all}"

            when(data.productName){
                "고데기" -> {
                    ic_product.setImageResource(R.drawable.ic_curlingiron)
                    ic_product.setColorFilter(null)
                }

                "여성용품 (대)",
                "여성용품 (중)",
                "여성용품" -> {
                    ic_product.setImageResource(R.drawable.ic_womentools)
                    ic_product.setColorFilter(null)
                }

                "보조 배터리" -> {
                    ic_product.setImageResource(R.drawable.ic_battery)
                    ic_product.setColorFilter(null)
                }

                "충전기" -> {
                    ic_product.setImageResource(R.drawable.ic_charger)
                    ic_product.setColorFilter(null)
                }

                "컵" -> {
                    ic_product.setImageResource(R.drawable.ic_cup)
                    ic_product.setColorFilter(null)
                }

                "드라이기" -> {
                    ic_product.setImageResource(R.drawable.ic_dryer)
                    ic_product.setColorFilter(null)
                }

                "머리끈" -> {
                    ic_product.setImageResource(R.drawable.ic_hairtie)
                    ic_product.setColorFilter(null)
                }

                "핫팩" -> {
                    ic_product.setImageResource(R.drawable.ic_handwarmer)
                    ic_product.setColorFilter(null)
                }

                "가위" -> {
                    ic_product.setImageResource(R.drawable.ic_scissors)
                    ic_product.setColorFilter(null)
                }

                "슬리퍼" -> {
                    ic_product.setImageResource(R.drawable.ic_slippers)
                    ic_product.setColorFilter(null)
                }

                "스테이플러" -> {
                    ic_product.setImageResource(R.drawable.ic_stapler)
                    ic_product.setColorFilter(null)
                }

                "테이프" -> {
                    ic_product.setImageResource(R.drawable.ic_tape)
                    ic_product.setColorFilter(null)
                }

                "우산" -> {
                    ic_product.setImageResource(R.drawable.ic_umbrella)
                    ic_product.setColorFilter(null)
                }

                "기화펜" -> {
                    ic_product.setImageResource(R.drawable.ic_pen)
                    ic_product.setColorFilter(null)
                }

                "농구공" -> {
                    ic_product.setImageResource(R.drawable.ic_basketball)
                    ic_product.setColorFilter(null)
                }

                "헬멧" -> {
                    ic_product.setImageResource(R.drawable.ic_helmet)
                    ic_product.setColorFilter(null)
                }

                "배드민턴 라켓" -> {
                    ic_product.setImageResource(R.drawable.ic_lacket)
                    ic_product.setColorFilter(null)
                }

                "돗자리" -> {
                    ic_product.setImageResource(R.drawable.ic_mat)
                    ic_product.setColorFilter(null)
                }

                "축구공" -> {
                    ic_product.setImageResource(R.drawable.ic_soccerball)
                    ic_product.setColorFilter(R.color.txtColor)
                }

                "담요" -> {
                    ic_product.setImageResource(R.drawable.ic_blanket)
                    ic_product.setColorFilter(null)
                }

                "보드게임" -> {
                    ic_product.setImageResource(R.drawable.ic_boardgame)
                    ic_product.setColorFilter(null)
                }

                "계산기" -> {
                    ic_product.setImageResource(R.drawable.ic_calculator)
                    ic_product.setColorFilter(null)
                }

                "실험복" -> {
                    ic_product.setImageResource(R.drawable.ic_labcoat)
                    ic_product.setColorFilter(null)
                }

                "타이레놀",
                    "소화제",
                    "해열제",
                    "진통제",
                    "지사제",
                    "감기약",
                    "빨간약",
                    "복통약" -> {
                        ic_product.setImageResource(R.drawable.ic_medicine)
                        ic_product.setColorFilter(null)
                    }

                "파스",
                "붕대",
                "종이 반창고" -> {
                    ic_product.setImageResource(R.drawable.ic_bandage)
                    ic_product.setColorFilter(null)
                }

                "에어파스" -> {
                    ic_product.setImageResource(R.drawable.ic_airparse)
                    ic_product.setColorFilter(null)
                }

                "밴드" -> {
                    ic_product.setImageResource(R.drawable.ic_band)
                    ic_product.setColorFilter(null)
                }

                "면봉" -> {
                    ic_product.setImageResource(R.drawable.ic_cottonswabs)
                    ic_product.setColorFilter(null)
                }

                "소독약" -> {
                    ic_product.setImageResource(R.drawable.ic_disinfectant)
                    ic_product.setColorFilter(null)
                }

                "거즈",
                    "거즈 (대)",
                    "거즈 (중)",
                    "거즈 (소)" -> {
                    ic_product.setImageResource(R.drawable.ic_gauze)
                    ic_product.setColorFilter(null)
                    }

                "A4 (박스 단위)" -> {
                    ic_product.setImageResource(R.drawable.ic_a4)
                    ic_product.setColorFilter(null)
                }

                "풀" -> {
                    ic_product.setImageResource(R.drawable.ic_glue)
                    ic_product.setColorFilter(null)
                }

                "검정펜",
                "펜" -> {
                    ic_product.setImageResource(R.drawable.ic_pen)
                    ic_product.setColorFilter(null)
                }

                "압핀" -> {
                    ic_product.setImageResource(R.drawable.ic_pin)
                    ic_product.setColorFilter(null)
                }

                "자" -> {
                    ic_product.setImageResource(R.drawable.ic_ruler)
                    ic_product.setColorFilter(null)
                }

                "온수찜질기" -> {
                    ic_product.setImageResource(R.drawable.ic_handwarmer)
                    ic_product.setColorFilter(null)
                }

                "마스크" -> {
                    ic_product.setImageResource(R.drawable.ic_mask)
                    ic_product.setColorFilter(null)
                }

                "빨대" -> {
                    ic_product.setImageResource(R.drawable.ic_straw)
                    ic_product.setColorFilter(null)
                }

                "텀블러" -> {
                    ic_product.setImageResource(R.drawable.ic_tumbler)
                    ic_product.setColorFilter(null)
                }

                "연고" -> {
                    ic_product.setImageResource(R.drawable.ic_ointment)
                    ic_product.setColorFilter(null)
                }

                "알콜솜" -> {
                    ic_product.setImageResource(R.drawable.ic_alcoholswab)
                    ic_product.setColorFilter(null)
                }

                "인공눈물" -> {
                    ic_product.setImageResource(R.drawable.ic_artificialtear)
                    ic_product.setColorFilter(null)
                }

                "솜" -> {
                    ic_product.setImageResource(R.drawable.ic_cotton)
                    ic_product.setColorFilter(null)
                }

                "가글" -> {
                    ic_product.setImageResource(R.drawable.ic_gargle)
                    ic_product.setColorFilter(null)
                }

                "과산화수소" -> {
                    ic_product.setImageResource(R.drawable.ic_peroxide)
                    ic_product.setColorFilter(null)
                }

                "핀셋" -> {
                    ic_product.setImageResource(R.drawable.ic_tweezer)
                    ic_product.setColorFilter(null)
                }

                "붕대용 테이프" -> {
                    ic_product.setImageResource(R.drawable.ic_tape4bandage)
                    ic_product.setColorFilter(null)
                }

                "샤프" -> {
                    ic_product.setImageResource(R.drawable.ic_mechanicalpencil)
                    ic_product.setColorFilter(null)
                }

                "샤프심" -> {
                    ic_product.setImageResource(R.drawable.ic_pencillead)
                    ic_product.setColorFilter(null)
                }

                "보드마커" -> {
                    ic_product.setImageResource(R.drawable.ic_boardmarker)
                    ic_product.setColorFilter(R.color.txtColor)
                }

                "케이블타이" -> {
                    ic_product.setImageResource(R.drawable.ic_cabletie)
                    ic_product.setColorFilter(R.color.txtColor)
                }

                "수정테이프" -> {
                    ic_product.setImageResource(R.drawable.ic_correctiontape)
                    ic_product.setColorFilter(null)
                }

                "지우개" -> {
                    ic_product.setImageResource(R.drawable.ic_eraser)
                    ic_product.setColorFilter(null)
                }

                "칼" -> {
                    ic_product.setImageResource(R.drawable.ic_knife)
                    ic_product.setColorFilter(null)
                }

                "펀치" -> {
                    ic_product.setImageResource(R.drawable.ic_punch)
                    ic_product.setColorFilter(null)
                }

                "빨간펜" -> {
                    ic_product.setImageResource(R.drawable.ic_pen_r)
                    ic_product.setColorFilter(null)
                }

                "스카치테이프" -> {
                    ic_product.setImageResource(R.drawable.ic_scotchtape)
                    ic_product.setColorFilter(null)
                }

                "렌즈 세척액" -> {
                    ic_product.setImageResource(R.drawable.ic_eyedrop)
                    ic_product.setColorFilter(null)
                }

                "줄자" -> {
                    ic_product.setImageResource(R.drawable.ic_tapemeasure)
                    ic_product.setColorFilter(null)
                }

                "섬유 탈취제" -> {
                    ic_product.setImageResource(R.drawable.ic_spray)
                    ic_product.setColorFilter(null)
                }

                "공구 상자" -> {
                    ic_product.setImageResource(R.drawable.ic_toolbox)
                    ic_product.setColorFilter(null)
                }

                "바람 주입기" -> {
                    ic_product.setImageResource(R.drawable.ic_pump)
                    ic_product.setColorFilter(null)
                }
            }
        }
    }
}