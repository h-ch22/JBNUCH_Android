package kr.ac.jbnu.ch.affiliates.helper

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.ch.affiliates.models.AffiliateDataModel
import kr.ac.jbnu.ch.pledge.helper.PledgeHelper
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel

class AffiliateHelper {
    companion object{
        var adList = ArrayList<StorageReference>()
        var storeList = ArrayList<AffiliateDataModel>()
        var storeList_filtered  = ArrayList<AffiliateDataModel>()
    }

    private var storage = FirebaseStorage.getInstance()
    private var helper = UserManagement()
    private val db = FirebaseFirestore.getInstance()

    fun changeFavoriteStatus(isFavorite : Boolean, id : String, completion: (Boolean) -> Unit){
        val docRef = db.collection("Users").document(UserManagement.userInfo?.uid ?: "").collection("Favorites")

        if(isFavorite){
           docRef.document(id).delete().addOnCompleteListener {
                if(it.isSuccessful){
                    completion(true)
                    return@addOnCompleteListener
                }

                else{
                    completion(false)
                    return@addOnCompleteListener
                }
            }
        }

        else{
            val data = HashMap<String, Any>()
            data.put("isFavorite", true)
            docRef.document(id).set(data).addOnCompleteListener {
                if(it.isSuccessful){
                    completion(true)
                    return@addOnCompleteListener
                }

                else{
                    completion(false)
                    return@addOnCompleteListener
                }
            }
        }
    }

    fun getFavorite(id : String, completion: (Boolean) -> Unit){
        val docRef = db.collection("Users").document(UserManagement.userInfo?.uid ?: "").collection("Favorites").document(id)

        docRef.get().addOnCompleteListener {
            if(it.isSuccessful){
                val document = it.result

                if(document.exists()){
                    completion(true)
                    return@addOnCompleteListener
                }

                else{
                    completion(false)
                    return@addOnCompleteListener
                }
            }

            else{
                completion(false)
                return@addOnCompleteListener
            }
        }
    }

    fun getStoreByPos(pos : String, completion : (Boolean) -> Unit){
        storeList_filtered.clear()

        if(pos != "gosa" && pos != "hyoja" && pos != "deokjin"){
            if(pos == "favorite"){
                storeList.forEach{
                    if(it.isFavorite){
                        storeList_filtered.add(it)
                    }
                }
            }

            else{
                storeList.forEach{
                    if(it.pos != "gosa" && it.pos != "hyoja" && it.pos != "deokjin"){
                        storeList_filtered.add(it)
                    }
                }
            }

            println(storeList_filtered)

            completion(true)

        }

        else{
            storeList.forEach{
                if(it.pos == pos){
                    storeList_filtered.add(it)
                    println(storeList_filtered)

                }
            }

            completion(true)

        }

    }

    fun getAdList(completion : (Boolean) -> Unit){
        adList.clear()

        if (UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COM || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CON || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CHE){
            for(i in 1..3){
                adList.add(storage.reference.child("${helper.convertCollegeCodeAsString(UserManagement.userInfo?.collegeCode)}/ad/ad_${i}.png"))
            }

            completion(true)
        }

        else{
            for(i in 1..3){
                adList.add(storage.reference.child("CH/ad/ad_${i}.png"))
            }

            completion(true)
        }
    }

