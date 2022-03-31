package kr.ac.jbnu.ch.feedbackhub.helper

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubDataModel
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubItemModel
import kr.ac.jbnu.ch.feedbackhub.models.FeedbackHubTypeModel
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FeedbackHubHelper {
    companion object{
        var feedbackList = ArrayList<FeedbackHubDataModel>()
        var myFeedbackList = ArrayList<FeedbackHubDataModel>()

        fun convertCategoryAsString(category : FeedbackHubItemModel) : String{
            var categoryAsString = ""

            when(category){
                FeedbackHubItemModel.APP -> categoryAsString = "앱"
                FeedbackHubItemModel.COMMUNICATION -> categoryAsString = "소통"
                FeedbackHubItemModel.FACILITY -> categoryAsString = "시설"
                FeedbackHubItemModel.FESTIVAL -> categoryAsString = "행사"
                FeedbackHubItemModel.OTHERS -> categoryAsString = "기타"
                FeedbackHubItemModel.PLEDGE -> categoryAsString = "공약"
                FeedbackHubItemModel.WELFARE -> categoryAsString = "복지"
            }

            return categoryAsString
        }

        fun convertTypeAsString(type : FeedbackHubTypeModel) : String{
            var typeAsString = ""

            when(type){
                FeedbackHubTypeModel.HEART -> typeAsString = "칭찬해요"
                FeedbackHubTypeModel.IMPROVE -> typeAsString = "개선해주세요"
                FeedbackHubTypeModel.QUESTION -> typeAsString = "궁금해요"
            }

            return typeAsString
        }
    }

    private val db = FirebaseFirestore.getInstance()

    fun uploadFeedback(data : FeedbackHubDataModel, completion : (Boolean) -> Unit){
        val formatter = SimpleDateFormat("yyyy. MM. dd. kk:mm:ss")
        val dateAsString = formatter.format(Date())

        val feedbackData = hashMapOf(
            "Author" to AES256Util.encrypt("${UserManagement.userInfo?.college} ${UserManagement.userInfo?.studentNo} ${UserManagement.userInfo?.name}").replace("\n", ""),
            "Title" to AES256Util.encrypt(data.title).replace("\n", ""),
            "Contents" to AES256Util.encrypt(data.contents).replace("\n", ""),
            "Type" to AES256Util.encrypt(data.type).replace("\n", ""),
            "Category" to AES256Util.encrypt(data.category).replace("\n", ""),
            "UID" to UserManagement.userInfo?.uid,
            "Date" to dateAsString
        )

        db.collection("Feedback").document().set(feedbackData).addOnCompleteListener {
            if(it.isSuccessful){
                completion(true)

                return@addOnCompleteListener
            }

            else{
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)

            return@addOnFailureListener
        }
    }

    fun getFeedbackList(completion : (Boolean) -> Unit){
        db.collection("Feedback").addSnapshotListener { value, error ->
            if(error != null){
                error.printStackTrace()

                completion(false)

                return@addSnapshotListener
            }

            else{
                for(dc in value!!.documentChanges){
                    val docID = dc.document.id
                    val author = dc.document.get("Author") as? String ?: ""
                    val category = dc.document.get("Category") as? String ?: ""
                    val contents = dc.document.get("Contents") as? String ?: ""
                    val date = dc.document.get("Date") as? String ?: ""
                    val title = dc.document.get("Title") as? String ?: ""
                    val type = dc.document.get("Type") as? String ?: ""
                    val uid = dc.document.get("UID") as? String ?: ""
                    val answer = dc.document.get("Answer") as? String ?: ""
                    val answer_author = dc.document.get("Answer_Author") as? String ?: ""
                    val answer_date = dc.document.get("Answer_Date") as? String ?: ""

                    val data = FeedbackHubDataModel(title, contents, category, type, author, date, answer, answer_author, answer_date, uid, docID)

                    when(dc.type){
                        DocumentChange.Type.ADDED -> {
                            feedbackList.add(data)
                        }

                        DocumentChange.Type.MODIFIED -> {
                            val index = feedbackList.indexOf(data)

                            if(index != null){
                                feedbackList[index] = data
                            }

                            else{
                                feedbackList.add(data)
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
                            val index = feedbackList.indexOf(data)

                            if(index != null){
                                feedbackList.removeAt(index)
                            }
                        }
                    }
                }

                feedbackList.sortByDescending { it.date }

                completion(true)
            }
        }
    }

    fun getMyFeedback(completion : (Boolean) -> Unit){
        db.collection("Feedback").whereEqualTo("UID", UserManagement.userInfo?.uid).get().addOnSuccessListener {
            for(document in it.documents){
                val docID = document.id
                val category = document.data?.get("Category") as? String ?: ""
                val contents = document.data?.get("Contents") as? String ?: ""
                val date = document.data?.get("Date") as? String ?: ""
                val title = document.data?.get("Title") as? String ?: ""
                val type = document.data?.get("Type") as? String ?: ""
                val answer = document.data?.get("Answer") as? String ?: ""
                val answer_author = document.data?.get("Answer_Author") as? String ?: ""
                val answer_date = document.data?.get("Answer_Date") as? String ?: ""

                val data = FeedbackHubDataModel(title, contents, category, type, "", date, answer, answer_author, answer_date, "", docID)

                if(!myFeedbackList.contains(data)){
                    myFeedbackList.add(data)
                }

                else{
                    val index = myFeedbackList.indexOf(data)

                    if(index != null){
                        myFeedbackList[index] = data
                    }
                }
            }

            feedbackList.sortByDescending { it.date }

            completion(true)

            return@addOnSuccessListener
        }.addOnFailureListener {
            it.printStackTrace()

            completion(false)

            return@addOnFailureListener
        }
    }

    fun uploadAnswer(id : String, answer : String, date : String, author : String, completion: (Boolean) -> Unit){
        val answerData : Map<String, Any> = mapOf(
            "Answer" to AES256Util.encrypt(answer),
            "Answer_Author" to AES256Util.encrypt(author),
            "Answer_Date" to AES256Util.encrypt(date))

        db.collection("Feedback").document(id).update(answerData).addOnCompleteListener {
            if(it.isSuccessful){
                completion(true)
            }

            else{
                completion(false)
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
        }
    }
}