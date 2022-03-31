package kr.ac.jbnu.ch.frameworks.view

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutStartBinding
import kr.ac.jbnu.ch.frameworks.models.onKeyBackPressedListener
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import kr.ac.jbnu.ch.userManagement.models.UserManagementResultModel
import kr.ac.jbnu.ch.userManagement.view.SignInView
import kr.ac.jbnu.ch.userManagement.view.LicenseView

class StartActivity : AppCompatActivity(), onKeyBackPressedListener {
    private lateinit var binding : LayoutStartBinding
    private lateinit var fragmentManager : FragmentManager
    private lateinit var transaction : FragmentTransaction
    private var mOnKeyBackPressedListener : onKeyBackPressedListener? = null
    private val signInView = SignInView()
    private val auth = FirebaseAuth.getInstance()
    private val helper = UserManagement()

    init{
        FirebaseApp.initializeApp(this)


        FirebaseMessaging.getInstance().subscribeToTopic("Notice_CH").addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Log.d("MainActivity", "Topic Subscription success.")
            }

            else{
                Log.d("MainActivity", "Failed to subscribe Topic")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_start)

        if(this.auth.currentUser == null){
            fragmentManager = supportFragmentManager
            transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
            transaction.replace(R.id.viewArea, signInView).commitAllowingStateLoss()
        }

        else{
            helper.getUserInfo{
                when(it){
                    UserManagementResultModel.success -> {
                        val intent = Intent(applicationContext, MainActivity :: class.java)
                        startActivity(intent)

                        finish()
                    }

                    else -> {
                        fragmentManager = supportFragmentManager
                        transaction = fragmentManager.beginTransaction()
                        transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
                        transaction.replace(R.id.viewArea, signInView).commitAllowingStateLoss()
                    }
                }
            }

        }
    }

    fun setOnKeyBackPreseedListener(listener : onKeyBackPressedListener?){
        mOnKeyBackPressedListener = listener
    }

    override fun onBackKeyPressed() {
        mOnKeyBackPressedListener?.onBackKeyPressed()
    }
}