package kr.ac.jbnu.ch.affiliates.helper

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.ch.affiliates.models.AffiliateDataModel
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel

class AffiliateHelper {
    companion object{
        var adList = ArrayList<StorageReference>()
        var storeList = ArrayList<AffiliateDataModel>()
    }

    private var storage = FirebaseStorage.getInstance()
    private var helper = UserManagement()
    private val db = FirebaseFirestore.getInstance()

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

                                        storeList.add(AffiliateDataModel(storeName, benefits, breakTime, closeTime, closed, id, location, menu, openTime, price, tel, storage.reference.child("storeLogo/${id}.png"),"College"))
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

                                    storeList.add(AffiliateDataModel(storeName, benefits, breakTime, closeTime, closed, id, location, menu, openTime, price, tel, storage.reference.child("storeLogo/${id}.png"),"CH"))
                                }
                            }

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

                                    storeList.add(AffiliateDataModel(storeName, benefits, breakTime, closeTime, closed, id, location, menu, openTime, price, tel, storage.reference.child("storeLogo/${id}.png"),"College"))
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

                            storeList.add(AffiliateDataModel(storeName, benefits, breakTime, closeTime, closed, id, location, menu, openTime, price, tel, storage.reference.child("storeLogo/${id}.png"),"CH"))
                        }

                        completion(true)
                    }
                }
            }
        }
    }
}