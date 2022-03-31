package kr.ac.jbnu.ch.sports.models

import kr.ac.jbnu.ch.userManagement.models.UserInfoModel

data class SportsDataModel(val id : String, val type : String, val roomName : String, val allPeople : Long, val currentPeople : Long, val locationDescription : String, val others : String, val manager : String, val location : String, val dateTime : String, val userInfo : UserInfoModel?, val address : String, val isOnline : Boolean, val status : String)
