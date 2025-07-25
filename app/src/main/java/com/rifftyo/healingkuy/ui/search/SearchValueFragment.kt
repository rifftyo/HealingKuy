package com.rifftyo.healingkuy.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifftyo.core.data.Resource
import com.rifftyo.core.ui.CityDestinationsAdapter
import com.rifftyo.healingkuy.databinding.FragmentSearchValueBinding
import com.rifftyo.healingkuy.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchValueFragment : Fragment() {

    private var _binding: FragmentSearchValueBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchValueBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleSearchValue()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun handleSearchValue() {
        val cityAdapter = CityDestinationsAdapter()

        with(binding) {
            rvCity.layoutManager = LinearLayoutManager(requireContext())
            rvCity.setHasFixedSize(true)
            rvCity.adapter = cityAdapter
        }

        viewModel.searchResultLiveData.observe(viewLifecycleOwner) { searchResult ->
            when (searchResult) {
                is Resource.Loading -> {
                    binding.imgNotFound.visibility = View.GONE
                    binding.tvNotFound.visibility = View.GONE
                    binding.rvCity.visibility = View.GONE
                }
                is Resource.Success -> {
                    val data = searchResult.data
                    if (data.isNullOrEmpty()) {
                        binding.imgNotFound.visibility = View.VISIBLE
                        binding.tvNotFound.visibility = View.VISIBLE
                        binding.rvCity.visibility = View.GONE
                        cityAdapter.submitList(emptyList())
                    } else {
                        binding.imgNotFound.visibility = View.GONE
                        binding.tvNotFound.visibility = View.GONE
                        binding.rvCity.visibility = View.VISIBLE
                        cityAdapter.submitList(data)
                    }
                }
                is Resource.Error -> {
                    binding.imgNotFound.visibility = View.VISIBLE
                    binding.tvNotFound.visibility = View.VISIBLE
                    binding.rvCity.visibility = View.GONE
                    cityAdapter.submitList(emptyList())
                }
            }
        }


        cityAdapter.onItemClick = { selectedData ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, selectedData.id)
            startActivity(intent)
        }
    }
}