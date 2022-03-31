package kr.ac.jbnu.ch.userManagement.models

import com.google.firebase.storage.StorageReference
import java.net.URL

data class UserInfoModel(val name : String, val phone : String, val studentNo : String, val college : String, val uid : String, val spot : String?, val profile : StorageReference?, val collegeCode : CollegeCodeModel?, val admin : AdminCodeModel?)