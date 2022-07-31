package kr.ac.jbnu.ch.meal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutMealBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.meal.helper.MealHelper
import java.util.*

class MealView : Fragment() {
    var day = MutableLiveData<String>("")
    private lateinit var helper : MealHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutMealBinding = DataBindingUtil.inflate(inflater , R.layout.layout_meal , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val dayNum = calendar.get(Calendar.DAY_OF_WEEK)

        when(dayNum){
            1 -> {
                day.value = "일"
            }

            2 -> {
                day.value = "월"
            }

            3 -> {
                day.value = "화"
            }

            4 -> {
                day.value = "수"
            }

            5 -> {
                day.value = "목"
            }

            6 -> {
                day.value = "금"
            }

            7 -> {
                day.value = "토"
            }
        }

        helper = MealHelper(context as MainActivity)

        day.value?.let {
            helper.getMeal(it){ it1 ->
                if(it1){
                    layout.root.post{
                        layout.menuJinsooLunch.text = MealHelper.mealList.get("jinsoo_lunch")
                        layout.menuJinsooDinner.text = MealHelper.mealList.get("jinsoo_dinner")
                        layout.menuMedicalLunch.text = MealHelper.mealList.get("medical_lunch")
                        layout.menuDormBreakfast.text = MealHelper.mealList.get("dorm_breakfast")
                        layout.menuDormLunch.text = MealHelper.mealList.get("dorm_lunch")
                        layout.menuDormDinner.text = MealHelper.mealList.get("dorm_dinner")

                    }
                }
            }
        }

        return layout.root
    }

    fun onClick(v : View){

    }
}