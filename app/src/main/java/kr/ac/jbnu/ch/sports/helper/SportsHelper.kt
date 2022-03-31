package kr.ac.jbnu.ch.sports.helper

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.sports.models.SportsDataModel
import kr.ac.jbnu.ch.sports.models.SportsPariticpateResultModel
import kr.ac.jbnu.ch.sports.models.SportsParticipantsModel
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.UserInfoModel
import org.w3c.dom.Document
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SportsHelper {
    companion object{
        var sportsList = ArrayList<SportsDataModel>()
        var particiapntsList = ArrayList<SportsParticipantsModel>()
    }

    private val db = FirebaseFirestore.getInstance()

    fun getSportsList(type : String, completion : (Boolean) -> Unit){
        sportsList.clear()

        db.collection("Sports").get().addOnSuccessListener{result ->
            for(document in result){
                val id = document.id
                val address = document.get("address") as String? ?: ""
                val allPeople = document.get("allPeople") as Long? ?: 0
                val college = document.get("college") as String? ?: ""
                val currentPeople = document.get("currentPeople") as Long? ?: 0
                val dateTime = document.get("dateTime") as String? ?: ""
                val isOnline = document.get("isOnline") as Boolean? ?: false
                val location = document.get("location") as String? ?: ""
                val locationDescription = document.get("locationDescription") as String? ?: ""
                val manager = document.get("manager") as String? ?: ""
                val name = document.get("name") as String? ?: ""
                val others = document.get("others") as String? ?: ""
                val phone = document.get("phone") as String? ?: ""
                val roomName = document.get("roomName") as String? ?: ""
                val sportsType = document.get("sportsType") as String? ?: ""
                val studentNo = document.get("studentNo") as String? ?: ""

                val formatter = SimpleDateFormat("yyyy.MM.dd. HH:mm:ss")
                val parsedDate = formatter.format(Date())
                val currentDate = formatter.parse(parsedDate.toString())
                val date = formatter.parse(dateTime)
                val diff = date.time - currentDate.time

                val data = SportsDataModel(id, sportsType, roomName, allPeople, currentPeople, locationDescription, others, manager, location, dateTime, UserInfoModel(name, phone, studentNo, college, "", null, null, null, null), address, isOnline, "")

                if(type == "My"){
                    if(manager == UserManagement.userInfo?.uid && diff > 0){
                        sportsList.add(data)
                    }

                    else{
                        getSportsDetails(data){
                            if(it){
                                for(userData in particiapntsList){
                                    if(userData.uid == UserManagement.userInfo?.uid){
                                        sportsList.add(data)
                                    }
                                }
                            }
                        }
                    }
                }

                else{
                    sportsList.add(data)
                }
            }

            sportsList.sortBy { it.dateTime }

            completion(true)
        }


    }

    fun getSportsDetails(data : SportsDataModel, completion : (Boolean) -> Unit){
        particiapntsList.clear()

        db.collection("Sports").document(data.id).collection("Participants").get().addOnSuccessListener { result ->
            for(document in result){
                val uid = document.id
                val college = document.get("college") as? String ?: ""
                val name = document.get("name") as? String ?: ""
                val phone = document.get("phone") as? String ?: ""
                val studentNo = document.get("studentNo") as? String ?: ""

                val data = SportsParticipantsModel(college, name, phone, studentNo, uid)

                particiapntsList.add(data)
            }

            completion(true)
        }
    }

    fun uploadSportsRoom(data : SportsDataModel, completion : (Boolean) -> Unit){
        val item = hashMapOf(
            "address" to data.address,
            "allPeople" to data.allPeople,
            "college" to data.userInfo?.college,
            "currentPeople" to data.currentPeople,
            "dateTime" to data.dateTime,
            "isOnline" to data.isOnline,
            "location" to data.location,
            "locationDescription" to data.locationDescription,
            "manager" to data.userInfo?.uid,
            "name" to AES256Util.encrypt(data.userInfo?.name).replace(System.getProperty("line.separator"), ""),
            "others" to data.others,
            "phone" to AES256Util.encrypt(data.userInfo?.phone).replace(System.getProperty("line.separator"), ""),
            "roomName" to data.roomName,
            "sportsType" to data.type,
            "studentNo" to AES256Util.encrypt(data.userInfo?.studentNo).replace(System.getProperty("line.separator"), "")
        )

        db.collection("Sports").add(item).addOnFailureListener {
            it.printStackTrace()
            completion(false)

            return@addOnFailureListener
        }.addOnCompleteListener {
            completion(true)

            return@addOnCompleteListener
        }
    }

    fun cancelParticipate(data : SportsDataModel, completion : (Boolean) -> Unit){
        db.collection("Sports").document(data.id).collection("Participants").document(UserManagement.userInfo?.uid ?: "").delete().addOnCompleteListener {
            if(it.isSuccessful){
                completion(true)

                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            it.printStackTrace()

            completion(false)

            return@addOnFailureListener
        }
    }

    fun remove(data : SportsDataModel, completion : (Boolean) -> Unit){
        val collectionRef = db.collection("Sports").document(data.id).collection("Participants")

        collectionRef.get().addOnSuccessListener {
            if(!it.isEmpty){
                for(document in it.documents){
                    collectionRef.document(document.id).delete().addOnFailureListener {
                        it.printStackTrace()

                        completion(false)

                        return@addOnFailureListener
                    }
                }
            }
        }

        db.collection("Sports").document(data.id).delete().addOnCompleteListener {
            completion(true)

            return@addOnCompleteListener
        }.addOnFailureListener {
            it.printStackTrace()

            completion(false)

            return@addOnFailureListener
        }
    }

    fun participate(data : SportsDataModel, completion : (SportsPariticpateResultModel) -> Unit){
        val docRef = db.collection("Sports").document(data.id).collection("Participants").document(UserManagement.userInfo?.uid ?: "")

        docRef.get().addOnSuccessListener {
            if(it.exists()){
                completion(SportsPariticpateResultModel.ALREADY_PARTICIPATED)

                return@addOnSuccessListener
            }

            else{
                val data = hashMapOf(
                    "name" to AES256Util.encrypt(UserManagement.userInfo?.name).replace(System.getProperty("line.separator"), ""),
                    "studentNo" to AES256Util.encrypt(UserManagement.userInfo?.studentNo).replace(System.getProperty("line.separator"), ""),
                    "phone" to AES256Util.encrypt(UserManagement.userInfo?.phone).replace(System.getProperty("line.separator"), ""),
                    "college" to AES256Util.encrypt(UserManagement.userInfo?.college).replace(System.getProperty("line.separator"), "")
                )

                docRef.set(data).addOnCompleteListener {
                    if(it.isSuccessful){
                        completion(SportsPariticpateResultModel.SUCCESS)

                        return@addOnCompleteListener
                    }

                    else{
                        completion(SportsPariticpateResultModel.ERROR)

                        return@addOnCompleteListener
                    }
                }.addOnFailureListener {
                    completion(SportsPariticpateResultModel.ERROR)

                    return@addOnFailureListener
                }
            }
        }
    }
}