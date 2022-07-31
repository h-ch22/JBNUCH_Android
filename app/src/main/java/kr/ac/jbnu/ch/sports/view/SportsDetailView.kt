package kr.ac.jbnu.ch.sports.view

import android.Manifest
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomedialog.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.firestore.auth.User
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutSportsdetailBinding
import kr.ac.jbnu.ch.frameworks.models.ListViewDecoration
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.sports.helper.SportsHelper
import kr.ac.jbnu.ch.sports.models.ParticipantsListAdapter
import kr.ac.jbnu.ch.sports.models.SportsDataModel
import kr.ac.jbnu.ch.sports.models.SportsPariticpateResultModel
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.view.SignUpView
import java.text.SimpleDateFormat
import java.util.*

class SportsDetailView(private val data : SportsDataModel) : Fragment(), OnMapReadyCallback {
    private lateinit var mapView : MapView
    private lateinit var txt_roomName : TextView
    private lateinit var txt_people : TextView
    private lateinit var txt_locationDescrption : TextView
    private lateinit var txt_sportsType : TextView
    private lateinit var txt_remainTime : TextView
    private lateinit var participantsLL : RecyclerView
    private lateinit var progressView : CircularProgressIndicator

    private var IS_ALREADY_PARTICIPATED = false

    private val helper = SportsHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutSportsdetailBinding = DataBindingUtil.inflate(inflater, R.layout.layout_sportsdetail, container, false)
        layout.view = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        this.mapView = layout.mapView
        this.progressView = layout.progressView
        participantsLL = layout.participantsLL

        layout.btnParticipate.visibility = View.VISIBLE

        val btn_cancel : ImageButton = layout.toolbar.findViewById(R.id.btn_cancel)

        if(!data.isOnline){
            mapView.getMapAsync(this)
        }

        else{
            mapView.visibility = View.GONE
        }

        if(data.manager != UserManagement.userInfo?.uid){
            for(participant in SportsHelper.particiapntsList){
                if(participant.uid == UserManagement.userInfo?.uid){
                    IS_ALREADY_PARTICIPATED = true

                    btn_cancel.visibility = View.VISIBLE
                    layout.btnParticipate.visibility = View.GONE
                }
            }

            if(!IS_ALREADY_PARTICIPATED){
                btn_cancel.visibility = View.GONE
            }
        }

