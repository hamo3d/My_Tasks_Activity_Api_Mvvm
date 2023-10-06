package com.example.mytasksactivity

import android.app.Application
import android.content.Context
import java.lang.ref.WeakReference

class MyApplication : Application() {
    companion object {
        private var context: WeakReference<Context>? = null

        fun getContext(): Context {
            return context!!.get()!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = WeakReference(applicationContext)
    }
}