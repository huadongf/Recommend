package com.example.recommend

import android.os.Handler
import android.os.Looper
import okhttp3.*
import java.io.IOException

object OkhttpUtil {
    //创建OkHttpClient
    @Volatile
    private var okHttpClient: OkHttpClient? = null
        get() {
            if (field == null) {
                synchronized(OkhttpUtil::class.java) {
                    if (field == null) {
                        field = OkHttpClient()
                    }
                }
            }
            return field
        }

    fun getRequest(okhttpBuilder: OkhttpBuilder) {
        //创建Request
        val request = Request.Builder()
            .url(okhttpBuilder.url.toString())
            .build()
        //创建Call
        val call = okHttpClient!!.newCall(request)
        //调用Call的enqueue方法，该方法的回调是在子线程
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                doInUiThread { okhttpBuilder.okhttpCall!!.onFailure(e.message!!) }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val data = response.body!!.string()
                    doInUiThread { okhttpBuilder.okhttpCall!!.onResponse(data) }
                } catch (e: IOException) {
                    doInUiThread { okhttpBuilder.okhttpCall!!.onFailure(e.message!!) }
                }
            }
        })
    }

    private fun doInUiThread(runnable: Runnable) {
        Handler(Looper.getMainLooper()).post(runnable)
    }
}