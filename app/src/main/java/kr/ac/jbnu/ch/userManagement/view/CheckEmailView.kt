package kr.ac.jbnu.ch.userManagement.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutCheckEmailBinding

class CheckEmailView(val email : String) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutCheckEmailBinding = DataBindingUtil.inflate(inflater, R.layout.layout_check_email, container, false)

        layout.view = this
        layout.lifecycleOwner = this

        if(email.contains("@naver.com") || email.contains("@daum.net") || email.contains("@hanmail.net") || email.contains("@nate.com") || email.contains("@gmail.com") || email.contains("@jbnu.ac.kr")){
            layout.btnOpenEmail.visibility = View.VISIBLE
        }

        else{
            layout.btnOpenEmail.visibility = View.GONE
        }

        layout.lifecycleOwner = this

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_moveToSignIn -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.viewArea, SignInView())
                transaction.commit()
            }

            R.id.btn_openEmail -> {
                if(email.contains("@naver.com")){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.naver.com"))

                    startActivity(intent)
                }

                else if(email.contains("@daum.net") || email.contains("@hanmail.net")){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.daum.net"))

                    startActivity(intent)
                }

                else if(email.contains("@nate.com")){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.nate.com"))

                    startActivity(intent)
                }

                else if(email.contains("@gmail.com") || email.contains("@jbnu.ac.kr")){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gmail.com"))

                    startActivity(intent)
                }
            }
        }
    }
}