package com.example.besinkitabi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.besinkitabi.model.Besin
import com.example.besinkitabi.roomdb.BesinDatabase
import com.example.besinkitabi.service.BesinAPIServis
import com.example.besinkitabi.util.OzelSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//context e erisebilmek icin AdroidViewModel kullandık
class BesinListesiViewModel(application: Application) :AndroidViewModel(application) {

    val besinler = MutableLiveData<List<Besin>>()
    val besinHataMesaji =MutableLiveData<Boolean>()
    val besinYukleniyor = MutableLiveData<Boolean>()

    private  val besinAPIServis = BesinAPIServis()
    private val ozelSharedPreferences = OzelSharedPreferences(getApplication())

    private val guncellemeZamani = 10*60*1000*1000*1000L
    fun refreshData(){
        val kaydedilmeZamani =ozelSharedPreferences.zamaniAl()

        if(kaydedilmeZamani != null && kaydedilmeZamani !=0L && System.nanoTime()-kaydedilmeZamani <guncellemeZamani){
            verileriRoomdanAl()
        }else{
            verileriInternettenAl()
        }
    }
    private fun verileriRoomdanAl(){
        besinYukleniyor.value=true
        viewModelScope.launch (Dispatchers.IO){
            val besinListesi =BesinDatabase(getApplication()).besinDao().getAllBesin()
            withContext(Dispatchers.Main){
                besinleriGoster(besinListesi)
                Toast.makeText(getApplication(),"Besinleri roomdan Aldık",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun refreshDataFromInternet(){
        verileriInternettenAl()
    }

    private  fun verileriInternettenAl(){
        besinYukleniyor.value=true

        viewModelScope.launch (Dispatchers.IO){
            val besinListesi =besinAPIServis.getData()
            withContext(Dispatchers.Main){
                besinYukleniyor.value=false
                roomaKaydet(besinListesi)
                Toast.makeText(getApplication(),"Besinleri Internetten Aldık",Toast.LENGTH_LONG).show()
            }
        }
    }
    private  fun besinleriGoster(besinListesi: List<Besin>){
        besinler.value=besinListesi
        besinHataMesaji.value=false
        besinYukleniyor.value=false
    }

    private  fun roomaKaydet(besinListesi : List<Besin>){
        viewModelScope.launch {
            val dao = BesinDatabase(getApplication()).besinDao()
            dao.deleteAllBesin()
            val uuidListesi =dao.insertAll(*besinListesi.toTypedArray())
            var i=0
            while (i<besinListesi.size){
                besinListesi[i].uuid =uuidListesi[i].toInt()
                i=i+1
            }
            besinleriGoster(besinListesi)
        }
        ozelSharedPreferences.zamaniKaydet(System.nanoTime())
    }
}