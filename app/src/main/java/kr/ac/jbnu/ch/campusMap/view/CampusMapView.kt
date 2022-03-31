package kr.ac.jbnu.ch.campusMap.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.awesomedialog.*
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.view.AffiliateDetailView
import kr.ac.jbnu.ch.affiliates.view.AffiliateMapView
import kr.ac.jbnu.ch.databinding.LayoutCampusmapBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity

class CampusMapView : Fragment(), OnMapReadyCallback {
    private lateinit var mapView : com.naver.maps.map.MapView
    private lateinit var campusMapView : ConstraintLayout
    private lateinit var toggleGroup : SingleSelectToggleGroup

    private var naverMap : NaverMap? = null
    private var locationSource : FusedLocationSource? = null
    private var markerList : ArrayList<Marker> = arrayListOf()
    private val latLng_NUR = hashMapOf("NUR" to "35.84747805239486, 127.14495440189158")
    private val latLng_ENG = hashMapOf(
        "ENG_1" to "35.84657677459349, 127.13256206828062",
        "ENG_2" to "35.84657677459349, 127.13256206828062",
        "ENG_3" to "35.846994531382094, 127.13351082924164",
        "ENG_4" to "35.847485076311926, 127.13273776477824",
        "ENG_5" to "35.8475110124338, 127.1314181892545",
        "ENG_6" to "35.84714445581063, 127.13457797148128",
        "ENG_7" to "35.846019748437946, 127.13448547014687",
        "ENG_8" to "35.84829241655058, 127.1334073508402",
        "ENG_9" to "35.84764604431293, 127.1337295107396"
    )

    private val latLng_AGR = hashMapOf(
        "AGR_1" to "35.849260834241385, 127.13348623036329",
        "AGR_2" to "35.849064621027324, 127.13192057960666",
        "AGR_3" to "35.84858674487811, 127.13477467113559"
    )

    private val latLng_COE = hashMapOf(
        "COE_1" to "35.84266103510864, 127.13197441748541",
        "COE_Science" to "35.845789347438554, 127.12969146692753",
        "COE_ART" to "35.84783864733538, 127.12976793345085"
    )

    private val latLng_SOC = hashMapOf(
        "SOC" to "35.84405847484623, 127.1336284800873",
        "COH_Social" to "35.84316841249447, 127.13382347587414"
    )

    private val latLng_COM = hashMapOf(
        "COM_1" to "35.84473487695274, 127.13378425204458",
        "COM_2" to "35.84470384981582, 127.13544289716468",
        "COM_3" to "35.84455905634085, 127.13621161534046"
    )

    private val latLng_CHE = hashMapOf(
        "CHE" to "35.84250145641751, 127.13276590941227"
    )

    private val latLng_ART = hashMapOf(
        "ART_ART" to "35.846215915727214, 127.12680304079065",
        "ART_1" to "35.85062837290454, 127.12743507772056",
        "ART_2" to "35.85083114042394, 127.12793540124528"
    )

    private val latLng_COH = hashMapOf(
        "COH_1" to "35.84318627789101, 127.13310501284555",
        "COH_Social" to "35.84316841249447, 127.13382347587414",
        "COH_2" to "35.844177737418455, 127.13280572765895"
    )

    private val latLng_CON = hashMapOf(
        "CON_Main" to "35.847494175903776, 127.13037150490443",
        "CON_1" to "35.84531298732976, 127.12744876966514",
        "CON_2" to "35.845053466508126, 127.1290417509836",
        "CON_3" to "35.847088466785046, 127.13039266169687",
        "CON_4" to "35.84662956772452, 127.13084166389157",
        "CON_5" to "35.846137500109684, 127.13074164974456"
    )

    private val latLng_Restaurant = hashMapOf(
        "Restaurant_Jeongdam" to "35.84449217509675, 127.13020293337858",
        "Restaurant_Jinsoo" to "35.84518712791879, 127.13132344965273",
        "Restaurant_Hoosaeng" to "35.84770178115334, 127.13434089675506"
    )

