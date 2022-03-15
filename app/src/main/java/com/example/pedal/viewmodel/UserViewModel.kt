package com.example.pedal.viewmodel

import android.app.Application
import android.location.Location
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pedal.data.UserDatabase
import com.example.pedal.data.network.modelresponse.Endpoint
import com.example.pedal.data.network.modelresponse.Posts
import com.example.pedal.data.network.utils.NetworkUtils
import com.example.pedal.model.Adress
import com.example.pedal.repository.UserRepository
import com.example.pedal.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(application : Application) :AndroidViewModel(application) {

    val readAllData : LiveData<List<User>>
    private val repository : UserRepository

    init{
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }
    fun deleteUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }
    fun deleteAllUser(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUser()
        }
    }



    //Below is just about creation

    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type

    private val _response_data = MutableLiveData<List<Adress>>()
    val response_data: LiveData<List<Adress>>
        get() = _response_data

    private val _adressSelected = MutableLiveData<String>()
    val adressSelected: LiveData<String>
        get() = _adressSelected

    val recycler_is_view: MutableLiveData<Boolean> = MutableLiveData()

    val start_or_end: MutableLiveData<Int> = MutableLiveData()


    fun getInfo(input: List<Adress>){
        _response_data.value = input
    }

    fun adressSelected(adress:String) {
        _adressSelected.value = adress
    }

    private val _titulo: MutableLiveData<String> = MutableLiveData()
    val titulopedal: LiveData<String>
        get() = _titulo
    private val _desc: MutableLiveData<String> = MutableLiveData()
    val descpedal: LiveData<String>
        get() = _desc
    private val _date: MutableLiveData<String> = MutableLiveData()
    val datepedal: LiveData<String>
        get() = _date
    private val _partida: MutableLiveData<String> = MutableLiveData()
    val partida: LiveData<String>
        get() = _partida
    private val _chegada: MutableLiveData<String> = MutableLiveData()
    val chegada: LiveData<String>
        get() = _chegada
    val _distancia: MutableLiveData<Float> = MutableLiveData()

    fun changetype(type: String){
        _type.value = type
    }

    fun partone(tituto: String, description:String){
        _titulo.value = tituto
        _desc.value = description
    }
    fun parttwo(partida:String){
        _date.value = "algumadata"
        _partida.value = partida
    }
    fun parttree(chegada:String){
        _chegada.value = chegada
    }
    fun createnewtour(){
        addUser(User(0,_titulo.value.toString(),_desc.value.toString(),_date.value.toString(),_partida.value.toString(),_chegada.value.toString(),_distancia.value.toString(),_type.value.toString()))
    }

    private fun distanceInMeter(startLat: Double, startLon: Double, endLat: Double, endLon: Double){
        var results = FloatArray(1)
        Location.distanceBetween(startLat,startLon,endLat,endLon,results)
        _distancia.value = results[0]
    }

    var lat_start: Double = 0.0
    var long_start: Double = 0.0
    var lat_end: Double = 0.0
    var long_end: Double = 0.0

    fun latlong(lat:Double,long: Double){
        if (start_or_end.value == 1){
            lat_start  = lat
            long_start = long
        }
        else {
            lat_end = lat
            long_end = long
            distanceInMeter(lat_start,long_start,lat_end,long_end)
        }
    }

    fun getDataFromApi(text : String) {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance("https://api.geoapify.com/v1/geocode/")

        val endpoint = retrofitClient.create(Endpoint::class.java)
        val callback = endpoint.getPosts(text,"85c3f1a79407478ba8592a38dd0d41d3")

        callback.enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                val list = mutableListOf<Adress>()
                response.body()?.features?.forEach {
                    list.add(Adress(it.properties.address_line1,it.properties.address_line2,it.properties.lat,it.properties.lon))
                }
                getInfo(list)
            }
            override fun onFailure(call: Call<Posts>, t: Throwable) {
            }
        })
    }
}