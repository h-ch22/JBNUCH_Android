package kr.ac.jbnu.ch.more.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.ch.R

class OpenSourceLicenseListAdapter : RecyclerView.Adapter<OpenSourceLicenseListAdapter.ViewHolder>(){
    private lateinit var context : Context
    private var list : ArrayList<OpenSourceDataModel> = arrayListOf(
        OpenSourceDataModel("Jetbrains Kotlin", "https://github.com/JetBrains/kotlin", "Apache-2.0 License", "Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors."),
        OpenSourceDataModel("Google Firebase for Android", "https://firebase.google.com/docs/android/setup?hl=ko", "Apache-2.0 License", "Copyright 2022 Google, Inc"),
        OpenSourceDataModel("NAVER Map Android SDK", "https://navermaps.github.io/ios-map-sdk/reference/Classes/NMFMapView.html#/c%3Aobjc%28cs%29NMFMapView%28im%29showOpenSourceLicense", "", "Copyright 2018-2021 NAVER Corp."),
        OpenSourceDataModel("Jsoup", "https://github.com/jhy/jsoup", "MIT License", "Copyright 2009-2022 Jonathan Hedley <https://jsoup.org/>"),
        OpenSourceDataModel("Smooth BottomBar", "https://github.com/ibrahimsn98/SmoothBottomBar", "MIT License", "Copyright 2019 İbrahim Süren"),
        OpenSourceDataModel("Page Indicator View", "https://github.com/romandanylyk/PageIndicatorView", "Apache-2.0 License", "Copyright 2017 Roman Danylyk"),
        OpenSourceDataModel("Glide", "https://github.com/bumptech/glide", "", "Copyright 2014 Google, Inc."),
        OpenSourceDataModel("android pdf viewer", "https://github.com/barteksc/AndroidPdfViewer", "Apache-2.0 License", "Copyright 2017 Bartosz Schiller"),
        OpenSourceDataModel("Gson", "https://github.com/google/gson", "Apache-2.0 License", "Copyright 2008 Google Inc."),
        OpenSourceDataModel("Awesome Dialog", "https://github.com/chnouman/AwesomeDialog", "Apache-2.0 License", "Copyright 2020 Muhammad Nouman"),
        OpenSourceDataModel("TedBottomPicker", "https://github.com/ParkSangGwon/TedBottomPicker", "Apache-2.0 License", "Copyright 2017 Ted Park"),
        OpenSourceDataModel("Android Image Cropper", "https://github.com/ArthurHub/Android-Image-Cropper", "Apache-2.0 License", "Copyright 2016, Arthur Teplitzki, 2013, Edmodo, Inc."),
        OpenSourceDataModel("Toggle Button Group", "https://github.com/nex3z/ToggleButtonGroup", "Apache-2.0 License", "Copyright 2018 nex3z"),
        OpenSourceDataModel("KotlinX Coroutines Android", "https://github.com/Kotlin/kotlinx.coroutines", "Apache-2.0 License", " Copyright 2000-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.")
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OpenSourceLicenseListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listmodel_opensource, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OpenSourceLicenseListAdapter.ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var txt_productName : TextView = view.findViewById(R.id.txt_productName)
        var txt_url : TextView = view.findViewById(R.id.txt_link)
        var txt_licenseType : TextView = view.findViewById(R.id.txt_licenseType)
        var txt_copyrightInfo : TextView = view.findViewById(R.id.txt_copyright)

        fun bind(data : OpenSourceDataModel){
            txt_productName.text = data.PRODUCT_NAME
            txt_url.text = data.URL
            txt_licenseType.text = data.LICENSE_TYPE
            txt_copyrightInfo.text = data.COPYRIGHT_INFO
        }
    }
}