package kr.ac.jbnu.ch.frameworks.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.messaging.FirebaseMessaging
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.affiliates.view.AffiliateView
import kr.ac.jbnu.ch.databinding.LayoutMainBinding
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.home.view.HomeView
import kr.ac.jbnu.ch.more.view.MoreView
import kr.ac.jbnu.ch.notice.view.NoticeView
import kr.ac.jbnu.ch.timeTable.view.TimeTableView
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.CollegeCodeModel
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity(), onKeyBackPressedListener{
    private var mOnKeyBackPressedListener : onKeyBackPressedListener? = null
    private lateinit var binding : LayoutMainBinding
    private lateinit var navigationBar : SmoothBottomBar
    private lateinit var viewArea : FrameLayout

    init{
        if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.SOC){
            FirebaseMessaging.getInstance().subscribeToTopic("Notice_SOC").addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("MainActivity", "Topic Subscription success.")
                }

                else{
                    Log.d("MainActivity", "Failed to subscribe Topic")
                }
            }
        }

        else if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.CON){
            FirebaseMessaging.getInstance().subscribeToTopic("Notice_CON").addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("MainActivity", "Topic Subscription success.")
                }

                else{
                    Log.d("MainActivity", "Failed to subscribe Topic")
                }
            }
        }

        else if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.COM){
            FirebaseMessaging.getInstance().subscribeToTopic("Notice_COM").addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("MainActivity", "Topic Subscription success.")
                }

                else{
                    Log.d("MainActivity", "Failed to subscribe Topic")
                }
            }
        }

        else if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.COH){
            FirebaseMessaging.getInstance().subscribeToTopic("Notice_COH").addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("MainActivity", "Topic Subscription success.")
                }

                else{
                    Log.d("MainActivity", "Failed to subscribe Topic")
                }
            }
        }

        else if(UserManagement.userInfo?.collegeCode == CollegeCodeModel.CHE){
            FirebaseMessaging.getInstance().subscribeToTopic("Notice_CHE").addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("MainActivity", "Topic Subscription success.")
                }

                else{
                    Log.d("MainActivity", "Failed to subscribe Topic")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.layout_main)
        navigationBar = binding.navigationBar
        viewArea = binding.mainViewArea

        setCurrentFragment(HomeView())

        navigationBar.setOnItemSelectedListener {
            when(it){
                0 -> {
                    setCurrentFragment(HomeView())
                }

                1 -> {
                    setCurrentFragment(AffiliateView())
                }

//                2 -> {
//                    setCurrentFragment(TimeTableView())
//                }

                2 -> {
                    setCurrentFragment(NoticeView())
                }

                3 -> {
                    setCurrentFragment(MoreView())
                }

                else -> {
                    setCurrentFragment(HomeView())
                }
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainViewArea,fragment)
            commit()
        }
    }

    override fun onBackKeyPressed() {
        mOnKeyBackPressedListener?.onBackKeyPressed()
    }

    override fun onRestart() {
        super.onRestart()

        if(UserManagement.userInfo?.uid == null){
            val intent = Intent(this, StartActivity :: class.java)
            startActivity(intent)

            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId){
            R.id.btn_toolbarBack -> {
                onBackPressed()
            }
        }

        return true
    }
}