    private val latLng_Cafe = hashMapOf(
        "Cafe_Tree" to "35.84682268877414, 127.12852189846933",
        "Cafe_StudentAssociate" to "35.84593020375495, 127.12849847226484",
        "Cafe_Library" to "35.84808205605726, 127.13238990443128",
        "Cafe_Silkroad" to "35.84461262667609, 127.13042502042425",
        "Cafe_CafeBene" to "35.844431634582925, 127.13023044860384",
        "Cafe_Jinsoo" to "35.845409534858106, 127.13114984907604"
    )

    private val latLng_ConvenienceStore = hashMapOf(
        "ConvenienceStore_ENG" to "35.846759089280475, 127.1326480051315",
        "ConvenienceStore_Jinsoo" to "35.845442511067226, 127.13095741465048",
        "ConvenienceStore_Silkroad" to "35.84467344204404, 127.13036785533161",
        "ConvenienceStore_StudentAssociate" to "35.84594256016972, 127.12850156831094",
        "ConvenienceStore_Library" to "35.848071741830296, 127.13234610447473"
    )

    private val latLng_Study = hashMapOf(
        "MainLibrary" to "35.84824578953613, 127.13201987786945",
        "SecondaryLibrary" to "35.847219347351334, 127.13572312202609",
        "BookStore" to "35.8457695765424, 127.12802747902366"
    )

    private val latLng_hospital = hashMapOf(
        "Emergency" to "35.846319386667524, 127.13963865412482",
        "Dentist" to "35.84698017531671, 127.1393399172006",
        "Main" to "35.846407109424895, 127.14132101586677",
        "Clinical" to "35.84876090252506, 127.1396043546476"
    )

    private val latLng_ATM = hashMapOf(
        "Kookmin" to "35.84438482086647, 127.12740238693017",
        "Jeonbuk" to "35.84632132069861, 127.12857944374139",
        "Post/NH" to "35.84632132069861, 127.12857944374139"
    )

    private val latLng_Others : HashMap<String, String> = hashMapOf(
        "SAMSUNGCultureDorm" to "35.8433073405292, 127.13013266851857",
        "Museum" to "35.84549977577212, 127.1261598623699",
        "StudentAssociate_1" to "35.84579058957624, 127.12824012388016",
        "StudentAssociate_2/Post" to "35.84632132069861, 127.12857944374139",
        "Club" to "35.84861696595411, 127.1285674650923",
        "Playground_L" to "35.84770043024747, 127.12799638708627",
        "Playground_S" to "35.84668934387066, 127.12781955842618",
        "Playground_secondary" to "35.84914029185945, 127.12667062916226"
        )

    companion object{
        const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutCampusmapBinding = DataBindingUtil.inflate(inflater , R.layout.layout_campusmap , container , false)

        layout.view = this
        layout.lifecycleOwner = this

        this.campusMapView = layout.campusMapView
        this.mapView = layout.mapView



        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }

        else{
            locationSource = FusedLocationSource(this, AffiliateMapView.PERMISSION_REQUEST_CODE)
        }

        mapView.getMapAsync(this)

        toggleGroup = layout.toggleGroupCollegeCategory

