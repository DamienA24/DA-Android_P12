package com.quizocr.joiefull

import android.app.Application
import com.quizocr.joiefull.di.AppModule
import com.quizocr.joiefull.di.AppModuleImpl

class JoieFullApp : Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl()
    }
}