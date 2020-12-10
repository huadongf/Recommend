package com.example.recommend

import android.app.Application
import android.content.Context


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @JvmStatic
        var context: Context? = null
            private set
    }
}