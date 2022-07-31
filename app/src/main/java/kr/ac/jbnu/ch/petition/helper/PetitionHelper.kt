package kr.ac.jbnu.ch.petition.helper

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.petition.models.PetitionDataModel
import kr.ac.jbnu.ch.petition.models.PetitionRecommendModel
import kr.ac.jbnu.ch.petition.models.PetitionStatusModel
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PetitionHelper {
    private val db = FirebaseFirestore.getInstance()
    private val userManagement = UserManagement()
    private val storage = FirebaseStorage.getInstance()

    companion object{
        var petitionList = ArrayList<PetitionDataModel>()
        var imgList = ArrayList<StorageReference>()
        var recommendList = ArrayList<PetitionRecommendModel>()
        var filteredList = ArrayList<PetitionDataModel>()
    }

    fun getImage(id : String, imageIndex : Int, completion : (Boolean) -> Unit){
        imgList.clear()

        for(i in 0..imageIndex-1){
            imgList.add(storage.reference.child("Petition/${id}/${i}.png"))
        }

        completion(true)
    }

    fun uploadPetition(category : String, title : String, contents : String, uri : MutableList<Uri>, completion : (Boolean) -> Unit){
        val formatter = SimpleDateFormat("yyyy. MM. dd. kk:mm:ss.SSSS")

        db.collection("Petition").add(
            hashMapOf(
                "title" to AES256Util.encrypt(title).replace(System.getProperty("line.separator"), ""),
                "contents" to AES256Util.encrypt(contents).replace(System.getProperty("line.separator"), ""),
                "imageIndex" to uri.size,
                "timeStamp" to formatter.format(Date()),
                "author" to AES256Util.encrypt(FirebaseAuth.getInstance().currentUser?.uid),
                "category" to category
            )
        ).addOnSuccessListener {
            if(uri.size > 0){
                for(i in 0..uri.size - 1){
                    storage.reference.child("Petition/${it.id}/${i}.png").putFile(uri.get(i)).addOnFailureListener{
                        it.printStackTrace()
                        completion(false)
                    }
                }

                completion(true)
            }

            else{completion(true)}
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
        }
    }

    fun remove(data : PetitionDataModel, completion: (Boolean) -> Unit){
        db.collection("Petition").document(data.id).delete().addOnCompleteListener {
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

    fun recommend(data : PetitionDataModel, completion : (Boolean) -> Unit){
        val dateFormatter = SimpleDateFormat("yyyy. MM. dd. kk:mm:ss.SSSS")

        db.collection("Petition").document(data.id).collection("Recommends").add(
            hashMapOf(
                "date" to dateFormatter.format(Date()),
                "college" to AES256Util.encrypt(UserManagement.userInfo?.college?.replace(System.getProperty("line.separator"), "")).replace(System.getProperty("line.separator"), ""),
                "name" to AES256Util.encrypt(UserManagement.userInfo?.name?.replace(System.getProperty("line.separator"), "")).replace(System.getProperty("line.separator"), ""),
                "studentNo" to AES256Util.encrypt(UserManagement.userInfo?.studentNo?.replace(System.getProperty("line.separator"), "")).replace(System.getProperty("line.separator"), ""),
                "uid" to UserManagement.userInfo?.uid
            )
        ).addOnCompleteListener {
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

    fun getRecommender(data : PetitionDataModel, completion: (Boolean) -> Unit){
        recommendList.clear()

        db.collection("Petition").document(data.id).collection("Recommends").get().addOnSuccessListener { result ->
            for(document in result){
                val docData = PetitionRecommendModel(
                    document.get("uid") as? String ?: "",
                    document.get("college") as? String ?: "",
                    document.get("name") as? String ?: "",
                    document.get("studentNo") as? String ?: "",
                    document.get("date") as? String ?: ""
                )

                Log.d("PetitionHelper", docData.toString())

                recommendList.add(docData)
            }

            Log.d("PetitionHelper", PetitionHelper.recommendList.toString())
            completion(true)
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
        }


    }

    fun getPetitionList(completion : (Boolean) -> Unit){
        petitionList.clear()
        filteredList.clear()

        db.collection("Petition").get().addOnSuccessListener { result ->
            for(document in result){
                val statusAsString = document.data.get("status") as? String ?: ""
                var statusAsModel : PetitionStatusModel? = null

                when(statusAsString){
                    "Answered" ->
                        statusAsModel = PetitionStatusModel.ANSWERED

                    "Finished" ->
                        statusAsModel = PetitionStatusModel.FINISH

                    else ->
                        statusAsModel = PetitionStatusModel.INPROCESS
                }

                val data = PetitionDataModel(document.get("category") as? String ?: "",
                    document.id,
                    document.data.get("author") as? String ?: "",
                    document.data.get("contents") as? String ?: "",
                    (document.data.get("imageIndex") as? Long ?: 0.0).toInt(),
                    (document.data.get("recommend") as? Long ?: 0.0).toInt(),
                    document.data.get("timeStamp") as? String ?: "",
                    document.data.get("title") as? String ?: "",
                    statusAsModel!!)

                petitionList.add(data)
            }

            filteredList.addAll(petitionList)

            filteredList.sortByDescending { it.timeStamp }

            Log.d("PetitionHelper", filteredList.toString())

            completion(true)
        }
    }
}