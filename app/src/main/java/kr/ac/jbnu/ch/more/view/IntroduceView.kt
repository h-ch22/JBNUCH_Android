package kr.ac.jbnu.ch.more.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutIntroduceBinding
import kr.ac.jbnu.ch.sports.view.SportsDetailView
import android.content.Intent
import android.net.Uri
import android.widget.ImageButton
import kr.ac.jbnu.ch.frameworks.view.MainActivity


class IntroduceView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutIntroduceBinding = DataBindingUtil.inflate(inflater, R.layout.layout_introduce, container, false)

        layout.view = this
        layout.lifecycleOwner = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_greet -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, GreetView())
                transaction.commit()
            }

            R.id.btn_department -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://jbnuch.co.kr"))
                startActivity(browserIntent)
            }

            R.id.btn_youtube -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC-k_69T0cKQriqjpB09fFkw"))
                startActivity(browserIntent)
            }

            R.id.btn_linkTree -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://linktr.ee/jbnu_ch"))
                startActivity(browserIntent)
            }

            R.id.btn_map -> {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.setCustomAnimations(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top)
                transaction.addToBackStack(null)

                transaction.replace(R.id.mainViewArea, IntroduceMapView())
                transaction.commit()
            }
        }
    }
}