package kr.ac.jbnu.ch.notice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutTranslateSelectLanguageBinding
import kr.ac.jbnu.ch.frameworks.helper.TranslationManager
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.notice.models.NoticeDataModel
import kr.ac.jbnu.ch.notice.models.NoticeListAdapter
import kr.ac.jbnu.ch.notice.models.TranslateLanguageSelectionViewAdapter
import kr.ac.jbnu.ch.notice.models.TranslationLanguageDataModel

class TranslateLanguageSelectionView(val noticeData : NoticeDataModel) : Fragment() {
    private val listAdapter = TranslateLanguageSelectionViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutTranslateSelectLanguageBinding = DataBindingUtil.inflate(inflater , R.layout.layout_translate_select_language , container , false)
        layout.view = this
        layout.lifecycleOwner = this

        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)
        title.text = resources.getString(R.string.TXT_TITLE_SELECT_LANGUAGE_TO_TRANSLATE)

        layout.languageList.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }

/*        listAdapter.setOnItemClickListener(object : TranslateLanguageSelectionViewAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: TranslationLanguageDataModel, pos: Int) {
                val helper = TranslationManager()

                val targetLanguage = helper.convertLanguageCode(data.languageCode)

                helper.translate(noticeData.noticeContents, targetLanguage){
                    if(it != ""){
                        TranslationManager.translatedText = it
                        (activity as MainActivity).onBackPressed()
                    }
                }
            }
        })*/

        val backBtn = layout.toolbar.findViewById<ImageButton>(R.id.btn_toolbarBack)
        backBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return layout.root
    }
}