package com.example.besinkitabi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.besinkitabi.model.Besin
import com.example.besinkitabi.roomdb.BesinDatabase
import kotlinx.coroutines.launch

//context e erisebilmek icin AdroidViewModel kullandık
class BesinDetayiViewModel(application: Application) :AndroidViewModel(application) {

    val besinLiveData = MutableLiveData<Besin>()

    fun  roomVerisiniAl(uuid: Int){
        viewModelScope.launch {
            val dao =BesinDatabase(getApplication()).besinDao()
            val besin =dao.getBesin(uuid)
            besinLiveData.value=besin
        }
    }

}