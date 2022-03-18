package com.example.pedal.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.pedal.MainActivity
import com.example.pedal.R
import com.example.pedal.viewmodel.UserViewModel
import com.example.pedal.databinding.FragmentAddTwoBinding
import com.example.pedal.utils.afterTextChangedDelayed
import com.example.pedal.utils.hideKeyboard


class AddTwoFragment : Fragment() {

    private var _binding: FragmentAddTwoBinding? = null
    private val binding get() = _binding!!

    private lateinit var mUserViewModel : UserViewModel
    private lateinit var adapterPartida: AdressAdapter
    private lateinit var adapterChegada: AdressAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTwoBinding.inflate(inflater, container, false)
        val view = binding.root

        mUserViewModel = (activity as MainActivity).viewmodel()

        changeUI(mUserViewModel.type.value.toString())


        adapterPartida = AdressAdapter(mUserViewModel,false)
        val recyclerViewPartida = binding.partidaRecycler
        recyclerViewPartida.adapter = adapterPartida
        recyclerViewPartida.layoutManager = LinearLayoutManager(requireContext())

        adapterChegada = AdressAdapter(mUserViewModel,true)
        val recyclerViewChegada = binding.chegadaRecycler
        recyclerViewChegada.adapter = adapterChegada
        recyclerViewChegada.layoutManager = LinearLayoutManager(requireContext())

        binding.fowardFab.visibility = View.GONE
        binding.fowardFab.isClickable = false

        mUserViewModel.recyclerChegadaIsView.value = false
        mUserViewModel.recyclerPartidaIsView.value = false

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fowardFab.setOnClickListener{
            insertData()
        }
        binding.backFab.setOnClickListener {
            findNavController().navigateUp()
        }


        mUserViewModel.recyclerPartidaIsView.observe(viewLifecycleOwner, Observer{ isVisible ->
            if (isVisible){
                binding.partidaRecycler.visibility = View.VISIBLE
                binding.partidaRecycler.isClickable = true
                binding.partidaRecycler.isFocusable = true
                binding.partidaRecycler.bringToFront()
            }
            else {
                binding.partidaRecycler.isClickable = false
                binding.partidaRecycler.isFocusable = false
                binding.partidaRecycler.visibility = View.GONE
            }
        })

        //firstadress
        binding.partidaPedalInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.partidaPedalInput.afterTextChangedDelayed { input ->
                    mUserViewModel.getDataFromApi(input)
                }
                mUserViewModel.recyclerPartidaIsView.value = true
            } else {
                mUserViewModel.recyclerPartidaIsView.value = false
            }
        }
        //quando o recebe o valor
        mUserViewModel.responseData.observe(viewLifecycleOwner, Observer{ data ->
            adapterPartida.setData(data)
        })
        // quando o endereço for escolhido
        mUserViewModel.partida.observe(viewLifecycleOwner, Observer{ adress ->
            hideKeyboard()
            binding.partidaPedalInput.setText(adress)
            mUserViewModel.changed()
            binding.chegadaPedalInput.requestFocus()
            mUserViewModel.recyclerPartidaIsView.value = false
        })



        mUserViewModel.recyclerChegadaIsView.observe(viewLifecycleOwner, Observer{ isVisible ->
            if (isVisible){
                binding.chegadaRecycler.visibility = View.VISIBLE
                binding.chegadaRecycler.isClickable = true
                binding.chegadaRecycler.isFocusable = true
                binding.chegadaRecycler.bringToFront()
            }
            else {
                binding.chegadaRecycler.isClickable = false
                binding.chegadaRecycler.isFocusable = false
                binding.chegadaRecycler.visibility = View.GONE
            }
        })

        //Secondadress
        binding.chegadaPedalInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.chegadaPedalInput.afterTextChangedDelayed { input ->
                    mUserViewModel.getDataFromApi(input)
                }
                mUserViewModel.recyclerChegadaIsView.value = true
            } else {
                mUserViewModel.recyclerChegadaIsView.value = false
            }
        }
        //quando o recebe o valor
        mUserViewModel.responseData.observe(viewLifecycleOwner, Observer{ data ->
            adapterChegada.setData(data)
        })
        // quando o endereço for escolhido
        mUserViewModel.chegada.observe(viewLifecycleOwner, Observer{ adress ->
            hideKeyboard()
            binding.chegadaPedalInput.setText(adress)
            mUserViewModel.recyclerChegadaIsView.value = false
            binding.fowardFab.visibility = View.VISIBLE
            binding.fowardFab.isClickable = true
        })

        
        mUserViewModel.distancia.observe(viewLifecycleOwner, Observer { distancia ->
            binding.distancia.text = distancia
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun insertData() {

        if(!(TextUtils.isEmpty(binding.partidaPedalInput.toString())) && !(TextUtils.isEmpty(binding.chegadaPedalInput.toString()))){
            mUserViewModel.createnewtour()
            findNavController().navigate(R.id.action_addTwoFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Preencha todos os campos",Toast.LENGTH_SHORT).show()
        }

    }

    private fun changeUI(check : String){
        if (check.equals("Urbano",true)){
            binding.img.load(R.drawable.urbano)
        }
        if (check.equals("Trilha",true)){
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