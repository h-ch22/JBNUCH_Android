package kr.ac.jbnu.ch.userManagement.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutTutorialBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.userManagement.models.TutorialImageAdapter

class TutorialView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutTutorialBinding = DataBindingUtil.inflate(inflater, R.layout.layout_tutorial, container, false)

        layout.view = this
        layout.lifecycleOwner = this

        val adapter = context?.let{ TutorialImageAdapter(it) }

        layout.imgList.adapter = adapter

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as StartActivity).onBackPressed()
        }

        layout.imgList.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        layout.txtDescription.text = "전북대앱의 우측 상단 메뉴를 터치하세요."
                    }

                    1 -> {
                        layout.txtDescription.text = "오아시스를 터치하세요."
                    }

                    2 -> {
                        layout.txtDescription.text = "학사정보를 터치하세요."
                    }

                    3 -> {
                        layout.txtDescription.text = "학적-기본조회를 터치하세요."
                    }

                    4 -> {
                        layout.txtDescription.text = "해당 화면을 캡처하고, 이미지를 불러오세요. (학적사항이 잘 보이도록 잘라주세요!)"
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        return layout.root
    }
}