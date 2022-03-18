package com.example.pedal.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pedal.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query ("DELETE FROM user_table")
    suspend fun deleteAllUser()

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun redAllData(): LiveData<List<User>>

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    suspend fun getAllData(): List<User>

}