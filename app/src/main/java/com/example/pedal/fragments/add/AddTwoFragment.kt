package com.example.pedal.fragments.add

import android.annotation.SuppressLint
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
import androidx.core.content.ContextCompat
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
import com.example.pedal.databinding.FragmentAddTwoBinding
import com.example.pedal.model.Adress
import com.example.pedal.utils.afterTextChangedDelayed
import com.example.pedal.utils.hideKeyboard
import com.example.pedal.utils.navigateWithAnimations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddTwoFragment : Fragment() {

    private var _binding: FragmentAddTwoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mUserViewModel : UserViewModel
    private lateinit var adapter: AdressAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTwoBinding.inflate(inflater, container, false)
        val view = binding.root

        mUserViewModel = (activity as MainActivity).viewmodel()
        changeUI(mUserViewModel.type.value.toString())
        mUserViewModel.start_or_end.value = 0

        adapter = AdressAdapter(mUserViewModel)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


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

        //firstadress
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


    }

    override fun onStart() {
        super.onStart()
        mUserViewModel.start_or_end.value = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun insertData() {
        //val date = binding.dataPedal.text.toString()
        val start = binding.partidaPedal.text.toString()


        if(!(TextUtils.isEmpty(start))){
            mUserViewModel.parttwo(start)
            findNavController().navigateWithAnimations(AddTwoFragmentDirections.actionAddTwoFragmentToAddThreeFragment())
        }else{
            Toast.makeText(requireContext(), "Preencha todos os campos",Toast.LENGTH_SHORT).show()
        }

    }

    private fun changeUI(check : String){
        if (check == "Urbano"){
            binding.fowardFab.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.img.load(R.drawable.urbano)
        }
        if (check == "Trilha"){
            binding.fowardFab.setBackgroundColor(resources.getColor(R.color.orange))
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