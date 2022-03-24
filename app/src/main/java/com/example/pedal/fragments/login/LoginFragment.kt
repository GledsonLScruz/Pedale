package com.example.pedal.fragments.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.pedal.MainActivity
import com.example.pedal.R
import com.example.pedal.databinding.FragmentLoginBinding
import jp.wasabeef.glide.transformations.BlurTransformation


class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setOnClickListener {
            (activity as MainActivity).continueWithGoogle()
        }
        //https://cdn.discordapp.com/attachments/558087898705166346/954359818452349028/blur_1.png
        binding.backgroundImg.load(R.drawable.blur){
            crossfade(true)
            crossfade(900)
        }
        registerDeviceBackStackCallback()

    }
    private fun registerDeviceBackStackCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }
    }
}