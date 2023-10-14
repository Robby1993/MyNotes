package com.robinson.notewithreminder.screen

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.robinson.notewithreminder.R
import com.robinson.notewithreminder.databinding.ActivityWebviewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebviewBinding
    var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(R.layout.activity_webview)
        url = "https://google.com"
        binding.wv1.webViewClient = mywbview()
        binding.wv1.settings.javaScriptEnabled = true
        binding.wv1.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.wv1.loadUrl(url!!)
    }

    private inner class mywbview : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            if ("https://google.com" == Uri.parse(url).host) {
                view.loadUrl(url!!)
                // This is my website, so do not override; let my WebView load the page
            } else {
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                // val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // startActivity(intent)
            }
            return true
        }
    }
}