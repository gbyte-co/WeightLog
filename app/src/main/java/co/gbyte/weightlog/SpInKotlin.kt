package co.gbyte.weightlog

import android.app.Application

class SpInKotlinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Settings.init(this)
    }
}