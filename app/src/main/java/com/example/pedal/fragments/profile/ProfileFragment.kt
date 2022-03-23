package com.example.pedal.fragments.profile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.pedal.MainActivity
import com.example.pedal.R
import com.example.pedal.UserConfig
import com.example.pedal.databinding.FragmentProfileBinding
import com.example.pedal.model.User
import com.example.pedal.viewmodel.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlin.concurrent.timer


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var muserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root

        db = (activity as MainActivity).db()
        muserViewModel = (activity as MainActivity).viewmodel()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exit.setOnClickListener {
            logoutBtn()
        }
        binding.backFab.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.profilePic.load(UserConfig.pic){
            crossfade(true)
            crossfade(500)
            scale(Scale.FILL)
            transformations(CircleCropTransformation())
        }

        binding.backup.setOnClickListener {
            if (muserViewModel.readAllData.value.isNullOrEmpty()) { Toast.makeText(requireContext(),"Crie pedais antes de fazer backup.", Toast.LENGTH_SHORT).show()}
            else {
                muserViewModel.backup()
                findNavController().navigate(R.id.action_profileFragment_to_loadingFragment)
            }
        }

        binding.download.setOnClickListener {
            muserViewModel.download()
            findNavController().navigate(R.id.action_profileFragment_to_loadingFragment)
        }
    }

    private fun transformToFirebase(data : List<User>): Map<String,Map<String,String>>{
        val map = mutableMapOf<String,Map<String,String>>()
        var count = 0

        data.forEach { User ->
            val city = hashMapOf(
                "name" to User.name,
                "description" to User.description,
                "date" to User.date,
                "start" to User.start,
                "destiny" to User.destiny,
                "distance" to User.distance,
                "type" to User.type
            )
            count ++
            map.put(count.toString(),city)

        }
        return map
    }
    private fun transformFromFirebase(download : Map<String,Map<String,String>>) :List<User>{
        val result = mutableListOf<User>()
        download.values.forEach { map ->
            result.add(User(0, map["name"].toString(),
                map["description"].toString(),
                map["date"].toString(),
                map["start"].toString(),
                map["destiny"].toString(),
                map["distance"].toString(),
                map["type"].toString()))

        }
        return result
    }

    private fun logoutBtn() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Sim"){ _,_ ->
            (activity as MainActivity).logoutGoogle()
        }
        builder.setNegativeButton("Não"){_,_ ->}
        builder.setTitle("Desconectar?")
        builder.setMessage("Tem certeza que deseja sair da sua conta?\nSeus dados não salvos serão perdidos.")
        builder.create().show()
    }
}

data class City (
    val a : List<User> = emptyList(),
    val b : String = "a",
    val c : String = "b",
    val d : String = "c"
        )
