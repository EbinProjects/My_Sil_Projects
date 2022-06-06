package com.softland.mytracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LocationViewMOdel(application: Application): AndroidViewModel(application) {
    private  val locatinData=LocationLiveData(application)
    fun getlocation()=locatinData

}