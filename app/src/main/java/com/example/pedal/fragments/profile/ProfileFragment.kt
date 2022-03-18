package com.example.pedal.fragments.profile

import android.os.Bundle
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
import com.example.pedal.viewmodel.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore


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
            (activity as MainActivity).logoutGoogle()
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.profilePic.load(UserConfig.pic){
            crossfade(true)
            crossfade(900)
            scale(Scale.FILL)
            transformations(CircleCropTransformation())
        }

        binding.backup.setOnClickListener {
            val allData = muserViewModel.getAllUsers()
            Toast.makeText(requireContext(),allData.toString(),Toast.LENGTH_SHORT).show()
        }

    }
}