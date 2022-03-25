package com.example.pedal.fragments.update

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.pedal.R
import com.example.pedal.databinding.FragmentUpdateBinding
import com.example.pedal.model.User
import com.example.pedal.viewmodel.UserViewModel


class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserViewModel : UserViewModel
    private val args by navArgs<UpdateFragmentArgs>()

    private var type = "Urbano"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val view = binding.root
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nomePedal.setText(args.currentUser.name)
        binding.descPedal.setText(args.currentUser.description)
        binding.dataSelecionada.setText(args.currentUser.date)
        //binding.start.setText(args.currentUser.start)
        //binding.destiny.setText(args.currentUser.destiny)
        //binding.distance.setText(args.currentUser.distance)

        changeUI(args.currentUser.type)

        binding.backFab.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.nomePedal.setOnClickListener {
            updateItem()
        }
        mUserViewModel.type.observe(viewLifecycleOwner, Observer { checked ->
            changeUI(checked)
        })
    }
    private fun updateItem(){
        val name = binding.nomePedal.text.toString()
        val description = binding.descPedal.text.toString()
        val date = binding.dataSelecionada.text.toString()
        //val start = binding.start.text.toString()
        //val destiny = binding.destiny.text.toString()
        //val distance = binding.distance.text.toString()

        //if (inputCheck(name,description,date,start,destiny,distance)){
        if (inputCheck(name,description,date,"","","")){
            //val updateUser = User(args.currentUser.id,name,description,date,start,destiny,distance,type)
            //mUserViewModel.updateUser(updateUser)
            Toast.makeText(requireContext(),"Updated Sucessfully",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        else{
            Toast.makeText(requireContext(),"Please feal all the fields",Toast.LENGTH_SHORT).show()
        }
    }
    private fun inputCheck(name : String,
                           description: String,
                           date : String,
                           start: String,
                           destiny: String,
                           distace: String) : Boolean{
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(description) && TextUtils.isEmpty(date) && TextUtils.isEmpty(start) && TextUtils.isEmpty(destiny) && TextUtils.isEmpty(distace) )
    }

    private fun changeUI(check : String){
        if (check == "Urbano"){
            binding.img.load(R.drawable.urbano){
                crossfade(true)
                crossfade(400)
            }
            //binding.update.setBackgroundColor(resources.getColor(R.color.purple_500))
            //.distance.setTextColor(resources.getColor(R.color.purple_500))
            binding.urbano.isChecked = true
            type = "Urbano"
        }
        if (check == "Trilha"){
            binding.img.load(R.drawable.trilha){
                crossfade(true)
                crossfade(400)
            }
            //binding.update.setBackgroundColor(resources.getColor(R.color.orange))
            //binding.distance.setTextColor(resources.getColor(R.color.orange))
            binding.trilha.isChecked = true
            type = "Trilha"
        }
    }
}