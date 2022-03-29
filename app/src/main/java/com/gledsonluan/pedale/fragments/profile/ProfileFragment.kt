package com.gledsonluan.pedale.fragments.profile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.gledsonluan.pedale.MainActivity
import com.gledsonluan.pedale.R
import com.gledsonluan.pedale.data.UserConfig
import com.gledsonluan.pedale.databinding.FragmentProfileBinding
import com.gledsonluan.pedale.viewmodel.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {

    private var chart : AnyChartView? = null
    private lateinit var binding : FragmentProfileBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var muserViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root

        db = (activity as MainActivity).db()
        muserViewModel = (activity as MainActivity).viewmodel()

        chart = binding.chart

        chartfuncion()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exit.setOnClickListener {
            logoutBtn()
        }
        binding.backFab.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.profilePic.load(UserConfig.pic){
            crossfade(true)
            crossfade(500)
            scale(Scale.FILL)
            transformations(CircleCropTransformation())
        }

        binding.backup.setOnClickListener {
            if (muserViewModel.readAllTours.value.isNullOrEmpty()) { Toast.makeText(requireContext(),"Crie pedais antes de fazer backup.", Toast.LENGTH_SHORT).show()}
            else {
                muserViewModel.backup()
                findNavController().navigate(R.id.action_profileFragment_to_loadingFragment)
            }
        }

        binding.download.setOnClickListener {
            muserViewModel.download()
            findNavController().navigate(R.id.action_profileFragment_to_loadingFragment)
        }



    }

    private fun chartfuncion() {
        val pie = AnyChart.pie()
        var n_urbano = 0
        var n_trilha = 0
        pie.title("Meus Pedais")
        muserViewModel.readAllTours.value?.forEach { user ->
            if (user.type == "Urbano"){
                n_urbano ++
            } else {
                n_trilha ++
            }
        }
        val values = listOf(n_urbano,n_trilha)

        val type = listOf("Urbano", "Trilha")
        val datapiechart : MutableList<DataEntry> = mutableListOf()

        for (index in values.indices){
            datapiechart.add(ValueDataEntry(type.elementAt(index),values.elementAt(index)))
        }
        pie.data(datapiechart)
        chart!!.setChart(pie)



    }

    private fun logoutBtn() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Sim"){ _,_ ->
            (activity as MainActivity).logoutGoogle()
            muserViewModel.deleteAllTours()
        }
        builder.setNegativeButton("Não"){_,_ ->}
        builder.setTitle("Desconectar?")
        builder.setMessage("Ao desconectar seus pedais não salvos serão perdidos.")
        builder.create().show()
    }
}
