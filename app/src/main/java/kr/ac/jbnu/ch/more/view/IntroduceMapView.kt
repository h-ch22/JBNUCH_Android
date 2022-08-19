package kr.ac.jbnu.ch.more.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
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
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.view.AffiliateDetailView
import kr.ac.jbnu.ch.affiliates.view.AffiliateMapView
import kr.ac.jbnu.ch.databinding.LayoutIntroduceMapBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity

class IntroduceMapView : Fragment(), OnMapReadyCallback {
    private lateinit var mapView : com.naver.maps.map.MapView
    private lateinit var introduceMapView : FrameLayout
    private var naverMap : NaverMap? = null
    private var locationSource : FusedLocationSource? = null

    companion object{
        const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutIntroduceMapBinding = DataBindingUtil.inflate(inflater, R.layout.layout_introduce_map, container, false)

        layout.view = this
        layout.lifecycleOwner = this
        this.mapView = layout.mapView
        this.introduceMapView = layout.introduceMapView

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

        Marker().apply{
            position = LatLng(35.84632132069861, 127.12857944374139)
            icon = OverlayImage.fromResource(R.drawable.ic_logo_no_slogan)

            width = 150
            height = 150

            captionText = resources.getString(R.string.TXT_SOURCE_CH_OFFICE)
            captionColor = resources.getColor(R.color.accent)

            map = naverMap
        }
    }

    fun requestPermission(){
        AwesomeDialog.build(activity as MainActivity)
            .title(resources.getString(R.string.TXT_ALERT_TITLE_REQUEST_PERMISSION), null, resources.getColor(R.color.black))
            .body(resources.getString(R.string.TXT_ALERT_CONTENTS_LOCATION_PERMISSION), null, resources.getColor(R.color.black))
            .icon(R.drawable.ic_warning)
            .onPositive(resources.getString(R.string.TXT_OK)){
                ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    AffiliateMapView.PERMISSION_REQUEST_CODE
                )
            }
            .onNegative(resources.getString(R.string.TXT_CANCEL))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_REQUEST_CODE -> {
                if(grantResults.isEmpty()){
                    Snackbar.make(introduceMapView, resources.getString(R.string.TXT_ALERT_PERMISSION_NOT_GRANTED), Snackbar.LENGTH_LONG).show()
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
                    Snackbar.make(introduceMapView, resources.getString(R.string.TXT_ALERT_PERMISSION_NOT_GRANTED), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}