package com.example.pedal.viewmodel

import android.app.Application
import android.location.Location
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

    private fun addUser(user: User){
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
    fun getAllUsers(){
    }


    //Below is just about creation
    private val _responseData = MutableLiveData<List<Adress>>()
    val responseData: LiveData<List<Adress>>
        get() = _responseData


    private val _titulo: MutableLiveData<String> = MutableLiveData()
    private val _desc: MutableLiveData<String> = MutableLiveData()
    private val _date: MutableLiveData<String> = MutableLiveData()

    private val _partida: MutableLiveData<String> = MutableLiveData()
    val partida : LiveData<String>
        get() = _partida
    private var latStart: Double = 0.0
    private var longStart: Double = 0.0

    private val _chegada: MutableLiveData<String> = MutableLiveData()
    val chegada : LiveData<String>
        get() = _chegada
    private var latEnd: Double = 0.0
    private var longEnd: Double = 0.0

    private val _type = MutableLiveData<String>()
    val type : LiveData<String>
        get() = _type
    private val _distancia: MutableLiveData<String> = MutableLiveData()
    val distancia : LiveData<String>
        get() = _distancia


    val recyclerPartidaIsView: MutableLiveData<Boolean> = MutableLiveData()
    val recyclerChegadaIsView: MutableLiveData<Boolean> = MutableLiveData()


    //called from Recycler Listener to pass the selected adress
    fun startPointSelected(start_point : String, lat: Double, long: Double){
        _partida.value = start_point
        latStart  = lat
        longStart = long
        distanceInMeter()
    }
    fun endPointSelected(adress:String,lat: Double,long: Double) {
        _chegada.value = adress
        latEnd = lat
        longEnd = long
        distanceInMeter()
    }


    fun setNameDescDateType(tituto: String, description:String, date:String, type:String){
        _titulo.value = tituto
        _desc.value = description
        _date.value = date
        _type.value = type
    }
    fun createnewtour(){
        addUser(User(0,_titulo.value.toString(),_desc.value.toString(),_date.value.toString(),_partida.value.toString(),_chegada.value.toString(),_distancia.value.toString(),_type.value.toString()))
        clearAllData()
    }

    fun createnewtourbybackup(user : User){
        addUser(user)
    }

    private fun clearAllData() {
        _titulo.value = ""
        _desc.value = ""
        _date.value = ""
        _partida.value = ""
        _chegada.value = ""
        _distancia.value = ""
        _type.value = ""
        _responseData.value = emptyList()
        latStart = 0.0
        longStart = 0.0
        latEnd = 0.0
        longEnd = 0.0
        getDataFromApi(" ")
    }
    fun changed(){
        _responseData.value = emptyList()
    }

    private fun distanceInMeter(){
        val results = FloatArray(1)
        Location.distanceBetween(latStart,longStart,latEnd,longEnd,results)
        val distFinal = String.format("%.2f", (results[0] / 1000))
        _distancia.value = distFinal
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
                _responseData.value = (list)
            }
            override fun onFailure(call: Call<Posts>, t: Throwable) {
            }
        })
    }
}