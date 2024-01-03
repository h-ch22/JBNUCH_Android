package kr.ac.jbnu.ch.affiliates.models

import android.net.Uri
import com.google.firebase.storage.StorageReference

data class AffiliateDataModel(val storeName : String?, val benefits : String?, val breakTime : String?, val closeTime : String?, val closed : String?, val id : String?, val location : String?, val menu : String?, val openTime : String?, val price : String?, val tel : String?, val storeLogo : StorageReference?, val affiliateType : String, val URL_Baemin : String?, val URL_Naver : String?, val pos : String?, var isFavorite : Boolean) {
}