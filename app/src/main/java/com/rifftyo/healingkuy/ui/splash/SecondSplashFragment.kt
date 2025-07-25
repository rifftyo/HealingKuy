package com.rifftyo.healingkuy.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rifftyo.healingkuy.R
import com.rifftyo.healingkuy.databinding.FragmentSecondSplashBinding

class SecondSplashFragment : Fragment() {

    private var _binding: FragmentSecondSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondSplashBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.fragment_container, ThirdSplashFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.imgStatusFirst.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FirstSplashFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.imgStatusThird.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ThirdSplashFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.imgStatusSecond.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue_splash))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}