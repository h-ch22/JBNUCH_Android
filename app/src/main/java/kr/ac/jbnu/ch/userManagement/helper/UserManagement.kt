package kr.ac.jbnu.ch.userManagement.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.storage.FirebaseStorage
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.frameworks.models.SignUpCallBackModel
import kr.ac.jbnu.ch.userManagement.models.*
import java.io.IOException

class UserManagement {
    private val db = Firebase.firestore
    private val storageRef = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    companion object{
        var legacyUserInfo : UserInfoModel? = null
        var userInfo : UserInfoModel? = null

        fun getLegacyUserInfo(email : String, completion : (Boolean) -> Unit){
            Firebase.firestore.collection("User").document(email).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val document = it.result

                    if(document.exists()){
                        val dept = document.get("dept") as String? ?: ""
                        val name = document.get("name") as String? ?: ""
                        val phone = document.get("phone") as String? ?: ""
                        val studentNo = document.get("studentNo") as String? ?: ""

                        legacyUserInfo = UserInfoModel(name, phone, studentNo, dept, "", "", null, null, null)

                        completion(true)
                    }

                    else{
                        completion(false)
                    }
                }

                else{
                    completion(false)
                }
            }.addOnFailureListener {
                it.printStackTrace()
                completion(false)
            }
        }
    }

    fun removeLegarycUserData(email : String, completion : (Boolean) -> Unit){
        db.collection("User").document(email).delete().addOnSuccessListener {
            completion(true)
        }.addOnFailureListener {
            completion(false)
        }
    }

    fun signIn(email : String, password : String, completion : (UserManagementResultModel) -> Unit){
        this.auth.signInWithEmailAndPassword(email.lowercase(), password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    getUserInfo {
                        when(it){
                            UserManagementResultModel.success -> {
                                completion(it)
                            }

                            else -> completion(it)
                        }
                    }
                }

                else{
                    this.db.collection("User").document(email).get().addOnCompleteListener{
                        if(it.isSuccessful){
                            val document = it.result

                            if(document.exists()){
                                completion(UserManagementResultModel.legacyUser)
                            }

                            else{
                                completion(UserManagementResultModel.userNotFound)
                            }
                        }
                    }
                }
            }
    }

    fun signUp(email : String, password : String, name : String, studentNo : String, phone : String, college : String, uri : Uri?,  context : Context, type : SignUpChangeViewModel, completion : (SignUpCallBackModel) -> Unit){
        val collectionRef = db.collection("Users")
        val legacyCollectionRef = db.collection("User")
        val studentNo_encrypted = AES256Util.encrypt(studentNo)
        val query = collectionRef.whereEqualTo("studentNo", studentNo_encrypted.replace(System.getProperty("line.separator"), ""))
        val legacyQuery = legacyCollectionRef.whereEqualTo("studentNo", studentNo.replace(System.getProperty("line.separator"), ""))

        Log.d("UserManagement", studentNo_encrypted)

        query.get().addOnSuccessListener {
            if(it.size() == 0){
                legacyQuery.get().addOnSuccessListener {
                    if(it.size() == 0){
                        val email_converted = email.replace(System.getProperty("line.separator"), "")
                        val password_converted = password.replace(System.getProperty("line.separator"), "")
                        val name_converted = AES256Util.encrypt(name.replace(System.getProperty("line.separator"), ""))
                        val phone_converted = AES256Util.encrypt(phone.replace(System.getProperty("line.separator"), ""))
                        val college_converted = AES256Util.encrypt(college.replace(System.getProperty("line.separator"), ""))
                        val studentNo_converted = AES256Util.encrypt(studentNo.replace(System.getProperty("line.separator"), ""))

                        if(email_converted.contains(" ")){
                            email_converted.replace(" ", "")
                        }

                        if(name_converted.contains(" ")){
                            name_converted.replace(" ", "")
                        }

                        if(phone_converted.contains(" ")){
                            phone_converted.replace(" ", "")
                        }

                        if(college_converted.contains(" ")){
                            college_converted.replace(" ", "")
                        }

                        if(studentNo_converted.contains(" ")){
                            studentNo_converted.replace(" ", "")
                        }

                        if(type == SignUpChangeViewModel.SIGNUP){
                            validateIDCard(AES256Util.decrypt(college_converted), AES256Util.decrypt(name_converted), AES256Util.decrypt(studentNo_converted), uri!!, context){ it ->
                                if(!it){
                                    completion(SignUpCallBackModel.FAIL_TO_CHECK_ID_CARD)
                                }

                                else{

                                    processSignUp(email, password, name, studentNo, phone, college, type){ it1 ->
                                        completion(it1)
                                    }

                                }
                            }


                        }

                        else{
                            processSignUp(email, password, name, studentNo, phone, college, type){ it1 ->
                                completion(it1)
                            }

                        }




                    }

                    else{
                        completion(SignUpCallBackModel.LEGACY_USER)

                        return@addOnSuccessListener
                    }
                }
            }

            else{
                completion(SignUpCallBackModel.STUDENT_NO_ALREADY_EXISTS)

                return@addOnSuccessListener
            }
        }
    }

    fun processSignUp(email : String, password : String, name : String, studentNo : String, phone : String, college : String, type : SignUpChangeViewModel, completion: (SignUpCallBackModel) -> Unit){
        val collectionRef = db.collection("Users")
        val legacyCollectionRef = db.collection("User")
        val studentNo_encrypted = AES256Util.encrypt(studentNo)
        val query = collectionRef.whereEqualTo("studentNo", studentNo_encrypted.replace(System.getProperty("line.separator"), ""))
        val legacyQuery = legacyCollectionRef.whereEqualTo("studentNo", studentNo.replace(System.getProperty("line.separator"), ""))
        val email_converted = email.replace(System.getProperty("line.separator"), "")
        val password_converted = password.replace(System.getProperty("line.separator"), "")
        val name_converted = AES256Util.encrypt(name.replace(System.getProperty("line.separator"), ""))
        val phone_converted = AES256Util.encrypt(phone.replace(System.getProperty("line.separator"), ""))
        val college_converted = AES256Util.encrypt(college.replace(System.getProperty("line.separator"), ""))
        val studentNo_converted = AES256Util.encrypt(studentNo.replace(System.getProperty("line.separator"), ""))

        if(email_converted.contains(" ")){
            email_converted.replace(" ", "")
        }

        if(name_converted.contains(" ")){
            name_converted.replace(" ", "")
        }

        if(phone_converted.contains(" ")){
            phone_converted.replace(" ", "")
        }

        if(college_converted.contains(" ")){
            college_converted.replace(" ", "")
        }

        if(studentNo_converted.contains(" ")){
            studentNo_converted.replace(" ", "")
        }

        auth.createUserWithEmailAndPassword(email_converted, password_converted).addOnCompleteListener {
            if(it.isSuccessful){

                FirebaseMessaging.getInstance().token.addOnSuccessListener(object :
                    OnSuccessListener<String> {
                    var token = ""

                    override fun onSuccess(p0: String?) {
                        if (p0 != null) {
                            val item = hashMapOf(
                                "college" to college_converted.replace(System.getProperty("line.separator"), ""),
                                "name" to name_converted.replace(System.getProperty("line.separator"), ""),
                                "phone" to phone_converted.replace(System.getProperty("line.separator"), ""),
                                "studentNo" to studentNo_converted.replace(System.getProperty("line.separator"), ""),
                                "collegeCode" to convertCollegeCodeAsString(convertCollegeToCollegeCode(college.replace(System.getProperty("line.separator"), ""))),
                                "token" to token
                            )

                            auth.currentUser?.let { it1 ->
                                collectionRef.document(it1.uid).set(item).addOnSuccessListener {
                                    if(type == SignUpChangeViewModel.TRANSFERDATA){
                                        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                                            if(task.isSuccessful){
                                                getUserInfo{
                                                    when(it){
                                                        UserManagementResultModel.success -> {

                                                            completion(SignUpCallBackModel.SUCCESS)

                                                            return@getUserInfo
                                                        }

                                                        else -> {
                                                            completion(SignUpCallBackModel.FAIL)

                                                            return@getUserInfo
                                                        }
                                                    }
                                                }
                                            }
                                        }.addOnFailureListener {
                                            it.printStackTrace()
                                            completion(SignUpCallBackModel.FAIL)

                                            return@addOnFailureListener
                                        }
                                    }

                                    completion(SignUpCallBackModel.SUCCESS)

                                    return@addOnSuccessListener
                                }.addOnFailureListener {
                                    it.printStackTrace()
                                    completion(SignUpCallBackModel.FAIL)

                                    return@addOnFailureListener
                                }
                            }
                        }
                    }

                })


            }

            else{
                try{
                    throw it.exception!!
                }

                catch(e : FirebaseAuthEmailException){
                    completion(SignUpCallBackModel.EMAIL_EXISTS)

                    return@addOnCompleteListener
                }

                catch(ex : FirebaseAuthException){
                    completion(SignUpCallBackModel.FAIL)

                    return@addOnCompleteListener
                }
            }
        }
    }

    fun registerLegacyUser(email : String, password : String, college : String, name : String, studentNo : String, phone : String, completion: (Boolean) -> Unit){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){

                FirebaseMessaging.getInstance().token.addOnSuccessListener(object :
                    OnSuccessListener<String> {
                    var token = ""

                    override fun onSuccess(p0: String?) {
                        if (p0 != null) {
                            token = p0

                            val item = hashMapOf(
                                "college" to AES256Util.encrypt(college),
                                "name" to AES256Util.encrypt(name),
                                "phone" to AES256Util.encrypt(phone),
                                "studentNo" to AES256Util.encrypt(studentNo),
                                "collegeCode" to convertCollegeCodeAsString(convertCollegeToCollegeCode("공과대학")),
                                "token" to token
                            )

                            auth.currentUser?.let { it1 ->
                                db.collection("Users").document(it1.uid).set(item).addOnSuccessListener {
                                    auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                                        if(task.isSuccessful){
                                            Log.d("UserManagement", "successfully sent email : $email for password reset")

                                            db.collection("User").document(email).delete().addOnCompleteListener {
                                                if(it.isSuccessful){
                                                    auth.signOut()
                                                    completion(true)
                                                }
                                            }

                                        }

                                        else{
                                            completion(false)
                                        }

                                    }.addOnFailureListener {
                                        it.printStackTrace()
                                        completion(false)
                                    }

                                }.addOnFailureListener { completion(false) }
                            }
                        }
                    }

                })


            }

            else{
                try{
                    throw it.exception!!
                }

                catch(e : FirebaseAuthEmailException){
                    completion(false)
                }

                catch(ex : FirebaseAuthException){
                    completion(false)
                }
            }
        }
    }

    fun getUserInfo(completion : (UserManagementResultModel) -> Unit){
        if (this.auth.currentUser?.uid == null){
            completion(UserManagementResultModel.userTokenExpired)
        }

        else{
            var token = ""

            FirebaseMessaging.getInstance().token.addOnSuccessListener(object :
                OnSuccessListener<String> {
                override fun onSuccess(p0: String?) {
                    if (p0 != null) {
                        token = p0
                        db.collection("Users").document(auth.currentUser?.uid!!).update("token" , token)

                    }
                }

            })

            this.db.collection("Users").document(this.auth.currentUser?.uid!!).get().addOnSuccessListener { document ->
                if(document != null){
                    val name = AES256Util.decrypt(document.get("name") as? String ?: "")
                    val phone = AES256Util.decrypt(document.get("phone") as? String ?: "")
                    val studentNo = AES256Util.decrypt( document.get("studentNo") as? String ?: "")
                    val college = AES256Util.decrypt(document.get("college") as? String ?: "")
                    val admin = document.get("admin") as? String ?: ""
                    val belong = document.get("belong") as? String ?: ""

                    var adminAsModel : AdminCodeModel? = null
                    var spotAsString : String? = null

                    if(admin != "" && belong != ""){
                        convertAdminCode("$belong, $admin"){
                            adminAsModel = it

                            if(it != null){
                                convertAdminCodeAsString(adminAsModel){
                                    spotAsString = it
                                }
                            }
                        }
                    }

                    val profileRef = this.storageRef.reference.child("Profile/" + this.auth.currentUser?.uid + ".png")

                    userInfo = UserInfoModel(name, phone, studentNo, college, auth.currentUser?.uid ?: "", spotAsString, profileRef, convertCollegeToCollegeCode(college), adminAsModel )

                    completion(UserManagementResultModel.success)
                }

                else{
                    completion(UserManagementResultModel.userNotFound)
                }
            }
        }
    }

    private fun validateIDCard(college : String, name : String, studentNo : String, uri : Uri, context : Context, completion : (Boolean) -> Unit){
        val image : FirebaseVisionImage
        var processName = false
        var processCollege = false
        var processStudentNo = false
        var processStatus = false

        try{

            val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(listOf("ko"))
                .build()

            val metadata = FirebaseVisionImageMetadata.Builder()
                .setWidth(480)
                .setHeight(360)
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .build()

            val detector = FirebaseVision.getInstance().getCloudTextRecognizer(options)

            image = FirebaseVisionImage.fromFilePath(context, uri)

            Log.d("validate", "Validate ID Card Process Started (name : ${name}, college : ${college}, studentNo : ${studentNo})")

            detector.processImage(image)
                .addOnSuccessListener {
                    for(block in it.textBlocks){
                        var lineText = block.text.replace("\\s".toRegex(), "")
                        var lineText_fin = lineText.replace("\n", "")

                        Log.d("validate", lineText_fin)

                        if(lineText_fin == college){
                            processCollege = true

                            Log.d("validate", "college equaled : college - ${processCollege}, name - ${processName}, studentNo - ${processStudentNo}, status - ${processStatus}")
                        }

                        else if(lineText_fin == name){
                            processName = true

                            Log.d("validate", "name equaled : college - ${processCollege}, name - ${processName}, studentNo - ${processStudentNo}, status - ${processStatus}")
                        }

                        else if(lineText_fin == studentNo){
                            processStudentNo = true

                            Log.d("validate", "studentNo equaled : college - ${processCollege}, name - ${processName}, studentNo - ${processStudentNo}, status - ${processStatus}")
                        }

                        else if(lineText_fin == "재학생" || lineText_fin == "휴학생"){
                            processStatus = true

                            Log.d("validate", "status equaled : college - ${processCollege}, name - ${processName}, studentNo - ${processStudentNo}, status - ${processStatus}")
                        }
                    }

                    if(processCollege && processName && processStudentNo && processStatus){
                        completion(true)

                        return@addOnSuccessListener

                    }

                    else{
                        completion(false)

                        return@addOnSuccessListener
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    completion(false)

                    return@addOnFailureListener
                }
        } catch(e : IOException){
            e.printStackTrace()
            completion(false)
        }
    }

    fun sendResetPasswordMail(email : String, completion : (Boolean) -> Unit){
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if(it.isSuccessful){
                completion(true)

                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            completion(false)

            return@addOnFailureListener
        }
    }

    fun signOut(completion: (Boolean) -> Unit){
        try{
            auth.signOut()



            completion(true)
        } catch(e : Exception){
            e.printStackTrace()
            completion(false)
        }
    }

    fun changePhone(phone : String, completion: (Boolean) -> Unit){
        db.collection("Users").document(auth.currentUser?.uid ?: "").update("phone", AES256Util.encrypt(phone).replace(System.getProperty("line.separator"), "")).addOnCompleteListener {
            if(it.isSuccessful){
                completion(true)
            }

            else{
                completion(false)
            }
        }.addOnFailureListener {
            completion(false)
        }
    }

    fun changePassword(newPassword : String, completion: (Boolean) -> Unit){
        auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener {
            if(it.isSuccessful){
                completion(true)
            }
        }?.addOnFailureListener {
            it.printStackTrace()
            completion(false)
        }
    }

    fun secession(completion: (Boolean) -> Unit){
        val uid = auth.currentUser?.uid

        if(auth.currentUser != null){
            auth.currentUser!!.delete().addOnCompleteListener {
                if(it.isSuccessful){
                    db.collection("Users").document(uid ?: "").delete().addOnSuccessListener {
                        completion(true)
                    }.addOnFailureListener {
                        completion(false)
                    }
                }

                else{
                    completion(false)
                }
            }.addOnFailureListener {
                it.printStackTrace()
                completion(false)
            }
        }

        else{
            completion(false)
        }

    }

    fun convertCollegeToCollegeCode(college : String) : CollegeCodeModel?{
        if(college.equals("")){
            return null
        }

        var collegeCode : CollegeCodeModel? = null

        when(college){
            "간호대학" -> collegeCode = CollegeCodeModel.NUR
            "공과대학" -> collegeCode = CollegeCodeModel.ENG
            "글로벌융합대학" -> collegeCode = CollegeCodeModel.GFC
            "농업생명과학대학" -> collegeCode = CollegeCodeModel.AGR
            "사범대학" -> collegeCode = CollegeCodeModel.COE
            "사회과학대학" -> collegeCode = CollegeCodeModel.SOC
            "상과대학" -> collegeCode = CollegeCodeModel.COM
            "생활과학대학" -> collegeCode = CollegeCodeModel.CHE
            "수의과대학" -> collegeCode = CollegeCodeModel.VET
            "약학대학" -> collegeCode = CollegeCodeModel.PHA
            "예술대학" -> collegeCode = CollegeCodeModel.ART
            "의과대학" -> collegeCode = CollegeCodeModel.MED
            "인문대학" -> collegeCode = CollegeCodeModel.COH
            "자연과학대학" -> collegeCode = CollegeCodeModel.CON
            "치과대학" -> collegeCode = CollegeCodeModel.COD
            "환경생명자원대학" -> collegeCode = CollegeCodeModel.COB
            "스마트팜학과" -> collegeCode = CollegeCodeModel.COF
        }

        return collegeCode
    }

    fun convertCollegeCodeAsString(collegeCodeModel: CollegeCodeModel?) : String?{
        if(collegeCodeModel == null){
            return null
        }

        var collegeCodeAsString : String? = null

        when(collegeCodeModel){
            CollegeCodeModel.NUR -> collegeCodeAsString = "NUR"
            CollegeCodeModel.ENG -> collegeCodeAsString = "ENG"
            CollegeCodeModel.GFC -> collegeCodeAsString = "GFC"
            CollegeCodeModel.AGR -> collegeCodeAsString = "AGR"
            CollegeCodeModel.COE -> collegeCodeAsString = "COE"
            CollegeCodeModel.SOC -> collegeCodeAsString = "SOC"
            CollegeCodeModel.COM -> collegeCodeAsString = "COM"
            CollegeCodeModel.CHE -> collegeCodeAsString = "CHE"
            CollegeCodeModel.VET -> collegeCodeAsString = "VET"
            CollegeCodeModel.PHA -> collegeCodeAsString = "PHA"
            CollegeCodeModel.ART -> collegeCodeAsString = "ART"
            CollegeCodeModel.MED -> collegeCodeAsString = "MED"
            CollegeCodeModel.COH -> collegeCodeAsString = "COH"
            CollegeCodeModel.CON -> collegeCodeAsString = "CON"
            CollegeCodeModel.COD -> collegeCodeAsString = "COD"
            CollegeCodeModel.COB -> collegeCodeAsString = "COB"
            CollegeCodeModel.COF -> collegeCodeAsString = "COF"
        }

        return collegeCodeAsString
    }

    fun convertAdminCode(admin : String, completion: (AdminCodeModel?) -> Unit){
        if(!admin.contains(", ")){
            Log.d("UserManagement", "Admin Code has not required character : $admin")

            completion(null)
        }

        val admin_splited = admin.split(", ")

        if(admin_splited.isEmpty() || admin_splited.size < 2)
        {
            Log.d("UserManagement", "Splited Admin Code has not required character : $admin_splited")

            completion(null)
        }

        when(admin_splited[0]){
            "CH" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_VicePresident)
                    }
                }
            }

            "CH_Execution" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_Execution_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_Execution_VicePresident)
                    }
                }
            }

            "CH_ExternalCoop" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_ExternalCoop_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_ExternalCoop_VicePresident)
                    }

                    "Member" -> {
                        completion(AdminCodeModel.CH_ExternalCoop_Member)
                    }
                }
            }

            "CH_Culture" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_Culture_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_Culture_VicePresident)
                    }

                    "Member" -> {
                        completion(AdminCodeModel.CH_Culture_Member)
                    }
                }
            }

            "CH_Affairs" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_Affairs_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_Affairs_VicePresident)
                    }

                    "Member" -> {
                        completion(AdminCodeModel.CH_Affairs_Member)
                    }
                }
            }

            "CH_Facility" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_Facility_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_Facility_VicePresident)
                    }

                    "Member" -> {
                        completion(AdminCodeModel.CH_Facility_Member)
                    }
                }
            }

            "CH_Policy" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_Policy_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_Policy_VicePresident)
                    }

                    "Member" -> {
                        completion(AdminCodeModel.CH_Policy_Member)
                    }
                }
            }

            "CH_Employment" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_Employment_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_Employment_VicePresident)
                    }

                    "Member" -> {
                        completion(AdminCodeModel.CH_Employment_Member)
                    }
                }
            }

            "CH_Welfare" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_Welfare_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_Welfare_VicePresident)
                    }

                    "Member" -> {
                        completion(AdminCodeModel.CH_Welfare_Member)
                    }
                }
            }

            "CH_PRD" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CH_PRD_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CH_PRD_VicePresident)
                    }

                    "Member" -> {
                        completion(AdminCodeModel.CH_PRD_Member)
                    }
                }
            }

            "SOC" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.SOC_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.SOC_VicePresident)
                    }
                }
            }

            "SOC_PRD" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.SOC_PRD_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.SOC_PRD_VicePresident)
                    }
                }
            }

            "COM" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.COM_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.COM_VicePresident)
                    }
                }
            }

            "COM_PRD" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.COM_PRD_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.COM_PRD_VicePresident)
                    }
                }
            }

            "COH" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.COH_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.COH_VicePresident)
                    }
                }
            }

            "COH_PRD" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.COH_PRD_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.COH_PRD_VicePresident)
                    }
                }
            }

            "CON" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CON_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CON_VicePresident)
                    }
                }
            }

            "CON_PRD" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CON_PRD_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CON_PRD_VicePresident)
                    }
                }
            }

            "CHE" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CHE_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CHE_VicePresident)
                    }
                }
            }

            "CHE_PRD" -> {
                when(admin_splited[1]){
                    "President" -> {
                        completion(AdminCodeModel.CHE_PRD_President)
                    }

                    "VicePresident" -> {
                        completion(AdminCodeModel.CHE_PRD_VicePresident)
                    }
                }
            }
        }
    }

    fun changeProfile(uri : Uri, completion : (Boolean) -> Unit){
        val profileRef = storageRef.reference.child("Profile/${auth.currentUser?.uid}.png")
        profileRef.putFile(uri).addOnFailureListener {
            Log.d("ProfileChanger : ", it.toString())
            completion(false)
        }.addOnSuccessListener {
            completion(true)
        }
    }

    fun convertAdminCodeAsString(adminCode : AdminCodeModel?, completion : (String?) -> Unit){
        when(adminCode){
            AdminCodeModel.CH_President -> completion("총학생회장")
            AdminCodeModel.CH_VicePresident -> completion("부총학생회장")
            AdminCodeModel.CH_Execution_President -> completion("중앙집행위원장")
            AdminCodeModel.CH_Execution_VicePresident -> completion("중앙집행부위원장")
            AdminCodeModel.CH_ExternalCoop_President -> completion("총학생회 대외협력국장")
            AdminCodeModel.CH_ExternalCoop_VicePresident -> completion("총학생회 대외협력부국장")
            AdminCodeModel.CH_ExternalCoop_Member -> completion("총학생회 대외협력국원")
            AdminCodeModel.CH_Employment_President -> completion("총학생회 취업지원국장")
            AdminCodeModel.CH_Employment_VicePresident -> completion("총학생회 취업지원부국장")
            AdminCodeModel.CH_Employment_Member -> completion("총학생회 취업지원국원")
            AdminCodeModel.CH_Culture_President -> completion("총학생회 문화예술국장")
            AdminCodeModel.CH_Culture_VicePresident -> completion("총학생회 문화예술부국장")
            AdminCodeModel.CH_Culture_Member -> completion("총학생회 문화예술국원")
            AdminCodeModel.CH_Affairs_President -> completion("총학생회 사무국장")
            AdminCodeModel.CH_Affairs_VicePresident -> completion("총학생회 사무부국장")
            AdminCodeModel.CH_Affairs_Member -> completion("총학생회 사무국원")
            AdminCodeModel.CH_Facility_President -> completion("총학생회 시설운영국장")
            AdminCodeModel.CH_Facility_VicePresident -> completion("총학생회 시설운영부국장")
            AdminCodeModel.CH_Facility_Member -> completion("총학생회 시설운영국원")
            AdminCodeModel.CH_Policy_President -> completion("총학생회 정책기획국장")
            AdminCodeModel.CH_Policy_VicePresident -> completion("총학생회 정책기획부국장")
            AdminCodeModel.CH_Policy_Member -> completion("총학생회 정책기획국원")
            AdminCodeModel.CH_Welfare_President -> completion("총학생회 학생복지국장")
            AdminCodeModel.CH_Welfare_VicePresident -> completion("총학생회 학생복지부국장")
            AdminCodeModel.CH_Welfare_Member -> completion("총학생회 학생복지국원")
            AdminCodeModel.CH_PRD_President -> completion("총학생회 홍보국장")
            AdminCodeModel.CH_PRD_VicePresident -> completion("총학생회 홍보부국장")
            AdminCodeModel.CH_PRD_Member -> completion("총학생회 홍보국원")
            AdminCodeModel.SOC_President -> completion("사회과학대학 학생회장")
            AdminCodeModel.SOC_VicePresident -> completion("사회과학대학 부학생회장")
            AdminCodeModel.SOC_PRD_President -> completion("사회과학대학 홍보국장")
            AdminCodeModel.SOC_PRD_VicePresident -> completion("사회과학대학 홍보부국장")
            AdminCodeModel.COM_President -> completion("상과대학 학생회장")
            AdminCodeModel.COM_VicePresident -> completion("상과대학 부학생회장")
            AdminCodeModel.COM_PRD_President -> completion("상과대학 홍보국장")
            AdminCodeModel.COM_PRD_VicePresident -> completion("상과대학 홍보부국장")
            AdminCodeModel.COH_President -> completion("인문대학 학생회장")
            AdminCodeModel.COH_VicePresident -> completion("인문대학 부학생회장")
            AdminCodeModel.COH_PRD_President -> completion("인문대학 홍보국장")
            AdminCodeModel.COH_PRD_VicePresident -> completion("인문대학 홍보부국장")
            AdminCodeModel.CON_President -> completion("자연과학대학 학생회장")
            AdminCodeModel.CON_VicePresident -> completion("자연과학대학 부학생회장")
            AdminCodeModel.CON_PRD_President -> completion("자연과학대학 홍보국장")
            AdminCodeModel.CON_PRD_VicePresident -> completion("자연과학대학 홍보부국장")
            AdminCodeModel.CHE_President -> completion("생활과학대학 학생회장")
            AdminCodeModel.CHE_VicePresident -> completion("생활과학대학 부학생회장")
            AdminCodeModel.CHE_PRD_President -> completion("생활과학대학 홍보국장")
            AdminCodeModel.CHE_PRD_VicePresident -> completion("생활과학대학 홍보부국장")
            else -> completion(null)
        }
    }

}