        toggleGroup.setOnCheckedChangeListener(object : SingleSelectToggleGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: SingleSelectToggleGroup?, checkedId: Int) {
                placeMarker(checkedId)
            }

        })

        return layout.root
    }

    fun placeMarker(selectedCategory : Int){
        markerList.forEach {
            it.map = null
        }

        markerList.clear()

        when(selectedCategory){
            R.id.btn_collegeCategory_AGR -> {
                latLng_AGR.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "AGR_1" -> {
                                    captionText = "농생대 1호관"
                                }

                                "AGR_2" -> {
                                    captionText = "농생대 2호관"
                                }

                                "AGR_3" -> {
                                    captionText = "농생대 3호관"
                                }
                            }

                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_ART -> {
                latLng_ART.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "ART_ART" -> {
                                    captionText = "예술대 미술관"
                                }

                                "ART_1" -> {
                                    captionText = "예술대 본관"
                                }

                                "ART_2" -> {
                                    captionText = "예술대 2호관"
                                }
                            }

                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_ATM -> {
                latLng_ATM.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "Kookmin" -> {
                                    captionText = "국민은행 ATM"
                                }

                                "Jeonbuk" -> {
                                    captionText = "전북은행 ATM"
                                }

                                "Post/NH" -> {
                                    captionText = "우체국 / NH농협"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_CHE -> {
                latLng_CHE.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "CHE" -> {
                                    captionText = "생활과학대학"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_COE -> {
                latLng_COE.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "COE_1" -> {
                                    captionText = "사범대 본관"
                                }

                                "COE_Science" -> {
                                    captionText = "사범대 과학관"
                                }

                                "COE_ART" -> {
                                    captionText = "사범대 예체능관"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_COH -> {
                latLng_COH.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "COH_1" -> {
                                    captionText = "인문대 1호관"
                                }

                                "COH_2" -> {
                                    captionText = "인문대 2호관"
                                }

                                "COH_Social" -> {
                                    captionText = "인문사회관"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_COM -> {
                latLng_COM.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "COM_1" -> {
                                    captionText = "상과대 1호관"
                                }

                                "COM_2" -> {
                                    captionText = "상과대 2호관"
                                }

                                "COM_3" -> {
                                    captionText = "상과대 3호관"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_CON -> {
                latLng_CON.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "CON_Main" -> {
                                    captionText = "자연대 본관"
                                }

                                "CON_1" -> {
                                    captionText = "자연대 1호관"
                                }

                                "CON_2" -> {
                                    captionText = "자연대 2호관"
                                }

                                "CON_3" -> {
                                    captionText = "자연대 3호관"
                                }

                                "CON_4" -> {
                                    captionText = "자연대 4호관"
                                }

                                "CON_5" -> {
                                    captionText = "자연대 5호관"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_Cafe -> {
                latLng_Cafe.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "Cafe_Tree" -> {
                                    captionText = "카페 느티나무"
                                }

                                "Cafe_StudentAssociate" -> {
                                    captionText = "학생회관 카페"
                                }

                                "Cafe_Library" -> {
                                    captionText = "중앙도서관 카페"
                                }

                                "Cafe_Silkroad" -> {
                                    captionText = "아로미마실"
                                }

                                "Cafe_CafeBene" -> {
                                    captionText = "카페베네 (5층)"
                                }

                                "Cafe_Jinsoo" -> {
                                    captionText = "진수당 카페"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_ConvenienceStore -> {
                latLng_ConvenienceStore.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "ConvenienceStore_ENG" -> {
                                    captionText = "공과대 CU"
                                }

                                "ConvenienceStore_Jinsoo" -> {
                                    captionText = "진수당 CU"
                                }

                                "ConvenienceStore_Silkroad" -> {
                                    captionText = "이마트24 뉴실크로드"
                                }

                                "ConvenienceStore_StudentAssociate" -> {
                                    captionText = "학생회관 CU"

                                }

                                "ConvenienceStore_Library" -> {
                                    captionText = "중앙도서관 CU"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_ENG -> {
                latLng_ENG.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "ENG_1" -> {
                                    captionText = "공과대학 1호관"
                                }

                                "ENG_2" -> {
                                    captionText = "공과대학 2호관"
                                }

                                "ENG_3" -> {
                                    captionText = "공과대학 3호관"
                                }

                                "ENG_4" -> {
                                    captionText = "공과대학 4호관"

                                }

                                "ENG_5" -> {
                                    captionText = "공과대학 5호관"
                                }

                                "ENG_6" -> {
                                    captionText = "공과대학 6호관"
                                }

                                "ENG_7" -> {
                                    captionText = "공과대학 7호관"
                                }

                                "ENG_8" -> {
                                    captionText = "공과대학 8호관"
                                }

                                "ENG_9" -> {
                                    captionText = "공과대학 9호관"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_Hospital -> {
                latLng_hospital.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "Emergency" -> {
                                    captionText = "전북대병원 권역응급의료센터"
                                }

                                "Dentist" -> {
                                    captionText = "전북대치과병원"
                                }


                                "Main" -> {
                                    captionText = "전북대병원 본관"
                                }

                                "Clinical" -> {
                                    captionText = "전북대병원 임상연구지원센터"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_NUR -> {
                latLng_NUR.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "NUR" -> {
                                    captionText = "간호대학"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_Others -> {
                latLng_Others.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "SAMSUNGCultureDorm" -> {
                                    captionText = "삼성문화회관"
                                }

                                "Museum" -> {
                                    captionText = "전북대학교 박물관"
                                }


                                "StudentAssociate_1" -> {
                                    captionText = "전북대학교 제1학생회관"
                                }

                                "StudentAssociate_2/Post" -> {
                                    captionText = "전북대학교 제2학생회관/우체국"
                                }

                                "Club" -> {
                                    captionText = "동아리전용관"
                                }

                                "Playground_L" -> {
                                    captionText = "대운동장"
                                }

                                "Playground_S" -> {
                                    captionText = "소운동장"
                                }

                                "Playground_secondary" -> {
                                    captionText = "보조구장"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_Restaurant -> {
                latLng_Restaurant.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "Restaurant_Jeongdam" -> {
                                    captionText = "정담원"
                                }

                                "Restaurant_Jinsoo" -> {
                                    captionText = "진수당"
                                }


                                "Restaurant_Hoosaeng" -> {
                                    captionText = "후생관"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_SOC -> {
                latLng_SOC.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "SOC" -> {
                                    captionText = "사회과학대학"
                                }

                                "COH_Social" -> {
                                    captionText = "인문사회관"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }

            R.id.btn_collegeCategory_Study -> {
                latLng_Study.forEach{
                    val location = it.value.split(", ")

                    if(location != null && location.size == 2){
                        Marker().apply{
                            position = LatLng(location[0].toDouble(), location[1].toDouble())
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            when(it.key){
                                "MainLibrary" -> {
                                    captionText = "중앙도서관"
                                }

                                "SecondaryLibrary" -> {
                                    captionText = "학습도서관"
                                }

                                "BookStore" -> {
                                    captionText = "교내 서점 (교보문고)"
                                }
                            }
                            captionColor = resources.getColor(R.color.accent)
                            map = naverMap

                            markerList.add(this)
                        }
                    }
                }
            }
        }
    }

    override fun onMapReady(p0: NaverMap) {
        if(locationSource != null){
            p0.locationSource = locationSource
        }

        p0.locationTrackingMode = LocationTrackingMode.Face

        val uiSettings = p0.uiSettings
        uiSettings.isCompassEnabled = true
        uiSettings.isScaleBarEnabled = true
        uiSettings.isZoomControlEnabled = true
        uiSettings.isIndoorLevelPickerEnabled = true
        uiSettings.isLocationButtonEnabled = true

        this.naverMap = p0
    }

    fun requestPermission(){
        AwesomeDialog.build(activity as MainActivity)
            .title("권한 상승이 필요합니다.", null, resources.getColor(R.color.black))
            .body("현재 위치를 표시하기 위해 위치 권한이 필요합니다.", null, resources.getColor(R.color.black))
            .icon(R.drawable.ic_warning)
            .onPositive("확인"){
                ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            }
            .onNegative("취소")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            AffiliateDetailView.PERMISSION_REQUEST_CODE -> {
                if(grantResults.isEmpty()){
                    Snackbar.make(campusMapView, "권한이 허용되지 않았습니다.", Snackbar.LENGTH_LONG).show()
                }

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationSource = FusedLocationSource(this,
                        PERMISSION_REQUEST_CODE
                    )

                    if(naverMap != null){
                        naverMap!!.locationSource = locationSource
                    }
                }

                else{
                    Snackbar.make(campusMapView, "권한이 허용되지 않았습니다.", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}