package com.gledsonluan.pedale.fragments.profile

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.gledsonluan.pedale.MainActivity
import com.gledsonluan.pedale.R
import com.gledsonluan.pedale.databinding.FragmentLoadingBinding
import com.gledsonluan.pedale.viewmodel.UserViewModel


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
            repeatCount = 3
            speed = 3f
            playAnimation()

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerDeviceBackStackCallback()

        Handler(Looper.getMainLooper()).postDelayed({
            if (muserViewModel.status_backup_or_download.value!!){
                binding.textLoading.text = "Concluído!"
                binding.anim.apply {
                    setAnimation(R.raw.load_sucess)
                    speed = 2f
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
                binding.textLoading.text = "Erro!"
                binding.anim.apply {
                    setAnimation(R.raw.load_error)
                    speed = 2f
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
            } }, 2000)
    }
    private fun registerDeviceBackStackCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }
    }
}