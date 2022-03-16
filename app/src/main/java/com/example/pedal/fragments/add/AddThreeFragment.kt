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
import com.example.pedal.databinding.FragmentAddThreeBinding
import com.example.pedal.model.Adress
import com.example.pedal.utils.afterTextChangedDelayed
import com.example.pedal.utils.hideKeyboard
import com.example.pedal.utils.navigateWithAnimations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddThreeFragment : Fragment() {

    private var _binding: FragmentAddThreeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: AdressAdapter
    private lateinit var mUserViewModel : UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddThreeBinding.inflate(inflater, container, false)
        val view = binding.root

        mUserViewModel = (activity as MainActivity).viewmodel()
        changeUI(mUserViewModel.type.value.toString())
        mUserViewModel.recycler_is_view.value = true
        mUserViewModel.getDataFromApi(" ")
        mUserViewModel.adressSelected(" ")

        mUserViewModel.getDistance = true


        adapter = AdressAdapter(mUserViewModel)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sucessFab.setOnClickListener{
            insertData()
        }
        binding.backFab.setOnClickListener {
            findNavController().navigateUp()
        }




        mUserViewModel.recycler_is_view.observe(viewLifecycleOwner, Observer{ isVisible ->
            if (isVisible){
                binding.recyclerView.visibility = View.VISIBLE
                binding.recyclerView.isClickable = true
                binding.recyclerView.isFocusable = true
                binding.recyclerView.bringToFront()
            }
            else {
                binding.recyclerView.isClickable = false
                binding.recyclerView.isFocusable = false
                binding.recyclerView.visibility = View.GONE
            }
        })

        binding.partidaPedal.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.partidaPedal.afterTextChangedDelayed { input ->
                    mUserViewModel.getDataFromApi(input)
                }
                mUserViewModel.recycler_is_view.value = true
            } else {
                mUserViewModel.recycler_is_view.value = false
            }
        }
        //quando o recebe o valor
        mUserViewModel.response_data.observe(viewLifecycleOwner, Observer{ data ->
            adapter.setData(data)
        })
        // quando o endereço for escolhido
        mUserViewModel.adressSelected.observe(viewLifecycleOwner, Observer{ adress ->
            hideKeyboard()
            binding.partidaPedal.clearFocus()
            binding.partidaPedal.setText(adress)
            mUserViewModel.recycler_is_view.value = false
        })


        mUserViewModel._distancia.observe(viewLifecycleOwner, Observer{ distance ->
            binding.distancia.text = distance.toString()
        })
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun insertData() {
        val start = binding.partidaPedal.text.toString()


        if(!(TextUtils.isEmpty(start))){
            mUserViewModel.parttree(start)
            mUserViewModel.createnewtour()
            Toast.makeText(requireContext(), "Criado com sucesso!",Toast.LENGTH_SHORT).show()
            //navigateback
            findNavController().navigate(R.id.action_addThreeFragment_to_listFragment)
            mUserViewModel.getDistance = false
        }else{
            Toast.makeText(requireContext(), "Preencha todos os campos",Toast.LENGTH_SHORT).show()
        }

    }

    private fun changeUI(check : String){
        if (check == "Urbano"){
            binding.sucessFab.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.img.load(R.drawable.urbano)
        }
        if (check == "Trilha"){
            binding.sucessFab.setBackgroundColor(resources.getColor(R.color.orange))
            binding.img.load(R.drawable.trilha)
        }
    }
    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }
}