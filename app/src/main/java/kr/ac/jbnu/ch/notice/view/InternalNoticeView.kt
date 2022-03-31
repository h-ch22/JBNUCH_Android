package kr.ac.jbnu.ch.notice.view

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutInternalnoticeBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity

class InternalNoticeView : Fragment() {
    private lateinit var webView : WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutInternalnoticeBinding = DataBindingUtil.inflate(inflater, R.layout.layout_internalnotice, container, false)

        val webView = layout.internalNoticeWebView
        webView.webViewClient = WebViewClient()

        val webSettings : WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportMultipleWindows(false)
        webSettings.javaScriptCanOpenWindowsAutomatically = false
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = false
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.domStorageEnabled = true

        webView.loadUrl("https://www.jbnu.ac.kr/kor/?menuID=139")

        this.webView = webView
        layout.view = this
        layout.lifecycleOwner = this

        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.clearCache(true)
                view?.loadUrl(request?.url.toString())

                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                layout.progressView.visibility = View.GONE

                Toast.makeText(context, "페이지를 불러오는 중 오류가 발생했습니다 : ${error?.description.toString()}", Toast.LENGTH_LONG).show()

                Log.d("InternalNoticeView", error?.description.toString())
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.d("InternalNoticeView", "Page Stated : ${url}")

                layout.progressView.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("InternalNoticeView", "Page Finished : ${url}")

                layout.progressView.visibility = View.GONE
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)

                handler?.proceed()
            }
        }

        webView.setOnKeyListener(object : View.OnKeyListener{
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if(p2?.action != KeyEvent.ACTION_DOWN){
                    return true
                }

                if(p1 == KeyEvent.KEYCODE_BACK){
                    if(webView.canGoBack()){
                        webView.goBack()
                    }

                    else{
                        (activity as MainActivity).onBackPressed()
                    }

                    return true
                }

                return false
            }

        })

        return layout.root
    }

    fun onClick(v : View){
        when(v.id){
            R.id.btn_backward -> {
                if(webView.canGoBack()){
                    webView.goBack()
                }
            }

            R.id.btn_forward -> {
                if(webView.canGoForward()){
                    webView.goForward()
                }
            }

            R.id.btn_refresh -> {
                webView.clearCache(true)
                webView.reload()
            }
         }
    }
}