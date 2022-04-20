package kr.ac.jbnu.ch.affiliates.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.awesomedialog.*
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.helper.AffiliateHelper
import kr.ac.jbnu.ch.affiliates.models.AffiliateDataModel
import kr.ac.jbnu.ch.databinding.LayoutStoremapBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import net.daum.mf.map.api.MapView
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledThreadPoolExecutor

class AffiliateMapView : Fragment(), OnMapReadyCallback {
    private lateinit var mapView : com.naver.maps.map.MapView
    private lateinit var affiliateMapView : FrameLayout
    private var naverMap : NaverMap? = null
    private var locationSource : FusedLocationSource? = null
    private val list = AffiliateHelper.storeList

    companion object{
        const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutStoremapBinding = DataBindingUtil.inflate(inflater , R.layout.layout_storemap , container , false)
        layout.view = this

        this.affiliateMapView = layout.affiliateMapView
        this.mapView = layout.mapView

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }

        else{
            locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
        }

        mapView.getMapAsync(this)

        return layout.root
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

        if(list.isEmpty()){
            Snackbar.make(affiliateMapView, "업체 리스트를 받아올 수 없습니다.", Snackbar.LENGTH_LONG).show()
            Log.d("AffiliateMapView", "List is Empty")

        }

        else{
            placeMarkers()
        }
    }

    fun placeMarkers(){
        list.forEach{data ->
            val location = data.location?.split(", ")

            if(location != null && location.size == 2){
                Marker().apply{
                    position = LatLng(location[0].toDouble(), location[1].toDouble())

                    when(data.affiliateType){
                        "CH" -> {
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.accent)

                            captionText = data.storeName ?: ""
                            captionColor = resources.getColor(R.color.accent)
                        }

                        else -> {
                            icon = MarkerIcons.BLACK
                            iconTintColor = resources.getColor(R.color.orange)

                            captionText = data.storeName ?: ""
                            captionColor = resources.getColor(R.color.orange)
                        }
                    }

                    subCaptionText = data.benefits ?: ""
                    subCaptionColor = resources.getColor(R.color.black)

                    this.setOnClickListener { o ->
                        changeView(data)

                        true
                    }

                    map = naverMap
                }
            }
        }
    }

    fun changeView(data : AffiliateDataModel){
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
        transaction.addToBackStack(null)

        transaction.replace(R.id.mainViewArea, AffiliateDetailView(data))
        transaction.commit()
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
                    Snackbar.make(affiliateMapView, "권한이 허용되지 않았습니다.", Snackbar.LENGTH_LONG).show()
                }

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)

                    if(naverMap != null){
                        naverMap!!.locationSource = locationSource
                    }
                }

                else{
                    Snackbar.make(affiliateMapView, "권한이 허용되지 않았습니다.", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}