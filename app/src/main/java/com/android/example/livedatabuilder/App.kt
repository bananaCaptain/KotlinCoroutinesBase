package com.android.example.livedatabuilder

import android.app.Application
import com.blankj.utilcode.util.LogUtils

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.getConfig().setBorderSwitch(false).isLogHeadSwitch = false
    }
}