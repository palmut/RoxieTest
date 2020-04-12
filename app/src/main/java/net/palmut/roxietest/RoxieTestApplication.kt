package net.palmut.roxietest

import android.app.Application
import android.util.Log
import net.palmut.roxietest.net.FileCache

class RoxieTestApplication : Application() {

    lateinit var cache: FileCache

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
        cache = FileCache(this)
    }

    override fun onLowMemory() {
        Log.d(TAG, "onLowMemory")
        super.onLowMemory()
    }

    companion object {
        private const val TAG = "RTApp"
    }
}