package com.example.pedal.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val start: String = "",
    val destiny: String = "",
    val distance: String = "",
    val type: String = ""
) : Parcelable