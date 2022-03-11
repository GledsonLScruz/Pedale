package com.example.pedal.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
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

    private var type = "Urbano"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mUserViewModel.changetype("Urbano")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.add.setOnClickListener{
            insertData()
        }

        binding.img.load(R.drawable.urbano){
            crossfade(true)
            crossfade(400)
        }


        binding.tipeDoPedal.setOnCheckedChangeListener{ group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            mUserViewModel.changetype(radio.text.toString())

        }

        mUserViewModel.type.observe(viewLifecycleOwner, Observer { checked ->
            changeUI(checked)
        })

        binding.backFab.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun insertData() {
        val name = binding.nomePedal.text.toString()
        val description = binding.descPedal.text.toString()
        val date = binding.dataPedal.text.toString()
        val start = binding.partidaPedal.text.toString()
        val destiny = binding.destinoPedal.text.toString()
        val distance = binding.distance.text.toString()


        if(inputCheck(name,description,date,start,destiny)){
            //create user object
            val user = User(0,name,description,date,start,destiny,distance,type)
            //add Data to database
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "Sucessfully added",Toast.LENGTH_SHORT).show()
            //navigateback
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please Fill out all Fields",Toast.LENGTH_SHORT).show()
        }

    }

    private fun inputCheck(name : String,
                           description: String,
                           date : String,
                           start: String,
                           destiny: String) : Boolean{
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(description) && TextUtils.isEmpty(date) && TextUtils.isEmpty(start) && TextUtils.isEmpty(destiny))
    }
    private fun changeUI(check : String){
        if (check == "Urbano"){
            binding.img.load(R.drawable.urbano){
                crossfade(true)
                crossfade(400)
            }
            binding.add.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.nomePedal.hint = "Pedal Urbano"
            binding.descPedal.hint = "Passeio em grupo com amigos"
            binding.distance.setTextColor(resources.getColor(R.color.purple_500))
            type = "Urbano"
        }
        if (check == "Trilha"){
            binding.img.load(R.drawable.trilha){
                crossfade(true)
                crossfade(400)
            }
            binding.add.setBackgroundColor(resources.getColor(R.color.orange))
            binding.nomePedal.hint = "Trilha"
            binding.descPedal.hint = "Trilha em grupo com amigos"
            binding.distance.setTextColor(resources.getColor(R.color.orange))
            type = "Trilha"
        }
    }
}