package com.example.besinkitabi.service

import com.example.besinkitabi.model.Besin
import retrofit2.http.GET

interface BesinAPI {
    //GET, POST

    //https://raw.githubusercontent.com/atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json

    // BASE_URL -> https://raw.githubusercontent.com/
    // atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json

    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")
    //suspend istenildiğinde durdurulabilen ve devam ettirilebilen fonksiyondur
    suspend fun getBesin() : List<Besin>
}