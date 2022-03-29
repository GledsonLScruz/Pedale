package com.gledsonluan.pedale.repository

import androidx.lifecycle.LiveData
import com.gledsonluan.pedale.data.TourDAO
import com.gledsonluan.pedale.model.Tour

class UserRepository(private val tourDAO: TourDAO) {

    val readAllTours: LiveData<List<Tour>> = tourDAO.redAllTours()

    suspend fun addTour(Tour : Tour){
        tourDAO.addTour(Tour)
    }
    suspend fun updateTour(Tour: Tour){
        tourDAO.updateTour(Tour)
    }
    suspend fun deleteTour(Tour: Tour){
        tourDAO.deleteTour(Tour)
    }
    suspend fun deleteAllTours(){
        tourDAO.deleteAllTours()
    }
}