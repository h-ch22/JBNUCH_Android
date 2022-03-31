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
    }

    fun getImage(id : String, imageIndex : Int, completion : (Boolean) -> Unit){
        imgList.clear()

        for(i in 0..imageIndex-1){
            imgList.add(storage.reference.child("Petition/${id}/${i}.png"))
        }

        completion(true)
    }


    fun uploadPetition(title : String, contents : String, uri : MutableList<Uri>, completion : (Boolean) -> Unit){
        val formatter = SimpleDateFormat("yyyy. MM. dd. kk:mm:ss.SSSS")

        db.collection("Petition").add(
            hashMapOf(
                "title" to AES256Util.encrypt(title).replace(System.getProperty("line.separator"), ""),
                "contents" to AES256Util.encrypt(contents).replace(System.getProperty("line.separator"), ""),
                "imageIndex" to uri.size,
                "timeStamp" to formatter.format(Date()),
                "author" to AES256Util.encrypt(FirebaseAuth.getInstance().currentUser?.uid)
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

    fun getPetitionList(completion : (Boolean) -> Unit){
        db.collection("Petition").addSnapshotListener{snapshot, e ->
            if( e != null ){
                e.printStackTrace()
                completion(false)
            }

            for(dc in snapshot!!.documentChanges){
                val statusAsString = dc.document.data.get("status") as? String ?: ""
                var statusAsModel : PetitionStatusModel? = null

                when(statusAsString){
                    "Answered" ->
                        statusAsModel = PetitionStatusModel.ANSWERED

                    "Finished" ->
                        statusAsModel = PetitionStatusModel.FINISH

                    else ->
                        statusAsModel = PetitionStatusModel.INPROCESS
                }

                val data = PetitionDataModel(dc.document.id,
                    dc.document.data.get("author") as? String ?: "",
                    dc.document.data.get("contents") as? String ?: "",
                    (dc.document.data.get("imageIndex") as? Long ?: 0.0).toInt(),
                    (dc.document.data.get("recommend") as? Long ?: 0.0).toInt(),
                    dc.document.data.get("timeStamp") as? String ?: "",
                    dc.document.data.get("title") as? String ?: "",
                    statusAsModel!!)

                when(dc.type){
                    DocumentChange.Type.ADDED -> {
                        if(!petitionList.contains(data)){
                            petitionList.add(data)
                        }
                    }

                    DocumentChange.Type.MODIFIED -> {
                        if(!petitionList.contains(data)){
                            petitionList.add(data)
                        }

                        else{
                            val index = petitionList.indexOf(data)

                            if(index != null){
                                petitionList[index] = data
                            }
                        }
                    }

                    DocumentChange.Type.REMOVED -> {
                        val index = petitionList.indexOf(data)

                        if(index != null){
                            petitionList.removeAt(index)
                        }
                    }
                }
            }

            petitionList.sortByDescending { it.timeStamp }
            completion(true)
        }
    }
}