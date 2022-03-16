package com.example.pedal.fragments.add

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.pedal.MainActivity
import com.example.pedal.R
import com.example.pedal.data.network.modelresponse.Endpoint
import com.example.pedal.data.network.modelresponse.Posts
import com.example.pedal.data.network.utils.NetworkUtils
import com.example.pedal.model.User
import com.example.pedal.viewmodel.UserViewModel
import com.example.pedal.databinding.FragmentAddBinding
import com.example.pedal.model.Adress
import com.example.pedal.utils.afterTextChangedDelayed
import com.example.pedal.utils.hideKeyboard
import com.example.pedal.utils.navigateWithAnimations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var mUserViewModel : UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root
        mUserViewModel = (activity as MainActivity).viewmodel()

        mUserViewModel.changetype("Urbano")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fowardFab.setOnClickListener{
            applyData()
            hideKeyboard()
        }
        binding.backFab.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.img.load(R.drawable.urbano){
            crossfade(true)
            crossfade(400)
        }

        binding.tipeDoPedal.setOnCheckedChangeListener{ group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            mUserViewModel.changetype(radio.text.toString())
        }

        mUserViewModel.type.observe(viewLifecycleOwner, Observer { type ->
            changeUI(type)
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun applyData() {
        val name = binding.nomePedal.text.toString()
        val description = binding.descPedal.text.toString()

        if(inputCheck(name,description)){
            mUserViewModel.partone(name,description)
            findNavController().navigateWithAnimations(AddFragmentDirections.actionAddFragmentToAddTwoFragment())
        }else{
            Toast.makeText(requireContext(), "Preencha todos os campos",Toast.LENGTH_SHORT).show()
        }
    }
    private fun inputCheck(name : String, description: String) : Boolean{
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(description))
    }

    private fun changeUI(check : String){
        if (check == "Urbano"){
            binding.img.load(R.drawable.urbano){
                crossfade(true)
                crossfade(400)
            }
            binding.fowardFab.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.nomePedal.hint = "Pedal Urbano"
            binding.descPedal.hint = "Passeio com amigos"
            binding.nomePedal.setTextColor(resources.getColor(R.color.purple_500))
        }
        if (check == "Trilha"){
            binding.img.load(R.drawable.trilha){
                crossfade(true)
                crossfade(400)
            }
            binding.fowardFab.setBackgroundColor(resources.getColor(R.color.orange))
            binding.nomePedal.hint = "Trilha"
            binding.descPedal.hint = "Trilha com amigos"
            binding.nomePedal.setTextColor(resources.getColor(R.color.orange))
        }
    }
    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }
}