    fun getStoreList(category : String, completion : (Boolean) -> Unit){
        storeList.clear()
        val affiliate_CH = db.collection("Affiliate").document("CH")
        val affiliate_College =
            helper.convertCollegeCodeAsString(UserManagement.userInfo?.collegeCode)?.let {
                db.collection("Affiliate").document(
                    it
                )
            }

        if(category == "All"){
            if (UserManagement.userInfo?.collegeCode == CollegeCodeModel.ENG || UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COM || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CON || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CHE){
                affiliate_College?.get()?.addOnCompleteListener {
                    if(it.isSuccessful){
                        val snapshot = it.result

                        if(snapshot != null){
                            val docMap = snapshot.data?.toMap()

                            if(docMap != null){
                                for((key, value) in docMap){
                                    val storeMap = value as Map<String, *>

                                    for((key, value) in storeMap){
                                        val storeMap = value as Map<String, *>
                                        val storeName = key
                                        val breakTime = storeMap.get("breakTime") as? String ?: ""
                                        val benefits = storeMap.get("benefits") as? String ?: ""
                                        val closeTime = storeMap.get("closeTime") as? String ?: ""
                                        val closed = storeMap.get("closed") as? String ?: ""
                                        val id = storeMap.get("id") as? String ?: ""
                                        val location = storeMap.get("location") as? String ?: ""
                                        val menu = storeMap.get("menu") as? String ?: ""
                                        val openTime = storeMap.get("openTime") as? String ?: ""
                                        val price = storeMap.get("price") as? String ?: ""
                                        val tel = storeMap.get("tel") as? String ?: ""
                                        val URL_Baemin = storeMap.get("URL_Baemin") as? String ?: ""
                                        val URL_Naver = storeMap.get("URL_Naver") as? String ?: ""
                                        val pos = storeMap.get("pos") as? String ?: ""
                                        var isFavorite = false

                                        getFavorite(id){
                                            if(it){
                                                isFavorite = true
                                            }

                                            else{
                                                isFavorite = false
                                            }
                                        }

                                        storeList.add(AffiliateDataModel(storeName, benefits, breakTime, closeTime, closed, id, location, menu, openTime, price, tel, storage.reference.child("storeLogo/${id}.png"),"College", URL_Baemin, URL_Naver, pos, isFavorite))


                                    }
                                }
                            }


                        }
                    }
                }
            }

            affiliate_CH.get().addOnCompleteListener {
                if(it.isSuccessful){
                    val snapshot = it.result

                    if(snapshot != null){
                        val docMap = snapshot.data?.toMap()

                        if(docMap != null){
                            for((key, value) in docMap){
                                val storeMap = value as Map<String, *>

                                for((key, value) in storeMap){
                                    val storeMap = value as Map<String, *>
                                    val storeName = key
                                    val breakTime = storeMap.get("breakTime") as? String ?: ""
                                    val benefits = storeMap.get("benefits") as? String ?: ""
                                    val closeTime = storeMap.get("closeTime") as? String ?: ""
                                    val closed = storeMap.get("closed") as? String ?: ""
                                    val id = storeMap.get("id") as? String ?: ""
                                    val location = storeMap.get("location") as? String ?: ""
                                    val menu = storeMap.get("menu") as? String ?: ""
                                    val openTime = storeMap.get("openTime") as? String ?: ""
                                    val price = storeMap.get("price") as? String ?: ""
                                    val tel = storeMap.get("tel") as? String ?: ""
                                    val URL_Baemin = storeMap.get("URL_Baemin") as? String ?: ""
                                    val URL_Naver = storeMap.get("URL_Naver") as? String ?: ""
                                    val pos = storeMap.get("pos") as? String ?: ""

                                    var isFavorite = false

                                    getFavorite(id){
                                        if(it){
                                            isFavorite = true
                                        }

                                        else{
                                            isFavorite = false
                                        }
                                    }

                                    storeList.add(AffiliateDataModel(storeName, benefits, breakTime, closeTime, closed, id, location, menu, openTime, price, tel, storage.reference.child("storeLogo/${id}.png"),"CH", URL_Baemin, URL_Naver, pos, isFavorite))



                                }
                            }
                            println(storeList)
                            completion(true)
                        }


                    }
                }
            }
        }

        else{
            if (UserManagement.userInfo?.collegeCode == CollegeCodeModel.ENG || UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COM || UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CON || UserManagement.userInfo?.collegeCode == CollegeCodeModel.CHE) {
                affiliate_College?.get()?.addOnCompleteListener {
                    if(it.isSuccessful){
                        val snapshot = it.result

                        if(snapshot != null){
                            if(snapshot.get(category) != null){
                                val docMap = snapshot.get(category) as Map<String, *>

                                for((key, value) in docMap){
                                    val storeMap = value as Map<String, *>
                                    val storeName = key
                                    val breakTime = storeMap.get("breakTime") as? String ?: ""
                                    val benefits = storeMap.get("benefits") as? String ?: ""
                                    val closeTime = storeMap.get("closeTime") as? String ?: ""
                                    val closed = storeMap.get("closed") as? String ?: ""
                                    val id = storeMap.get("id") as? String ?: ""
                                    val location = storeMap.get("location") as? String ?: ""
                                    val menu = storeMap.get("menu") as? String ?: ""
                                    val openTime = storeMap.get("openTime") as? String ?: ""
                                    val price = storeMap.get("price") as? String ?: ""
                                    val tel = storeMap.get("tel") as? String ?: ""

                                    val URL_Baemin = storeMap.get("URL_Baemin") as? String ?: ""
                                    val URL_Naver = storeMap.get("URL_Naver") as? String ?: ""
                                    val pos = storeMap.get("pos") as? String ?: ""
                                    var isFavorite = false

                                    getFavorite(id){
                                        if(it){
                                            isFavorite = true
                                        }

                                        else{
                                            isFavorite = false
                                        }
                                    }

                                    storeList.add(AffiliateDataModel(storeName, benefits, breakTime, closeTime, closed, id, location, menu, openTime, price, tel, storage.reference.child("storeLogo/${id}.png"),"CH", URL_Baemin, URL_Naver, pos, isFavorite))


                                }
                            }


                        }
                    }
                }
            }
                affiliate_CH.get().addOnCompleteListener {
                if(it.isSuccessful){
                    val snapshot = it.result

                    if(snapshot != null){
                        val docMap = snapshot.get(category) as Map<String, *>

                        for((key, value) in docMap){
                            val storeMap = value as Map<String, *>
                            val storeName = key
                            val breakTime = storeMap.get("breakTime") as? String ?: ""
                            val benefits = storeMap.get("benefits") as? String ?: ""
                            val closeTime = storeMap.get("closeTime") as? String ?: ""
                            val closed = storeMap.get("closed") as? String ?: ""
                            val id = storeMap.get("id") as? String ?: ""
                            val location = storeMap.get("location") as? String ?: ""
                            val menu = storeMap.get("menu") as? String ?: ""
                            val openTime = storeMap.get("openTime") as? String ?: ""
                            val price = storeMap.get("price") as? String ?: ""
                            val tel = storeMap.get("tel") as? String ?: ""

                            val URL_Baemin = storeMap.get("URL_Baemin") as? String ?: ""
                            val URL_Naver = storeMap.get("URL_Naver") as? String ?: ""
                            val pos = storeMap.get("pos") as? String ?: ""
                            var isFavorite = false

                            getFavorite(id){
                                if(it){
                                    isFavorite = true
                                }

                                else{
                                    isFavorite = false
                                }
                            }

                            storeList.add(AffiliateDataModel(storeName, benefits, breakTime, closeTime, closed, id, location, menu, openTime, price, tel, storage.reference.child("storeLogo/${id}.png"),"CH", URL_Baemin, URL_Naver, pos, isFavorite))


                        }

                        completion(true)
                    }
                }
            }
        }
    }
}