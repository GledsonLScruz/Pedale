package com.example.pedal.fragments.read

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
import coil.load
import com.example.pedal.R
import com.example.pedal.databinding.FragmentReadBinding
import com.example.pedal.viewmodel.UserViewModel

class ReadFragment : Fragment() {

    private var _binding: FragmentReadBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserViewModel : UserViewModel
    private val args by navArgs<ReadFragmentArgs>()

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

        binding.name.text = args.currentUser.name
        binding.description.text = args.currentUser.description
        binding.date.text = args.currentUser.date
        binding.start.text = args.currentUser.start
        binding.destiny.text = args.currentUser.destiny
        binding.distance.text = args.currentUser.distance
        binding.type.text = args.currentUser.type

        if (args.currentUser.type == "Urbano"){
            binding.img.load(R.drawable.urbano){
                crossfade(true)
                crossfade(400)
            }
        }
        else{
            binding.img.load(R.drawable.trilha){
                crossfade(true)
                crossfade(400)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.editFab.setOnClickListener {
            val action = ReadFragmentDirections.actionReadFragmentToUpdateFragment(args.currentUser)
            findNavController().navigate(action)
        }
        binding.deleteButton.setOnClickListener {
            deleteUser()
        }
    }
    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Sim"){ _,_ ->
            mUserViewModel.deleteUser(args.currentUser)
            Toast.makeText(
                requireContext(),
                "Removido: ${args.currentUser.name}",
                Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        builder.setNegativeButton("Não"){_,_ ->}
        builder.setTitle("Delete ${args.currentUser.name}?")
        builder.setMessage("Tem certeza que deseja remover: ${args.currentUser.name}?")
        builder.create().show()
    }
}