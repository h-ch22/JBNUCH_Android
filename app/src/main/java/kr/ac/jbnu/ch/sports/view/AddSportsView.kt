package kr.ac.jbnu.ch.sports.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.FragmentTransaction
import com.example.awesomedialog.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutAddsportsBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.sports.helper.SportsHelper
import kr.ac.jbnu.ch.sports.models.SportsDataModel
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import java.text.SimpleDateFormat
import java.util.*

class AddSportsView : Fragment(){
    var onlineChecked = false
    var roomName = ObservableField<String>("")
    var type = ObservableField<String>("")
    var allPeople = ObservableField<String>("")
    var currentPeople = ObservableField<String>("")
    var location = ObservableField<String>("")
    var others = ObservableField<String>("")

    private var date = ""
    private val calendar = Calendar.getInstance()
    private val helper = SportsHelper()

    private var selectedAddress = ""
    private var selectedLatLng = ""
    private var locationDescription = ""

    private lateinit var btn_selectDate : MaterialButton
    private lateinit var btn_selectLocation : MaterialButton
    private lateinit var btn_confirm : MaterialButton
    private lateinit var progressView : LinearProgressIndicator

    fun setBtnVisibility() : Int{
        return if (onlineChecked) GONE else VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentFragmentManager.setFragmentResultListener("userData", this, object : FragmentResultListener{
            override fun onFragmentResult(requestKey: String, result: Bundle) {
                Log.d("AddSportsView" , "data : ${result}")

                selectedAddress = result.get("address") as String
                locationDescription = result.get("description") as String
                selectedLatLng = result.get("coord") as String

                if(btn_selectLocation != null){
                    btn_selectLocation.text = locationDescription + "\n" + selectedAddress
                    btn_selectLocation.setBackgroundColor(resources.getColor(R.color.accent))
                    btn_selectLocation.setTextColor(resources.getColor(R.color.white))
                    btn_selectLocation.setIconTintResource(R.color.white)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutAddsportsBinding = DataBindingUtil.inflate(inflater , R.layout.layout_addsports , container , false)

        layout.view = this
        this.progressView = layout.progressView


        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)
        title.text = resources.getString(R.string.TXT_UPLOAD)

        layout.isOnline.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1){
                    layout.btnSelectLocation.visibility = GONE
                    btn_selectLocation.visibility = View.GONE
                    btn_selectLocation.isEnabled = false
                    onlineChecked = true
                }

                else{
                    layout.btnSelectLocation.visibility = VISIBLE
                    btn_selectLocation.visibility = View.VISIBLE
                    btn_selectLocation.isEnabled = true
                    onlineChecked = false
                }
            }

        })

        btn_selectDate = layout.btnSelectDate
        btn_selectLocation = layout.btnSelectLocation
        btn_confirm = layout.btnConfirm

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_selectDate -> {
                val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    btn_selectDate.text = "${year}. ${month + 1}. ${dayOfMonth}"
                    date = "${year}. ${month + 1}. ${dayOfMonth}. "

                    TimePickerDialog(context, R.style.DialogTheme, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        btn_selectDate.text = "${date} ${hour} : ${minute}"
                        date += "${hour}:${minute}:00"
                    }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false).show()
                }

                context?.let {
                    DatePickerDialog(it, R.style.DialogTheme, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                }
            }

            R.id.btn_selectLocation -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, LocationSelectionView())
                transaction.commit()
            }

            R.id.btn_confirm -> {
                if(onlineChecked){
                    locationDescription = resources.getString(R.string.TXT_ISONLINE)
                }

                if(!onlineChecked && (roomName.get() == "" || type.get() == "" || allPeople.get() == "" || currentPeople.get() == "" || selectedAddress == "" || selectedLatLng == "" || date == "")){
                    AwesomeDialog.build(activity as MainActivity)
                        .title(resources.getString(R.string.TXT_ALERT_TITLE_EMPTY_FIELD), null, resources.getColor(R.color.black))
                        .body(resources.getString(R.string.TXT_ALERT_CONTENTS_EMPTY_FIELD), null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive(resources.getString(R.string.TXT_OK)){

                        }
                }

                else if(onlineChecked && (roomName.get() == "" || type.get() == "" || allPeople.get() == "" || currentPeople.get() == "" || date == "")){
                    AwesomeDialog.build(activity as MainActivity)
                        .title(resources.getString(R.string.TXT_ALERT_TITLE_EMPTY_FIELD), null, resources.getColor(R.color.black))
                        .body(resources.getString(R.string.TXT_ALERT_CONTENTS_EMPTY_FIELD), null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive(resources.getString(R.string.TXT_OK)){

                        }
                }

                else if(allPeople.get()!!.toInt() <= currentPeople.get()!!.toInt()){
                    AwesomeDialog.build(activity as MainActivity)
                        .title(resources.getString(R.string.TXT_LIMIT_PEOPLE), null, resources.getColor(R.color.black))
                        .body(resources.getString(R.string.TXT_ALERT_CONTENTS_LIMIT_PEOPLE), null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive(resources.getString(R.string.TXT_OK)){

                        }
                }

                else{
                    btn_confirm.visibility = View.GONE
                    progressView.visibility = View.VISIBLE

                    val formatter = SimpleDateFormat("yyyy.MM.dd. kk:mm:ss")
                    val selectedDate = formatter.parse(date)

                    helper.uploadSportsRoom(
                        SportsDataModel("", type.get()!!, roomName.get()!!, allPeople.get()!!.toLong(), currentPeople.get()!!.toLong(), locationDescription, others.get()!!, UserManagement.userInfo?.uid ?: "", selectedLatLng, formatter.format(selectedDate), UserManagement.userInfo, selectedAddress, onlineChecked, "")
                    ){
                        if(it){
                            AwesomeDialog.build(activity as MainActivity)
                                .title(resources.getString(R.string.TXT_ALERT_TITLE_UPLOAD_SUCCESS), null, resources.getColor(R.color.black))
                                .body(resources.getString(R.string.TXT_ALERT_CONTENTS_UPLOAD_SUCCESS), null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_select)
                                .onPositive(resources.getString(R.string.TXT_OK)){
                                    fragmentManager?.popBackStack()

                                }
                        }

                        else{
                            AwesomeDialog.build(activity as MainActivity)
                                .title(resources.getString(R.string.TXT_ERROR), null, resources.getColor(R.color.black))
                                .body(resources.getString(R.string.TXT_ALERT_CONTENTS_ERROR), null, resources.getColor(R.color.black))
                                .icon(R.drawable.ic_warning)
                                .onPositive(resources.getString(R.string.TXT_OK)){
                                    btn_confirm.visibility = View.VISIBLE
                                    progressView.visibility = View.GONE
                                }
                        }
                    }
                }
            }
        }
    }
}