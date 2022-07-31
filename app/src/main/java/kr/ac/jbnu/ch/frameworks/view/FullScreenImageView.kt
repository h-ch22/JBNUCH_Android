package kr.ac.jbnu.ch.frameworks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutAffiliatesListBinding
import kr.ac.jbnu.ch.databinding.LayoutFullscreenimgviewBinding
import kr.ac.jbnu.ch.frameworks.models.GlideApp
import kr.ac.jbnu.ch.userManagement.helper.UserManagement

class FullScreenImageView(val url : StorageReference) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutFullscreenimgviewBinding = DataBindingUtil.inflate(inflater , R.layout.layout_fullscreenimgview , container , false)

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        context?.let {
            GlideApp.with(it)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(layout.img)
        }

        return layout.root
    }
}