package com.example.pedal.repository

import androidx.lifecycle.LiveData
import com.example.pedal.data.UserDao
import com.example.pedal.model.User

class UserRepository(private val userDAO: UserDao) {

    val readAllData: LiveData<List<User>> = userDAO.redAllData()

    suspend fun addUser(user : User){
        userDAO.addUser(user)
    }
    suspend fun updateUser(user: User){
        userDAO.updateUser(user)
    }
    suspend fun deleteUser(user: User){
        userDAO.deleteUser(user)
    }
    suspend fun deleteAllUser(){
        userDAO.deleteAllUser()
    }
    suspend fun getAllUsers(){
        userDAO.getAllData()
    }
}