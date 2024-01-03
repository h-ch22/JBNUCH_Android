package kr.ac.jbnu.ch.notice.helper

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.ch.notice.models.NoticeDataModel
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.AdminCodeModel
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoticeHelper {
    private val db = FirebaseFirestore.getInstance()
    private val userManagement = UserManagement()
    private val storage = FirebaseStorage.getInstance()
    private val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    companion object{
        var noticeList = ArrayList<NoticeDataModel>()
        var imgList = ArrayList<StorageReference>()
    }

    fun createId() : String{
        return (1..28)
            .map{allowedChars.random()}
            .joinToString("")
    }

    fun getImage(id : String, imageIndex : Int, type : String, completion : (Boolean) -> Unit){
        imgList.clear()

        for(i in 0..imageIndex-1){
            imgList.add(storage.reference.child("notice/CH/${id}/${i}.png"))
        }

        completion(true)
    }

    fun uploadNotice(title : String, contents : String, uri : MutableList<Uri>, completion: (Boolean) -> Unit){
        val formatter = SimpleDateFormat("yyyy. MM. dd. HH:mm")
        val id = createId()
        val data = mapOf<String, Any>(
            "title" to title,
            "dateTime" to formatter.format(Date()),
            "id" to id,
            "imageIndex" to uri.size,
            "contents" to contents,
            "url" to ""
        )

        var college = ""

        when(UserManagement.userInfo?.admin){
            AdminCodeModel.CH_PRD_President, AdminCodeModel.CH_PRD_VicePresident -> {
                college = "CH"
            }

            AdminCodeModel.SOC_PRD_President, AdminCodeModel.SOC_PRD_VicePresident -> {
                college = "SOC"
            }

            AdminCodeModel.CON_PRD_President, AdminCodeModel.CON_PRD_VicePresident -> {
                college = "CON"
            }

            AdminCodeModel.COM_PRD_President, AdminCodeModel.COM_PRD_VicePresident -> {
                college = "COM"
            }

            AdminCodeModel.COH_PRD_President, AdminCodeModel.COH_PRD_VicePresident -> {
                college = "COH"
            }

            AdminCodeModel.CHE_PRD_President, AdminCodeModel.CHE_PRD_VicePresident -> {
                college = "CHE"
            }

            else -> {
                completion(false)

                return
            }
        }
        db.collection("Notice").document(college).get().addOnCompleteListener {
            if(it.isSuccessful){
                val document = it.result

                if(document.exists()){
                    db.collection("Notice").document(college).update(id, data).addOnCompleteListener {
                        if(it.isSuccessful){
                            if(uri.size >= 1){
                                for(i in 0..uri.size - 1){
                                    storage.reference.child("notice/${college}/${id}/${i}.png").putFile(uri.get(i)).addOnFailureListener {
                                        it.printStackTrace()
                                        completion(false)
                                    }
                                }

                                completion(true)
                            }

                            else{
                                completion(true)
                            }

                            return@addOnCompleteListener
                        }

                        else{
                            completion(false)

                            return@addOnCompleteListener
                        }
                    }.addOnFailureListener {
                        it.printStackTrace()

                        completion(false)

                        return@addOnFailureListener
                    }
                }

                else{
                    db.collection("Notice").document(college).set(hashMapOf(id to data)).addOnCompleteListener {
                        if(it.isSuccessful){
                            if(uri.size >= 1){
                                for(i in 0..uri.size - 1){
                                    storage.reference.child("notice/${college}/${id}/${i}.png").putFile(uri.get(i)).addOnFailureListener {
                                        it.printStackTrace()
                                        completion(false)
                                    }
                                }

                                completion(true)
                            }

                            else{
                                completion(true)
                            }

                            return@addOnCompleteListener
                        }

                        else{
                            completion(false)

                            return@addOnCompleteListener
                        }
                    }.addOnFailureListener {
                        it.printStackTrace()

                        completion(false)

                        return@addOnFailureListener
                    }
                }
            }
        }

    }

    fun getNotice(category : String, completion : (Boolean) -> Unit) {
        noticeList.clear()
        if(category == "College"){
            if (UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COM || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CON || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CHE) {
                userManagement.convertCollegeCodeAsString(UserManagement.userInfo?.collegeCode)?.let {
                    db.collection("Notice").document(it).get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val document = it.result

                            if (document.exists()) {
                                val docMap = document.data?.toMap()

                                if (docMap != null) {
                                    for ((key, value) in docMap) {
                                        val noticeMap = value as Map<String, *>

                                        val title = noticeMap.get("title") as? String ?: ""
                                        val contents = noticeMap.get("contents") as? String ?: ""
                                        val id = noticeMap.get("id") as? String ?: ""
                                        val imageIndex = noticeMap.get("imageIndex") as? Long ?: 0.0
                                        val date = noticeMap.get("dateTime") as? String ?: ""

                                        noticeList.add(
                                            NoticeDataModel(
                                                id,
                                                title,
                                                contents,
                                                date,
                                                "College",
                                                imageIndex.toInt()
                                            )
                                        )
                                    }

                                    completion(true)

                                } else {
                                    completion(false)
                                }
                            } else {
                                completion(false)
                            }
                        } else {
                            completion(false)
                        }
                    }
                }
            }

            else{
                completion(false)
            }
        }

        else{
            db.collection("Notice").document("CH").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result

                    if (document.exists()) {
                        val docMap = document.data?.toMap()

                        if (docMap != null) {
                            for ((key, value) in docMap) {
                                val noticeMap = value as Map<String, *>

                                val title = noticeMap.get("title") as? String ?: ""
                                val contents = noticeMap.get("contents") as? String ?: ""
                                val id = noticeMap.get("id") as? String ?: ""
                                val imageIndex = noticeMap.get("imageIndex") as? Long ?: 0.0
                                val date = noticeMap.get("dateTime") as? String ?: ""

                                noticeList.add(
                                    NoticeDataModel(
                                        id,
                                        title,
                                        contents,
                                        date,
                                        "CH",
                                        imageIndex.toInt()
                                    )
                                )
                            }

                            noticeList.sortByDescending { it.noticeDate }

                            completion(true)
                        } else {
                            completion(false)
                        }
                    } else {
                        completion(false)
                    }
                } else {
                    completion(false)
                }
            }

        }


    }
}