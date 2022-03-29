package com.gledsonluan.pedale.fragments.add

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
import com.gledsonluan.pedale.MainActivity
import com.gledsonluan.pedale.R
import com.gledsonluan.pedale.viewmodel.UserViewModel
import com.gledsonluan.pedale.databinding.FragmentAddTwoBinding
import com.gledsonluan.pedale.utils.afterTextChangedDelayed
import com.gledsonluan.pedale.utils.hideKeyboard


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

        mUserViewModel._recyclerDestinyIsView.value = false
        mUserViewModel._recyclerStartIsView.value = false

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


        mUserViewModel._recyclerStartIsView.observe(viewLifecycleOwner, Observer{ isVisible ->
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
                mUserViewModel._recyclerStartIsView.value = true
            } else {
                mUserViewModel._recyclerDestinyIsView.value = false
            }
        }
        //quando o recebe o valor
        mUserViewModel.responseData.observe(viewLifecycleOwner, Observer{ data ->
            adapterPartida.setData(data)
        })
        // quando o endereço for escolhido
        mUserViewModel.start.observe(viewLifecycleOwner, Observer{ adress ->
            hideKeyboard()
            binding.partidaPedalInput.setText(adress)
            mUserViewModel.changed()
            binding.chegadaPedalInput.requestFocus()
            mUserViewModel._recyclerStartIsView.value = false
        })



        mUserViewModel._recyclerDestinyIsView.observe(viewLifecycleOwner, Observer{ isVisible ->
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
                mUserViewModel._recyclerDestinyIsView.value = true
            } else {
                mUserViewModel._recyclerDestinyIsView.value = false
            }
        }
        //quando o recebe o valor
        mUserViewModel.responseData.observe(viewLifecycleOwner, Observer{ data ->
            adapterChegada.setData(data)
        })
        // quando o endereço for escolhido
        mUserViewModel.destiny.observe(viewLifecycleOwner, Observer{ adress ->
            hideKeyboard()
            binding.chegadaPedalInput.setText(adress)
            mUserViewModel._recyclerDestinyIsView.value = false
            binding.fowardFab.visibility = View.VISIBLE
            binding.fowardFab.isClickable = true
        })

        
        mUserViewModel.distance.observe(viewLifecycleOwner, Observer { distancia ->
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