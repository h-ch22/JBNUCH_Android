package kr.ac.jbnu.ch.products.helper

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.products.models.ProductDataModel
import kr.ac.jbnu.ch.products.models.ProductLogDataModel
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class ProductsHelper {
    private val db = Firebase.firestore

    companion object{
        var productList = ArrayList<ProductDataModel>()
        var productLogList = ArrayList<ProductLogDataModel>()
    }

    fun getProductList(type : String, completion : (Boolean) -> Unit){
        productList.clear()

        when(type){
            "CH" -> {
                db.collection("Products").document("CH").get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val document = it.result

                        if(document != null){
                            val docMap = document.data?.toMap()

                            if(docMap != null){
                                for((key, value) in docMap){
                                    val productMap = value as Map<String, *>
                                    val productName = key
                                    val all = productMap.get("all") as? Long ?: 0
                                    val late = productMap.get("late") as? Long ?: 0

                                    productList.add(ProductDataModel(productName, all.toString(), late.toString()))
                                }

                                completion(true)
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

                    else{
                        completion(false)
                        return@addOnCompleteListener
                    }
                }
            }

            "College" -> {

            }

            else -> {
                completion(false)

                return
            }
        }
    }

    fun getLog(completion: (Boolean) -> Unit){
        productLogList.clear()

        db.collection("Products").document("CH").collection("Log").get().addOnSuccessListener { result ->
            for(document in result){
                val studentNo = document.get("studentNo") as? String ?: ""

                if(UserManagement.userInfo?.studentNo ?: "" == AES256Util.decrypt(studentNo)){
                    val dateTime = document.get("dateTime") as? String ?: ""
                    val number = document.get("number") as? Long ?: 0
                    val product = document.get("product") as? String ?: ""
                    val isReturned = document.get("isReturned") as? Boolean ?: false

                    productLogList.add(ProductLogDataModel(number.toString(), product, isReturned, dateTime))

                    Log.d("ProductsHelper", productLogList.toString())
                }
            }

            completion(true)
        }
    }
}