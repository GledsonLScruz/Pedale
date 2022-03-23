package com.example.pedal.fragments.add

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.pedal.MainActivity
import com.example.pedal.R
import com.example.pedal.viewmodel.UserViewModel
import com.example.pedal.databinding.FragmentAddBinding
import com.example.pedal.utils.hideKeyboard
import com.example.pedal.utils.navigateWithAnimations
import java.text.SimpleDateFormat
import java.util.*
import android.widget.DatePicker





class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserViewModel : UserViewModel

    private var type = "Urbano"
    private var datepedals = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root

        mUserViewModel = (activity as MainActivity).viewmodel()
        changeUI()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fowardFab.setOnClickListener{
                sendDataToViewModel()
                hideKeyboard()
            }
            backFab.setOnClickListener {
                findNavController().navigateUp()
            }
            tipeDoPedal.setOnCheckedChangeListener{ group, checkedId ->
                val radio: RadioButton = group.findViewById(checkedId)
                type = radio.text.toString()
                changeUI()
            }
            selectDate.setOnClickListener {
                getdate()
            }
        }






    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendDataToViewModel() {
        val name = binding.nomePedal.text.toString()
        val description = binding.descPedal.text.toString()

        if(inputCheck(name,description,datepedals)){
            mUserViewModel.setNameDescDateType(name,description,datepedals,type)
            findNavController().navigateWithAnimations(AddFragmentDirections.actionAddFragmentToAddTwoFragment())
        }else{
            Toast.makeText(requireContext(), "Preencha todos os campos",Toast.LENGTH_SHORT).show()
        }
    }
    private fun inputCheck(name : String, description: String,date : String) : Boolean{
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(date))
    }

    private fun changeUI(){
        if (type == "Urbano"){
            binding.apply {
                img.load(R.drawable.urbano){
                    crossfade(true)
                    crossfade(400)
                }
                fowardFab.setBackgroundColor(resources.getColor(R.color.purple_500))
                nomePedal.hint = "Pedal Urbano"
                descPedal.hint = "Passeio com amigos"
                nomePedal.setTextColor(resources.getColor(R.color.purple_500))
            }
        }
        if (type == "Trilha"){
            binding.apply {
                img.load(R.drawable.trilha){
                    crossfade(true)
                    crossfade(400)
                }
                fowardFab.setBackgroundColor(resources.getColor(R.color.orange))
                nomePedal.hint = "Trilha"
                descPedal.hint = "Trilha com amigos"
                nomePedal.setTextColor(resources.getColor(R.color.orange))
            }
        }
    }

    private fun getdate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(),{ view, year, monthOfYear, dayOfMonth ->
            val Month : String = if (monthOfYear < 10){
                "0" + monthOfYear
            } else {
                monthOfYear.toString()
            }
            val Day : String = if (dayOfMonth < 10 ){
                "0" + dayOfMonth
            } else {
                dayOfMonth.toString()
            }
            datepedals = Day + "/" + Month + "/" + year
            binding.dataSelecionada.text = datepedals
        }, year, month, day)

        val dp = dpd.datePicker
        //Set the DatePicker minimum date selection to current date
        dp.minDate = c.timeInMillis
        dpd.show()
    }

    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }
}