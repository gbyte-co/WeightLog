package co.gbyte.weightlog

import android.app.Application

class WeightLog : Application(){
    override fun onCreate() {
        super.onCreate()
        Settings.init(this)
    }
}
