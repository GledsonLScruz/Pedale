package com.example.pedal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.pedal.databinding.ActivityMainBinding
import com.example.pedal.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mUserViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1500)
        setTheme(R.style.Theme_Pedal)
        setContentView(R.layout.activity_main)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    }
    fun viewmodel() :UserViewModel{
        return mUserViewModel
    }
}