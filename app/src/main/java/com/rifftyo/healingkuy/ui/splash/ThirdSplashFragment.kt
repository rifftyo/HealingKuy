package com.rifftyo.healingkuy.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rifftyo.healingkuy.R
import com.rifftyo.healingkuy.databinding.FragmentThirdSplashBinding
import com.rifftyo.healingkuy.ui.login.LoginActivity

class ThirdSplashFragment : Fragment() {

    private var _binding: FragmentThirdSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgStatusFirst.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FirstSplashFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.imgStatusSecond.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SecondSplashFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.imgStatusThird.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue_splash))

        binding.btnNext.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}