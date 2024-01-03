package kr.ac.jbnu.ch.affiliates.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.awesomedialog.*
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.helper.AffiliateHelper
import kr.ac.jbnu.ch.affiliates.models.AffiliateDataModel
import kr.ac.jbnu.ch.databinding.LayoutAffiliateDetailBinding
import kr.ac.jbnu.ch.frameworks.models.GlideApp
import kr.ac.jbnu.ch.frameworks.view.MainActivity

class AffiliateDetailView(private val data : AffiliateDataModel, private val helper : AffiliateHelper) : Fragment() , OnMapReadyCallback{
    private lateinit var view : LinearLayout
    private lateinit var mapView : MapView
    private lateinit var layout : LayoutAffiliateDetailBinding

    companion object{
        const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.layout = DataBindingUtil.inflate(inflater , R.layout.layout_affiliate_detail , container , false)
        layout.view = this
        this.view = layout.storeDetailLL
        this.mapView = layout.mapView

        if(data.URL_Baemin == ""){
            layout.btnDeliver.visibility = View.GONE
        }

        if(data.URL_Naver == ""){
            layout.btnNaver.visibility = View.GONE
        }

        mapView.getMapAsync(this)

        layout.txtStoreName.text = data.storeName
        layout.txtBenefits.text = data.benefits

        layout.txtBenefits.movementMethod = ScrollingMovementMethod()

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms( CenterCrop(), RoundedCorners(16))

        GlideApp.with(requireContext())
            .load(data.storeLogo)
            .placeholder(R.drawable.ic_logo_no_slogan)
            .apply(requestOptions)
            .into(layout.storeLogo)

        if(data.menu == ""){
            layout.menuInfoLL.visibility = View.GONE
        }

        else{
            layout.txtMenuName.text = data.menu
            layout.txtPrice.text = data.price + "원"
        }

        return layout.root
    }

    fun call(){
        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CALL_PHONE) } != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }

        else{
            val call_Intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.tel))
            startActivity(call_Intent)
        }

    }

    fun changeFavoriteStatus(){
        helper.changeFavoriteStatus(data.isFavorite, data.id ?: ""){
            if(it){
                if(data.isFavorite){
                    layout.btnFavorite.setColorFilter(resources.getColor(R.color.gray))
                    data.isFavorite = false
                }

                else{
                    layout.btnFavorite.setColorFilter(resources.getColor(R.color.accent))
                    data.isFavorite = true
                }
            }
        }
    }

    fun openInfo(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.URL_Naver))
        startActivity(browserIntent)
    }

    fun openDelivery(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.URL_Baemin))
        startActivity(browserIntent)
    }

    fun requestPermission(){
        AwesomeDialog.build(activity as MainActivity)
            .title(resources.getString(R.string.TXT_ALERT_TITLE_REQUEST_PERMISSION), null, resources.getColor(R.color.black))
            .body(resources.getString(R.string.TXT_ALERT_CONTENTS_CALL_PERMISSION), null, resources.getColor(R.color.black))
            .icon(R.drawable.ic_warning)
            .onPositive(resources.getString(R.string.TXT_OK)){
                ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.CALL_PHONE),
                    PERMISSION_REQUEST_CODE
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
                    Snackbar.make(view, resources.getString(R.string.TXT_ALERT_PERMISSION_NOT_GRANTED), Snackbar.LENGTH_LONG).show()
                }

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call()
                }

                else{
                    Snackbar.make(view, resources.getString(R.string.TXT_ALERT_PERMISSION_NOT_GRANTED), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onMapReady(p0: NaverMap) {
        val location = data.location?.split(", ")

        if(location != null && location.size == 2){
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(location[0].toDouble(), location[1].toDouble()))
                .animate(CameraAnimation.Easing, 1000)

            p0.moveCamera(cameraUpdate)

            val marker = Marker()

            marker.position = LatLng(location[0].toDouble(), location[1].toDouble())
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = resources.getColor(R.color.accent)
            marker.captionText = data.storeName ?: ""
            marker.captionColor = resources.getColor(R.color.accent)
            marker.subCaptionText = data.benefits ?: ""
            marker.subCaptionColor = resources.getColor(R.color.black)
            marker.map = p0
        }
    }

}