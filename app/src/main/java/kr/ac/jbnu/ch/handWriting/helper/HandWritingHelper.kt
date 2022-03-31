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
        db.collection("HandWriting").addSnapshotListener{snapshot, e ->
            if(e != null){
                e.printStackTrace()
                completion(false)
            }

            for(dc in snapshot!!.documentChanges){
                val data = HandWritingDataModel(
                    dc.document.id,
                    dc.document.data.get("college") as? String ?: "",
                    dc.document.data.get("date") as? String ?: "",
                    dc.document.data.get("examDate") as? String ?: "",
                    dc.document.data.get("examName") as? String ?: "",
                    dc.document.data.get("howTO") as? String ?: "",
                    (dc.document.data.get("imageIndex") as? Long ?: 0).toInt(),
                    dc.document.data.get("meter") as? String ?: "",
                    dc.document.data.get("name") as? String ?: "",
                    (dc.document.data.get("recommend") as? Long ?: 0).toInt(),
                    dc.document.data.get("review") as? String ?: "",
                    dc.document.data.get("studentNo") as? String ?: "",
                    dc.document.data.get("term") as? String ?: "",
                    dc.document.data.get("title") as? String ?: "",
                    dc.document.data.get("uid") as? String ?: ""
                )

                when(dc.type){
                    DocumentChange.Type.ADDED -> {
                        if(!handWritingList.contains(data)){
                            handWritingList.add(data)
                        }
                    }

                    DocumentChange.Type.MODIFIED -> {
                        if(!handWritingList.contains(data)){
                            handWritingList.add(data)
                        }

                        else{
                            val index = handWritingList.indexOf(data)

                            if(index != null){
                                handWritingList[index] = data
                            }
                        }
                    }

                    DocumentChange.Type.REMOVED -> {
                        val index = handWritingList.indexOf(data)

                        if(index != null){
                            handWritingList.removeAt(index)
                        }
                    }
                }
            }

            handWritingList.sortByDescending { it.date }
            completion(true)
        }
    }
}