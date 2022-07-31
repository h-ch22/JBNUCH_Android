package kr.ac.jbnu.ch.more.helper

import com.google.firebase.firestore.FirebaseFirestore

class VersionManagement {
    companion object{
        var version = ""
        var build = ""
    }

    private val db = FirebaseFirestore.getInstance()

    fun getLatestVersion(completion : (Boolean) -> Unit){
        db.collection("Version").document("Android").get().addOnCompleteListener {
            if(it.isSuccessful){
                val document = it.result

                if(document != null && document.exists()){
                    version = document.get("Version") as String
                    build = document.get("Build") as String

                    completion(true)
                }

                else{
                    completion(false)
                }
            }
        }
    }
}