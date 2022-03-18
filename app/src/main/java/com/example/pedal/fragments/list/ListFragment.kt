package com.example.pedal.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pedal.R
import com.example.pedal.viewmodel.UserViewModel
import com.example.pedal.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var mUserViewModel : UserViewModel

    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter = ListAdapter(findNavController())
        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        mUserViewModel.readAllData.observe(viewLifecycleOwner, Observer { allData ->
            adapter.setData(allData)
            if (allData.toString() == "[]"){
                binding.delete.visibility = View.GONE
                binding.delete.isClickable = false
                binding.recyclerview.visibility = View.GONE
                binding.recyclerview.isClickable = false
                binding.ctanew.visibility = View.VISIBLE
            } else{
                binding.delete.visibility = View.VISIBLE
                binding.delete.isClickable = true
                binding.recyclerview.visibility = View.VISIBLE
                binding.recyclerview.isClickable = true
                binding.ctanew.visibility = View.GONE
            }
        })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        binding.delete.setOnClickListener {
            deleteAllUser()
        }
        binding.perfilFab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_profileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun deleteAllUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mUserViewModel.deleteAllUser()
            Toast.makeText(
                requireContext(),
                "Tudo apagado com sucesso!",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Delete all users?")
        builder.setMessage("Are you sure you want to delete all users?")
        builder.create().show()
    }
}