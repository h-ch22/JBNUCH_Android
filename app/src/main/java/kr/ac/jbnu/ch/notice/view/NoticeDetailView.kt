package kr.ac.jbnu.ch.notice.view

import android.database.Observable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutNoticedetailBinding
import kr.ac.jbnu.ch.notice.helper.NoticeHelper
import kr.ac.jbnu.ch.notice.models.NoticeDataModel
import kr.ac.jbnu.ch.notice.models.NoticeImageAdapter
import kr.ac.jbnu.ch.notice.models.OpenGraphParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.regex.Matcher

class NoticeDetailView(val data : NoticeDataModel) : Fragment() {
    private val helper = NoticeHelper()
    private var map : HashMap<String, String> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutNoticedetailBinding = DataBindingUtil.inflate(inflater , R.layout.layout_noticedetail , container , false)
        val title : TextView = layout.toolbar.findViewById(R.id.txt_toolbarTitle)
        layout.view = this
        layout.lifecycleOwner = this

        if(data.imageIndex > 0){
            val adapter = context?.let { NoticeImageAdapter(it, data) }

            layout.imgList.visibility = View.VISIBLE
            layout.pageIndicatorView.visibility = View.VISIBLE
            helper.getImage(data.noticeId, data.imageIndex, ""){
                if(it){
                    layout.imgList.adapter = adapter
                }
            }
        }

        else{
            layout.imgList.visibility = View.GONE
            layout.pageIndicatorView.visibility = View.GONE
        }

        layout.txtContents.text = data.noticeContents
        title.text = data.noticeTitle

        val urlList = extractLinks()

//        if(urlList.size > 0){
//            urlList.forEach{
//                CoroutineScope(Dispatchers.Default).launch{
//                    val ogTags = OpenGraphParser.parse(it)
//
//                    for(i : Int in 0..ogTags.size-1){
//                        val tag : Element = ogTags.get(i)
//                        val text = tag.attr("property")
//
//                        if("og:url".equals(text)){
//                            map.put("url", tag.attr("content"))
//                        }
//
//
//                    }
//                }
//            }
//
//        }



        return layout.root
    }

    private fun extractLinks() : Array<String>{
        val links : ArrayList<String> = ArrayList<String>()

        val matcher : Matcher = Patterns.WEB_URL.matcher(data.noticeContents)

        while(matcher.find()){
            val url = matcher.group()

            links.add(url)
        }

        Log.d("NoticeDetailView", links.toString())

        return links.toArray(arrayOfNulls<String>(links.size))
    }

    private fun parseOpenGraph(){
        for (i in extractLinks()){
            CoroutineScope(Dispatchers.IO).launch {
                val document = Jsoup.connect(i).get()
                val elements = document.select("meta[property^=og:]")

                elements?.let{
                    it.forEach{el ->
                        when(el.attr("property")){
                            "og:url" -> {

                            }

                            "og:site_name" -> {

                            }

                            "og:title" -> {

                            }

                            "og:description" -> {

                            }

                            "og:image" -> {

                            }
                        }
                    }
                }
            }
        }
    }

    fun changeToFullScreenImgView(){

    }
}