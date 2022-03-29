package com.gledsonluan.pedale.fragments.read

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gledsonluan.pedale.viewmodel.UserViewModel

import android.content.Intent
import coil.load
import com.gledsonluan.pedale.databinding.FragmentReadBinding


class ReadFragment : Fragment() {

    private var _binding: FragmentReadBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserViewModel : UserViewModel
    private val args by navArgs<com.gledsonluan.pedale.fragments.read.ReadFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReadBinding.inflate(inflater, container, false)
        val view = binding.root
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.text = args.currentTour.name
        binding.description.text = args.currentTour.description
        binding.date.text = args.currentTour.date
        binding.start.text = args.currentTour.start
        binding.destiny.text = args.currentTour.destiny
        binding.distance.text = args.currentTour.distance
        binding.type.text = args.currentTour.type

        if (args.currentTour.type == "Urbano"){
            binding.img.load(com.gledsonluan.pedale.R.drawable.urbano){
                crossfade(true)
                crossfade(400)
            }
        }
        else{
            binding.img.load(com.gledsonluan.pedale.R.drawable.trilha){
                crossfade(true)
                crossfade(400)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.editFab.setOnClickListener {
            val action =
                com.gledsonluan.pedale.fragments.read.ReadFragmentDirections.actionReadFragmentToUpdateFragment(
                    args.currentTour
                )
            findNavController().navigate(action)
        }
        binding.deleteButton.setOnClickListener {
            deleteUser()
        }
        binding.shareFab.setOnClickListener {
            shareTour()
        }
    }
    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Sim"){ _,_ ->
            mUserViewModel.deleteTour(args.currentTour)
            Toast.makeText(
                requireContext(),
                "Removido: ${args.currentTour.name}",
                Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        builder.setNegativeButton("Não"){_,_ ->}
        builder.setTitle("Delete ${args.currentTour.name}?")
        builder.setMessage("Tem certeza que deseja remover: ${args.currentTour.name}?")
        builder.create().show()
    }

    private fun shareTour(){
        /*Create an ACTION_SEND Intent*/
        val intent = Intent(Intent.ACTION_SEND)
        /*This will be the actual content you wish you share.*/
        val shareBody = "Pedal: ${args.currentTour.name}\nDescrição: ${args.currentTour.description}\nData: ${args.currentTour.date}\nTipo do pedal: ${args.currentTour.type}\nLocal de inicio: ${args.currentTour.start}\nDestino: ${args.currentTour.destiny}\nDistância: ${args.currentTour.distance}Km"
        /*The type of the content is text, obviously.*/
        intent.type = "text/plain"
        /*Applying information Subject and Body.*/
        intent.putExtra(Intent.EXTRA_SUBJECT,"Pedal"
        )
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        /*Fire!*/
        startActivity(Intent.createChooser(intent, "share using"))
    }

}