package com.gledsonluan.pedale.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gledsonluan.pedale.data.UserConfig
import com.gledsonluan.pedale.data.TourDatabase
import com.gledsonluan.pedale.data.network.modelresponse.Endpoint
import com.gledsonluan.pedale.data.network.modelresponse.Posts
import com.gledsonluan.pedale.data.network.utils.NetworkUtils
import com.gledsonluan.pedale.model.Adress
import com.gledsonluan.pedale.repository.UserRepository
import com.gledsonluan.pedale.model.Tour
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(application : Application) :AndroidViewModel(application) {

    val readAllTours: LiveData<List<Tour>>
    private val repository : UserRepository
    private val db = Firebase.firestore

    init{
        val tourDao = TourDatabase.getDatabase(application).userDao()
        repository = UserRepository(tourDao)
        readAllTours = repository.readAllTours
    }

    private fun addTour(Tour: Tour){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTour(Tour)
        }
    }

    private fun updateTour(Tour: Tour){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTour(Tour)
        }
    }

    fun deleteTour(Tour: Tour){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTour(Tour)
        }
    }

    fun deleteAllTours(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTours()
        }
    }

    //status acessed by Loading fragment to show sucess or error message
    private var _status_backup_or_download : MutableLiveData<Boolean> = MutableLiveData(false)
    val status_backup_or_download: LiveData<Boolean>
        get() = _status_backup_or_download


    fun backup(){
        val data = readAllTours.value!!


        db.collection("users").document(UserConfig.id).collection("data")
            .get()
            .addOnSuccessListener {
                val lista = mutableListOf<Tour>()
                if (it.documents.size > 0){
                    for (doc in it){
                        lista.add(doc.toObject())
                    }
                    lista.minus(data).forEach {  user ->
                        db.collection("users").document(UserConfig.id).collection("data")
                            .document(user.id.toString())
                            .delete()
                            .addOnSuccessListener {
                                _status_backup_or_download.value = true
                            }
                            .addOnFailureListener{
                                _status_backup_or_download.value = false
                            }
                    }
                }
            }
        data.forEach { Tour ->
            db.collection("users").document(UserConfig.id).collection("data").document(Tour.id.toString())
                .set(Tour)
                .addOnSuccessListener {
                    _status_backup_or_download.value = true
                }
                .addOnFailureListener{
                    _status_backup_or_download.value = false
                }
        }
    }

    fun download(){
        db.collection("users").document(UserConfig.id).collection("data")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                var count = 0
                for (document in documentSnapshot){
                    Log.d("SERVER",document.toString())
                    addTourbybackup(document.toObject())
                    count ++
                    if (count == documentSnapshot.size()){
                        _status_backup_or_download.value = true
                    }
                }
            }
            .addOnFailureListener{
                _status_backup_or_download.value = false
            }
    }


    //All variables used to create a new Tour by AddFragment
    private val _responseData = MutableLiveData<List<Adress>>()
    val responseData: LiveData<List<Adress>>
        get() = _responseData

    private val _name: MutableLiveData<String> = MutableLiveData()
    private val _description: MutableLiveData<String> = MutableLiveData()
    private val _date: MutableLiveData<String> = MutableLiveData()

    private val _start: MutableLiveData<String> = MutableLiveData()
    val start : LiveData<String>
        get() = _start
    private var latStart: Double = 0.0
    private var longStart: Double = 0.0

    private val _destiny: MutableLiveData<String> = MutableLiveData()
    val destiny : LiveData<String>
        get() = _destiny
    private var latDestiny: Double = 0.0
    private var longDestiny: Double = 0.0

    private val _type = MutableLiveData<String>()
    val type : LiveData<String>
        get() = _type
    private val _distance: MutableLiveData<String> = MutableLiveData()
    val distance : LiveData<String>
        get() = _distance


    val _recyclerStartIsView: MutableLiveData<Boolean> = MutableLiveData()
    val _recyclerDestinyIsView: MutableLiveData<Boolean> = MutableLiveData()


    //Called from Adress Adapter Listener to pass the selected start and destiny point adress
    fun startPointSelected(start_point : String, lat: Double, long: Double){
        _start.value = start_point
        latStart  = lat
        longStart = long
        distanceInMeter()
    }
    fun endPointSelected(destiny_point:String,lat: Double,long: Double) {
        _destiny.value = destiny_point
        latDestiny = lat
        longDestiny = long
        distanceInMeter()
    }

    //Calculate the distance between the two points
    private fun distanceInMeter(){
        val result = FloatArray(1)
        Location.distanceBetween(latStart,longStart,latDestiny,longDestiny,result)
        val distanceInMeter = String.format("%.2f", (result[0] / 1000))
        _distance.value = distanceInMeter
    }


    fun setNameDescDateType(tituto: String, description:String, date:String, type:String){
        _name.value = tituto
        _description.value = description
        _date.value = date
        _type.value = type
    }
    fun createnewtour(){
        addTour(
            Tour(0,_name.value.toString(),_description.value.toString(), _date.value.toString(),
            _start.value.toString(), latStart.toString(),longStart.toString(),
            _destiny.value.toString(),latDestiny.toString(),longDestiny.toString(),
            _distance.value.toString(),_type.value.toString())
        )
        clearAllData()
    }
    fun updateTour(id: Int){
        updateTour(
            Tour(id,_name.value.toString(),_description.value.toString(), _date.value.toString(),
            _start.value.toString(), latStart.toString(),longStart.toString(),
            _destiny.value.toString(),latDestiny.toString(),longDestiny.toString(),
            _distance.value.toString(),_type.value.toString())
        )
        clearAllData()
    }

    fun addTourbybackup(Tour : Tour){
        addTour(Tour)
    }

    fun changed(){
        _responseData.value = emptyList()
    }

    private fun clearAllData() {
        _name.value = ""
        _description.value = ""
        _date.value = ""

        _start.value = ""
        latStart = 0.0
        longStart = 0.0

        _destiny.value = ""
        latDestiny = 0.0
        longDestiny = 0.0

        _distance.value = ""
        _type.value = ""
        _responseData.value = emptyList()
        getDataFromApi(" ")
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