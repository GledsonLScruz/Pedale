package com.example.pedal.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.pedal.R
import com.example.pedal.model.User
import com.example.pedal.viewmodel.UserViewModel
import com.example.pedal.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mUserViewModel : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.add.setOnClickListener{
            insertData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun insertData() {
        val fisrtname = binding.firstname.text.toString()
        val lastname = binding.lastname.text.toString()
        val age = binding.age.text

        if(inputCheck(fisrtname, lastname,age)){
            //create user object
            val user = User(0,fisrtname,lastname,Integer.parseInt(age.toString()))
            //add Data to database
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "Sucessfully added",Toast.LENGTH_SHORT).show()
            //navigateback
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please Fill out all Fields",Toast.LENGTH_SHORT).show()
        }

    }

    private fun inputCheck(firstName : String, lastName: String, age : Editable) : Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }
}