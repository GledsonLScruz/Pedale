package com.gledsonluan.pedale.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gledsonluan.pedale.model.Tour

@Database(entities = [Tour::class], version = 1, exportSchema = false )
abstract class TourDatabase:RoomDatabase() {

    abstract fun userDao(): TourDAO

    companion object{
        @Volatile
        private var INSTANCE: TourDatabase? = null

        fun getDatabase(context: Context): TourDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TourDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}