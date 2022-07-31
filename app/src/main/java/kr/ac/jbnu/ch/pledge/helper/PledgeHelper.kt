package kr.ac.jbnu.ch.pledge.helper

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kr.ac.jbnu.ch.pledge.models.PledgeDataModel
import kr.ac.jbnu.ch.pledge.models.PledgeStatusModel

class PledgeHelper {
    private val db = FirebaseFirestore.getInstance()

    companion object{
        var pledgeList = ArrayList<PledgeDataModel>()
        var pledgeList_filtered = ArrayList<PledgeDataModel>()
    }

    fun getPledgeListByCategory(type : String, category : String, completion : (Boolean) -> Unit){
        pledgeList_filtered.clear()

        pledgeList.forEach{
            if(it.category == category){
                pledgeList_filtered.add(it)
            }
        }

        completion(true)
    }

    fun getPledgeList(type : String, completion : (Boolean) -> Unit){
        pledgeList.clear()

        when(type){
            "CH" -> {
                db.collection("Pledge").document("CH").collection("Status").get().addOnSuccessListener {
                    for(document in it){
                        val category = document.id
                        val pledgeMap = document.data.keys

                        pledgeMap.forEach {
                            Log.d("PledgeHelper", it + " -> " + document.data.get(it).toString())
                            val status = document.data.get(it).toString()
                            var statusAsModel : PledgeStatusModel? = null

                            when(status){
                                "Preparing" -> statusAsModel = PledgeStatusModel.PREPARE
                                "InProgress" -> statusAsModel = PledgeStatusModel.INPROCESS
                                "Complete" -> statusAsModel = PledgeStatusModel.DONE
                            }

                            pledgeList.add(PledgeDataModel(it, convertStatusAsKorean(statusAsModel), convertCategoryAsKorean(category)))
                        }
                    }

                    completion(true)
                }.addOnFailureListener {
                    completion(false)
                }
            }

            else -> {

            }
        }
    }

    fun convertCategoryAsKorean(eng : String) : String{
        var categoryAsKorean = ""

        when(eng){
            "App" -> categoryAsKorean = "앱"
            "Bachelor" -> categoryAsKorean = "취/창업 및 학사"
            "Culture" -> categoryAsKorean = "문화 및 예술"
            "Dorm" -> categoryAsKorean = "생활관"
            "Facility" -> categoryAsKorean = "시설 및 안전"
            "HumanRights" -> categoryAsKorean = "인권"
            "Welfare" -> categoryAsKorean = "복지"
        }

        return categoryAsKorean
    }

    fun convertStatusAsKorean(status : PledgeStatusModel?) : String{
        var statusAsKorean = ""

        when(status){
            PledgeStatusModel.DONE -> statusAsKorean = "이행 완료"
            PledgeStatusModel.INPROCESS -> statusAsKorean = "진행 중"
            PledgeStatusModel.PREPARE -> statusAsKorean = "준비 중"
        }

        return statusAsKorean
    }
}