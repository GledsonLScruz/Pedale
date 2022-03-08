package com.example.pedal.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pedal.R
import com.example.pedal.databinding.FragmentAddBinding
import com.example.pedal.databinding.FragmentUpdateBinding
import com.example.pedal.model.User
import com.example.pedal.viewmodel.UserViewModel


class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mUserViewModel : UserViewModel

    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val view = binding.root
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateFirstname.setText(args.currentUser.firstName)
        binding.updateLastname.setText(args.currentUser.lastName)
        binding.updateAge.setText(args.currentUser.age.toString())

        binding.update.setOnClickListener {
            updateItem()
        }

        setHasOptionsMenu(true)

    }
    private fun updateItem(){
        val firstName = binding.updateFirstname.text.toString()
        val lastName = binding.updateLastname.text.toString()
        val age = Integer.parseInt(binding.updateAge.text.toString())

        if (inputCheck(firstName,lastName,binding.updateAge.text)){
            val updateUser = User(args.currentUser.id,firstName,lastName,age)
            mUserViewModel.updateUser(updateUser)
            Toast.makeText(requireContext(),"Updated Sucessfully",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        else{
            Toast.makeText(requireContext(),"Please feal all the fields",Toast.LENGTH_SHORT).show()
        }
    }
    private fun inputCheck(firstName : String, lastName: String, age : Editable) : Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mUserViewModel.deleteUser(args.currentUser)
            Toast.makeText(
                requireContext(),
                "Sucessfully removed: ${args.currentUser.firstName}",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Delete ${args.currentUser.firstName}?")
        builder.setMessage("Are you sure you want to delete ${args.currentUser.firstName}?")
        builder.create().show()
    }
}