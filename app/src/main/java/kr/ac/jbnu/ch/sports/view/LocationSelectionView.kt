package kr.ac.jbnu.ch.sports.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.awesomedialog.*
import com.google.android.material.snackbar.Snackbar
import com.google.common.net.HttpHeaders
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.selects.select
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.view.AffiliateDetailView
import kr.ac.jbnu.ch.affiliates.view.AffiliateMapView
import kr.ac.jbnu.ch.databinding.LayoutSportsmapBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.sports.models.AddressModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LocationSelectionView : Fragment(), OnMapReadyCallback {
    var description = ObservableField<String>("")

    private lateinit var mapView : MapView
    private lateinit var naverMap : NaverMap
    private lateinit var infoLL : CardView
    private lateinit var mainActivity : MainActivity
    private lateinit var txt_address : TextView

    private var locationSource : FusedLocationSource? = null
    private var selectedCoord = ""
    private var selectedAddress = ""

    private val API_KEY = "v60rvp0b1d"
    private val API_SECRET = "LFlzEd2DFKeCTazcXxff7paRv097u0o0ux7vHaPU"

    companion object{
        const val PERMISSION_REQUEST_CODE = 1001
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_done -> {
                val args = Bundle()
                args.putString("coord", selectedCoord)
                args.putString("address", selectedAddress)
                args.putString("description", description.get())

                activity?.supportFragmentManager?.setFragmentResult("userData", args)

                fragmentManager?.popBackStack()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutSportsmapBinding = DataBindingUtil.inflate(inflater, R.layout.layout_sportsmap, container, false)
        layout.view = this

        this.mapView = layout.mapView
        this.infoLL = layout.infoLL
        this.txt_address = layout.txtAddress

        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }

        else{
            locationSource = FusedLocationSource(this, AffiliateMapView.PERMISSION_REQUEST_CODE)
        }

        mapView.getMapAsync(this)

        return layout.root
    }

    override fun onMapReady(p0: NaverMap) {
        if(locationSource != null){
            p0.locationSource = locationSource
        }

        p0.locationTrackingMode = LocationTrackingMode.Face

        naverMap = p0
        var address = ""

        val marker = Marker().apply{
            icon = MarkerIcons.BLACK
            iconTintColor = resources.getColor(R.color.accent)

            captionText = "장소"
            captionColor = resources.getColor(R.color.accent)

            position = p0.cameraPosition.target

            val latitude = position.latitude
            val longitude = position.longitude

            val thread = Thread{
                address = reverseGeocode(latitude, longitude)

                mainActivity.runOnUiThread {
                    subCaptionText = address

                    if(subCaptionText == ""){
                        subCaptionText = "${position.latitude}, ${position.longitude}"
                    }

                    txt_address.text = "주소 : ${address}"

                    selectedAddress = address
                    selectedCoord = "${position.latitude}, ${position.longitude}"
                }
            }

            thread.start()

            map = p0

        }

        p0.addOnCameraChangeListener { reason, animated ->
            infoLL.visibility = View.GONE
            marker.position = p0.cameraPosition.target

            val latitude = p0.cameraPosition.target.latitude
            val longitude = p0.cameraPosition.target.longitude

            val thread = Thread{
                address = reverseGeocode(latitude, longitude)

                mainActivity.runOnUiThread {
                    marker.subCaptionText = address

                    if(marker.subCaptionText == ""){
                        marker.subCaptionText = "${p0.cameraPosition.target.latitude}, ${p0.cameraPosition.target.longitude}"
                    }

                    txt_address.text = "주소 : ${address}"
                    selectedAddress = address
                    selectedCoord = "${p0.cameraPosition.target.latitude}, ${p0.cameraPosition.target.longitude}"
                }
            }

            thread.start()
        }

        p0.addOnCameraIdleListener {
            infoLL.visibility = View.VISIBLE
        }
    }


    private fun reverseGeocode(lat : Double, lng : Double) : String{
        var finalAddress = ""

        try{
            var stringBuilder = StringBuilder()
            var bufferedReader : BufferedReader? = null

            val RGC_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&coords=" + "${lng},${lat}" + "&sourcecrs=epsg:4326&output=json&orders=addr,admcode,roadaddr&output=xml"

            Log.d("LocationSelectionView", RGC_URL)

            val connection : HttpsURLConnection = URL(RGC_URL).openConnection() as HttpsURLConnection

            if(connection != null){
                connection.setRequestProperty("X-NCP-APIGW-API-KEY-ID", API_KEY)
                connection.setRequestProperty("X-NCP-APIGW-API-KEY", API_SECRET)
                connection.requestMethod = "GET"
                connection.doInput = true

                val responseCode = connection.responseCode

                if(responseCode == 200){
                    bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
                }

                else{
                    bufferedReader = BufferedReader(InputStreamReader(connection.errorStream))
                }

                var line = ""

                while(true){
                    line = bufferedReader.readLine() ?: break
                    stringBuilder.append(line + "\n")

                    Log.d("LocationSelectionView", stringBuilder.toString())
                }

                val gson = Gson()
                val address = gson.fromJson(stringBuilder.toString(), kr.ac.jbnu.ch.frameworks.models.Address::class.java)

                finalAddress = address.results?.get(0)?.region?.area1?.name.toString() + " "
                finalAddress += address.results?.get(0)?.region?.area2?.name.toString() + " "
                finalAddress += address.results?.get(0)?.region?.area3?.name.toString() + "\n"
                finalAddress += address.results?.get(2)?.land?.name.toString() + " "
                finalAddress += address.results?.get(2)?.land?.number1.toString() + " "
                finalAddress += "- " + address.results?.get(2)?.land?.number2.toString() + " "
                finalAddress += address.results?.get(2)?.land?.addition0?.value.toString()
            }
        }

        catch(e : Exception){
            e.printStackTrace()
        }

        return finalAddress
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
            PERMISSION_REQUEST_CODE -> {
                if(grantResults.isEmpty()){
                    Toast.makeText(context, "권한이 허용되지 않았습니다.", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(context, "권한이 허용되지 않았습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}