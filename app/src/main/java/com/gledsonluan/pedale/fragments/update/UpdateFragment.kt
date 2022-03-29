package com.gledsonluan.pedale.fragments.update

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.gledsonluan.pedale.MainActivity
import com.gledsonluan.pedale.R
import com.gledsonluan.pedale.databinding.FragmentUpdateBinding
import com.gledsonluan.pedale.fragments.add.AdressAdapter
import com.gledsonluan.pedale.utils.afterTextChangedDelayed
import com.gledsonluan.pedale.utils.hideKeyboard
import com.gledsonluan.pedale.viewmodel.UserViewModel
import java.util.*


class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var mUserViewModel : UserViewModel

    private lateinit var adapterPartida: AdressAdapter
    private lateinit var adapterChegada: AdressAdapter

    private var type = "Urbano"
    private var datepedals = ""

    private val args by navArgs<com.gledsonluan.pedale.fragments.update.UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
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


        mUserViewModel._recyclerStartIsView.value = false
        mUserViewModel._recyclerDestinyIsView.value = false



        binding.nomePedal.setText(args.currenTour.name)
        binding.descPedal.setText(args.currenTour.description)
        binding.dataSelecionada.text = args.currenTour.date
        datepedals = args.currenTour.date
        mUserViewModel.setNameDescDateType(args.currenTour.name,args.currenTour.description,args.currenTour.date,args.currenTour.type)
        binding.partidaPedalInput.setText(args.currenTour.start)
        mUserViewModel.startPointSelected(args.currenTour.start,args.currenTour.latStart.toDouble(),args.currenTour.longStart.toDouble())
        binding.chegadaPedalInput.setText(args.currenTour.destiny)
        mUserViewModel.endPointSelected(args.currenTour.destiny,args.currenTour.latDestiny.toDouble(),args.currenTour.longDestiny.toDouble())
        binding.distancia.text = args.currenTour.distance

        changeUI(args.currenTour.type)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            updateFab.setOnClickListener{
                sendDataToViewModel()
            }
            backFab.setOnClickListener {
                findNavController().navigateUp()
            }
            tipeDoPedal.setOnCheckedChangeListener{ group, checkedId ->
                val radio: RadioButton = group.findViewById(checkedId)
                type = radio.text.toString()
                changeUI(type)
            }
            selectDate.setOnClickListener {
                getdate()
            }
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
                mUserViewModel._recyclerStartIsView.value = false
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
        })


        mUserViewModel.distance.observe(viewLifecycleOwner, Observer { distancia ->
            binding.distancia.text = distancia
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun sendDataToViewModel() {
        val name = binding.nomePedal.text.toString()
        val description = binding.descPedal.text.toString()
        val partida = binding.partidaPedalInput.toString()
        val chegada = binding.chegadaPedalInput.toString()

        if(inputCheck(name,description,datepedals,partida,chegada)){
            mUserViewModel.setNameDescDateType(name,description,datepedals,type)
            mUserViewModel.updateTour(args.currenTour.id)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Preencha todos os campos",Toast.LENGTH_SHORT).show()
        }
    }
    private fun inputCheck(name : String, description: String,date : String, partida: String, chegada: String) : Boolean{
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(date)|| TextUtils.isEmpty(partida) || TextUtils.isEmpty(chegada))
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
}