        helper.getSportsDetails(data){
            if(it){
                val decorationHeight = ListViewDecoration(20)
                val listAdapter = ParticipantsListAdapter(data)

                participantsLL.apply{
                    layoutManager = LinearLayoutManager(activity)
                    adapter = listAdapter
                    addItemDecoration(decorationHeight)
                }

                if(data.manager == UserManagement.userInfo?.uid){
                    layout.btnParticipate.visibility = View.GONE
                    btn_cancel.visibility = View.VISIBLE
                    layout.participantsLL.visibility = View.VISIBLE
                }

                else{
                    SportsHelper.particiapntsList.forEach {
                        if(it.uid == UserManagement.userInfo?.uid){
                            layout.btnParticipate.visibility = View.GONE
                            btn_cancel.visibility = View.VISIBLE
                            layout.participantsLL.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        btn_cancel.setOnClickListener {
            if(data.manager == UserManagement.userInfo?.uid){
                AwesomeDialog.build(activity as MainActivity)
                    .title("용병 모집 취소", null, resources.getColor(R.color.black))
                    .body("용병 모집을 취소하시겠습니까?", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onPositive("확인"){
                        progressView.visibility = View.VISIBLE

                        helper.remove(data){
                            if(it){
                                progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("취소 완료", null, resources.getColor(R.color.black))
                                    .body("용병 모집을 취소했습니다.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_select)
                                    .onPositive("확인")
                            }

                            else{
                                progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("오류", null, resources.getColor(R.color.black))
                                    .body("요청하신 작업을 처리하는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인")
                            }
                        }
                    }
                    .onNegative("취소")
            }

            else{
                AwesomeDialog.build(activity as MainActivity)
                    .title("용병 지원 취소", null, resources.getColor(R.color.black))
                    .body("용병 지원을 취소하시겠습니까?", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_warning)
                    .onPositive("확인"){
                        progressView.visibility = View.VISIBLE

                        helper.cancelParticipate(data){
                            if(it){
                                progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("취소 완료", null, resources.getColor(R.color.black))
                                    .body("용병 지원을 취소했습니다.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_select)
                                    .onPositive("확인")
                            }

                            else{
                                progressView.visibility = View.GONE

                                AwesomeDialog.build(activity as MainActivity)
                                    .title("오류", null, resources.getColor(R.color.black))
                                    .body("요청하신 작업을 처리하는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                    .icon(R.drawable.ic_warning)
                                    .onPositive("확인")
                            }
                        }
                    }
                    .onNegative("취소")
            }
        }

        txt_roomName = layout.listModel.findViewById(R.id.txt_sportsRoomName)
        txt_people = layout.listModel.findViewById(R.id.txt_sports_peopleCount)
        txt_locationDescrption = layout.listModel.findViewById(R.id.txt_position)
        txt_sportsType = layout.listModel.findViewById(R.id.txt_sportsType)
        txt_remainTime = layout.listModel.findViewById(R.id.txt_timeRemaining)

        putData()

        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun putData(){
        txt_roomName.text = data.roomName
        txt_people.text = "${data.currentPeople} / ${data.allPeople}"

        if(data.isOnline){
            val locationDescription = "<b>온라인 진행</b>"

            txt_locationDescrption.text = Html.fromHtml(locationDescription)
        }

        else{
            val locationDescription = "<b>${data.locationDescription}</b> <br>${data.address}"

            txt_locationDescrption.text = Html.fromHtml(locationDescription)
        }

        txt_sportsType.text = data.type

        val formatter = SimpleDateFormat("yyyy.MM.dd. HH:mm:ss")
        val parsedDate = formatter.format(Date())
        val currentDate = formatter.parse(parsedDate.toString())
        val date = formatter.parse(data.dateTime)
        val diff = date.time - currentDate.time

        val timer = object : CountDownTimer(diff, 1000){
            override fun onTick(p0: Long) {
                val hour = (p0 / 3600000).toInt()
                val minute = (p0 % 3600000 / 60000).toInt()
                val sec = (p0 % 3600000 % 60000 / 1000).toInt()

                txt_remainTime.text = "${hour}:${minute}:${sec}"
            }

            override fun onFinish() {

            }
        }.start()
    }

    override fun onMapReady(p0: NaverMap) {
        val location = data.location.split(", ")

        if(location.size == 2){
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(location[0].toDouble(), location[1].toDouble()))
                .animate(CameraAnimation.Easing, 1000)

            p0.moveCamera(cameraUpdate)

            Marker().apply{
                position = LatLng(location[0].toDouble(), location[1].toDouble())
                icon = MarkerIcons.BLACK
                iconTintColor = resources.getColor(R.color.accent)

                captionText = data.roomName
                captionColor = resources.getColor(R.color.accent)

                map = p0
            }
        }
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_participate -> {
                AwesomeDialog.build(activity as MainActivity)
                    .title("용병 지원 확인", null, resources.getColor(R.color.black))
                    .body("지원하시겠습니까?", null, resources.getColor(R.color.black))
                    .icon(R.drawable.ic_select)
                    .onPositive("확인"){
                        progressView.visibility = View.VISIBLE

                        helper.participate(data){
                            when(it){
                                SportsPariticpateResultModel.SUCCESS -> {
                                    progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                        .title("지원 완료", null, resources.getColor(R.color.black))
                                        .body("정상 처리되었습니다.", null, resources.getColor(R.color.black))
                                        .icon(R.drawable.ic_select)
                                        .onPositive("확인")
                                }

                                SportsPariticpateResultModel.ERROR -> {
                                    progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                        .title("오류", null, resources.getColor(R.color.black))
                                        .body("요청하신 작업을 처리하는 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.", null, resources.getColor(R.color.black))
                                        .icon(R.drawable.ic_warning)
                                        .onPositive("확인")
                                }

                                SportsPariticpateResultModel.ALREADY_PARTICIPATED -> {
                                    progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                        .title("오류", null, resources.getColor(R.color.black))
                                        .body("이미 지원한 방입니다.", null, resources.getColor(R.color.black))
                                        .icon(R.drawable.ic_warning)
                                        .onPositive("확인")
                                }

                                SportsPariticpateResultModel.USERLIMIT -> {
                                    progressView.visibility = View.GONE

                                    AwesomeDialog.build(activity as MainActivity)
                                        .title("오류", null, resources.getColor(R.color.black))
                                        .body("모집 인원을 초과하였습니다.", null, resources.getColor(R.color.black))
                                        .icon(R.drawable.ic_warning)
                                        .onPositive("확인")
                                }
                            }
                        }
                    }
                    .onNegative("취소")
            }
        }
    }
}