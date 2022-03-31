package kr.ac.jbnu.ch.notice.models

data class NoticeDataModel(val noticeId : String, val noticeTitle : String, val noticeContents : String, val noticeDate : String, val noticeType : String, val imageIndex : Int) {
}