package com.example.pedal.fragments.profile

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pedal.MainActivity
import com.example.pedal.R
import com.example.pedal.databinding.FragmentLoadingBinding
import com.example.pedal.viewmodel.UserViewModel


class LoadingFragment : Fragment() {

    private lateinit var binding : FragmentLoadingBinding
    private lateinit var muserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoadingBinding.inflate(inflater,container,false)
        val view = binding.root

        muserViewModel = (activity as MainActivity).viewmodel()
        binding.anim.apply {
            setAnimation(R.raw.loading)
            repeatCount = 2
            playAnimation()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            if (muserViewModel.status_backup.value!!){
            binding.anim.apply {
                setAnimation(R.raw.load_sucess)
                playAnimation()
                repeatCount = 0
                addAnimatorListener(object : Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        findNavController().navigate(R.id.action_loadingFragment_to_listFragment)
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })

            }

        } else {
            binding.anim.apply {
                setAnimation(R.raw.load_error)
                playAnimation()
                repeatCount = 0
                addAnimatorListener(object : Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        findNavController().navigate(R.id.action_loadingFragment_to_listFragment)
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })

            }
        }
        }, 2000)
    }
}