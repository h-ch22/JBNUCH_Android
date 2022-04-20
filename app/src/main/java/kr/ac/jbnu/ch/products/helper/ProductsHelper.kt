package kr.ac.jbnu.ch.products.helper

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.ch.frameworks.helper.AES256Util
import kr.ac.jbnu.ch.products.models.ProductDataModel
import kr.ac.jbnu.ch.products.models.ProductLogDataModel
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel

class ProductsHelper {
    private val db = Firebase.firestore
    private val userManagement = UserManagement()

    companion object{
        var productList = ArrayList<ProductDataModel>()
        var productLogList = ArrayList<ProductLogDataModel>()
        var collegeProductList = ArrayList<ProductDataModel>()
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
                if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.CON ||
                        UserManagement.userInfo?.collegeCode == CollegeCodeModel.COM ||
                        UserManagement.userInfo?.collegeCode == CollegeCodeModel.CHE ||
                        UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH ||
                        UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC){
                    when(UserManagement.userInfo?.collegeCode){
                        CollegeCodeModel.CON,
                        CollegeCodeModel.COH,
                        CollegeCodeModel.CHE -> {
                            db.collection("Products")
                                .document(userManagement.convertCollegeCodeAsString(UserManagement.userInfo?.collegeCode)!!)
                                .get()
                                .addOnCompleteListener {
                                if(it.isSuccessful){
                                    val document = it.result

                                    if(document != null){
                                        val docMap = document.data?.toMap()

                                        if(docMap != null){
                                            for((key, value) in docMap){
                                                val productMap = value as Map<String, *>
                                                val _key = key

                                                var productName = ""

                                                when(_key){
                                                    "CurlingIron" -> {
                                                        productName = "고데기"
                                                    }

                                                    "WomenSet" -> {
                                                        productName = "여성용품"
                                                    }

                                                    "Battery",
                                                    "battery" -> {
                                                        productName = "보조 배터리"
                                                    }

                                                    "charger" -> {
                                                        productName = "충전기"
                                                    }

                                                    "cup" -> {
                                                        productName = "컵"
                                                    }

                                                    "dryer" -> {
                                                        productName = "드라이기"
                                                    }

                                                    "hairTie" -> {
                                                        productName = "머리끈"
                                                    }

                                                    "hotPack" -> {
                                                        productName = "핫팩"
                                                    }

                                                    "scissor" -> {
                                                        productName = "가위"
                                                    }

                                                    "slippers" -> {
                                                        productName = "슬리퍼"
                                                    }

                                                    "Stapler",
                                                    "stapler" -> {
                                                        productName = "스테이플러"
                                                    }

                                                    "tape" -> {
                                                        productName = "테이프"
                                                    }

                                                    "Umbrella",
                                                    "umbrella" -> {
                                                        productName = "우산"
                                                    }

                                                    "vaporizationPen" -> {
                                                        productName = "기화펜"
                                                    }

                                                    "basketBall",
                                                    "BasketBall" -> {
                                                        productName = "농구공"
                                                    }

                                                    "helmet",
                                                    "Helmet" -> {
                                                        productName = "헬멧"
                                                    }

                                                    "Lacket" -> {
                                                        productName = "배드민턴 라켓"
                                                    }

                                                    "Mat" -> {
                                                        productName = "돗자리"
                                                    }

                                                    "soccerBall",
                                                    "SoccerBall" -> {
                                                        productName = "축구공"
                                                    }

                                                    "blanket" -> {
                                                        productName = "담요"
                                                    }

                                                    "boardGame" -> {
                                                        productName = "보드게임"
                                                    }

                                                    "calculator" -> {
                                                        productName = "계산기"
                                                    }

                                                    "labCoat" -> {
                                                        productName = "실험복"
                                                    }



                                                    else -> {}
                                                }

                                                val all = productMap.get("all") as? Long ?: 0
                                                val late = productMap.get("late") as? Long ?: 0

                                                collegeProductList.add(ProductDataModel(productName, all.toString(), late.toString()))
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

                        CollegeCodeModel.COM,
                        CollegeCodeModel.SOC -> {
                            db.collection("Products")
                                .document(userManagement.convertCollegeCodeAsString(UserManagement.userInfo?.collegeCode)!!)
                                .get()
                                .addOnCompleteListener{
                                    if(it.isSuccessful){
                                        val snapshot = it.result

                                        if(snapshot != null){
                                            val docMap = snapshot.data?.toMap()

                                            if(docMap != null){
                                                for((key, value) in docMap){
                                                    val productMap = value as Map<String, *>

                                                    for((key, value) in productMap){
                                                        val _map = value as Map<String, *>
                                                        val _key = key

                                                        var productName = ""

                                                        when(_key){
                                                            "CurlingIron" -> {
                                                                productName = "고데기"
                                                            }

                                                            "WomenTools",
                                                            "WomenSet" -> {
                                                                productName = "여성용품"
                                                            }

                                                            "Battery",
                                                            "battery" -> {
                                                                productName = "보조 배터리"
                                                            }

                                                            "Charger",
                                                            "charger" -> {
                                                                productName = "충전기"
                                                            }

                                                            "cup" -> {
                                                                productName = "컵"
                                                            }

                                                            "dryer" -> {
                                                                productName = "드라이기"
                                                            }

                                                            "HairTie",
                                                            "hairTie" -> {
                                                                productName = "머리끈"
                                                            }

                                                            "hotPack" -> {
                                                                productName = "핫팩"
                                                            }

                                                            "scissors",
                                                            "scissor" -> {
                                                                productName = "가위"
                                                            }

                                                            "Slippers",
                                                            "slippers" -> {
                                                                productName = "슬리퍼"
                                                            }

                                                            "Stapler",
                                                            "stapler" -> {
                                                                productName = "스테이플러"
                                                            }

                                                            "tape" -> {
                                                                productName = "테이프"
                                                            }

                                                            "Umbrella",
                                                            "umbrella" -> {
                                                                productName = "우산"
                                                            }

                                                            "vaporizationPen" -> {
                                                                productName = "기화펜"
                                                            }

                                                            "basketBall",
                                                            "BasketBall" -> {
                                                                productName = "농구공"
                                                            }

                                                            "helmet",
                                                            "Helmet" -> {
                                                                productName = "헬멧"
                                                            }

                                                            "Lacket" -> {
                                                                productName = "배드민턴 라켓"
                                                            }

                                                            "Mat" -> {
                                                                productName = "돗자리"
                                                            }

                                                            "soccerBall",
                                                            "SoccerBall" -> {
                                                                productName = "축구공"
                                                            }

                                                            "Blanket",
                                                            "blanket" -> {
                                                                productName = "담요"
                                                            }

                                                            "boardGame" -> {
                                                                productName = "보드게임"
                                                            }

                                                            "calculator" -> {
                                                                productName = "계산기"
                                                            }

                                                            "labCoat" -> {
                                                                productName = "실험복"
                                                            }

                                                            "Tylenol" -> {
                                                                productName = "타이레놀"
                                                            }

                                                            "adhesivePlaster" -> {
                                                                productName = "종이 반창고"
                                                            }

                                                            "airPars",
                                                            "airParse" -> {
                                                                productName = "에어파스"
                                                            }

                                                            "band" -> {
                                                                productName = "밴드"
                                                            }

                                                            "bandage" -> {
                                                                productName = "붕대"
                                                            }

                                                            "cottonSwab" -> {
                                                                productName = "면봉"
                                                            }

                                                            "digestiveMedicine" -> {
                                                                productName = "소화제"
                                                            }

                                                            "disinfectant" -> {
                                                                productName = "소독약"
                                                            }

                                                            "feverRemedy" -> {
                                                                productName = "해열제"
                                                            }

                                                            "gauze_large" -> {
                                                                productName = "거즈 (대)"
                                                            }

                                                            "gauze_medium" -> {
                                                                productName = "거즈 (중)"
                                                            }

                                                            "gauze_small" -> {
                                                                productName = "거즈 (소)"
                                                            }

                                                            "painKiller" -> {
                                                                productName = "진통제"
                                                            }

                                                            "womenSet_L" -> {
                                                                productName = "여성용품 (대)"
                                                            }

                                                            "womenSet_M" -> {
                                                                productName = "여성용품 (중)"
                                                            }

                                                            "A4" -> {
                                                                productName = "A4 (박스 단위)"
                                                            }

                                                            "Glue" -> {
                                                                productName = "풀"
                                                            }

                                                            "Pen" -> {
                                                                productName = "펜"
                                                            }

                                                            "Pin" -> {
                                                                productName = "압핀"
                                                            }

                                                            "Ruler" -> {
                                                                productName = "자"
                                                            }

                                                            "Tape" -> {
                                                                productName = "테이프"
                                                            }

                                                            "Heater" -> {
                                                                productName = "온수찜질기"
                                                            }

                                                            "Mask" -> {
                                                                productName = "마스크"
                                                            }

                                                            "Straw" -> {
                                                                productName = "빨대"
                                                            }

                                                            "Tumbler" -> {
                                                                productName = "텀블러"
                                                            }

                                                            "Ointment" -> {
                                                                productName = "연고"
                                                            }

                                                            "alcoholCotton" -> {
                                                                productName = "알콜솜"
                                                            }

                                                            "antidiarrheal" -> {
                                                                productName = "지사제"
                                                            }

                                                            "artificialTears" -> {
                                                                productName = "인공눈물"
                                                            }

                                                            "coldMedicine" -> {
                                                                productName = "감기약"
                                                            }

                                                            "cotton" -> {
                                                                productName = "솜"
                                                            }

                                                            "gargle" -> {
                                                                productName = "가글"
                                                            }

                                                            "gauze" -> {
                                                                productName = "거즈"
                                                            }

                                                            "hydrogenPeroxide" -> {
                                                                productName = "과산화수소"
                                                            }

                                                            "mercuroChrome" -> {
                                                                productName = "빨간약"
                                                            }

                                                            "pars" -> {
                                                                productName = "파스"
                                                            }

                                                            "pincette" -> {
                                                                productName = "핀셋"
                                                            }

                                                            "stomachMedicine" -> {
                                                                productName = "복통약"
                                                            }

                                                            "tapeForBandage" -> {
                                                                productName = "붕대용 테이프"
                                                            }

                                                            "Sharp" -> {
                                                                productName = "샤프"
                                                            }

                                                            "SharpCore" -> {
                                                                productName = "샤프심"
                                                            }

                                                            "blackPen" -> {
                                                                productName = "검정펜"
                                                            }

                                                            "boardMarker" -> {
                                                                productName = "보드마커"
                                                            }

                                                            "cableTie" -> {
                                                                productName = "케이블타이"
                                                            }

                                                            "correctionTape" -> {
                                                                productName = "수정테이프"
                                                            }

                                                            "erasor" -> {
                                                                productName = "지우개"
                                                            }

                                                            "knife" -> {
                                                                productName = "칼"
                                                            }

                                                            "punch" -> {
                                                                productName = "펀치"
                                                            }

                                                            "redPen" -> {
                                                                productName = "빨간펜"
                                                            }

                                                            "ruler" -> {
                                                                productName = "자"
                                                            }

                                                            "scotchTape" -> {
                                                                productName = "스카치테이프"
                                                            }

                                                            "renu" -> {
                                                                productName = "렌즈 세척액"
                                                            }

                                                            "tapeMeasure" -> {
                                                                productName = "줄자"
                                                            }

                                                            "textileDeodorant" -> {
                                                                productName = "섬유 탈취제"
                                                            }

                                                            "toolSet" -> {
                                                                productName = "공구 상자"
                                                            }

                                                            "windInjector" -> {
                                                                productName = "바람 주입기"

                                                            }

                                                            else -> {}
                                                        }

                                                        val all = _map.get("all") as? Long ?: 0
                                                        val late = _map.get("late") as? Long ?: 0

                                                        collegeProductList.add(ProductDataModel(productName, all.toString(), late.toString()))

                                                        Log.d("ProductsHelper", collegeProductList.toString())
                                                    }
                                                }
                                            }

                                            completion(true)
                                        }
                                    }
                                }
                        }

                        else -> {
                            completion(false)
                            return
                        }
                    }
                }

                else{
                    completion(false)
                    return
                }
            }

            else -> {
                completion(false)

                return
            }
        }
    }

    fun getLog(college : String, completion: (Boolean) -> Unit){
        productLogList.clear()

        when(college){
            "CH" -> {
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

            "College" -> {
                val userManagement = UserManagement()

                userManagement.convertCollegeCodeAsString(UserManagement.userInfo?.collegeCode)?.let {
                    db.collection("Products").document(it).collection("Log").get().addOnSuccessListener { result ->
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
        }


    }
}