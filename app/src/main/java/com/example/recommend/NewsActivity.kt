package com.example.recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news.*


class NewsActivity : AppCompatActivity() {
    companion object {
        const val KEY_URL = "key_url"
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        //跳转传过来的数据
        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val content = intent.getStringExtra("url")
        //图片展示
        Glide.with(this).load(image).into(fruitImageView)
        //标题展示
        collapsingToolbar.title = title
        //设置toolbar
        setSupportActionBar(toolbar)
        //顶部返回按钮
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        //加载url
        wv_news_web_view.webChromeClient = WebChromeClient()
        wv_news_web_view.webViewClient = WebViewClient()
        wv_news_web_view.settings.javaScriptEnabled = true
        wv_news_web_view.settings.defaultTextEncodingName = "utf-8"
        wv_news_web_view.loadUrl(content!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}