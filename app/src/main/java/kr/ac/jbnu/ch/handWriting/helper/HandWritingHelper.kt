package kr.ac.jbnu.ch.handWriting.helper

import android.net.Uri
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.handWriting.models.HandWritingDataModel
import java.text.SimpleDateFormat

class HandWritingHelper {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    companion object{
        var handWritingList = ArrayList<HandWritingDataModel>()
        var imgList = ArrayList<StorageReference>()
    }

    fun getImage(id : String, imageIndex : Int, completion : (Boolean) -> Unit){
        imgList.clear()

        for(i in 0..imageIndex - 1){
            imgList.add(storage.reference.child("handWriting/${id}/${i}.png"))
        }

        completion(true)
    }

    fun uploadHandWriting(data : HandWritingDataModel, uri : MutableList<Uri>, completion : (Boolean) -> Unit){
        db.collection("HandWriting").add(
            hashMapOf(
                "college" to data.college,
                "date" to data.date,
                "examDate" to data.examDate,
                "howTO" to data.howTO,
                "imageIndex" to data.imageIndex,
                "meter" to data.meter,
                "name" to AES256Util.encrypt(data.name).replace(System.getProperty("line.separator"), ""),
                "recommend" to 0,
                "review" to data.review,
                "studentNo" to AES256Util.encrypt(data.studentNo).replace(System.getProperty("line.separator"), ""),
                "term" to data.term,
                "title" to data.title,
                "uid" to data.uid,
                "examName" to data.examName
            )
        ).addOnSuccessListener {
            if(uri.size > 0){
                for(i in 0..uri.size - 1){
                    storage.reference.child("handWriting/${it.id}/${i}/png").putFile(uri.get(i)).addOnFailureListener {
                        it.printStackTrace()
                        completion(false)
                    }
                }

                completion(true)
            }

            else{
                completion(true)
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
        }
    }

    fun getHandWritingList(completion : (Boolean) -> Unit){
        handWritingList.clear()

        db.collection("HandWriting").get().addOnSuccessListener{result ->
            for(document in result){
                val data = HandWritingDataModel(
                    document.id,
                    document.data.get("college") as? String ?: "",
                    document.data.get("date") as? String ?: "",
                    document.data.get("examDate") as? String ?: "",
                    document.data.get("examName") as? String ?: "",
                    document.data.get("howTO") as? String ?: "",
                    (document.data.get("imageIndex") as? Long ?: 0).toInt(),
                    document.data.get("meter") as? String ?: "",
                    document.data.get("name") as? String ?: "",
                    (document.data.get("recommend") as? Long ?: 0).toInt(),
                    document.data.get("review") as? String ?: "",
                    document.data.get("studentNo") as? String ?: "",
                    document.data.get("term") as? String ?: "",
                    document.data.get("title") as? String ?: "",
                    document.data.get("uid") as? String ?: ""
                )

                handWritingList.add(data)
            }

            handWritingList.sortByDescending { it.date }
            completion(true)
        }
    }
}