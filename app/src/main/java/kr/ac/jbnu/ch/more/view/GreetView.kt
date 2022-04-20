package kr.ac.jbnu.ch.more.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutGreetBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import java.io.IOException
import java.io.InputStream

class GreetView : Fragment() {
    var greetText = MutableLiveData<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutGreetBinding = DataBindingUtil.inflate(inflater, R.layout.layout_greet, container, false)

        layout.view = this
        layout.lifecycleOwner = this

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        layout.txtGreet.movementMethod = ScrollingMovementMethod()

        try {
            val inputStream = context?.assets?.open("greet/greet.txt")
            val size: Int = inputStream?.available() ?: 0
            val buffer = ByteArray(size)

            inputStream?.read(buffer)?.toString()

            greetText.value = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return layout.root
    }
}