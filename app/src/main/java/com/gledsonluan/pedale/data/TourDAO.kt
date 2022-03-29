package com.gledsonluan.pedale.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gledsonluan.pedale.model.Tour

@Dao
interface TourDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTour(Tour: Tour)

    @Update
    suspend fun updateTour(Tour: Tour)

    @Delete
    suspend fun deleteTour(Tour: Tour)

    @Query ("DELETE FROM user_table")
    suspend fun deleteAllTours()

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun redAllTours(): LiveData<List<Tour>>

}